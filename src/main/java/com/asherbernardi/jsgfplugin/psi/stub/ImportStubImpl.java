package com.asherbernardi.jsgfplugin.psi.stub;

import com.intellij.psi.stubs.StubBase;
import com.intellij.psi.stubs.StubElement;
import com.asherbernardi.jsgfplugin.psi.JsgfRuleImportName;
import com.asherbernardi.jsgfplugin.psi.impl.JsgfPsiImplInjections;
import org.jetbrains.annotations.Nullable;

public class ImportStubImpl extends StubBase<JsgfRuleImportName> implements ImportStub {

  private final String fullyQualifiedRuleName;

  protected ImportStubImpl(@Nullable StubElement parent, String fullyQualifiedRuleName) {
    super(parent, JsgfStubElementTypes.IMPORT_STUB_TYPE);
    this.fullyQualifiedRuleName = fullyQualifiedRuleName;
  }

  @Override
  public boolean isStarImport() {
    return getUnqualifiedRuleName().equals("*");
  }

  @Override
  public String getFullyQualifiedRuleName() {
    return fullyQualifiedRuleName;
  }

  @Override
  public String getUnqualifiedRuleName() {
    return JsgfPsiImplInjections.unqualifiedRuleNameFromFQRN(fullyQualifiedRuleName);
  }

  @Override
  public String getFullyQualifiedGrammarName() {
    return JsgfPsiImplInjections.fullyQualifiedGrammarNameFromFQRN(fullyQualifiedRuleName);
  }

  @Override
  public String getSimpleGrammarName() {
    return JsgfPsiImplInjections.simpleGrammarNameFromFQRN(fullyQualifiedRuleName);
  }

  @Override
  public String getPackageName() {
    return JsgfPsiImplInjections.packageNameFromFQRN(fullyQualifiedRuleName);
  }

  @Override
  public String toString() {
    return "ImportStub";
  }
}
