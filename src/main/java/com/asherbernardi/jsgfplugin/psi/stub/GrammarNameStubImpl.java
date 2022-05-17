package com.asherbernardi.jsgfplugin.psi.stub;

import com.asherbernardi.jsgfplugin.psi.impl.JsgfPsiImplInjections;
import com.intellij.psi.stubs.StubBase;
import com.intellij.psi.stubs.StubElement;
import com.asherbernardi.jsgfplugin.psi.JsgfGrammarName;
import org.jetbrains.annotations.Nullable;

public class GrammarNameStubImpl extends StubBase<JsgfGrammarName> implements GrammarNameStub {

  private final String fqgn;

  protected GrammarNameStubImpl(@Nullable StubElement parent, String fqgn) {
    super(parent, JsgfStubElementTypes.GRAMMAR_NAME_STUB_TYPE);
    this.fqgn = fqgn;
  }

  @Override
  public String getFQGN() {
    return fqgn;
  }

  @Override
  public String toString() {
    return "GrammarNameStub";
  }
}
