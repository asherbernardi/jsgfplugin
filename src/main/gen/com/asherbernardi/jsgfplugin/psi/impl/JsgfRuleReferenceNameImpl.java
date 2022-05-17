// This is a generated file. Not intended for manual editing.
package com.asherbernardi.jsgfplugin.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.asherbernardi.jsgfplugin.psi.JsgfBnfTypes.*;
import com.asherbernardi.jsgfplugin.psi.*;
import com.asherbernardi.jsgfplugin.psi.reference.LocalReferencePair;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.SearchScope;

public class JsgfRuleReferenceNameImpl extends RuleReferenceNameMixin implements JsgfRuleReferenceName {

  public JsgfRuleReferenceNameImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull JsgfVisitor visitor) {
    visitor.visitRuleReferenceName(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof JsgfVisitor) accept((JsgfVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public LocalReferencePair getReferencePair() {
    return JsgfPsiImplInjections.getReferencePair(this);
  }

  @Override
  @NotNull
  public PsiReference[] getReferences() {
    return JsgfPsiImplInjections.getReferences(this);
  }

  @Override
  @NotNull
  public SearchScope getUseScope() {
    return JsgfPsiImplInjections.getUseScope(this);
  }

}
