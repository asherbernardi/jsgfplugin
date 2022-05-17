package com.asherbernardi.jsgfplugin.psi.reference;

import com.asherbernardi.jsgfplugin.JsgfIcons;
import com.asherbernardi.jsgfplugin.psi.JsgfFile;
import com.asherbernardi.jsgfplugin.psi.JsgfRuleDeclarationName;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiPolyVariantReference;
import com.intellij.psi.PsiReferenceBase;
import com.asherbernardi.jsgfplugin.psi.JsgfRuleImportName;
import com.asherbernardi.jsgfplugin.psi.RuleName;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class OtherFileRuleNameReference extends PsiReferenceBase<JsgfRuleImportName> implements
    PsiPolyVariantReference {

  private final String unqualifiedName;
  private final String fullyQualifiedGrammarName;

  public OtherFileRuleNameReference(@NotNull JsgfRuleImportName element, TextRange range) {
    super(element, range);
    // Careful of "IntellijIdeaRulezzz" which is used to make sure the autocomplete takes context into account
    // https://intellij-support.jetbrains.com/hc/en-us/community/posts/206752355-The-dreaded-IntellijIdeaRulezzz-string
    unqualifiedName = element.getUnqualifiedRuleName();
    fullyQualifiedGrammarName = element.getFullyQualifiedGrammarName();
  }

  public String getUnqualifiedName() {
    return unqualifiedName;
  }

  public String getFullyQualifiedGrammarName() {
    return fullyQualifiedGrammarName;
  }

  @NotNull
  protected abstract List<JsgfRuleDeclarationName> getRulesByPackage(boolean publicOnly);

  @NotNull
  protected abstract List<JsgfRuleDeclarationName> getRules(boolean publicOnly);

  public boolean canResolve() {
    return resolve() != null;
  }

  @NotNull
  @Override
  public JsgfResolveResultRule[] multiResolve(boolean incompleteCode) {
    List<JsgfResolveResultRule> results = new ArrayList<>();
    List<JsgfRuleDeclarationName> refs = getRules(false);
    for (JsgfRuleDeclarationName ref : refs) {
      results.add(new JsgfResolveResultRule(ref, false));
    }
    return results.toArray(new JsgfResolveResultRule[0]);
  }

  @Nullable
  @Override
  public PsiElement resolve() {
    JsgfResolveResultRule[] resolveResults = multiResolve(false);
    if (resolveResults.length > 0 && getElement().isStarImport()) {
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
    List<JsgfRuleDeclarationName> names = getRulesByPackage(true);
    List<LookupElement> variants = new ArrayList<>();
    String grammarName = getFullyQualifiedGrammarName();
    for (final RuleName ref : names) {
      if (ref.getRuleName() != null && !ref.getRuleName().isEmpty()) {
        variants.add(LookupElementBuilder
            .create(grammarName + '.' + ref.getRuleName()).withIcon(JsgfIcons.FILE)
            .withTypeText(ref.getContainingFile().getName())
        );
      }
    }
    if (!names.isEmpty()) {
      variants.add(LookupElementBuilder
          .create(grammarName + ".*").withIcon(JsgfIcons.FILE)
          .withTypeText("All rules in " + names.get(0).getContainingFile().getName()));
    }
    return variants.toArray();
  }
}
