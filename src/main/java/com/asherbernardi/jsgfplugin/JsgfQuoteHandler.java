package com.asherbernardi.jsgfplugin;

import com.intellij.codeInsight.editorActions.SimpleTokenSetQuoteHandler;
import org.antlr.intellij.adaptor.lexer.PSIElementTypeFactory;

/**
 * Handles quote typing and deleting.
 * TODO: This class is not registered with the plugin, because it wasn't working right.
 * @author asherbernardi
 */
public class JsgfQuoteHandler extends SimpleTokenSetQuoteHandler {
  public JsgfQuoteHandler() {
    super(PSIElementTypeFactory.createTokenSet(JsgfLanguage.INSTANCE, JsgfLexer.QUOTE));
  }
}
