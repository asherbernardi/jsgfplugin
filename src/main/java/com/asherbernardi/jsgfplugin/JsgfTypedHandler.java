package com.asherbernardi.jsgfplugin;

import com.asherbernardi.jsgfplugin.psi.JsgfRuleDefinition;
import com.intellij.codeInsight.editorActions.TypedHandlerDelegate;
import com.intellij.codeInsight.editorActions.TypedHandlerUtil;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiErrorElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.asherbernardi.jsgfplugin.psi.JsgfBnfTypes;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;

/**
 * Handles automatic insertion of a matching angle bracket when
 * appropriate. For example, if you type '<', a matching '>' will
 * appear, then if you type '>', the IDE will not insert
 * another character but skip over the closing '>'.
 * @author asherbernardi
 */
public class JsgfTypedHandler extends TypedHandlerDelegate {

  private static final IElementType LT_TYPE = JsgfBnfTypes.LANGLE;
  private static final IElementType GT_TYPE = JsgfBnfTypes.RANGLE;
  /**
   * The set of tokens that delineate the chunks of code within which brace matching can occur
   */
  private static final TokenSet INVALID_INSIDE_REFERENCE = TokenSet.create(
      JsgfBnfTypes.SEMICOLON,
      JsgfBnfTypes.LPAREN,
      JsgfBnfTypes.RPAREN,
      JsgfBnfTypes.LBRACK,
      JsgfBnfTypes.RBRACK
  );

  @NotNull
  @Override
  public Result charTyped(char c, @NotNull Project project, @NotNull Editor editor,
      @NotNull PsiFile file) {
    // for auto-inserting a ">" if a "<" is typed
    if (file.getFileType().equals(JsgfFileType.INSTANCE)) {
      if (c == '<'){
      TypedHandlerUtil.handleAfterGenericLT(editor, LT_TYPE, GT_TYPE, INVALID_INSIDE_REFERENCE);
    }
      if (c == '=') {
        int offset = editor.getCaretModel().getOffset();
        PsiDocumentManager.getInstance(file.getProject()).commitDocument(editor.getDocument());
        PsiElement element = file.findElementAt(offset-1); // should always be '='
        boolean shouldInsertSemicolon = false;
        if (element != null && element.getParent() instanceof JsgfRuleDefinition) {
          PsiElement next = PsiTreeUtil.skipSiblingsForward(element, PsiErrorElement.class, PsiComment.class);
          if (next == null
              || (next instanceof PsiWhiteSpace && next.getText().contains("\n"))) {
            next = PsiTreeUtil.skipSiblingsForward(element,
                PsiWhiteSpace.class, PsiErrorElement.class, PsiComment.class);
            if (next == null
                || JsgfUtil.textStartsWith(next.getNode(), "public")) {
              shouldInsertSemicolon = true;
            }
          }
        }
        if (shouldInsertSemicolon) {
          editor.getDocument().insertString(offset, " ;");
          editor.getCaretModel().moveToOffset(offset + 1);
        }
      }
    }
    return Result.CONTINUE;
  }

  @NotNull
  @Override
  public Result beforeCharTyped(char c, @NotNull Project project, @NotNull Editor editor,
      @NotNull PsiFile file, @NotNull FileType fileType) {
    // for ignoring when you type a closing ">" if it already matches with a previous "<"
    if (fileType.equals(JsgfFileType.INSTANCE)) {
      if (c == '>') {
        if (TypedHandlerUtil.handleGenericGT(editor, LT_TYPE, GT_TYPE, INVALID_INSIDE_REFERENCE)) {
          return Result.STOP;
        }
      }
    }
    return Result.CONTINUE;
  }
}
