package com.asherbernardi.jsgfplugin.psi;

import com.intellij.extapi.psi.StubBasedPsiElementBase;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiReference;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractJsgfStubElement<T extends StubElement<?>> extends StubBasedPsiElementBase<T> {

  public AbstractJsgfStubElement(@NotNull ASTNode node) {
    super(node);
  }

  public AbstractJsgfStubElement(@NotNull T stub, @NotNull IStubElementType nodeType) {
    super(stub, nodeType);
  }

  @Override
  public final PsiReference getReference() {
    PsiReference ref = ref();
    if (ref != null) return ref;
    PsiReference[] refs = refs();
    if (refs.length == 0) return null;
    return refs[0];
  }

  @Override
  public final PsiReference @NotNull [] getReferences() {
    PsiReference[] refs = refs();
    if (refs.length > 0) return refs;
    PsiReference ref = ref();
    if (ref != null) return new PsiReference[]{ref};
    return PsiReference.EMPTY_ARRAY;
  }

  @Nullable
  protected PsiReference ref() {
    return null;
  }

  @NotNull
  protected PsiReference @NotNull [] refs() {
    return PsiReference.EMPTY_ARRAY;
  }
}
