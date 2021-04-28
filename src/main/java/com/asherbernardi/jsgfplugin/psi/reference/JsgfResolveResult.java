package com.asherbernardi.jsgfplugin.psi.reference;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import org.jetbrains.annotations.NotNull;

public class JsgfResolveResult extends PsiElementResolveResult {

  private final boolean isLocal;

  public JsgfResolveResult(@NotNull PsiElement element, boolean isLocal) {
    super(element);
    this.isLocal = isLocal;
  }

  public boolean isLocal() {
    return isLocal;
  }
}
