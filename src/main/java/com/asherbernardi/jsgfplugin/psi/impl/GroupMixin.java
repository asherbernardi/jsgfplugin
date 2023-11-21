package com.asherbernardi.jsgfplugin.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.asherbernardi.jsgfplugin.psi.JsgfGroupExp;
import org.jetbrains.annotations.NotNull;

public abstract class GroupMixin extends ASTWrapperPsiElement implements JsgfGroupExp {

  public GroupMixin(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public String toString() {
    return (isOptionalGroup() ? "Optional " : "") + "Group";
  }
}
