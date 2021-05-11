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
  private final String fullyQualifiedGrammarName;

  public OtherFileNameReference(@NotNull JsgfRuleImportName element, TextRange range) {
    super(element, range);
    // Careful of "IntellijIdeaRulezzz" which is used to make sure the autocomplete takes context into account
    // https://intellij-support.jetbrains.com/hc/en-us/community/posts/206752355-The-dreaded-IntellijIdeaRulezzz-string
    unqualifiedName = element.getUnqualifiedRuleName();
    fullyQualifiedGrammarName = element.getRuleName();
  }

  public String getUnqualifiedName() {
    return unqualifiedName;
  }

  public String getFullyQualifiedGrammarName() {
    return fullyQualifiedGrammarName;
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
  public JsgfResolveResult[] multiResolve(boolean incompleteCode) {
    final List<RuleDeclarationName> refs = getRules(false);
    List<JsgfResolveResult> results = new ArrayList<>();
    for (RuleDeclarationName ref : refs) {
      results.add(new JsgfResolveResult(ref, false));
    }
    return results.toArray(new JsgfResolveResult[0]);
  }

  @Nullable
  @Override
  public PsiElement resolve() {
    JsgfResolveResult[] resolveResults = multiResolve(false);
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
    String packageName = getFullyQualifiedGrammarName();
    for (final RuleName ref : names) {
      if (ref.getRuleName() != null && !ref.getRuleName().isEmpty()) {
        variants.add(LookupElementBuilder
            .create(packageName + '.' + ref.getRuleName()).withIcon(JsgfIcons.FILE)
            .withTypeText(ref.getContainingFile().getName())
        );
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
