// This is a generated file. Not intended for manual editing.
package com.asherbernardi.jsgfplugin.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import com.asherbernardi.jsgfplugin.psi.stub.ImportStub;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;

public interface ImportName extends StubBasedPsiElement<ImportStub>, RuleName {

  boolean isStarImport();

  default String getFullyQualifiedRuleName() {
    final ImportStub stub = getStub();
    if (stub != null) {
      return stub.getFullyQualifiedRuleName();
    }
    return getRuleName();
  }

  @Override
  default ImportName setRuleName(@NotNull String newName) throws IncorrectOperationException {
    if (isStarImport()) {
      throw new IncorrectOperationException("Cannot rename a '*' import");
    }
    return (ImportName) RuleName.super.setRuleName(newName);
  }

  String getUnqualifiedRuleName();

  String getFullyQualifiedGrammarName();

  String getSimpleGrammarName();

  String getPackageName();

}
