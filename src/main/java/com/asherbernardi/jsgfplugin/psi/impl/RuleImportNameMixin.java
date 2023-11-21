package com.asherbernardi.jsgfplugin.psi.impl;

import com.asherbernardi.jsgfplugin.JsgfElementFactory;
import com.asherbernardi.jsgfplugin.psi.AbstractJsgfStubElement;
import com.asherbernardi.jsgfplugin.psi.reference.OtherFileRuleNameReference;
import com.intellij.extapi.psi.StubBasedPsiElementBase;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.stubs.IStubElementType;
import com.asherbernardi.jsgfplugin.psi.JsgfRuleImportName;
import com.asherbernardi.jsgfplugin.psi.stub.ImportStub;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;

public abstract class RuleImportNameMixin extends AbstractJsgfStubElement<ImportStub> implements
    JsgfRuleImportName {

  public RuleImportNameMixin(@NotNull ASTNode node) {
    super(node);
  }

  public RuleImportNameMixin(@NotNull ImportStub stub, @NotNull IStubElementType nodeType) {
    super(stub, nodeType);
  }

  @Override
  public String toString() {
    return "ImportName: <" + getText() + ">";
  }

  @Override
  public PsiElement setRuleName(@NotNull String newName) throws IncorrectOperationException {
    JsgfRuleImportName newImportName = JsgfElementFactory.createRuleImport(getProject(), newName);
    getNode().replaceAllChildrenToChildrenOf(newImportName.getNode());
    return this;
  }

  @Override
  protected @NotNull PsiReference @NotNull [] refs() {
    return getReferencePair().getReferenceArray();
  }
}
