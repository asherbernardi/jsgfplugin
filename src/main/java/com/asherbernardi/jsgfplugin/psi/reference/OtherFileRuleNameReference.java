package com.asherbernardi.jsgfplugin.psi.reference;

import com.asherbernardi.jsgfplugin.JsgfIcons;
import com.asherbernardi.jsgfplugin.JsgfLanguage;
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
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.PsiModificationTracker;
import com.intellij.util.IncorrectOperationException;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OtherFileRuleNameReference extends JsgfDefaultCachedReference<JsgfRuleImportName> implements
    PsiPolyVariantReference {

  public OtherFileRuleNameReference(@NotNull JsgfRuleImportName element, TextRange range,
      @Nullable OtherFileGrammarNameReference grammarNameReference) {
    super(element, range);
  }

  @Override
  protected CachedValueProvider<ResolveResult[]> getCachedValueProvider(JsgfRuleImportName element) {
    return new MyCachedValueProvider(element);
  }

  private static class MyCachedValueProvider implements CachedValueProvider<ResolveResult[]> {

    private final JsgfRuleImportName element;

    MyCachedValueProvider(JsgfRuleImportName element) {
      this.element = element;
    }

    @Override
    public @Nullable Result<ResolveResult[]> compute() {
      List<JsgfResolveResultRule> results = new ArrayList<>();
      List<JsgfRuleDeclarationName> refs = JsgfUtil.resolveImportRules(element);
      if (refs.isEmpty()) {
        return new Result<>(ResolveResult.EMPTY_ARRAY,
            // For broken references, re-compute at any change
            PsiModificationTracker.getInstance(element.getProject()).forLanguage(JsgfLanguage.INSTANCE));
      }
      List<Object> dependencies = new ArrayList<>();
      dependencies.add(element);
      for (JsgfRuleDeclarationName rule : refs) {
        results.add(new JsgfResolveResultRule(rule, false));
        dependencies.add(rule);
      }
      return new Result<>(results.toArray(new JsgfResolveResultRule[0]), dependencies.toArray());
    }
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
    OtherFileGrammarNameReference grammarRef = myElement.getReferencePair().getGrammarReference();
    List<JsgfRuleDeclarationName> names = new ArrayList<>();
    List<LookupElement> variants = new ArrayList<>();
    if (grammarRef != null) {
      ResolveResult[] resolves = grammarRef.multiResolve(false);
      for (ResolveResult resolve : resolves) {
        JsgfFile importFile = (JsgfFile) resolve.getElement().getContainingFile().getOriginalFile();
        names.addAll(JsgfUtil.findRulesInFile(importFile, true));
      }
      for (final RuleName ref : names) {
        if (ref.getRuleName() != null && !ref.getRuleName().isEmpty()) {
          variants.add(LookupElementBuilder
              .create(ref.getRuleName()).withIcon(JsgfIcons.FILE)
              .withTypeText(ref.getContainingFile().getName())
          );
        }
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
}
