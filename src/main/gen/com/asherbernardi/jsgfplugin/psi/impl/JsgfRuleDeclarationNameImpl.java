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
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.search.SearchScope;
import com.intellij.util.IncorrectOperationException;
import javax.swing.Icon;
import com.asherbernardi.jsgfplugin.psi.stub.RuleDeclarationStub;
import com.intellij.psi.stubs.IStubElementType;

public class JsgfRuleDeclarationNameImpl extends RuleDeclarationNameMixin implements JsgfRuleDeclarationName {

  public JsgfRuleDeclarationNameImpl(@NotNull ASTNode node) {
    super(node);
  }

  public JsgfRuleDeclarationNameImpl(@NotNull RuleDeclarationStub stub, @NotNull IStubElementType type) {
    super(stub, type);
  }

  public void accept(@NotNull JsgfVisitor visitor) {
    visitor.visitRuleDeclarationName(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof JsgfVisitor) accept((JsgfVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public PsiElement getRuleNameIdentifier() {
    return notNullChild(findChildByType(RULE_NAME_IDENTIFIER));
  }

  @Override
  public String getName() {
    return JsgfPsiImplInjections.getName(this);
  }

  @Override
  public PsiElement setName(@NotNull String name) throws IncorrectOperationException {
    return JsgfPsiImplInjections.setName(this, name);
  }

  @Override
  public boolean isPublicRule() {
    return JsgfPsiImplInjections.isPublicRule(this);
  }

  @Override
  public ItemPresentation getPresentation() {
    return JsgfPsiImplInjections.getPresentation(this);
  }

  @Override
  @NotNull
  public Icon getElementIcon(int flags) {
    return JsgfPsiImplInjections.getElementIcon(this, flags);
  }

  @Override
  @Nullable
  public PsiElement getNameIdentifier() {
    return JsgfPsiImplInjections.getNameIdentifier(this);
  }

  @Override
  @NotNull
  public SearchScope getUseScope() {
    return JsgfPsiImplInjections.getUseScope(this);
  }

}
