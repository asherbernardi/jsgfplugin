package com.asherbernardi.jsgfplugin.psi.stub;

import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.IndexSink;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.asherbernardi.jsgfplugin.JsgfLanguage;
import com.asherbernardi.jsgfplugin.psi.JsgfRuleDeclarationName;
import com.asherbernardi.jsgfplugin.psi.impl.JsgfRuleDeclarationNameImpl;
import java.io.IOException;
import org.jetbrains.annotations.NotNull;

public class RuleDeclarationStubElementType extends IStubElementType<RuleDeclarationStub, JsgfRuleDeclarationName> {

  public static String ID = "jsgf.rule";

  public RuleDeclarationStubElementType() {
    super("RULE", JsgfLanguage.INSTANCE);
  }

  @Override
  public JsgfRuleDeclarationName createPsi(@NotNull RuleDeclarationStub stub) {
    return new JsgfRuleDeclarationNameImpl(stub, this);
  }

  @Override
  public @NotNull RuleDeclarationStub createStub(@NotNull JsgfRuleDeclarationName psi,
      StubElement<?> parentStub) {
    return new RuleDeclarationStubImpl(parentStub, psi.getRuleName(), psi.isPublicRule());
  }

  @Override
  public @NotNull String getExternalId() {
    return ID;
  }

  @Override
  public void serialize(@NotNull RuleDeclarationStub stub, @NotNull StubOutputStream dataStream)
      throws IOException {
    dataStream.writeName(stub.getName());
    dataStream.writeBoolean(stub.isPublicRule());
  }

  @Override
  public @NotNull RuleDeclarationStub deserialize(@NotNull StubInputStream dataStream,
      StubElement parentStub) throws IOException {
    String ruleName = dataStream.readNameString();
    boolean isPublicRule = dataStream.readBoolean();
    return new RuleDeclarationStubImpl(parentStub, ruleName, isPublicRule);
  }

  @Override
  public void indexStub(@NotNull RuleDeclarationStub stub, @NotNull IndexSink sink) {
    sink.occurrence(JsgfStubElementTypes.RULE_INDEX_KEY, stub.getName());
  }

  @SuppressWarnings("unused")
  public static RuleDeclarationStubElementType getInstance(String debugName) {
    return JsgfStubElementTypes.RULE_DECLARATION_STUB_TYPE;
  }
}
