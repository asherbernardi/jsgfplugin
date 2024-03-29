package com.asherbernardi.jsgfplugin.psi.stub;

import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.IndexSink;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.asherbernardi.jsgfplugin.JsgfLanguage;
import com.asherbernardi.jsgfplugin.psi.JsgfGrammarName;
import com.asherbernardi.jsgfplugin.psi.impl.JsgfGrammarNameImpl;
import java.io.IOException;
import org.jetbrains.annotations.NotNull;

public class GrammarNameStubElementType extends JsgfStubElementType<GrammarNameStub, JsgfGrammarName> {

  public GrammarNameStubElementType() {
    super("GRAMMAR_NAME_STUB", JsgfLanguage.INSTANCE);
  }

  @Override
  public JsgfGrammarName createPsi(@NotNull GrammarNameStub stub) {
    return new JsgfGrammarNameImpl(stub, this);
  }

  @Override
  public @NotNull GrammarNameStub createStub(@NotNull JsgfGrammarName psi,
      StubElement<?> parentStub) {
    return new GrammarNameStubImpl(parentStub, psi.getFQGN());
  }

  @Override
  public void serialize(@NotNull GrammarNameStub stub, @NotNull StubOutputStream dataStream)
      throws IOException {
    dataStream.writeName(stub.getFQGN());
  }

  @Override
  public @NotNull GrammarNameStub deserialize(@NotNull StubInputStream dataStream,
      StubElement parentStub) throws IOException {
    String ruleName = dataStream.readNameString();
    return new GrammarNameStubImpl(parentStub, ruleName);
  }

  @Override
  public void indexStub(@NotNull GrammarNameStub stub, @NotNull IndexSink sink) {
    sink.occurrence(JsgfStubElementTypes.GRAMMAR_INDEX_KEY, stub.getFQGN());
  }

  @SuppressWarnings("unused")
  public static GrammarNameStubElementType getInstance(String debugName) {
    return JsgfStubElementTypes.GRAMMAR_NAME_STUB_TYPE;
  }
}
