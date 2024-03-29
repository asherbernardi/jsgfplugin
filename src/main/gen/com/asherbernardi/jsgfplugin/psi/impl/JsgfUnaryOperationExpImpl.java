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

public class JsgfUnaryOperationExpImpl extends UnaryOperationMixin implements JsgfUnaryOperationExp {

  public JsgfUnaryOperationExpImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull JsgfVisitor visitor) {
    visitor.visitUnaryOperationExp(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof JsgfVisitor) accept((JsgfVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public JsgfExpansion getExpansion() {
    return notNullChild(PsiTreeUtil.getChildOfType(this, JsgfExpansion.class));
  }

  @Override
  @Nullable
  public JsgfTag getTag() {
    return PsiTreeUtil.getChildOfType(this, JsgfTag.class);
  }

  @Override
  public boolean isStar() {
    return JsgfPsiImplInjections.isStar(this);
  }

  @Override
  public boolean isPlus() {
    return JsgfPsiImplInjections.isPlus(this);
  }

}
