package com.asherbernardi.jsgfplugin.psi;

import com.asherbernardi.jsgfplugin.JsgfElementFactory;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.NavigatablePsiElement;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.SearchScope;
import com.asherbernardi.jsgfplugin.JsgfIcons;
import javax.swing.Icon;
import org.jetbrains.annotations.NotNull;

public interface RuleName extends PsiElement, NavigatablePsiElement {

  default String getRuleName() {
    return getText();
  }

  default RuleName setRuleName(@NotNull String newName) {
    RuleName newRuleName = JsgfElementFactory.createRule(getProject(), newName);
    getNode().replaceAllChildrenToChildrenOf(newRuleName.getNode());
    return this;
  }

  default ItemPresentation getPresentation() {
    RuleName oldThis = this;
    return new ItemPresentation() {
      @Override
      public String getPresentableText() {
        return "<" + getRuleName() + ">";
      }

      @Override
      public String getLocationString() {
        PsiFile file = getContainingFile();
        return file == null ? "" : file.getName();
      }

      @Override
      public Icon getIcon(boolean unused) {
        return JsgfIcons.FILE;
      }
    };
  }

  @NotNull
  @Override
  SearchScope getUseScope();

  String toString();
}