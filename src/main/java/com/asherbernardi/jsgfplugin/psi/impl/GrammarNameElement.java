package com.asherbernardi.jsgfplugin.psi.impl;

import com.intellij.lang.ASTNode;
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode;
import org.jetbrains.annotations.NotNull;

public class GrammarNameElement extends ANTLRPsiNode {

  public GrammarNameElement(@NotNull ASTNode node) {
    super(node);
  }

  public String getName() {
    return getText();
  }

  @Override
  public String toString() {
    return "GrammarName: " + getName();
  }
}
