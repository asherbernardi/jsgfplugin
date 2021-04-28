package com.asherbernardi.jsgfplugin.psi.stub;

import com.intellij.psi.stubs.StubElement;
import com.asherbernardi.jsgfplugin.psi.JsgfRuleImportName;

public interface ImportStub extends StubElement<JsgfRuleImportName> {

  boolean isStarImport();

  String getFullyQualifiedRuleName();

  String getUnqualifiedRuleName();

  String getFullyQualifiedGrammarName();

  String getSimpleGrammarName();

  String getPackageName();
}
