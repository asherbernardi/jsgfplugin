package com.asherbernardi.jsgfplugin;

import static com.asherbernardi.jsgfplugin.psi.JsgfBnfTypes.QUOTE;
import static com.asherbernardi.jsgfplugin.psi.JsgfBnfTypes.STRING_TEXT;

import com.intellij.codeInsight.editorActions.QuoteHandler;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.highlighter.HighlighterIterator;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles quote typing and deleting.
 * @author asherbernardi
 */

public class JsgfQuoteHandler implements QuoteHandler {

  @Override
  public boolean isClosingQuote(HighlighterIterator iterator, int offset) {
    return iterator.getTokenType() == QUOTE && !mapQuotesForLine(iterator, offset).get(offset);
  }

  @Override
  public boolean isOpeningQuote(HighlighterIterator iterator, int offset) {
    return iterator.getTokenType() == QUOTE && mapQuotesForLine(iterator, offset).get(offset);
  }

  @Override
  public boolean isInsideLiteral(HighlighterIterator iterator) {
    return iterator.getTokenType() == QUOTE
        || iterator.getTokenType() == STRING_TEXT;
  }

  @Override
  public boolean hasNonClosedLiteral(Editor editor, HighlighterIterator iterator, int offset) {
    return mapQuotesForLine(iterator, offset).size() % 2 != 0;
  }

  private static Map<Integer, Boolean> mapQuotesForLine(HighlighterIterator iterator, int offset) {
    Map<Integer, Boolean> result = new HashMap<>();
    int start = iterator.getStart();
    try {
      Document doc = iterator.getDocument();
      int lineEnd = doc.getLineEndOffset(doc.getLineNumber(offset));
      int lineStart = doc.getLineStartOffset(doc.getLineNumber(offset));
      revertIterator(iterator, lineStart);
      boolean inString = false;
      while (!iterator.atEnd() && iterator.getStart() < lineEnd) {
        if (iterator.getTokenType() == QUOTE) {
          result.put(iterator.getStart(), !inString);
          inString = !inString;
        }
        iterator.advance();
      }
      // If we reach the end of a line while in a string, we have a non-close literal
      return result;
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
