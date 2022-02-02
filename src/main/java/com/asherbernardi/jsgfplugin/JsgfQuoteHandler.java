package com.asherbernardi.jsgfplugin;

import static com.asherbernardi.jsgfplugin.psi.JsgfBnfTypes.QUOTE_CLOSE;
import static com.asherbernardi.jsgfplugin.psi.JsgfBnfTypes.QUOTE_OPEN;
import static com.asherbernardi.jsgfplugin.psi.JsgfBnfTypes.STRING_TEXT;

import com.intellij.codeInsight.editorActions.QuoteHandler;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.highlighter.HighlighterIterator;

/**
 * Handles quote typing and deleting.
 * @author asherbernardi
 */

public class JsgfQuoteHandler implements QuoteHandler {

  @Override
  public boolean isClosingQuote(HighlighterIterator iterator, int offset) {
    return iterator.getTokenType() == QUOTE_CLOSE;
  }

  @Override
  public boolean isOpeningQuote(HighlighterIterator iterator, int offset) {
    return iterator.getTokenType() == QUOTE_OPEN;
  }

  @Override
  public boolean isInsideLiteral(HighlighterIterator iterator) {
    return iterator.getTokenType() == QUOTE_OPEN
        || iterator.getTokenType() == STRING_TEXT
        || iterator.getTokenType() == QUOTE_CLOSE;
  }

  @Override
  public boolean hasNonClosedLiteral(Editor editor, HighlighterIterator iterator, int offset) {
    int start = iterator.getStart();
    try {
      Document doc = editor.getDocument();
      int lineEnd = doc.getLineEndOffset(doc.getLineNumber(offset));
      // a JSGF string literal is properly composed of three tokens QUOTE_OPEN -> STRING_TEXT -> QUOTE_CLOSE
      // or two tokens QUOTE_OPEN -> QUOTE_CLOSE
      // Therefore, to check if all strings literals are closed, we confirm that any encountered
      // QUOTE_OPEN is followed by a STRING_TEXT or a QUOTE_CLOSE and any encountered STRING_TEXT
      // is followed by a QUOTE_CLOSE
      while (!iterator.atEnd() && iterator.getStart() < lineEnd) {
        if (iterator.getTokenType() == QUOTE_OPEN) {
          iterator.advance();
          if (iterator.getTokenType() != STRING_TEXT && iterator.getTokenType() != QUOTE_CLOSE) {
            return true;
          }
        } else if (iterator.getTokenType() == STRING_TEXT) {
          iterator.advance();
          if (iterator.getTokenType() != QUOTE_CLOSE) {
            return true;
          }
        } else {
          iterator.advance();
        }
      }
      return false;
    } finally {
      revertIterator(iterator, start);
    }
  }

  private static void revertIterator(HighlighterIterator iterator, int targetStart) {
    while (iterator.getStart() < targetStart) {
      iterator.advance();
    }
    while (iterator.getStart() > targetStart || iterator.atEnd()) {
      iterator.retreat();
    }
  }
}
