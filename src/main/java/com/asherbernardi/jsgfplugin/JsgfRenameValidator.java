package com.asherbernardi.jsgfplugin;

import com.intellij.lexer.Lexer;
import com.intellij.patterns.ElementPattern;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.refactoring.rename.RenameInputValidator;
import com.intellij.util.ProcessingContext;
import com.asherbernardi.jsgfplugin.psi.JsgfBnfTypes;
import com.asherbernardi.jsgfplugin.psi.JsgfGrammarName;
import com.asherbernardi.jsgfplugin.psi.RuleName;
import org.jetbrains.annotations.NotNull;

/**
 * Tells Intellij which characters are allowed to be in a rule name, so the rename
 * refactoring can fail if an illegal character is provided.
 */
public class JsgfRenameValidator implements RenameInputValidator {

  private final static ElementPattern<PsiElement> pattern = PlatformPatterns.psiElement().withElementType(TokenSet.create(JsgfBnfTypes.RULE_REFERENCE_NAME, JsgfBnfTypes.RULE_DECLARATION_NAME, JsgfBnfTypes.RULE_IMPORT_NAME, JsgfBnfTypes.GRAMMAR_NAME));

  @Override
  public @NotNull ElementPattern<? extends PsiElement> getPattern() {
    return pattern;
  }

  @Override
  public boolean isInputValid(@NotNull String newName, @NotNull PsiElement element,
      @NotNull ProcessingContext context) {
    if (element instanceof RuleName) {
      Lexer lex = new JsgfLexerAdapter();
      lex.start('<' + newName + '>');
      lex.advance();
      IElementType type = lex.getTokenType();
      String parsed = lex.getTokenText();
      return type == JsgfBnfTypes.RULE_NAME_IDENTIFIER && newName.equals(parsed);
    }
    if (element instanceof JsgfGrammarName) {
      Lexer lex = new JsgfLexerAdapter();
      lex.start(newName);
      IElementType type = lex.getTokenType();
      String parsed = lex.getTokenText();
      return type == JsgfBnfTypes.IDENTIFIER && newName.equals(parsed);
    }
    return false;
  }
}
