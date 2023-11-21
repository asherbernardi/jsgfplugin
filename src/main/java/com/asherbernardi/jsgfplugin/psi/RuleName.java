package com.asherbernardi.jsgfplugin.psi;

import com.asherbernardi.jsgfplugin.JsgfElementFactory;
import com.asherbernardi.jsgfplugin.psi.impl.JsgfPsiImplInjections;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.NavigatablePsiElement;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.SearchScope;
import com.asherbernardi.jsgfplugin.JsgfIcons;
import com.intellij.util.IncorrectOperationException;
import javax.swing.Icon;
import org.jetbrains.annotations.NotNull;

public interface RuleName extends PsiElement, NavigatablePsiElement {

  /**
   * @return the unqualified rule name of this rule. It may be the same as getText(), if
   * this rule is already unqualified.
   */
  default String getRuleName() {
    return JsgfPsiImplInjections.unqualifiedRuleNameFromFQRN(getFQRN());
  }

  /**
   * @return The fully qualified rule name of this rule. Equal to the text of the element
   */
  default String getFQRN() {
    return getText();
  }

  default PsiElement setRuleName(@NotNull String newName) throws IncorrectOperationException {
    RuleName newRuleName = JsgfElementFactory.createRule(getProject(), newName);
    getNode().replaceAllChildrenToChildrenOf(newRuleName.getNode());
    return this;
  }

  String toString();
}