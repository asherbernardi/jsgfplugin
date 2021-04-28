// This is a generated file. Not intended for manual editing.
package com.asherbernardi.jsgfplugin.psi;

import com.intellij.psi.StubBasedPsiElement;
import com.asherbernardi.jsgfplugin.psi.stub.ImportStub;

public interface ImportName extends StubBasedPsiElement<ImportStub>, RuleName {

  boolean isStarImport();

  default String getFullyQualifiedRuleName() {
    final ImportStub stub = getStub();
    if (stub != null) {
      return stub.getFullyQualifiedRuleName();
    }
    return getRuleName();
  }

  String getUnqualifiedRuleName();

  String getFullyQualifiedGrammarName();

  String getSimpleGrammarName();

  String getPackageName();

}
