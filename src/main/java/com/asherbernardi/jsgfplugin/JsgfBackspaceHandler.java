package com.asherbernardi.jsgfplugin;

import com.intellij.codeInsight.editorActions.BackspaceHandlerDelegate;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiUtilCore;
import com.asherbernardi.jsgfplugin.psi.JsgfBnfTypes;
import org.jetbrains.annotations.NotNull;

/**
 * Handles automatic deletion of a matching angle bracket when
 * appropriate. Parentheses, brackets, and braces are automatically
 * handled by the JsgfPairedBraceMatcher via TypedHandler, but
 * angle brackets aren't included in that.
 * @author asherbernardi
 */
public class JsgfBackspaceHandler extends BackspaceHandlerDelegate {

  @Override
  public void beforeCharDeleted(char c, @NotNull PsiFile file, @NotNull Editor editor) {

  }

  @Override
  public boolean charDeleted(char c, @NotNull PsiFile file, @NotNull Editor editor) {
    if (file.getFileType().equals(JsgfFileType.INSTANCE) && c == '<') {
      int openAngleOffset = editor.getCaretModel().getOffset();
      if (editor.getDocument().getTextLength() <= openAngleOffset) return false; //virtual space after end of file
      IElementType nextTokenType = PsiUtilCore.getElementAtOffset(file, openAngleOffset + 1).getNode().getElementType();
      if (nextTokenType == JsgfBnfTypes.RANGLE) {
        editor.getDocument().deleteString(openAngleOffset, openAngleOffset + 1);
        return true;
      }
    }
    return false;
  }
}
