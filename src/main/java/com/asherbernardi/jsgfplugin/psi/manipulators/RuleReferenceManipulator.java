package com.asherbernardi.jsgfplugin.psi.manipulators;

import com.asherbernardi.jsgfplugin.psi.RuleNameSplit;
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
    String fqrn = element.getFQRN();
    RuleNameSplit split = RuleNameSplit.fromFQRN(fqrn);
    String newQualifiedName;
    if (split.hasFQGN() && range.equals(split.getFQGNRange())) {
      // New content is a simple grammar name
      newQualifiedName = split.replaceSimpleGrammarName(newContent);
    } else if (range.equals(split.getUQRNRange())) {
      // New content is an unqualified rule name
      newQualifiedName = split.replaceUQRN(newContent);
    } else {
      throw new IncorrectOperationException("Wrong range: " + range + " to replace import name: " + element.getText());
    }
    return (JsgfRuleReferenceName) element.setFQRN(newQualifiedName);
  }
}
