// This is a generated file. Not intended for manual editing.
package com.asherbernardi.jsgfplugin.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface JsgfRuleDefinition extends PsiElement {

  @Nullable
  JsgfAlternatives getAlternatives();

  @NotNull
  JsgfRuleDeclaration getRuleDeclaration();

  String getRuleName();

  boolean isPublicRule();

}
