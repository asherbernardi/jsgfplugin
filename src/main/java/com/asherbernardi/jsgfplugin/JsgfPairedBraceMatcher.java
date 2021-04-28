package com.asherbernardi.jsgfplugin;

import com.asherbernardi.jsgfplugin.psi.JsgfBnfTypes;
import com.intellij.lang.BracePair;
import com.intellij.lang.PairedBraceMatcher;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Provides matching for parentheses, brackets, braces, and angle brackets
 * @author asherbernardi
 */
public class JsgfPairedBraceMatcher implements PairedBraceMatcher {
  private final BracePair parens = new BracePair(
      JsgfBnfTypes.LPAREN,
      JsgfBnfTypes.RPAREN,
      false);
  private final BracePair bracks = new BracePair(
      JsgfBnfTypes.LBRACK,
      JsgfBnfTypes.RBRACK,
      false);
  // This class doesn't work with '<>' which is why we need the special TypeHandler
  private final BracePair angles = new BracePair(
      JsgfBnfTypes.LANGLE,
      JsgfBnfTypes.RANGLE,
      true);
  private final BracePair braces = new BracePair(
      JsgfBnfTypes.LBRACE,
      JsgfBnfTypes.RBRACE,
      true);
  private final BracePair[] pairs = new BracePair[]{parens, bracks, angles, braces};

  @NotNull
  @Override
  public BracePair[] getPairs() {
    return pairs;
  }

  @Override
  public boolean isPairedBracesAllowedBeforeType(@NotNull IElementType lbraceType,
      @Nullable IElementType contextType) {
    return true;
  }

  @Override
  public int getCodeConstructStart(PsiFile file, int openingBraceOffset) {
    return openingBraceOffset;
  }
}
