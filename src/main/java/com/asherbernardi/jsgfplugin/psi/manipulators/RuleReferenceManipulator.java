package com.asherbernardi.jsgfplugin.psi.manipulators;

import com.asherbernardi.jsgfplugin.psi.impl.JsgfPsiImplInjections;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.AbstractElementManipulator;
import com.intellij.util.IncorrectOperationException;
import com.asherbernardi.jsgfplugin.psi.JsgfRuleReferenceName;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RuleReferenceManipulator extends AbstractElementManipulator<JsgfRuleReferenceName> {

  @Nullable
  @Override
  public JsgfRuleReferenceName handleContentChange(@NotNull JsgfRuleReferenceName element,
      @NotNull TextRange range, String newContent) throws IncorrectOperationException {
    String fqgn = JsgfPsiImplInjections.fullyQualifiedGrammarNameFromFQRN(element.getRuleName());
    String newQualifiedName = (fqgn.isEmpty() ? "" : fqgn + "." + newContent) + newContent;
    return (JsgfRuleReferenceName) element.setRuleName(newQualifiedName);
  }
}
