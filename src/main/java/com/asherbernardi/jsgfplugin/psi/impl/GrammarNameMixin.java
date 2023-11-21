package com.asherbernardi.jsgfplugin.psi.impl;

import com.intellij.extapi.psi.StubBasedPsiElementBase;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.psi.stubs.IStubElementType;
import com.asherbernardi.jsgfplugin.psi.JsgfGrammarName;
import com.asherbernardi.jsgfplugin.psi.stub.GrammarNameStub;
import org.jetbrains.annotations.NotNull;

public abstract class GrammarNameMixin extends StubBasedPsiElementBase<GrammarNameStub>
    implements JsgfGrammarName, PsiNameIdentifierOwner {

  public GrammarNameMixin(@NotNull ASTNode node) {
    super(node);
  }

  public GrammarNameMixin(@NotNull GrammarNameStub stub, @NotNull IStubElementType nodeType) {
    super(stub, nodeType);
  }

  @Override
  public String toString() {
    return "GrammarName: " + getFQGN();
  }
}
