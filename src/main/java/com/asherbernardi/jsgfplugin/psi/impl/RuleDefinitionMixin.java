package com.asherbernardi.jsgfplugin.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.asherbernardi.jsgfplugin.psi.JsgfRuleDefinition;
import org.jetbrains.annotations.NotNull;

public abstract class RuleDefinitionMixin extends ASTWrapperPsiElement implements
    JsgfRuleDefinition {

  public RuleDefinitionMixin(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public String toString() {
    return "RuleDefinition: <" + getRuleName() + ">" + (isPublicRule() ? " (public)" : "");
  }
}
