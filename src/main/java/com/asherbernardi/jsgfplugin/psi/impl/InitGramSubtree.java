package com.asherbernardi.jsgfplugin.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.IElementType;
import org.antlr.intellij.adaptor.psi.IdentifierDefSubtree;
import org.jetbrains.annotations.NotNull;

public class InitGramSubtree extends IdentifierDefSubtree {

  public InitGramSubtree(@NotNull ASTNode node, @NotNull IElementType idElementTyp) {
    super(node, idElementTyp);
  }

  @Override
  public String toString() {
    return "GrammarInitialization";
  }
}
