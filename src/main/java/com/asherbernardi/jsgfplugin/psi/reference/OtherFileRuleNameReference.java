package com.asherbernardi.jsgfplugin.psi.reference;

import com.asherbernardi.jsgfplugin.JsgfIcons;
import com.asherbernardi.jsgfplugin.JsgfUtil;
import com.asherbernardi.jsgfplugin.psi.JsgfFile;
import com.asherbernardi.jsgfplugin.psi.JsgfRuleDeclarationName;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiPolyVariantReference;
import com.intellij.psi.PsiReferenceBase;
import com.asherbernardi.jsgfplugin.psi.JsgfRuleImportName;
import com.asherbernardi.jsgfplugin.psi.RuleName;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.impl.source.resolve.ResolveCache;
import com.intellij.util.IncorrectOperationException;
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
  public ResolveResult[] multiResolve(boolean incompleteCode) {
    PsiFile file = getElement().getContainingFile();
    return ResolveCache.getInstance(file.getProject()).resolveWithCaching(this, MyResolver.INSTANCE, false, false, file);
  }

  @Nullable
  @Override
  public PsiElement resolve() {
    ResolveResult[] resolveResults = multiResolve(false);
    if (resolveResults.length > 0 && getElement().isStarImport())
      return ((JsgfFile) resolveResults[0].getElement().getContainingFile()).getGrammarName();
    if (resolveResults.length == 1)
      return resolveResults[0].getElement();
    return null;
  }

  @Override
  public PsiElement handleElementRename(@NotNull String newElementName)
      throws IncorrectOperationException {
    if (getElement().isStarImport()) {
      throw new IncorrectOperationException("Cannot rename a '*' import");
    }
    return super.handleElementRename(newElementName);
  }

  @NotNull
  @Override
  public Object @NotNull [] getVariants() {
    List<JsgfRuleDeclarationName> names = getRulesByPackage(true);
    List<LookupElement> variants = new ArrayList<>();
    for (final RuleName ref : names) {
      if (ref.getRuleName() != null && !ref.getRuleName().isEmpty()) {
        variants.add(LookupElementBuilder
            .create(ref.getRuleName()).withIcon(JsgfIcons.FILE)
            .withTypeText(ref.getContainingFile().getName())
        );
      }
    }
    if (!names.isEmpty()) {
      variants.add(LookupElementBuilder
          .create("*").withIcon(JsgfIcons.FILE)
          .withTypeText("All rules in " + names.get(0).getContainingFile().getName()));
    } else {
      // Otherwise add the grammar names as variants
      variants.addAll(JsgfUtil.allFilesAsImportVariants(getElement()));
    }
    return variants.toArray();
  }

  private static class MyResolver implements ResolveCache.PolyVariantContextResolver<OtherFileRuleNameReference> {
    static final MyResolver INSTANCE = new MyResolver();

    @Override
    public ResolveResult @NotNull [] resolve(@NotNull OtherFileRuleNameReference ref, @NotNull PsiFile containingFile, boolean incompleteCode) {
      List<JsgfResolveResultRule> results = new ArrayList<>();
      List<JsgfRuleDeclarationName> refs = ref.getRules(false);
      for (JsgfRuleDeclarationName rule : refs) {
        results.add(new JsgfResolveResultRule(rule, false));
      }
      return results.toArray(new JsgfResolveResultRule[0]);
    }
  }
}
