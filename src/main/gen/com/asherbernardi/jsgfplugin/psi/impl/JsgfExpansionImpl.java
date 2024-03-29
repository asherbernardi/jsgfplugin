// This is a generated file. Not intended for manual editing.
package com.asherbernardi.jsgfplugin.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.asherbernardi.jsgfplugin.psi.JsgfBnfTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.asherbernardi.jsgfplugin.psi.*;

public abstract class JsgfExpansionImpl extends ASTWrapperPsiElement implements JsgfExpansion {

  public JsgfExpansionImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull JsgfVisitor visitor) {
    visitor.visitExpansion(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof JsgfVisitor) accept((JsgfVisitor)visitor);
    else super.accept(visitor);
  }

}
