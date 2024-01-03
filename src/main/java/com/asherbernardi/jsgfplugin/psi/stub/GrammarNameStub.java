package com.asherbernardi.jsgfplugin.psi.stub;

import com.intellij.psi.stubs.StubElement;
import com.asherbernardi.jsgfplugin.psi.JsgfGrammarName;

public interface GrammarNameStub extends StubElement<JsgfGrammarName> {

  String getFQGN();
}
