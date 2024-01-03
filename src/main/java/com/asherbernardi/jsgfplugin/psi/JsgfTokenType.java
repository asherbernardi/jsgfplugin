package com.asherbernardi.jsgfplugin.psi;

import com.intellij.psi.tree.IElementType;
import com.asherbernardi.jsgfplugin.JsgfLanguage;
import org.jetbrains.annotations.NotNull;

public class JsgfTokenType extends IElementType {

  public JsgfTokenType(@NotNull String debugName) {
    super(debugName, JsgfLanguage.INSTANCE);
  }
}
