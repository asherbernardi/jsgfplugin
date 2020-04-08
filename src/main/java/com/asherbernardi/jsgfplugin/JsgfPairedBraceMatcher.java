package com.asherbernardi.jsgfplugin;

import com.intellij.lang.BracePair;
import com.intellij.lang.PairedBraceMatcher;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.asherbernardi.jsgfplugin.psi.JsgfTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Provides matching for parentheses, brackets, braces, and angle brackets
 * @author asherbernardi
 */
public class JsgfPairedBraceMatcher implements PairedBraceMatcher {
  private final BracePair parens = new BracePair(
      JsgfTypes.getTokenElementType(JsgfLexer.OPENPARAN),
      JsgfTypes.getTokenElementType(JsgfLexer.CLOSEPARAN),
      true);
  private final BracePair bracks = new BracePair(
      JsgfTypes.getTokenElementType(JsgfLexer.OPENBRACKET),
      JsgfTypes.getTokenElementType(JsgfLexer.CLOSEBRACKET),
      true);
  // This class doesn't work with '<>' which is why we need the special TypeHandler
  private final BracePair arrows = new BracePair(
      JsgfTypes.getTokenElementType(JsgfLexer.OPENARROW),
      JsgfTypes.getTokenElementType(JsgfLexer.CLOSEARROW),
      false);
  private final BracePair curlies = new BracePair(
      JsgfTypes.getTokenElementType(JsgfLexer.OPENCURLY),
      JsgfTypes.getTokenElementType(JsgfLexer.CLOSECURLY),
      false);
  private final BracePair[] pairs = new BracePair[]{parens, bracks, arrows, curlies};

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
