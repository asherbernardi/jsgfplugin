package com.asherbernardi.jsgfplugin.psi.manipulators;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.AbstractElementManipulator;
import com.intellij.util.IncorrectOperationException;
import com.asherbernardi.jsgfplugin.psi.impl.ImportNameElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ImportNameManipulator extends AbstractElementManipulator<ImportNameElement> {

  @Nullable
  @Override
  public ImportNameElement handleContentChange(@NotNull ImportNameElement element,
      @NotNull TextRange range, String newContent) throws IncorrectOperationException {
    String newQualifiedName = element.getFullyQualifiedGrammarName() + "." + newContent;
    return (ImportNameElement) element.setName(newQualifiedName);
  }
}
