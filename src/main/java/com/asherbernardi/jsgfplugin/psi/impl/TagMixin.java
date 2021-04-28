package com.asherbernardi.jsgfplugin.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.asherbernardi.jsgfplugin.psi.JsgfTag;
import org.jetbrains.annotations.NotNull;

public abstract class TagMixin extends ASTWrapperPsiElement implements JsgfTag {

  public TagMixin(@NotNull ASTNode node) {
    super(node);
  }

  public String getTagText() {
    String actualText = getText();
    // Remove escaped chars
    return actualText.substring(1, actualText.length() - 1)
        .replaceAll("\\\\([\\\\}])", "$1");
  }

  @Override
  public String toString() {
    return "Tag: \"" + getTagText() + "\"";
  }
}
