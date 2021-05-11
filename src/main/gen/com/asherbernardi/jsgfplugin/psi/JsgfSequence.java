// This is a generated file. Not intended for manual editing.
package com.asherbernardi.jsgfplugin.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface JsgfSequence extends PsiElement {

  @NotNull
  List<JsgfGroup> getGroupList();

  @NotNull
  List<JsgfRuleReference> getRuleReferenceList();

  @NotNull
  List<JsgfString> getStringList();

  @NotNull
  List<JsgfTag> getTagList();

  @NotNull
  List<JsgfTerminal> getTerminalList();

  Double getWeight();

}
