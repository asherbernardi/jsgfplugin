package com.asherbernardi.jsgfplugin;

import com.intellij.lexer.FlexAdapter;

public class JsgfLexerAdapter extends FlexAdapter {
  public JsgfLexerAdapter() {
    super(new JsgfLexer(null));
  }
}
