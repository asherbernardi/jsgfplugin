package com.asherbernardi.jsgfplugin.psi.impl;

import com.asherbernardi.jsgfplugin.psi.AbstractJsgfPsiElement;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.asherbernardi.jsgfplugin.psi.JsgfStringExp;
import com.intellij.openapi.paths.PsiDynaReference;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceService.Hints;
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry;
import org.jetbrains.annotations.NotNull;

public abstract class StringMixin extends AbstractJsgfPsiElement implements JsgfStringExp {

  public StringMixin(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public String toString() {
    return "QuotedToken: " + getStringText();
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
