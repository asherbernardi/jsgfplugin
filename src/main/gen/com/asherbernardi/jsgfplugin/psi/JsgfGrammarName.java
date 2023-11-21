// This is a generated file. Not intended for manual editing.
package com.asherbernardi.jsgfplugin.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import com.asherbernardi.jsgfplugin.psi.stub.GrammarNameStub;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.search.SearchScope;

public interface JsgfGrammarName extends GrammarName, StubBasedPsiElement<GrammarNameStub> {

  @NotNull
  PsiElement getIdentifier();

  @NotNull String getName();

  PsiElement setName(@NotNull String newName);

  ItemPresentation getPresentation();

  @NotNull SearchScope getUseScope();

  @Nullable PsiElement getNameIdentifier();

}
