package com.asherbernardi.jsgfplugin.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import java.util.regex.Pattern;

/**
 * Some static types which can be easily referenced.
 * @author asherbernardi
 */
public interface JsgfTypes {

  IElementType LINE_COMMENT = new JsgfTokenType("Line comment");
  IElementType DOC_COMMENT = new JsgfTokenType("Documentation comment");
  IElementType BLOCK_COMMENT = new JsgfTokenType("Block comment");

  TokenSet KEYWORDS =
      TokenSet.create(
          JsgfBnfTypes.JSGF_IDENT,
          JsgfBnfTypes.VERSION,
          JsgfBnfTypes.IMPORT,
          JsgfBnfTypes.GRAMMAR,
          JsgfBnfTypes.PUBLIC,
          JsgfBnfTypes.SEMICOLON
      );

  TokenSet IDENTIFIERS =
      TokenSet.create(
          JsgfBnfTypes.IDENTIFIER, JsgfBnfTypes.RULE_NAME_IDENTIFIER
      );

  TokenSet COMMENTS =
      TokenSet.create(
          LINE_COMMENT, DOC_COMMENT, BLOCK_COMMENT
      );

  TokenSet STRINGS =
      TokenSet.create(
          JsgfBnfTypes.STRING_EXP
      );

  TokenSet LITERALS =
      TokenSet.create(
          JsgfBnfTypes.TERMINAL_IDENTIFIER, JsgfBnfTypes.TAG_TOKEN
      );

  Pattern COUNT_RANGE_TAG_PATTERN = Pattern.compile("#" + "?([0-9]+),?([0-9]+)?");
}
