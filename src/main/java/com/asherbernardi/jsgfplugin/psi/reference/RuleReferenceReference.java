package com.asherbernardi.jsgfplugin.psi.reference;

import com.asherbernardi.jsgfplugin.JsgfIcons;
import com.asherbernardi.jsgfplugin.psi.JsgfFile;
import com.asherbernardi.jsgfplugin.psi.impl.RuleReferenceElement;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.PsiPolyVariantReference;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.asherbernardi.jsgfplugin.JsgfUtil;
import com.asherbernardi.jsgfplugin.psi.impl.ImportNameElement;
import com.asherbernardi.jsgfplugin.psi.impl.RuleNameElement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RuleReferenceReference extends PsiReferenceBase<RuleReferenceElement>
    implements PsiPolyVariantReference {
  /**
   * The name of the rule to which this reference references.
   * If reference mode is UNQUALIFIED, it is equal to myElement.getName().
   */
  private String ruleName;
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
  private REFERENCE_MODE mode;

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

  public RuleReferenceReference(@NotNull RuleReferenceElement element, TextRange range) {
    super(element, range);
    String name = element.getName();
    Matcher matcherQualified = (Pattern.compile("([^.]+)\\.([^.]+)")).matcher(name);
    Matcher matcherFullyQualified = (Pattern.compile("(.*)\\.([^.]+)\\.([^.]+)")).matcher(name);
    // If the name has one period it is qualified
    if (matcherQualified.matches()) {
      mode = REFERENCE_MODE.QUALIFIED;
      ruleName = matcherQualified.group(2);
      simpleGrammarName = matcherQualified.group(1);
    }
    // If the name has more than one period it is fully-qualified
    else if (matcherFullyQualified.matches()) {
      mode = REFERENCE_MODE.FULLY_QUALIFIED;
      ruleName = matcherFullyQualified.group(3);
      simpleGrammarName = matcherFullyQualified.group(2);
      packageName = matcherFullyQualified.group(1);
    }
    // If the name has no periods it is unqualified
    else {
      mode = REFERENCE_MODE.UNQUALIFIED;
      ruleName = name;
    }
  }

  public boolean canResolve() {
    return resolve() != null;
  }

  @NotNull
  @Override
  public ResolveResult[] multiResolve(boolean incompleteCode) {
    Set<ResolveResult> results = new HashSet<>();
    JsgfFile file = (JsgfFile) myElement.getContainingFile();
    // Local rule names take precedence
    if (mode == REFERENCE_MODE.UNQUALIFIED) {
      final List<RuleNameElement> names = JsgfUtil.findRulesInFile(file, ruleName);
      for (RuleNameElement name : names) {
        results.add(new PsiElementResolveResult(name));
      }
    }
    // If we can't find the rule declared in this grammar, or this not an unqualified rule check the imports
    // We only use cached import resolutions, since we assume that the imports would be resolved
    // before the rule references.
    Set<ImportNameElement> importNames = file.getImports();
    if ((results.isEmpty() || mode != REFERENCE_MODE.UNQUALIFIED) && importNames != null) {
      final List<ImportNameElement> importList = new ArrayList<>();
      switch (mode) {
        // If mode is unqualified then we need to consider all imports
        case UNQUALIFIED:
          importList.addAll(importNames);
          break;
        // If mode is QUALIFIED then we only consider imports with the same simple grammar name
        case QUALIFIED:
          importNames.forEach(importNameElement -> {
            if (importNameElement.getSimpleGrammarName().equals(simpleGrammarName))
              importList.add(importNameElement);
          });
          break;
        // If mode is FULLY_QUALIFIED then we only consider imports with the same fully-qualified grammar name
        case FULLY_QUALIFIED:
          importNames.forEach(importNameElement -> {
            if (importNameElement.getPackageName().equals(packageName) &&
                importNameElement.getSimpleGrammarName().equals(simpleGrammarName))
              importList.add(importNameElement);
          });
      }
      for (ImportNameElement importName : importList) {
        // Add all the matching rules of a '*' import
        if (importName.isStarImport()) {
          ResolveResult[] cache = ((OtherFileNameReference) importName.getReference()).retrieveCache(false);
          List<ResolveResult> foundImports = new ArrayList<>();
          if (cache != null) {
            for (ResolveResult result : cache) {
              if (((RuleNameElement) result.getElement()).getName().equals(ruleName)) {
                foundImports.add(result);
              }
            }
          }
          // This check will make sure that if you add a rule in another file, we will find it
          if (foundImports.isEmpty()) {
            // If we don't find it the first time, we recache and try again
            cache = ((OtherFileNameReference) importName.getReference()).multiResolve(false, false);
            for (ResolveResult result : cache) {
              if (((RuleNameElement) result.getElement()).getName().equals(ruleName)) {
                foundImports.add(result);
              }
            }
          }
          results.addAll(foundImports);
        }
        // Add imports with a name that matches
        else if (importName.getUnqualifiedName().equals(ruleName)) {
          ResolveResult[] cache = ((OtherFileNameReference) importName.getReference()).retrieveCache(false);
          if (cache != null)
            Collections.addAll(results, cache);
        }
      }
    }
    return results.toArray(new ResolveResult[results.size()]);
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
  public Object[] getVariants() {
    // Note: when getting variants, Intellij saves a temporary version of the file in memory before
    // it saves anything. You have to call getOriginalFile() to get the actual file
    JsgfFile file = (JsgfFile) myElement.getContainingFile().getOriginalFile();
    List<RuleNameElement> names = JsgfUtil.findRulesInFile(file);
    List<LookupElement> variants = new ArrayList<LookupElement>();
    for (final RuleNameElement name : names) {
      if (name.getName() != null && name.getName().length() > 0) {
        variants.add(LookupElementBuilder
            .create(name.getName()).withIcon(JsgfIcons.FILE)
            .withTypeText("Rule")
        );
      }
    }
    final Collection<ImportNameElement> importNames = PsiTreeUtil
        .findChildrenOfType(file, ImportNameElement.class);
    for (ImportNameElement importName : importNames) {
      variants.add(LookupElementBuilder.create(importName.getUnqualifiedName()).withIcon(JsgfIcons.FILE)
          .withTypeText("Imported rule from " + importName.getName()));
    }
    return variants.toArray();
  }

  @Override
  public PsiElement handleElementRename(@NotNull String newElementName)
      throws IncorrectOperationException {
    return super.handleElementRename(newElementName);
  }
}