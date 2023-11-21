package com.asherbernardi.jsgfplugin.psi;

import com.intellij.psi.tree.IElementType;
import com.asherbernardi.jsgfplugin.JsgfLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class JsgfElementType extends IElementType {

  public JsgfElementType(@NotNull @NonNls String debugName) {
    super(debugName, JsgfLanguage.INSTANCE);
  }
}
