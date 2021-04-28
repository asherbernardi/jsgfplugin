package com.asherbernardi.jsgfplugin.psi.stub;

import com.intellij.psi.stubs.StubBase;
import com.intellij.psi.stubs.StubElement;
import com.asherbernardi.jsgfplugin.psi.JsgfRuleDeclarationName;
import org.jetbrains.annotations.Nullable;

public class RuleDeclarationStubImpl extends StubBase<JsgfRuleDeclarationName> implements RuleDeclarationStub {

  private String name;
  private boolean isPublic;

  protected RuleDeclarationStubImpl(@Nullable StubElement parent, String name, boolean isPublic) {
    super(parent, JsgfStubElementTypes.RULE_DECLARATION_STUB_TYPE);
    this.name = name;
    this.isPublic = isPublic;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public boolean isPublicRule() {
    return isPublic;
  }

  @Override
  public String toString() {
    return "RuleDeclarationStub" + (isPublicRule() ? " (public)" : "");
  }
}
