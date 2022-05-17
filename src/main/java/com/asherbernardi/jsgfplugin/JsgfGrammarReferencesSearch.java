package com.asherbernardi.jsgfplugin;

import com.intellij.openapi.application.QueryExecutorBase;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.searches.ReferencesSearch.SearchParameters;
import com.intellij.util.Processor;
import com.asherbernardi.jsgfplugin.psi.JsgfGrammarName;
import org.jetbrains.annotations.NotNull;

/**
 * For searching for the references of grammars that are referenced via the file name,
 * rather than the grammar name
 */
public class JsgfGrammarReferencesSearch extends QueryExecutorBase<PsiReference, SearchParameters> {

  @Override
  public void processQuery(@NotNull SearchParameters searchParameters,
      @NotNull Processor<? super PsiReference> processor) {
    PsiElement element = searchParameters.getElementToSearch();
    if (element instanceof JsgfGrammarName) {
      searchParameters.getOptimizer().searchWord(getTrimmedFileName(element), searchParameters.getEffectiveSearchScope(), true, element);
      searchParameters.getOptimizer().searchWord(((JsgfGrammarName) element).getSimpleGrammarName(), searchParameters.getEffectiveSearchScope(), true, element);
    }
  }

  private String getTrimmedFileName(PsiElement element) {
    PsiFile file = element.getContainingFile().getOriginalFile();
    String fileName = file.getName();
    return JsgfUtil.stripExtension(fileName);
  }
}
