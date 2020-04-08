package com.asherbernardi.jsgfplugin;

import org.antlr.intellij.adaptor.lexer.ANTLRLexerAdaptor;

public class JsgfLexerAdapter extends ANTLRLexerAdaptor {
  public JsgfLexerAdapter() {
    super(JsgfLanguage.INSTANCE, new JsgfLexer(null));
  }
}
