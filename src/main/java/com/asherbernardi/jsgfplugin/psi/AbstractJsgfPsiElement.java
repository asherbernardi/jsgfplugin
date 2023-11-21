package com.asherbernardi.jsgfplugin.psi;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiReference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractJsgfPsiElement extends ASTWrapperPsiElement {

  private PsiReference[] myReferences;

  public AbstractJsgfPsiElement(@NotNull ASTNode node) {
    super(node);
  }

  private void initRefs() {
//    if (myReferences == null) {
      PsiReference ref = ref();
      if (ref != null) {
        myReferences = new PsiReference[]{ref};
        return;
      }
      PsiReference[] refs = refs();
      if (refs.length == 0) {
        myReferences = PsiReference.EMPTY_ARRAY;
        return;
      }
      myReferences = refs;
//    }
  }

  @Override
  public final PsiReference getReference() {
    initRefs();
    if (myReferences.length == 0) {
      return null;
    }
    return myReferences[0];
  }

  @Override
  public final PsiReference @NotNull [] getReferences() {
    initRefs();
    return myReferences;
  }

  @Nullable
  protected PsiReference ref() {
    return null;
  }

  @NotNull
  protected PsiReference @NotNull [] refs() {
    return PsiReference.EMPTY_ARRAY;
  }

  @Override
  public void subtreeChanged() {
    myReferences = null;
  }
}
