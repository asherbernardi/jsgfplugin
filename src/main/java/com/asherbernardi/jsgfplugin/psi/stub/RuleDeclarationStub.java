package com.asherbernardi.jsgfplugin.psi.stub;

import com.intellij.psi.stubs.StubElement;
import com.asherbernardi.jsgfplugin.psi.JsgfRuleDeclarationName;

public interface RuleDeclarationStub extends StubElement<JsgfRuleDeclarationName> {

  String getName();

  boolean isPublicRule();
}
