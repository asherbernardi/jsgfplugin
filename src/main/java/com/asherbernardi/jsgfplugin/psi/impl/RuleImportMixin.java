package com.asherbernardi.jsgfplugin.psi.impl;

import com.asherbernardi.jsgfplugin.psi.JsgfRuleImport;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

public abstract class RuleImportMixin extends ASTWrapperPsiElement implements
    JsgfRuleImport {

  public RuleImportMixin(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public String toString() {
    return "RuleImport: " + getText();
  }
}
