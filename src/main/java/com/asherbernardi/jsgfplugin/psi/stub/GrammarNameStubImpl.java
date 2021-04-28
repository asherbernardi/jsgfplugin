package com.asherbernardi.jsgfplugin.psi.stub;

import com.intellij.psi.stubs.StubBase;
import com.intellij.psi.stubs.StubElement;
import com.asherbernardi.jsgfplugin.psi.JsgfGrammarName;
import org.jetbrains.annotations.Nullable;

public class GrammarNameStubImpl extends StubBase<JsgfGrammarName> implements GrammarNameStub {

  private String name;

  protected GrammarNameStubImpl(@Nullable StubElement parent, String name) {
    super(parent, JsgfStubElementTypes.GRAMMAR_NAME_STUB_TYPE);
    this.name = name;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return "GrammarNameStub";
  }
}
