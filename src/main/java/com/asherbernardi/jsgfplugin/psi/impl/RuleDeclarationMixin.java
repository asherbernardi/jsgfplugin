package com.asherbernardi.jsgfplugin.psi.impl;

import com.asherbernardi.jsgfplugin.psi.JsgfRuleDeclaration;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

public abstract class RuleDeclarationMixin extends ASTWrapperPsiElement implements
    JsgfRuleDeclaration {

  public RuleDeclarationMixin(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public String toString() {
    return "RuleDeclaration: " + getText();
  }
}
