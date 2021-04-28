package com.asherbernardi.jsgfplugin.psi.reference;

import com.asherbernardi.jsgfplugin.JsgfIcons;
import com.asherbernardi.jsgfplugin.psi.JsgfFile;
import com.asherbernardi.jsgfplugin.psi.RuleDeclarationName;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiPolyVariantReference;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.ResolveResult;
import com.asherbernardi.jsgfplugin.psi.JsgfRuleImportName;
import com.asherbernardi.jsgfplugin.psi.JsgfRuleDeclarationName;
import com.asherbernardi.jsgfplugin.psi.RuleName;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class OtherFileNameReference extends PsiReferenceBase<JsgfRuleImportName> implements
    PsiPolyVariantReference {

  private final String unqualifiedName;
  private final String qualifiedName;
  private final String packageName;
//  /**
//   * Keeps track of the resolved elements, so that we don't go looking throughout the project
//   * for grammars everytime it resolves (which destroys performance). The boolean refers to
//   * <code>incompleteCode</code> from <code>multiResolve()</code>
//   */
//  private HashMap<Boolean, List<ResolveResult>> cachedResolve = new HashMap<>();
//  /**
//   * Keeps track of the text of the resolved elements at the time they were resolved. This way,
//   * if you change an element in another file, the old cache will not resolve to that element.
//   */
//  private HashMap<ResolveResult, String> cachedResolveNames = new HashMap<>();

  public OtherFileNameReference(JsgfRuleImportName element, TextRange range) {
    super(element, range);
    // Careful of "IntellijIdeaRulezzz" which is used to make sure the autocomplete takes context into account
    // https://intellij-support.jetbrains.com/hc/en-us/community/posts/206752355-The-dreaded-IntellijIdeaRulezzz-string
    qualifiedName = element.getRuleName();
    unqualifiedName = element.getUnqualifiedRuleName();
    packageName = element.getFullyQualifiedGrammarName();
  }

  public String getUnqualifiedName() {
    return unqualifiedName;
  }

  public String getQualifiedName() {
    return qualifiedName;
  }

  public String getPackageName() {
    return packageName;
  }

  @NotNull
  protected abstract List<RuleDeclarationName> getRulesByPackage(boolean publicOnly);

  @NotNull
  protected abstract List<RuleDeclarationName> getRules(boolean publicOnly);

  public boolean canResolve() {
    return resolve() != null;
  }

  @NotNull
  @Override
  public ResolveResult[] multiResolve(boolean incompleteCode) {
    final List<RuleDeclarationName> refs = getRules(false);
    List<ResolveResult> results = new ArrayList<>();
    for (RuleDeclarationName ref : refs) {
      ResolveResult result = new JsgfResolveResult(ref, false);
      results.add(result);
    }
    return results.toArray(new ResolveResult[0]);
  }

  @Nullable
  @Override
  public PsiElement resolve() {
    ResolveResult[] resolveResults = multiResolve(false);
    if (resolveResults.length > 0 && myElement.isStarImport()) {
      return ((JsgfFile) resolveResults[0].getElement().getContainingFile()).getGrammarName();
    }
    if (resolveResults.length == 1) {
      return resolveResults[0].getElement();
    }
    return null;
  }

  @NotNull
  @Override
  public Object[] getVariants() {
    List<RuleDeclarationName> names = getRulesByPackage(true);
    List<LookupElement> variants = new ArrayList<>();
    JsgfRuleImportName element = myElement;
    String unqualifiedName = element.getUnqualifiedRuleName();
    String packageName = element.getFullyQualifiedGrammarName();
    for (final PsiElement ref : names) {
      if (ref instanceof JsgfRuleDeclarationName) {
        RuleName name = (RuleName) ref;
        if (name.getRuleName() != null && !name.getRuleName().isEmpty()) {
          variants.add(LookupElementBuilder
              .create(packageName + '.' + name.getRuleName()).withIcon(JsgfIcons.FILE)
              .withTypeText(name.getContainingFile().getName())
          );
        }
      }
    }
    if (!names.isEmpty()) {
      variants.add(LookupElementBuilder
          .create(packageName + ".*").withIcon(JsgfIcons.FILE)
          .withTypeText("All rules in " + names.get(0).getContainingFile().getName()));
    }
    return variants.toArray();
  }
}
