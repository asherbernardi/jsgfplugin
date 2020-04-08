package com.asherbernardi.jsgfplugin;

import com.intellij.codeInsight.editorActions.TabOutScopesTracker;
import com.intellij.codeInsight.editorActions.TypedHandler;
import com.intellij.codeInsight.editorActions.TypedHandlerDelegate;
import com.intellij.codeInsight.highlighting.BraceMatcher;
import com.intellij.codeInsight.highlighting.BraceMatchingUtil;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.editor.highlighter.HighlighterIterator;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiUtilCore;
import org.antlr.intellij.adaptor.lexer.TokenIElementType;
import org.jetbrains.annotations.NotNull;

/**
 * Handles automatic insertion of a matching angle bracket when
 * appropriate. For example, if you type '<', a matching '>' will
 * appear, then if you type '>', the IDE will not insert
 * another character but skip over the closing '>'.
 * @author asherbernardi
 */
public class JsgfArrowTypedHandler extends TypedHandlerDelegate {

  @NotNull
  @Override
  public Result charTyped(char c, @NotNull Project project, @NotNull Editor editor,
      @NotNull PsiFile file) {
    // Copied and adapted directly from TypedHandler, because the necessary method was private >:(
    if (c == '<') {
      char lparenChar = c;
      FileType fileType = file.getFileType();
      int offset = editor.getCaretModel().getOffset();
      HighlighterIterator iterator = ((EditorEx) editor).getHighlighter().createIterator(offset);
      boolean atEndOfDocument = offset == editor.getDocument().getTextLength();

      if (!atEndOfDocument)
        iterator.retreat();
      if (iterator.atEnd())
        return Result.CONTINUE;
      BraceMatcher braceMatcher = BraceMatchingUtil.getBraceMatcher(fileType, iterator);
      if (iterator.atEnd())
        return Result.CONTINUE;
      IElementType braceTokenType = iterator.getTokenType();
      final CharSequence fileText = editor.getDocument().getCharsSequence();
      if (!braceMatcher.isLBraceToken(iterator, fileText, fileType))
        return Result.CONTINUE;

      if (!iterator.atEnd()) {
        iterator.advance();

        if (!iterator.atEnd() &&
            !BraceMatchingUtil
                .isPairedBracesAllowedBeforeTypeInFileType(braceTokenType, iterator.getTokenType(),
                    fileType)) {
          return Result.CONTINUE;
        }

        iterator.retreat();
      }

      int lparenOffset = BraceMatchingUtil
          .findLeftmostLParen(iterator, braceTokenType, fileText, fileType);
      if (lparenOffset < 0)
        lparenOffset = 0;

      iterator = ((EditorEx) editor).getHighlighter().createIterator(lparenOffset);
      IElementType nextTokenType = PsiUtilCore.getElementAtOffset(file, lparenOffset).getNode().getElementType();
      // Don't insert extra arrow bracket if the caret is next to an identifier token
      if (!(nextTokenType instanceof TokenIElementType) ||
//          (((TokenIElementType) nextTokenType).getANTLRTokenType() != JsgfLexer.RULE_DECLARATION_NAME_IDENTIFIER &&
//              ((TokenIElementType) nextTokenType).getANTLRTokenType() != JsgfLexer.RULE_REFERENCE_NAME_IDENTIFIER &&
//              ((TokenIElementType) nextTokenType).getANTLRTokenType() != JsgfLexer.RULE_IMPORT_NAME_IDENTIFIER &&
          (((TokenIElementType) nextTokenType).getANTLRTokenType() != JsgfLexer.IDENTIFIER &&
//              ((TokenIElementType) nextTokenType).getANTLRTokenType() != JsgfLexer.RULE_IDENTIFIER &&
              ((TokenIElementType) nextTokenType).getANTLRTokenType() != JsgfLexer.TERMINAL &&
              ((TokenIElementType) nextTokenType).getANTLRTokenType() != JsgfLexer.CLOSEARROW &&
              fileText.charAt(lparenOffset + 1) != '>' &&
              !Character.isUnicodeIdentifierPart(fileText.charAt(lparenOffset + 1)))
          ) {
        editor.getDocument().insertString(offset, ">");
        TabOutScopesTracker.getInstance().registerEmptyScope(editor, offset);
      }
    }
    return Result.CONTINUE;
  }

  @NotNull
  @Override
  public Result beforeCharTyped(char c, @NotNull Project project, @NotNull Editor editor,
      @NotNull PsiFile file, @NotNull FileType fileType) {
    // for ignoring when you type a closing ">" if it already matches with a previous "<"
    if (c == '>') {
      if (TypedHandler.handleRParen(editor, file.getFileType(), c))
        return Result.STOP;
    }
    return Result.CONTINUE;
  }
}
