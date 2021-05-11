package com.asherbernardi.jsgfplugin.psi.impl;

import com.asherbernardi.jsgfplugin.psi.reference.RuleReferenceReference;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.asherbernardi.jsgfplugin.psi.JsgfRuleReferenceName;
import org.jetbrains.annotations.NotNull;

/**
 * @author asherbernardi
 */
public abstract class RuleReferenceNameMixin extends ASTWrapperPsiElement implements JsgfRuleReferenceName {

  public RuleReferenceNameMixin(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public String toString() {
    return "RuleReferenceName: <" + getRuleName() + ">";
  }

  @NotNull
  @Override
  public RuleReferenceReference getReference() {
    return JsgfPsiImplInjections.getReference(this);
  }
}
