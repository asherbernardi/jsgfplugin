package com.asherbernardi.jsgfplugin.psi.impl;

import com.asherbernardi.jsgfplugin.psi.JsgfUnaryOperationExp;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

public abstract class ExpansionWithTagMixin extends ASTWrapperPsiElement implements
    JsgfUnaryOperationExp {

  public ExpansionWithTagMixin(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public String toString() {
    return "Expansion with tag: " + getText();
  }
}
