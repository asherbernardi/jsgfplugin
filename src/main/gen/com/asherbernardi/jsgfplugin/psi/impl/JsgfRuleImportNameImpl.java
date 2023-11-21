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
import com.asherbernardi.jsgfplugin.psi.reference.OtherFileReferencePair;
import com.intellij.psi.search.SearchScope;
import com.asherbernardi.jsgfplugin.psi.stub.ImportStub;
import com.intellij.psi.stubs.IStubElementType;

public class JsgfRuleImportNameImpl extends RuleImportNameMixin implements JsgfRuleImportName {

  public JsgfRuleImportNameImpl(@NotNull ASTNode node) {
    super(node);
  }

  public JsgfRuleImportNameImpl(@NotNull ImportStub stub, @NotNull IStubElementType type) {
    super(stub, type);
  }

  public void accept(@NotNull JsgfVisitor visitor) {
    visitor.visitRuleImportName(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof JsgfVisitor) accept((JsgfVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  public boolean isStarImport() {
    return JsgfPsiImplInjections.isStarImport(this);
  }

  @Override
  public String getUnqualifiedRuleName() {
    return JsgfPsiImplInjections.getUnqualifiedRuleName(this);
  }

  @Override
  public String getFullyQualifiedGrammarName() {
    return JsgfPsiImplInjections.getFullyQualifiedGrammarName(this);
  }

  @Override
  public String getSimpleGrammarName() {
    return JsgfPsiImplInjections.getSimpleGrammarName(this);
  }

  @Override
  public String getPackageName() {
    return JsgfPsiImplInjections.getPackageName(this);
  }

  @Override
  public OtherFileReferencePair getReferencePair() {
    return JsgfPsiImplInjections.getReferencePair(this);
  }

  @Override
  public @NotNull SearchScope getUseScope() {
    return JsgfPsiImplInjections.getUseScope(this);
  }

  @Override
  public String getFullyQualifiedRuleName() {
    return JsgfPsiImplInjections.getFullyQualifiedRuleName(this);
  }

}
