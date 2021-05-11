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
import com.asherbernardi.jsgfplugin.psi.stub.GrammarNameStub;
import com.intellij.psi.stubs.IStubElementType;

public class JsgfGrammarNameImpl extends GrammarNameMixin implements JsgfGrammarName {

  public JsgfGrammarNameImpl(@NotNull ASTNode node) {
    super(node);
  }

  public JsgfGrammarNameImpl(@NotNull GrammarNameStub stub, @NotNull IStubElementType type) {
    super(stub, type);
  }

  public void accept(@NotNull JsgfVisitor visitor) {
    visitor.visitGrammarName(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof JsgfVisitor) accept((JsgfVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public PsiElement getIdentifier() {
    return notNullChild(findChildByType(IDENTIFIER));
  }

  @Override
  public String getName() {
    return JsgfPsiImplInjections.getName(this);
  }

}
