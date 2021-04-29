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

public class GrammarNameStubElementType extends IStubElementType<GrammarNameStub, JsgfGrammarName> {

  public static String ID = "jsgf.grammar";

  public GrammarNameStubElementType() {
    super("GRAMMAR", JsgfLanguage.INSTANCE);
  }

  @Override
  public JsgfGrammarName createPsi(@NotNull GrammarNameStub stub) {
    return new JsgfGrammarNameImpl(stub, this);
  }

  @Override
  public @NotNull GrammarNameStub createStub(@NotNull JsgfGrammarName psi,
      StubElement<?> parentStub) {
    return new GrammarNameStubImpl(parentStub, psi.getName());
  }

  @Override
  public @NotNull String getExternalId() {
    return ID;
  }

  @Override
  public void serialize(@NotNull GrammarNameStub stub, @NotNull StubOutputStream dataStream)
      throws IOException {
    dataStream.writeName(stub.getName());
  }

  @Override
  public @NotNull GrammarNameStub deserialize(@NotNull StubInputStream dataStream,
      StubElement parentStub) throws IOException {
    String ruleName = dataStream.readNameString();
    return new GrammarNameStubImpl(parentStub, ruleName);
  }

  @Override
  public void indexStub(@NotNull GrammarNameStub stub, @NotNull IndexSink sink) {
    sink.occurrence(JsgfStubElementTypes.GRAMMAR_INDEX_KEY, stub.getName());
  }

  public static GrammarNameStubElementType getInstance() {
    return JsgfStubElementTypes.GRAMMAR_NAME_STUB_TYPE;
  }
}
