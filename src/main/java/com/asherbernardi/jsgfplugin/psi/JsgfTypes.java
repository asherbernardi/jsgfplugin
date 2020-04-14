package com.asherbernardi.jsgfplugin.psi;

import com.asherbernardi.jsgfplugin.JsgfLanguage;
import com.asherbernardi.jsgfplugin.JsgfLexer;
import com.asherbernardi.jsgfplugin.JsgfParser;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import org.antlr.intellij.adaptor.lexer.PSIElementTypeFactory;
import org.antlr.intellij.adaptor.lexer.RuleIElementType;
import org.antlr.intellij.adaptor.lexer.TokenIElementType;
import org.intellij.lang.annotations.MagicConstant;

/**
 * Some static types which can be easily referenced.
 * @author asherbernardi
 */
public class JsgfTypes {
  public static IElementType BAD_TOKEN_TYPE = new IElementType("BAD_TOKEN", JsgfLanguage.INSTANCE);

  public static final List<TokenIElementType> TOKEN_ELEMENT_TYPES =
      PSIElementTypeFactory.getTokenIElementTypes(JsgfLanguage.INSTANCE);
  public static final List<RuleIElementType> RULE_ELEMENT_TYPES =
      PSIElementTypeFactory.getRuleIElementTypes(JsgfLanguage.INSTANCE);

  public static final TokenSet WHITE_SPACES =
      PSIElementTypeFactory.createTokenSet(JsgfLanguage.INSTANCE,
          JsgfLexer.NL, JsgfLexer.SPACE);
  public static final TokenSet COMMENTS =
      PSIElementTypeFactory.createTokenSet(JsgfLanguage.INSTANCE,
          JsgfLexer.COMMENT, JsgfLexer.BLOCKCOMMENT, JsgfLexer.DOCCOMMENT);
  public static final TokenSet STRING =
      PSIElementTypeFactory.createTokenSet(JsgfLanguage.INSTANCE,
          JsgfLexer.QUOTE, JsgfLexer.STRING_TEXT);

  public static final TokenSet KEYWORDS =
      PSIElementTypeFactory.createTokenSet(JsgfLanguage.INSTANCE,
          JsgfLexer.JSGF_IDENT, JsgfLexer.VERSION, JsgfLexer.IMPORT, JsgfLexer.GRAMMAR,
          JsgfLexer.PUBLIC, JsgfLexer.SEMICOLON);

  public static final List<String> KEYWORD_LITERALS =
      Arrays.asList(
          JsgfLexer.VOCABULARY.getLiteralName(JsgfLexer.JSGF_IDENT),
          JsgfLexer.VOCABULARY.getLiteralName(JsgfLexer.VERSION),
          JsgfLexer.VOCABULARY.getLiteralName(JsgfLexer.IMPORT),
          JsgfLexer.VOCABULARY.getLiteralName(JsgfLexer.GRAMMAR),
          JsgfLexer.VOCABULARY.getLiteralName(JsgfLexer.PUBLIC),
          JsgfLexer.VOCABULARY.getLiteralName(JsgfLexer.SEMICOLON)
  );

  public static RuleIElementType getRuleElementType(@MagicConstant(valuesFromClass = JsgfParser.class)int ruleIndex){
    return RULE_ELEMENT_TYPES.get(ruleIndex);
  }

  public static TokenIElementType getTokenElementType(@MagicConstant(valuesFromClass = JsgfLexer.class)int ruleIndex){
    return TOKEN_ELEMENT_TYPES.get(ruleIndex);
  }
}
