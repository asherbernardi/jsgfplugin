package com.asherbernardi.jsgfplugin.psi.impl;

import com.intellij.extapi.psi.StubBasedPsiElementBase;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.psi.stubs.IStubElementType;
import com.asherbernardi.jsgfplugin.psi.RuleDeclarationName;
import com.asherbernardi.jsgfplugin.psi.stub.RuleDeclarationStub;
import org.jetbrains.annotations.NotNull;

public abstract class RuleDeclarationNameMixin extends StubBasedPsiElementBase<RuleDeclarationStub>
    implements RuleDeclarationName, PsiNameIdentifierOwner {

  public RuleDeclarationNameMixin(@NotNull ASTNode node) {
    super(node);
  }

  public RuleDeclarationNameMixin(@NotNull RuleDeclarationStub stub,
      @NotNull IStubElementType nodeType) {
    super(stub, nodeType);
  }

  @Override
  public String toString() {
    return "RuleDeclarationName: <" + getRuleName() + ">";
  }
}
