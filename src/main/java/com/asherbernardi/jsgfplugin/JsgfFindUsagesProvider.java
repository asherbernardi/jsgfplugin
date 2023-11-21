package com.asherbernardi.jsgfplugin;

import com.asherbernardi.jsgfplugin.psi.GrammarName;
import com.asherbernardi.jsgfplugin.psi.JsgfGrammarDeclaration;
import com.asherbernardi.jsgfplugin.psi.JsgfGrammarName;
import com.asherbernardi.jsgfplugin.psi.JsgfRuleDefinition;
import com.asherbernardi.jsgfplugin.psi.JsgfTypes;
import com.intellij.lang.cacheBuilder.DefaultWordsScanner;
import com.intellij.lang.cacheBuilder.WordsScanner;
import com.intellij.lang.findUsages.FindUsagesProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.asherbernardi.jsgfplugin.psi.JsgfRuleDeclarationName;
import com.asherbernardi.jsgfplugin.psi.RuleName;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Helps find the usages of a rule name declaration and grammar declarations.
 * @author asherbernardi
 */
public class JsgfFindUsagesProvider implements FindUsagesProvider {
  @Nullable
  @Override
  public WordsScanner getWordsScanner() {
    // Default word scanner seems to be fine
    return new DefaultWordsScanner(new JsgfLexerAdapter(),
        JsgfTypes.IDENTIFIERS,
        JsgfTypes.COMMENTS,
        JsgfTypes.LITERALS);
  }

  @Override
  public boolean canFindUsagesFor(@NotNull PsiElement psiElement) {
    return psiElement instanceof JsgfRuleDeclarationName || psiElement instanceof JsgfGrammarName;
  }

  @Nullable
  @Override
  public String getHelpId(@NotNull PsiElement psiElement) {
    return null;
  }

  @NotNull
  @Override
  public String getType(@NotNull PsiElement element) {
    if (element instanceof RuleName)
      return "jsgf rule";
    if (element instanceof GrammarName)
      return "jsgf grammar";
    return "";
  }

  @NotNull
  @Override
  public String getDescriptiveName(@NotNull PsiElement element) {
    if (element instanceof RuleName)
      return ((RuleName) element).getName();
    if (element instanceof GrammarName)
      return ((GrammarName) element).getName();
    return "";
  }

  @NotNull
  @Override
  public String getNodeText(@NotNull PsiElement element, boolean useFullName) {
    if (element instanceof RuleName) {
      PsiElement parent = PsiTreeUtil.findFirstParent(element, parentEl -> parentEl instanceof JsgfRuleDefinition);
      if (parent != null) {
        return parent.getText();
      }
    }
    if (element instanceof GrammarName) {
      PsiElement parent = PsiTreeUtil.findFirstParent(element, parentEl -> parentEl instanceof JsgfGrammarDeclaration);
      if (parent != null) {
        return parent.getText();
      }
    }
    return "";
  }
}