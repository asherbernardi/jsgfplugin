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

public class JsgfOptionalGroupExpImpl extends JsgfGroupExpImpl implements JsgfOptionalGroupExp {

  public JsgfOptionalGroupExpImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull JsgfVisitor visitor) {
    visitor.visitOptionalGroupExp(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof JsgfVisitor) accept((JsgfVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public JsgfExpansion getExpansion() {
    return PsiTreeUtil.getChildOfType(this, JsgfExpansion.class);
  }

}
