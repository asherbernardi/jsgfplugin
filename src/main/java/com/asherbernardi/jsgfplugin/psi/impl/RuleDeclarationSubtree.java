package com.asherbernardi.jsgfplugin.psi.impl;

import com.asherbernardi.jsgfplugin.JsgfLexer;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import java.text.ParseException;
import org.antlr.intellij.adaptor.lexer.TokenIElementType;
import org.antlr.intellij.adaptor.psi.IdentifierDefSubtree;
import org.antlr.v4.parse.v4ParserException;
import org.jetbrains.annotations.NotNull;

public class RuleDeclarationSubtree extends IdentifierDefSubtree {

  public RuleDeclarationSubtree(@NotNull ASTNode node, @NotNull IElementType idElementTyp) {
    super(node, idElementTyp);
  }

  public String getRuleName() {
    RuleDeclarationNameElement ruleName = PsiTreeUtil
        .findChildrenOfType(this, RuleDeclarationNameElement.class).toArray(new RuleDeclarationNameElement[1])[0];
    if (ruleName == null)
      return "";
    return ruleName.getName();
  }

  public boolean isPublicRule() {
    IElementType childType = getFirstChild().getNode().getElementType();
    return childType instanceof TokenIElementType &&
        ((TokenIElementType) childType).getANTLRTokenType() == JsgfLexer.PUBLIC;
  }

  public RuleDeclarationNameElement getRuleNameElement() {
    RuleDeclarationNameElement ruleName = PsiTreeUtil
        .getChildOfType(this, RuleDeclarationNameElement.class);
    if (ruleName != null) {
      return ruleName;
    }
    return null;
  }

  @Override
  public String toString() {
    return "RuleDeclaration: <" + getRuleName() + ">" + (isPublicRule()?" (public)":"");
  }
}
