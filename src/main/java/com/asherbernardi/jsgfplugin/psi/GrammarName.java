package com.asherbernardi.jsgfplugin.psi;

import com.intellij.psi.StubBasedPsiElement;
import com.asherbernardi.jsgfplugin.psi.stub.GrammarNameStub;

public interface GrammarName extends StubBasedPsiElement<GrammarNameStub>{

  String getName();

}