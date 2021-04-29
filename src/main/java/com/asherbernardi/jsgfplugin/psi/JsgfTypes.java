package com.asherbernardi.jsgfplugin.psi;

import com.intellij.psi.tree.TokenSet;
import java.util.regex.Pattern;

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
