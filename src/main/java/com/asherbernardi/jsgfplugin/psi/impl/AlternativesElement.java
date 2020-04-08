package com.asherbernardi.jsgfplugin.psi.impl;

import com.asherbernardi.jsgfplugin.JsgfLexer;
import com.asherbernardi.jsgfplugin.JsgfParser;
import com.asherbernardi.jsgfplugin.psi.JsgfTypes;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import java.util.List;
import org.antlr.intellij.adaptor.lexer.TokenIElementType;
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode;
import org.jetbrains.annotations.NotNull;

public class AlternativesElement extends ANTLRPsiNode {

  public AlternativesElement(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public String toString() {
    return "Alternatives: " + getText();
  }

  /**
   * @return the JsgfSequences which make up the alternatives of this element
   */
  public SequenceElement[] getAlternatives() {
    List<SequenceElement> result = PsiTreeUtil.getChildrenOfAnyType(this, SequenceElement.class);
    return result.toArray(new SequenceElement[0]);
  }

  public PsiElement[] getOrSymbols() {
    List<PsiElement> result = findChildrenByType(JsgfTypes.getTokenElementType(JsgfLexer.ORSYMBOL));
    return result.toArray(new PsiElement[0]);
  }
}
