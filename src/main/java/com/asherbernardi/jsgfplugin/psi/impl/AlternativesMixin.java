package com.asherbernardi.jsgfplugin.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.TokenSet;
import com.asherbernardi.jsgfplugin.psi.JsgfAlternativesExp;
import com.asherbernardi.jsgfplugin.psi.JsgfBnfTypes;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public abstract class AlternativesMixin extends ASTWrapperPsiElement implements JsgfAlternativesExp {

  public AlternativesMixin(@NotNull ASTNode node) {
    super(node);
  }

  protected List<PsiElement> getOrSymbols_() {
    return findChildrenByType(TokenSet.create(JsgfBnfTypes.OR));
  }

  @Override
  public String toString() {
    return "Alternatives: " + getText();
  }
}
