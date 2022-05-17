package com.asherbernardi.jsgfplugin.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.asherbernardi.jsgfplugin.psi.JsgfTerminalExp;
import org.jetbrains.annotations.NotNull;

public abstract class TerminalMixin extends ASTWrapperPsiElement implements JsgfTerminalExp {

  public TerminalMixin(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public String toString() {
    return "TerminalSymbol: " + getText();
  }
}
