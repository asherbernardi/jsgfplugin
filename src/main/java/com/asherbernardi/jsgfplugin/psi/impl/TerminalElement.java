package com.asherbernardi.jsgfplugin.psi.impl;

import com.intellij.lang.ASTNode;
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode;
import org.jetbrains.annotations.NotNull;

public class TerminalElement extends ANTLRPsiNode {

  public TerminalElement(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public String toString() {
    return "TerminalSymbol: " + getText();
  }
}
