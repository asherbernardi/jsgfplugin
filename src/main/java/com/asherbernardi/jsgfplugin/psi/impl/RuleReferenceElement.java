package com.asherbernardi.jsgfplugin.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiReference;
import com.asherbernardi.jsgfplugin.psi.reference.RuleReferenceReference;
import org.jetbrains.annotations.NotNull;

/**
 * @author asherbernardi
 */
public class RuleReferenceElement extends RuleNameElement {

  public RuleReferenceElement(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public PsiReference getReference() {
    TextRange range = new TextRange(0, getName().length());
    return new RuleReferenceReference(this, range);
  }

  @Override
  public String toString() {
    return "RuleReferenceName: <" + getName() + ">";
  }
}
