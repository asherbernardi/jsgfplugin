package com.asherbernardi.jsgfplugin.psi.manipulators;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.AbstractElementManipulator;
import com.intellij.util.IncorrectOperationException;
import com.asherbernardi.jsgfplugin.psi.JsgfRuleImportName;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ImportNameManipulator extends AbstractElementManipulator<JsgfRuleImportName> {

  @Nullable
  @Override
  public JsgfRuleImportName handleContentChange(@NotNull JsgfRuleImportName element,
      @NotNull TextRange range, String newContent) throws IncorrectOperationException {
    String newQualifiedName = element.getFullyQualifiedGrammarName() + "." + newContent;
    return (JsgfRuleImportName) element.setRuleName(newQualifiedName);
  }
}
