package com.asherbernardi.jsgfplugin.psi.impl;

import com.asherbernardi.jsgfplugin.psi.reference.OtherFileNameReference;
import com.intellij.extapi.psi.StubBasedPsiElementBase;
import com.intellij.lang.ASTNode;
import com.intellij.psi.stubs.IStubElementType;
import com.asherbernardi.jsgfplugin.psi.JsgfRuleImportName;
import com.asherbernardi.jsgfplugin.psi.stub.ImportStub;
import org.jetbrains.annotations.NotNull;

public abstract class RuleImportNameMixin extends StubBasedPsiElementBase<ImportStub> implements
    JsgfRuleImportName {

  public RuleImportNameMixin(@NotNull ASTNode node) {
    super(node);
  }

  public RuleImportNameMixin(@NotNull ImportStub stub, @NotNull IStubElementType nodeType) {
    super(stub, nodeType);
  }

  @Override
  public String toString() {
    return "ImportName: <" + getRuleName() + ">";
  }

  @Override
  @NotNull
  public OtherFileNameReference getReference() {
    return JsgfPsiImplInjections.getReference(this);
  }
}
