// This is a generated file. Not intended for manual editing.
package com.asherbernardi.jsgfplugin.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import com.asherbernardi.jsgfplugin.psi.stub.RuleDeclarationStub;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.search.SearchScope;
import com.intellij.util.IncorrectOperationException;
import javax.swing.Icon;

public interface JsgfRuleDeclarationName extends RuleDeclarationName, StubBasedPsiElement<RuleDeclarationStub> {

  @NotNull
  PsiElement getRuleNameIdentifier();

  String getName();

  PsiElement setName(@NotNull String name) throws IncorrectOperationException;

  boolean isPublicRule();

  ItemPresentation getPresentation();

  @NotNull
  Icon getElementIcon(int flags);

  @Nullable
  PsiElement getNameIdentifier();

  @NotNull
  SearchScope getUseScope();

}
