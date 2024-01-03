package com.asherbernardi.jsgfplugin.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.asherbernardi.jsgfplugin.psi.JsgfHeader;
import org.jetbrains.annotations.NotNull;

public abstract class HeaderMixin extends ASTWrapperPsiElement implements JsgfHeader {

  public HeaderMixin(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public String toString() {
    return "JSGF Header";
  }
}
