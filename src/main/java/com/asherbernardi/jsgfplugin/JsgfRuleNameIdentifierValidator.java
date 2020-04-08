package com.asherbernardi.jsgfplugin;

import com.asherbernardi.jsgfplugin.psi.JsgfTypes;
import com.intellij.lang.refactoring.NamesValidator;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import org.antlr.intellij.adaptor.lexer.ANTLRLexerAdaptor;
import org.antlr.intellij.adaptor.lexer.TokenIElementType;
import org.jetbrains.annotations.NotNull;

/**
 * Tells Intellij which characters are allowed to be in a rule name, so the rename
 * refactoring can fail if an illegal character is provided.
 */
public class JsgfRuleNameIdentifierValidator implements NamesValidator {

  @Override
  public boolean isKeyword(@NotNull String s, Project project) {
    return JsgfTypes.KEYWORD_LITERALS.contains(s);
  }

  @Override
  public boolean isIdentifier(@NotNull String s, Project project) {
    Lexer lex = new ANTLRLexerAdaptor(JsgfLanguage.INSTANCE, new JsgfLexer(null));
    lex.start('<' + s + '>');
    lex.advance();
    TokenIElementType type = ((TokenIElementType) lex.getTokenType());
    String parsed = lex.getTokenText();
    return (type.getANTLRTokenType() == JsgfLexer.RULE_NAME_IDENTIFIER ||
        type.getANTLRTokenType() == JsgfLexer.IDENTIFIER) && s.equals(parsed);
  }
}
