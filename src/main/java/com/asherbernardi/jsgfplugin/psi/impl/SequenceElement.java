package com.asherbernardi.jsgfplugin.psi.impl;

import com.asherbernardi.jsgfplugin.JsgfLexer;
import com.asherbernardi.jsgfplugin.psi.JsgfTypes;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import org.antlr.intellij.adaptor.lexer.TokenIElementType;
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode;
import org.jetbrains.annotations.NotNull;

public class SequenceElement extends ANTLRPsiNode {

  public SequenceElement(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public String toString() {
    Double weight = getWeight();
    return "Sequence: " + getText() + (weight == null ? "" : " (weight = " + weight + ")");
  }

  /**
   * @return the weight applied to the sequence if it exists, otherwise null
   */
  public Double getWeight() {
    ASTNode weight = getNode().getTreeParent().findChildByType(JsgfTypes.getTokenElementType(JsgfLexer.WEIGHT));
    if (weight != null)
      return Double.parseDouble(weight.getText().substring(1, weight.getTextLength() - 1));
    return null;
  }
}
