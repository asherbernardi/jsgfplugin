package com.asherbernardi.jsgfplugin;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

import com.asherbernardi.jsgfplugin.psi.JsgfBnfTypes;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.CodeInsightColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

/**
 * Provides syntax highlighting for tokens in Jsgf.
 * @author asherbernardi
 */
public class JsgfSyntaxHighlighter extends SyntaxHighlighterBase {
  private static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];

  public static final String[] keywordTags = {"@author", "@version", "@see", "@example"};

  @NotNull
  @Override
  public Lexer getHighlightingLexer() {
    return new JsgfLexerAdapter();
  }

  @NotNull
  @Override
  public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
    JsgfHighlightType highlightType = JsgfHighlightType.fromTokenType(tokenType);
    if (highlightType != null) {
      return new TextAttributesKey[] {highlightType.getTextAttributesKey()};
    }
    return EMPTY_KEYS;
  }

  public enum JsgfHighlightType {
    RULE_NAME(createTextAttributesKey("Jsgf_RULE_NAME", DefaultLanguageHighlighterColors.INSTANCE_METHOD),
        JsgfBnfTypes.LANGLE,
        JsgfBnfTypes.RANGLE,
        JsgfBnfTypes.RULE_NAME_IDENTIFIER),
    TERMINAL(createTextAttributesKey("Jsgf_TERMINAL", DefaultLanguageHighlighterColors.NUMBER),
        JsgfBnfTypes.TERMINAL_IDENTIFIER),
    LINE_COMMENT(createTextAttributesKey("Jsgf_LINE_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT),
        JsgfParserDefinition.LINE_COMMENT),
    BLOCK_COMMENT(createTextAttributesKey("Jsgf_BLOCK_COMMENT", DefaultLanguageHighlighterColors.BLOCK_COMMENT),
        JsgfParserDefinition.BLOCK_COMMENT),
    DOC_COMMENT(createTextAttributesKey("Jsgf_DOC_COMMENT", DefaultLanguageHighlighterColors.DOC_COMMENT),
        JsgfParserDefinition.DOC_COMMENT),
    DOC_TAG(createTextAttributesKey("Jsgf_DOC_TAG", DefaultLanguageHighlighterColors.DOC_COMMENT_TAG)),
    STRING(createTextAttributesKey("Jsgf_STRING", DefaultLanguageHighlighterColors.STRING),
        JsgfBnfTypes.QUOTE,
        JsgfBnfTypes.STRING_TEXT),
    ID(createTextAttributesKey("Jsgf_ID", DefaultLanguageHighlighterColors.IDENTIFIER),
        JsgfBnfTypes.IDENTIFIER),
    KEYWORD(createTextAttributesKey("Jsgf_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD),
        JsgfBnfTypes.GRAMMAR,
        JsgfBnfTypes.INCLUDE,
        JsgfBnfTypes.IMPORT,
        JsgfBnfTypes.PUBLIC,
        JsgfBnfTypes.VERSION,
        JsgfBnfTypes.JSGF_IDENT,
        JsgfBnfTypes.SEMICOLON),
    TAG(createTextAttributesKey("Jsgf_TAG", DefaultLanguageHighlighterColors.TEMPLATE_LANGUAGE_COLOR)),
    BAD_CHARACTER(createTextAttributesKey("Jsgf_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER),
        TokenType.BAD_CHARACTER,
        JsgfBnfTypes.STRING_NL),
    WEIGHT(createTextAttributesKey("Jsgf_WEIGHT", DefaultLanguageHighlighterColors.INLINE_PARAMETER_HINT_HIGHLIGHTED),
        JsgfBnfTypes.WEIGHT),
    BAD_REFERENCE(createTextAttributesKey("Jsgf_BAD_REFERENCE", CodeInsightColors.WRONG_REFERENCES_ATTRIBUTES)),
    ;

    private final TextAttributesKey textAttributesKey;
    private final IElementType[] tokenTypes;
    private static final Map<IElementType, JsgfHighlightType> elementTypeMap = new HashMap<>();
    private static final Map<TextAttributesKey, JsgfHighlightType> textAttributeKeyMap = new HashMap<>();

    static {
      for (JsgfHighlightType jht : values()) {
        for (IElementType elementType : jht.tokenTypes) {
          elementTypeMap.put(elementType, jht);
        }
        textAttributeKeyMap.put(jht.textAttributesKey, jht);
      }
    }

    JsgfHighlightType(TextAttributesKey textAttributesKey, IElementType... tokenTypes) {
      this.textAttributesKey = textAttributesKey;
      this.tokenTypes = tokenTypes;
    }

    public TextAttributesKey getTextAttributesKey() {
      return textAttributesKey;
    }

    public IElementType[] getTokenTypes() {
      return tokenTypes;
    }

    public static JsgfHighlightType fromTokenType(IElementType elementType) {
      return elementTypeMap.get(elementType);
    }

    public static JsgfHighlightType fromTextAttributesKey(TextAttributesKey textAttributesKey) {
      return textAttributeKeyMap.get(textAttributesKey);
    }
  }
}