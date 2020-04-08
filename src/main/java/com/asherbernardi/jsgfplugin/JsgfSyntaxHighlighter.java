package com.asherbernardi.jsgfplugin;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.CodeInsightColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import org.antlr.intellij.adaptor.lexer.PSIElementTypeFactory;
import org.antlr.intellij.adaptor.lexer.TokenIElementType;
import org.jetbrains.annotations.NotNull;

/**
 * Provides syntax highlighting for tokens in Jsgf.
 * @author asherbernardi
 */
public class JsgfSyntaxHighlighter extends SyntaxHighlighterBase {
  private static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];
  public static final TextAttributesKey RULE_NAME =
      createTextAttributesKey("Jsgf_RULE_NAME", DefaultLanguageHighlighterColors.INSTANCE_METHOD);
  public static final TextAttributesKey TERMINAL =
      createTextAttributesKey("Jsgf_TERMINAL", DefaultLanguageHighlighterColors.NUMBER);
  public static final TextAttributesKey LINE_COMMENT =
      createTextAttributesKey("Jsgf_LINE_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT);
  public static final TextAttributesKey BLOCK_COMMENT =
      createTextAttributesKey("Jsgf_BLOCK_COMMENT", DefaultLanguageHighlighterColors.BLOCK_COMMENT);
  public static final TextAttributesKey DOC_COMMENT =
      createTextAttributesKey("Jsgf_DOC_COMMENT", DefaultLanguageHighlighterColors.DOC_COMMENT);
  public static final TextAttributesKey DOC_TAG =
      createTextAttributesKey("Jsgf_DOC_TAG", DefaultLanguageHighlighterColors.DOC_COMMENT_TAG);
  public static final TextAttributesKey STRING =
      createTextAttributesKey("Jsgf_STRING", DefaultLanguageHighlighterColors.STRING);
  public static final TextAttributesKey ID =
      createTextAttributesKey("Jsgf_ID", DefaultLanguageHighlighterColors.IDENTIFIER);
  public static final TextAttributesKey KEYWORD =
      createTextAttributesKey("Jsgf_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD);
  public static final TextAttributesKey TAG =
      createTextAttributesKey("Jsgf_TAG", DefaultLanguageHighlighterColors.TEMPLATE_LANGUAGE_COLOR);
  public static final TextAttributesKey BAD_CHARACTER =
      createTextAttributesKey("Jsgf_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER);
  public static final TextAttributesKey WEIGHT =
      createTextAttributesKey("Jsgf_WEIGHT", DefaultLanguageHighlighterColors.INLINE_PARAMETER_HINT_HIGHLIGHTED);
  public static final TextAttributesKey BAD_REFERENCE =
      createTextAttributesKey("Jsgf_BAD_REFERENCE", CodeInsightColors.WRONG_REFERENCES_ATTRIBUTES);

  public static final String[] keywordTags = {"@author", "@version", "@see", "@example"};

  static {
    PSIElementTypeFactory.defineLanguageIElementTypes(JsgfLanguage.INSTANCE,
                                                      JsgfParser.tokenNames,
                                                      JsgfParser.ruleNames);
  }

  @NotNull
  @Override
  public Lexer getHighlightingLexer() {
    return new JsgfLexerAdapter();
  }

  @NotNull
  @Override
  public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
    TextAttributesKey attrKey;
    if (tokenType instanceof TokenIElementType) {
      TokenIElementType myType = (TokenIElementType) tokenType;
      int ttype = myType.getANTLRTokenType();
      switch ( ttype ) {
        case JsgfLexer.IDENTIFIER:
          attrKey = ID;
          break;
        case JsgfLexer.GRAMMAR:
        case JsgfLexer.IMPORT:
        case JsgfLexer.PUBLIC:
        case JsgfLexer.VERSION:
        case JsgfLexer.JSGF_IDENT:
        case JsgfLexer.SEMICOLON:
          attrKey = KEYWORD;
          break;
        case JsgfLexer.OPENARROW:
        case JsgfLexer.CLOSEARROW:
        case JsgfLexer.RULE_NAME_IDENTIFIER:
          attrKey = RULE_NAME;
          break;
        case JsgfLexer.TERMINAL:
          attrKey = TERMINAL;
          break;
        case JsgfLexer.QUOTE:
        case JsgfLexer.STRING_TEXT:
          attrKey = STRING;
          break;
        case JsgfLexer.COMMENT:
          attrKey = LINE_COMMENT;
          break;
        case JsgfLexer.BLOCKCOMMENT:
          attrKey = BLOCK_COMMENT;
          break;
        case JsgfLexer.DOCCOMMENT:
          attrKey = DOC_COMMENT;
          break;
        case JsgfLexer.WEIGHT:
          attrKey = WEIGHT;
          break;
        case JsgfLexer.BAD_CHARACTER:
          attrKey = BAD_CHARACTER;
          break;
        case JsgfLexer.OPENCURLY:
        case JsgfLexer.CLOSECURLY:
        case JsgfLexer.TAG_TEXT:
          attrKey = TAG;
          break;
        default:
          return EMPTY_KEYS;
      }
    }
    else {
      return EMPTY_KEYS;
    }
    return new TextAttributesKey[] {attrKey};
  }
}