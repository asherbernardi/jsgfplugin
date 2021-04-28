package com.asherbernardi.jsgfplugin.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.asherbernardi.jsgfplugin.psi.JsgfString;
import org.jetbrains.annotations.NotNull;

public abstract class StringMixin extends ASTWrapperPsiElement implements JsgfString {

  public StringMixin(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public String toString() {
    return "QuotedToken: " + getStringText();
  }
}
