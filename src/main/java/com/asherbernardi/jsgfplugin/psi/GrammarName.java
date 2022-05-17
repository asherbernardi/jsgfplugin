package com.asherbernardi.jsgfplugin.psi;

import com.intellij.psi.NavigatablePsiElement;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import com.asherbernardi.jsgfplugin.JsgfElementFactory;
import com.asherbernardi.jsgfplugin.psi.impl.JsgfPsiImplInjections;
import com.asherbernardi.jsgfplugin.psi.stub.GrammarNameStub;
import org.jetbrains.annotations.NotNull;

public interface GrammarName extends StubBasedPsiElement<GrammarNameStub>, NavigatablePsiElement {

  /**
   * @return the simple grammar name for this grammar declaration
   */
  @NotNull
  default String getSimpleGrammarName() {
    return JsgfPsiImplInjections.simpleGrammarNameFromFQGN(getFQGN());
  }

  default String getPackageName() {
    return JsgfPsiImplInjections.packageNameFromFQGN(getFQGN());
  }

  default String getFQGN() {
    return getText();
  }

  default PsiElement setSimpleGrammarName(@NotNull String newSimpleName) {
    String packageName = getPackageName();
    return setFQGN((!packageName.isEmpty() ? packageName + "." : "") + newSimpleName);
  }

  default PsiElement setFQGN(@NotNull String newFQGN) {
    GrammarName newGrammarName = JsgfElementFactory.createGrammarName(getProject(),
        newFQGN);
    getNode().replaceAllChildrenToChildrenOf(newGrammarName.getNode());
    return this;
  }

}