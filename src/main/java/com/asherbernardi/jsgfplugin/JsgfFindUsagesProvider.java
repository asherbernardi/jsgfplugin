package com.asherbernardi.jsgfplugin;

import com.asherbernardi.jsgfplugin.psi.impl.RuleDeclarationSubtree;
import com.intellij.lang.cacheBuilder.WordsScanner;
import com.intellij.lang.findUsages.FindUsagesProvider;
import com.intellij.openapi.util.Condition;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.asherbernardi.jsgfplugin.psi.impl.RuleDeclarationNameElement;
import com.asherbernardi.jsgfplugin.psi.impl.RuleNameElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Helps find the usages of a rule name declaration.
 * @author asherbernardi
 */
public class JsgfFindUsagesProvider implements FindUsagesProvider {
  @Nullable
  @Override
  public WordsScanner getWordsScanner() {
    // Default word scanner seems to be fine
    return null;
  }

  @Override
  public boolean canFindUsagesFor(@NotNull PsiElement psiElement) {
    return (psiElement instanceof RuleDeclarationNameElement);
  }

  @Nullable
  @Override
  public String getHelpId(@NotNull PsiElement psiElement) {
    return null;
  }

  @NotNull
  @Override
  public String getType(@NotNull PsiElement element) {
    if (element instanceof RuleNameElement)
      return "jsgf rule";
    return "";
  }

  @NotNull
  @Override
  public String getDescriptiveName(@NotNull PsiElement element) {
    if (element instanceof RuleNameElement)
      return element.getText();
    return "";
  }

  @NotNull
  @Override
  public String getNodeText(@NotNull PsiElement element, boolean useFullName) {
    if (element instanceof RuleNameElement) {
      return PsiTreeUtil.findFirstParent(element, new Condition<PsiElement>() {
        @Override
        public boolean value(PsiElement psiElement) {
          if (psiElement instanceof RuleDeclarationSubtree)
            return true;
          return false;
        }
      }).getText();
    } else {
      return "";
    }
  }
}