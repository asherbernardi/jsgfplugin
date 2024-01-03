// This is a generated file. Not intended for manual editing.
package com.asherbernardi.jsgfplugin.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import com.asherbernardi.jsgfplugin.psi.stub.ImportStub;
import com.asherbernardi.jsgfplugin.psi.reference.OtherFileReferencePair;

public interface JsgfRuleImportName extends RuleName, StubBasedPsiElement<ImportStub> {

  boolean isStarImport();

  String getUnqualifiedRuleName();

  String getFullyQualifiedGrammarName();

  String getSimpleGrammarName();

  String getPackageName();

  OtherFileReferencePair getReferencePair();

}
