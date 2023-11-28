package com.asherbernardi.jsgfplugin.psi.reference;

import com.asherbernardi.jsgfplugin.JsgfLanguage;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiPolyVariantReference;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValueProvider.Result;
import com.intellij.psi.util.CachedValuesManager;
import com.asherbernardi.jsgfplugin.JsgfUtil;
import com.asherbernardi.jsgfplugin.psi.JsgfFile;
import com.asherbernardi.jsgfplugin.psi.JsgfRuleImportName;
import com.intellij.psi.util.PsiModificationTracker;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OtherFileGrammarNameReference extends JsgfDefaultCachedReference<JsgfRuleImportName> implements
    PsiPolyVariantReference {

  public OtherFileGrammarNameReference(@NotNull JsgfRuleImportName element, TextRange range) {
    super(element, range);
    // Careful of "IntellijIdeaRulezzz" which is used to make sure the autocomplete takes context into account
    // https://intellij-support.jetbrains.com/hc/en-us/community/posts/206752355-The-dreaded-IntellijIdeaRulezzz-string
  }

  @Override
  protected CachedValueProvider<ResolveResult[]> getCachedValueProvider(
      JsgfRuleImportName element) {
    return new MyCachedValueProvider(element);
  }

  /**
   * If this reference is a grammar part of the import, then the resolve element can be trusted to
   * be a GrammarName. If not, then if the import is not a star import, it will resolve to a
   * rule name
   */
  private static class MyCachedValueProvider implements CachedValueProvider<ResolveResult[]> {

    private final JsgfRuleImportName element;

    MyCachedValueProvider(JsgfRuleImportName element) {
      this.element = element;
    }

    @Override
    public @Nullable Result<ResolveResult[]> compute() {
      List<JsgfResolveResultGrammar> results = new ArrayList<>();
      List<Object> dependencies = new ArrayList<>();
      dependencies.add(element);
      for (JsgfFile file : JsgfUtil.findFilesByPackageGrammarName(element)) {
        if (file.getGrammarName() != null) {
          results.add(new JsgfResolveResultGrammar(file.getGrammarName(), element));
          dependencies.add(file);
        }
      }
      for (JsgfFile file : JsgfUtil.findFilesByPackageFilePath(element)) {
        if (file.getGrammarName() != null) {
          results.add(new JsgfResolveResultGrammar(file, element));
          dependencies.add(file);
        }
      }
      if (results.isEmpty()) {
        return new Result<>(ResolveResult.EMPTY_ARRAY,
            // For broken references, re-compute at any change
            PsiModificationTracker.getInstance(element.getProject()).forLanguage(JsgfLanguage.INSTANCE));
      }
      return new Result<>(results.toArray(new JsgfResolveResultGrammar[0]), dependencies.toArray());
    }
  }

  @Nullable
  @Override
  public PsiElement resolve() {
    ResolveResult[] resolveResults = multiResolve(false);
    if (resolveResults.length == 1)
      return resolveResults[0].getElement();
    return null;
  }
}
