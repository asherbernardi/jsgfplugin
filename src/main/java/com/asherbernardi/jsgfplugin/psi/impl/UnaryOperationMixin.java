package com.asherbernardi.jsgfplugin.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.asherbernardi.jsgfplugin.psi.JsgfUnaryOperationExp;
import org.jetbrains.annotations.NotNull;

public abstract class UnaryOperationMixin extends ASTWrapperPsiElement implements
    JsgfUnaryOperationExp {

  public UnaryOperationMixin(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public String toString() {
    return "Unary: " + getText();
  }
}
