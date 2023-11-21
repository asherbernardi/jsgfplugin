package com.asherbernardi.jsgfplugin.psi.reference;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiPolyVariantReference;
import com.intellij.psi.PsiReferenceBase;
import com.asherbernardi.jsgfplugin.JsgfIcons;
import com.asherbernardi.jsgfplugin.JsgfUtil;
import com.asherbernardi.jsgfplugin.psi.JsgfFile;
import com.asherbernardi.jsgfplugin.psi.JsgfRuleImportName;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OtherFileGrammarNameReference extends PsiReferenceBase<JsgfRuleImportName> implements
    PsiPolyVariantReference {

  public OtherFileGrammarNameReference(@NotNull JsgfRuleImportName element, TextRange range) {
    super(element, range);
    // Careful of "IntellijIdeaRulezzz" which is used to make sure the autocomplete takes context into account
    // https://intellij-support.jetbrains.com/hc/en-us/community/posts/206752355-The-dreaded-IntellijIdeaRulezzz-string
  }

  /**
   * If this reference is a grammar part of the import, then the resolve element can be trusted to
   * be a GrammarName. If not, then if the import is not a star import, it will resolve to a
   * rule name
   */
  @NotNull
  @Override
  public JsgfResolveResultGrammar[] multiResolve(boolean incompleteCode) {
    List<JsgfResolveResultGrammar> results = new ArrayList<>();
    List<JsgfFile> filesByPackage = JsgfUtil.findFilesByPackageGrammarName(getElement());
    for (JsgfFile file : filesByPackage) {
      if (file.getGrammarName() != null) {
        results.add(new JsgfResolveResultGrammar(file.getGrammarName(), getElement()));
      }
    }
    List<JsgfFile> filesByPath = JsgfUtil.findFilesByPackageFilePath(getElement());
    for (JsgfFile file : filesByPath) {
      if (file.getGrammarName() != null) {
        results.add(new JsgfResolveResultGrammar(file, getElement()));
      }
    }
    return results.toArray(new JsgfResolveResultGrammar[0]);
  }

  @Nullable
  @Override
  public PsiElement resolve() {
    JsgfResolveResultGrammar[] resolveResults = multiResolve(false);
    if (resolveResults.length == 1)
      return resolveResults[0].getElement();
    return null;
  }
}
