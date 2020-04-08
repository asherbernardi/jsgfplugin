package com.asherbernardi.jsgfplugin.psi.impl;

import com.intellij.lang.ASTNode;
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode;
import org.jetbrains.annotations.NotNull;

public class StringElement extends ANTLRPsiNode {

  public StringElement(@NotNull ASTNode node) {
    super(node);
  }

  public String getStringText() {
    return getText().substring(1, getTextLength())
        .replace("\\\\", "\\")
        .replace("\\\"", "\"");
  }

  @Override
  public String toString() {
    return "QuotedToken: " + getStringText();
  }
}
