package com.asherbernardi.jsgfplugin.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.asherbernardi.jsgfplugin.psi.JsgfSequence;
import org.jetbrains.annotations.NotNull;

public abstract class SequenceMixin extends ASTWrapperPsiElement implements JsgfSequence {

  public SequenceMixin(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public String toString() {
    Double weight = getWeight();
    return "Sequence: " + getText() + (weight == null ? "" : " (weight = " + weight + ")");
  }
}
