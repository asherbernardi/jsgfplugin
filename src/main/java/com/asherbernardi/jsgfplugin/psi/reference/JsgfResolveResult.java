package com.asherbernardi.jsgfplugin.psi.reference;

import com.asherbernardi.jsgfplugin.psi.RuleDeclarationName;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import org.jetbrains.annotations.NotNull;

public class JsgfResolveResult extends PsiElementResolveResult {

  private final boolean isLocal;

  public JsgfResolveResult(@NotNull RuleDeclarationName element, boolean isLocal) {
    super(element);
    this.isLocal = isLocal;
  }

  public boolean isLocal() {
    return isLocal;
  }

  @NotNull
  @Override
  public RuleDeclarationName getElement() {
    return (RuleDeclarationName) super.getElement();
  }
}
