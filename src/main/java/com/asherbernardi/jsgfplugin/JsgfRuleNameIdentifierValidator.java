package com.asherbernardi.jsgfplugin;

import com.asherbernardi.jsgfplugin.psi.JsgfBnfTypes;
import com.asherbernardi.jsgfplugin.psi.JsgfTypes;
import com.intellij.lang.refactoring.NamesValidator;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

/**
 * Tells Intellij which characters are allowed to be in a rule name, so the rename
 * refactoring can fail if an illegal character is provided.
 */
public class JsgfRuleNameIdentifierValidator implements NamesValidator {

  @Override
  public boolean isKeyword(@NotNull String s, Project project) {
    Lexer lex = new JsgfLexerAdapter();
    lex.start('<' + s + '>');
    lex.advance();
    IElementType type = lex.getTokenType();
    String parsed = lex.getTokenText();
    return JsgfTypes.KEYWORDS.contains(type) && s.equals(parsed);
  }

  @Override
  public boolean isIdentifier(@NotNull String s, Project project) {
    Lexer lex = new JsgfLexerAdapter();
    lex.start('<' + s + '>');
    lex.advance();
    IElementType type = lex.getTokenType();
    String parsed = lex.getTokenText();
    return (type == JsgfBnfTypes.RULE_NAME_IDENTIFIER ||
        type == JsgfBnfTypes.IDENTIFIER) && s.equals(parsed);
  }
}
