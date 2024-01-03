package com.asherbernardi.jsgfplugin.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.asherbernardi.jsgfplugin.psi.JsgfGrammarDeclaration;
import org.jetbrains.annotations.NotNull;

public abstract class GrammarDeclarationMixin extends ASTWrapperPsiElement implements
    JsgfGrammarDeclaration {

  public GrammarDeclarationMixin(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public String toString() {
    return "GrammarInitialization";
  }
}
