package com.asherbernardi.jsgfplugin.psi.impl;

import com.asherbernardi.jsgfplugin.JsgfIcons;
import com.intellij.extapi.psi.StubBasedPsiElementBase;
import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.stubs.IStubElementType;
import com.asherbernardi.jsgfplugin.psi.JsgfGrammarName;
import com.asherbernardi.jsgfplugin.psi.stub.GrammarNameStub;
import javax.swing.Icon;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class GrammarNameMixin extends StubBasedPsiElementBase<GrammarNameStub>
    implements JsgfGrammarName, PsiNameIdentifierOwner {

  public GrammarNameMixin(@NotNull ASTNode node) {
    super(node);
  }

  public GrammarNameMixin(@NotNull GrammarNameStub stub, @NotNull IStubElementType nodeType) {
    super(stub, nodeType);
  }

  @NotNull
  public String getName() {
    return getFQGN();
  }

  @Override
  public String getFQGN() {
    GrammarNameStub stub = getStub();
    if (stub != null) stub.getFQGN();
    return getText();
  }

  @Override
  public PsiElement setName(@NotNull String newName) {
    return setFQGN(newName);
  }

  public @NotNull SearchScope getUseScope() {
    return GlobalSearchScope.allScope(getProject());
  }

  public ItemPresentation getPresentation() {
    return new ItemPresentation() {
      @Override
      public String getPresentableText() {
        return getName();
      }

      @Override
      public String getLocationString() {
        return getContainingFile().getName();
      }

      @Override
      public Icon getIcon(boolean unused) {
        return JsgfIcons.FILE;
      }
    };
  }

  @Nullable
  public PsiElement getNameIdentifier() {
    return this;
  }

  @Override
  public String toString() {
    return "GrammarName: " + getFQGN();
  }
}
