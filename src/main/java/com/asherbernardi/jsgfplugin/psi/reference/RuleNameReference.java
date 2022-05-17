package com.asherbernardi.jsgfplugin.psi.reference;

import com.intellij.codeInsight.completion.InsertHandler;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiPolyVariantReference;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.util.PsiUtilCore;
import com.asherbernardi.jsgfplugin.JsgfIcons;
import com.asherbernardi.jsgfplugin.JsgfUtil;
import com.asherbernardi.jsgfplugin.psi.JsgfBnfTypes;
import com.asherbernardi.jsgfplugin.psi.JsgfFile;
import com.asherbernardi.jsgfplugin.psi.JsgfRuleDeclarationName;
import com.asherbernardi.jsgfplugin.psi.JsgfRuleImportName;
import com.asherbernardi.jsgfplugin.psi.JsgfRuleReferenceName;
import com.asherbernardi.jsgfplugin.psi.stub.ImportStubIndex;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RuleNameReference extends PsiReferenceBase<JsgfRuleReferenceName>
    implements PsiPolyVariantReference {
  /**
   * The unqualified name of the rule to which this reference references.
   * If reference mode is UNQUALIFIED, it is equal to myElement.getText().
   */
  private final String ruleName;
  /**
   * The name of the grammar of the rule to which this reference references.
   * Is null unless reference mode is QUALIFIED or FULLY_QUALIFIED.
   */
  private String simpleGrammarName = null;
  /**
   * The name of the package of the rule to which this reference references.
   * Is null unless reference mode is FULLY_QUALIFIED.
   */
  private String packageName = null;
  private final REFERENCE_MODE mode;

  private GrammarNameReference grammarNameReference;

  /**
   * Rule references in JSGF can be:
   * <ul>
   *   <li> unqualified: &lt;ruleName&gt;
   *   <li> qualified: &lt;SimpleGrammarName.ruleName&gt;
   *   <li> fully-qualified: &lt;com.package.name.SimpleGrammarName.ruleName&gt;
   * </ul>
   * And each of these modes resolves references differently.
   */
  private enum REFERENCE_MODE {
    UNQUALIFIED, QUALIFIED, FULLY_QUALIFIED
  }

  public static final InsertHandler<LookupElement> RULE_REFERENCE_INSERT_HANDLER = (context, lookupElement) -> {
    int offset = context.getTailOffset();
    PsiElement element = PsiUtilCore.getElementAtOffset(context.getFile().getOriginalFile(), offset);
    if (element.getNode().getElementType() != JsgfBnfTypes.RANGLE) {
      context.getEditor().getDocument().insertString(offset, ">");
    }
    context.getEditor().getCaretModel().moveToOffset(offset + 1);
  };

  public RuleNameReference(@NotNull JsgfRuleReferenceName element, TextRange range, String qualifiedName, @Nullable GrammarNameReference grammarNameReference) {
    super(element, range);
    Matcher matcherQualified = (Pattern.compile("([^.]+)\\.([^.]+)")).matcher(qualifiedName);
    Matcher matcherFullyQualified = (Pattern.compile("(.*)\\.([^.]+)\\.([^.]+)")).matcher(qualifiedName);
    // If the name has one period it is qualified
    if (matcherQualified.matches()) {
      mode = REFERENCE_MODE.QUALIFIED;
      ruleName = matcherQualified.group(2);
      simpleGrammarName = matcherQualified.group(1);
      if (grammarNameReference == null) throw new IllegalArgumentException("Must provide a GrammarNameReference for a qualified rule reference");
      this.grammarNameReference = grammarNameReference;
    }
    // If the name has more than one period it is fully-qualified
    else if (matcherFullyQualified.matches()) {
      mode = REFERENCE_MODE.FULLY_QUALIFIED;
      ruleName = matcherFullyQualified.group(3);
      simpleGrammarName = matcherFullyQualified.group(2);
      packageName = matcherFullyQualified.group(1);
      if (grammarNameReference == null) throw new IllegalArgumentException("Must provide a GrammarNameReference for a qualified rule reference");
      this.grammarNameReference = grammarNameReference;
    }
    // If the name has no periods it is unqualified
    else {
      mode = REFERENCE_MODE.UNQUALIFIED;
      ruleName = qualifiedName;
      this.grammarNameReference = null;
    }
  }

  public boolean canResolve() {
    return resolve() != null;
  }

  @NotNull
  @Override
  public JsgfResolveResultRule[] multiResolve(boolean incompleteCode) {
    Set<JsgfResolveResultRule> results = new HashSet<>();
    JsgfFile file = (JsgfFile) myElement.getContainingFile();
    // Local rule names take precedence
    if (mode == REFERENCE_MODE.UNQUALIFIED) {
      final List<JsgfRuleDeclarationName> names = JsgfUtil.findRulesInFile(file, ruleName);
      for (JsgfRuleDeclarationName name : names) {
        results.add(new JsgfResolveResultRule(name, true));
      }
    }
    // If we can't find the rule declared in this grammar, or this not an unqualified rule check the imports
    // We only use cached import resolutions, since we assume that the imports would be resolved
    // before the rule references.
    if (results.isEmpty()) {
      final List<JsgfRuleImportName> importList = new ArrayList<>();
      switch (mode) {
        // If mode is unqualified then we need to consider all imports
        case UNQUALIFIED:
          file.getImportNames().stream().filter(Objects::nonNull).forEachOrdered(importList::add);
          break;
        // If mode is QUALIFIED or FULLY-QUALIFIED then we pull the resolve from the attached
        // grammar reference
        case QUALIFIED:
        case FULLY_QUALIFIED:
          JsgfResolveResultGrammar[] grammarResolve = grammarNameReference.multiResolve(false);
          Arrays.stream(grammarResolve).map(JsgfResolveResultGrammar::getImporter)
              .forEachOrdered(importList::add);
          break;
      }
      for (@NotNull JsgfRuleImportName importName : importList) {
        // Add all the matching rules of a '*' import
        if (importName.isStarImport()) {
          JsgfResolveResultRule[] importResolve =
              importName.getReferencePair().getRuleReference().multiResolve(false);
          // We can assume the element for the resolve result is a rule name, because we selected
          // the second element from importName.getReferences()
          Arrays.stream(importResolve)
              .filter(result -> result.getElement().getRuleName().equals(ruleName))
              .forEachOrdered(results::add);
        }
        // Add imports with a name that matches
        else if (importName.getUnqualifiedRuleName().equals(ruleName)) {
          JsgfResolveResultRule[] importResolve =
              importName.getReferencePair().getRuleReference().multiResolve(false);
          Collections.addAll(results, importResolve);
        }
      }
    }
    return results.toArray(new JsgfResolveResultRule[0]);
  }

  @Nullable
  @Override
  public PsiElement resolve() {
    ResolveResult[] resolveResults = multiResolve(false);
    if (resolveResults.length == 1)
      return resolveResults[0].getElement();
    return null;
  }

  @NotNull
  @Override
  public Object [] getVariants() {
    // Note: when getting variants, Intellij saves a temporary version of the file in memory before
    // it saves anything. You have to call getOriginalFile() to get the actual file
    JsgfFile file = (JsgfFile) myElement.getContainingFile().getOriginalFile();
    List<JsgfRuleDeclarationName> names = JsgfUtil.findRulesInFile(file);
    List<LookupElement> variants = new ArrayList<>();
    final Map<String, Integer> unqualifiedNameCount = new HashMap<>();
    for (final JsgfRuleDeclarationName ruleName : names) {
      String name = ruleName.getRuleName();
      if (name != null && name.length() > 0) {
        variants.add(LookupElementBuilder.create(name)
            .withIcon(JsgfIcons.FILE)
            .withTypeText("Rule")
            .withInsertHandler(RULE_REFERENCE_INSERT_HANDLER)
        );
        unqualifiedNameCount.put(name, unqualifiedNameCount.computeIfAbsent(name, n -> 0) + 1);
      }
    }
    // All imports will be added as variants with lookup strings equal to the unqualified,
    // qualified, and fully qualified rule names for those imports, with precedence given to
    // the unqualified names. If an imported rules unqualified name is shared with a local rule
    // or another imported rule, then we remove that as an import string and given precedence
    // to the qualified name. If still there's an overlap between qualified names, the qualified
    // name is removed and precedence given to the fully qualified rule name.
    final Collection<JsgfRuleImportName> importNames = ImportStubIndex.getImportsInFile(file);
    final Map<String, Integer> qualifiedNameCount = new HashMap<>();
    final List<ImportNameLookupElementBuilder> builders = new ArrayList<>();
    for (JsgfRuleImportName importName : importNames) {
      for (JsgfRuleDeclarationName importedRule : JsgfUtil.findImportRules(importName, true)) {
        String name = importedRule.getRuleName();
        String qualifiedName = importName.getSimpleGrammarName() + "." + name;
        String fullyQualifiedName = importName.getFullyQualifiedGrammarName() + "." + name;
        builders.add(new ImportNameLookupElementBuilder(name, qualifiedName, fullyQualifiedName, importName));
        unqualifiedNameCount.put(name, unqualifiedNameCount.computeIfAbsent(name, n -> 0) + 1);
        qualifiedNameCount.put(qualifiedName, qualifiedNameCount.computeIfAbsent(qualifiedName, n -> 0) + 1);
      }
    }
    for (ImportNameLookupElementBuilder builder : builders) {
      if (unqualifiedNameCount.get(builder.getUN()) > 1) {
        builder.removeUN();
      }
      if (qualifiedNameCount.get(builder.getQN()) > 1) {
        builder.removeQN();
      }
      variants.add(builder.getLookupElement());
    }
    // always add NULL and VOID as options
    variants.add(LookupElementBuilder.create("VOID").withIcon(JsgfIcons.FILE));
    variants.add(LookupElementBuilder.create("NULL").withIcon(JsgfIcons.FILE));
    return variants.toArray();
  }

  private static class ImportNameLookupElementBuilder {
    final LinkedList<String> names = new LinkedList<>();
    final JsgfRuleImportName importName;

    ImportNameLookupElementBuilder(String un, String qn, String fqn, JsgfRuleImportName importName) {
      names.push(fqn);
      names.push(qn);
      names.push(un);
      this.importName = importName;
    }

    String getUN() {
      if (names.size() == 3)
        return names.peek();
      return null;
    }

    void removeUN() {
      if (names.size() == 3)
        names.pop();
    }

    String getQN() {
      if (names.size() == 3)
        return names.get(1);
      if (names.size() == 2)
        return names.peek();
      return null;
    }

    void removeQN() {
      if (names.size() == 2)
        names.pop();
    }

    LookupElement getLookupElement() {
      return LookupElementBuilder.create(names.pop())
          .withLookupStrings(names)
          .withIcon(JsgfIcons.FILE)
          .withTypeText("Imported rule from " + importName.getFullyQualifiedGrammarName())
          .withInsertHandler(RULE_REFERENCE_INSERT_HANDLER)
          ;
    }
  }
}