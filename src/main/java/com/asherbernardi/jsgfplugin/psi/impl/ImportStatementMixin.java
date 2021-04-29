package com.asherbernardi.jsgfplugin.psi.impl;

import com.asherbernardi.jsgfplugin.psi.JsgfRuleImport;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.asherbernardi.jsgfplugin.psi.JsgfImportStatement;
import org.jetbrains.annotations.NotNull;

public abstract class ImportStatementMixin extends ASTWrapperPsiElement implements JsgfImportStatement {

  public ImportStatementMixin(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public String toString() {
    JsgfRuleImport ruleImport = getRuleImport();
    return "ImportStatement: " + (ruleImport != null ? ruleImport.getText() : "NULL");
  }
}
