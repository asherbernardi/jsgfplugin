package com.asherbernardi.jsgfplugin.psi;

import com.asherbernardi.jsgfplugin.JsgfLanguage;
import com.asherbernardi.jsgfplugin.JsgfLexer;
import com.asherbernardi.jsgfplugin.JsgfParser;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import org.intellij.lang.annotations.MagicConstant;

/**
 * Some static types which can be easily referenced.
 * @author asherbernardi
 */
public interface JsgfTypes {
  TokenSet KEYWORDS =
      TokenSet.create(
          JsgfBnfTypes.JSGF_IDENT,
          JsgfBnfTypes.VERSION,
          JsgfBnfTypes.IMPORT,
          JsgfBnfTypes.GRAMMAR,
          JsgfBnfTypes.PUBLIC,
          JsgfBnfTypes.INCLUDE,
          JsgfBnfTypes.SEMICOLON
      );

  Pattern COUNT_RANGE_TAG_PATTERN = Pattern.compile("#" + "?([0-9]+),?([0-9]+)?");
}
