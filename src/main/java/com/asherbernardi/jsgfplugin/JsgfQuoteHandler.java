package com.asherbernardi.jsgfplugin;

import com.asherbernardi.jsgfplugin.psi.JsgfBnfTypes;
import com.intellij.codeInsight.editorActions.MultiCharQuoteHandler;
import com.intellij.codeInsight.editorActions.SimpleTokenSetQuoteHandler;
import com.intellij.openapi.editor.highlighter.HighlighterIterator;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Handles quote typing and deleting.
 * TODO: This class is not registered with the plugin, because it wasn't working right.
 * @author asherbernardi
 */
public class JsgfQuoteHandler extends SimpleTokenSetQuoteHandler implements MultiCharQuoteHandler {
  public JsgfQuoteHandler() {
    super(TokenSet.create(JsgfBnfTypes.QUOTE));
  }

  @Override
  public @Nullable
  CharSequence getClosingQuote(@NotNull HighlighterIterator iterator, int offset) {
    return "\"";
  }
}
