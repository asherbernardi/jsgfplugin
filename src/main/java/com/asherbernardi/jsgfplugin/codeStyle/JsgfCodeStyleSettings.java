package com.asherbernardi.jsgfplugin.codeStyle;

import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CustomCodeStyleSettings;

public class JsgfCodeStyleSettings extends CustomCodeStyleSettings {

  public boolean INDENT_SINGLE_GROUP_RULES = false;
  public boolean INDENT_SINGLE_ALTERNATIVES_RULES = true;

  public JsgfCodeStyleSettings(CodeStyleSettings settings) {
    super("JsgfCodeStyleSettings", settings);
  }

}
