package com.asherbernardi.jsgfplugin.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.asherbernardi.jsgfplugin.psi.JsgfSequenceExp;
import org.jetbrains.annotations.NotNull;

public abstract class SequenceMixin extends ASTWrapperPsiElement implements JsgfSequenceExp {

  public SequenceMixin(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public String toString() {
    return "Sequence: " + getText();
  }
}
