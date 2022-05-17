package com.asherbernardi.jsgfplugin.psi.reference;

import com.asherbernardi.jsgfplugin.psi.JsgfRuleDeclarationName;
import com.intellij.psi.PsiElementResolveResult;
import org.jetbrains.annotations.NotNull;

public class JsgfResolveResultRule extends PsiElementResolveResult {

  private final boolean isLocal;

  public JsgfResolveResultRule(@NotNull JsgfRuleDeclarationName element, boolean isLocal) {
    super(element);
    this.isLocal = isLocal;
  }

  public boolean isLocal() {
    return isLocal;
  }

  @NotNull
  @Override
  public JsgfRuleDeclarationName getElement() {
    return (JsgfRuleDeclarationName) super.getElement();
  }
}
