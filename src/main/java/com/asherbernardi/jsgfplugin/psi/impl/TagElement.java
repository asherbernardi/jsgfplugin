package com.asherbernardi.jsgfplugin.psi.impl;

import com.intellij.lang.ASTNode;
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode;
import org.jetbrains.annotations.NotNull;

public class TagElement extends ANTLRPsiNode {

  public TagElement(@NotNull ASTNode node) {
    super(node);
  }

  public String getTagText() {
    return getText().substring(1, getTextLength())
        .replace("\\\\", "\\")
        .replace("\\}", "}");
  }

  @Override
  public String toString() {
    return "Tag: " + getTagText();
  }
}
