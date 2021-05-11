package com.asherbernardi.jsgfplugin.psi.stub;

import com.asherbernardi.jsgfplugin.psi.JsgfRuleImportName;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.IndexSink;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.asherbernardi.jsgfplugin.JsgfLanguage;
import com.asherbernardi.jsgfplugin.psi.impl.JsgfRuleImportNameImpl;
import java.io.IOException;
import org.jetbrains.annotations.NotNull;

public class ImportStubElementType extends IStubElementType<ImportStub, JsgfRuleImportName> {

  public static String ID = "jsgf.import";

  public ImportStubElementType() {
    super("IMPORT", JsgfLanguage.INSTANCE);
  }

  @Override
  public JsgfRuleImportName createPsi(@NotNull ImportStub stub) {
    return new JsgfRuleImportNameImpl(stub, this);
  }

  @Override
  public @NotNull ImportStub createStub(@NotNull JsgfRuleImportName psi,
      StubElement<?> parentStub) {
    return new ImportStubImpl(parentStub, psi.getRuleName());
  }

  @Override
  public @NotNull String getExternalId() {
    return ID;
  }

  @Override
  public void serialize(@NotNull ImportStub stub, @NotNull StubOutputStream dataStream)
      throws IOException {
    dataStream.writeName(stub.getFullyQualifiedRuleName());
  }

  @Override
  public @NotNull ImportStub deserialize(@NotNull StubInputStream dataStream,
      StubElement parentStub) throws IOException {
    String ruleName = dataStream.readNameString();
    return new ImportStubImpl(parentStub, ruleName);
  }

  @Override
  public void indexStub(@NotNull ImportStub stub, @NotNull IndexSink sink) {
    sink.occurrence(JsgfStubElementTypes.IMPORT_INDEX_KEY, stub.getFullyQualifiedRuleName());
  }

  @SuppressWarnings("unused")
  public static ImportStubElementType getInstance(String debugName) {
    return JsgfStubElementTypes.IMPORT_STUB_TYPE;
  }
}
