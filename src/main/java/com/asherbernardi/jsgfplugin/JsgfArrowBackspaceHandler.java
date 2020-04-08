package com.asherbernardi.jsgfplugin;

import com.intellij.codeInsight.editorActions.BackspaceHandlerDelegate;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiUtilCore;
import org.antlr.intellij.adaptor.lexer.TokenIElementType;
import org.jetbrains.annotations.NotNull;

/**
 * Handles automatic deletion of a matching angle bracket when
 * appropriate. Parentheses, brackets, and braces are automatically
 * handled by the JsgfPairedBraceMatcher via TypedHandler, but
 * angle brackets aren't included in that.
 * @author asherbernardi
 */
public class JsgfArrowBackspaceHandler extends BackspaceHandlerDelegate {

  @Override
  public void beforeCharDeleted(char c, @NotNull PsiFile file, @NotNull Editor editor) {

  }

  @Override
  public boolean charDeleted(char c, @NotNull PsiFile file, @NotNull Editor editor) {
    if (c == '<') {
      int openArrowOffset = editor.getCaretModel().getOffset();
      final CharSequence chars = editor.getDocument().getCharsSequence();
      if (editor.getDocument().getTextLength() <= openArrowOffset) return false; //virtual space after end of file
      IElementType nextTokenType = PsiUtilCore.getElementAtOffset(file, openArrowOffset + 1).getNode().getElementType();
      if (nextTokenType instanceof TokenIElementType &&
          ((TokenIElementType) nextTokenType).getANTLRTokenType() == JsgfLexer.CLOSEARROW) {
        editor.getDocument().deleteString(openArrowOffset, openArrowOffset + 1);
        return true;
      }
    }
    return false;
  }
}
