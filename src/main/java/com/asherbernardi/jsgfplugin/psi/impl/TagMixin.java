package com.asherbernardi.jsgfplugin.psi.impl;

import com.asherbernardi.jsgfplugin.psi.AbstractJsgfPsiElement;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.asherbernardi.jsgfplugin.psi.JsgfTag;
import com.intellij.openapi.paths.PsiDynaReference;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceService.Hints;
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry;
import org.jetbrains.annotations.NotNull;

public abstract class TagMixin extends AbstractJsgfPsiElement implements JsgfTag {

  public TagMixin(@NotNull ASTNode node) {
    super(node);
  }

  public String getTagText() {
    String actualText = getText();
    // Remove escaped chars
    return actualText.substring(1, actualText.length() - 1)
        .replaceAll("\\\\([\\\\}])", "$1");
  }

  @Override
  public String toString() {
    return "Tag: \"" + getTagText() + "\"";
  }

  @Override
  protected @NotNull PsiReference @NotNull [] refs() {
    Hints hints = Hints.NO_HINTS;
    PsiReference[] psiReferences = ReferenceProvidersRegistry.getReferencesFromProviders(this, hints);
    Integer offset = hints.offsetInElement;
    if (offset != null) {
      return PsiDynaReference.filterByOffset(psiReferences, offset);
    }
    return psiReferences;
  }
}
