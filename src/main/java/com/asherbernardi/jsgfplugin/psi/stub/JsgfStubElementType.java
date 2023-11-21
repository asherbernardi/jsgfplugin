package com.asherbernardi.jsgfplugin.psi.stub;

import com.intellij.lang.Language;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class JsgfStubElementType<S extends StubElement<P>, P extends PsiElement> extends
    IStubElementType<S, P> {

  public static final String ID_PREFIX = "jsgf.grammar";

  public JsgfStubElementType(
      @NotNull @NonNls String debugName,
      @Nullable Language language) {
    super(debugName, language);
  }

  @Override
  public @NotNull String getExternalId() {
    return ID_PREFIX + this;
  }
}
