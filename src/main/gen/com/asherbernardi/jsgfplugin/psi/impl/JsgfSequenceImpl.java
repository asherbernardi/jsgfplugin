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

public class JsgfSequenceImpl extends SequenceMixin implements JsgfSequence {

  public JsgfSequenceImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull JsgfVisitor visitor) {
    visitor.visitSequence(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof JsgfVisitor) accept((JsgfVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<JsgfGroup> getGroupList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, JsgfGroup.class);
  }

  @Override
  @NotNull
  public List<JsgfRuleReference> getRuleReferenceList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, JsgfRuleReference.class);
  }

  @Override
  @NotNull
  public List<JsgfString> getStringList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, JsgfString.class);
  }

  @Override
  @NotNull
  public List<JsgfTag> getTagList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, JsgfTag.class);
  }

  @Override
  @NotNull
  public List<JsgfTerminal> getTerminalList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, JsgfTerminal.class);
  }

  @Override
  public Double getWeight() {
    return JsgfPsiImplInjections.getWeight(this);
  }

}
