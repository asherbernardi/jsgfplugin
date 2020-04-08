package com.asherbernardi.jsgfplugin;

import com.intellij.lang.Language;

public class JsgfLanguage extends Language {
  public static final JsgfLanguage INSTANCE = new JsgfLanguage();

  private JsgfLanguage() {
    super("Jsgf");
  }
}
