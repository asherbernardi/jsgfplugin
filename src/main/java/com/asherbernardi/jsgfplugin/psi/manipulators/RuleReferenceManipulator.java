package com.asherbernardi.jsgfplugin.psi.manipulators;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.AbstractElementManipulator;
import com.intellij.util.IncorrectOperationException;
import com.asherbernardi.jsgfplugin.psi.impl.RuleReferenceElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RuleReferenceManipulator extends AbstractElementManipulator<RuleReferenceElement> {

  @Nullable
  @Override
  public RuleReferenceElement handleContentChange(@NotNull RuleReferenceElement element,
      @NotNull TextRange range, String newContent) throws IncorrectOperationException {
    return (RuleReferenceElement) element.setName(newContent);
  }
}
