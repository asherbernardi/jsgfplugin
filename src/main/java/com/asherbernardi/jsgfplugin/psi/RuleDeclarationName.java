package com.asherbernardi.jsgfplugin.psi;

import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.StubBasedPsiElement;
import com.asherbernardi.jsgfplugin.psi.stub.RuleDeclarationStub;

public interface RuleDeclarationName extends StubBasedPsiElement<RuleDeclarationStub>, RuleName,
    PsiNamedElement {

  boolean isPublicRule();
}