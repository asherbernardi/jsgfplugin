package com.asherbernardi.jsgfplugin.psi.impl;

import com.asherbernardi.jsgfplugin.psi.JsgfRuleReferenceExp;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

public abstract class RuleReferenceMixin extends ASTWrapperPsiElement implements
    JsgfRuleReferenceExp {

  public RuleReferenceMixin(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public String toString() {
    return "RuleReference: " + getText();
  }
}
