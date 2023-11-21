package com.asherbernardi.jsgfplugin.psi.manipulators;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.AbstractElementManipulator;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import com.asherbernardi.jsgfplugin.JsgfElementFactory;
import com.asherbernardi.jsgfplugin.psi.JsgfBnfTypes;
import com.asherbernardi.jsgfplugin.psi.JsgfStringExp;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StringExpManipulator extends AbstractElementManipulator<JsgfStringExp> {

  @Nullable
  @Override
  public JsgfStringExp handleContentChange(@NotNull JsgfStringExp element,
      @NotNull TextRange range, String newContent) throws IncorrectOperationException {
    String newText;
    String oldText = element.getText();
    TextRange valueRange = getRangeInElement(element);
    try {
      String textBeforeRange = oldText.substring(valueRange.getStartOffset(), range.getStartOffset());
      String textAfterRange = oldText.substring(range.getEndOffset(), valueRange.getEndOffset());
      newText = textBeforeRange + newContent + textAfterRange;
    } catch(StringIndexOutOfBoundsException e) {
      throw new IncorrectOperationException(e);
    }
    newText = newText
        .replace("\\", "\\\\")
        .replace("\"", "\\\"");
    JsgfStringExp stringExp = JsgfElementFactory.createStringExpansion(element.getProject(), newText);
    element.getNode().replaceAllChildrenToChildrenOf(stringExp.getNode());
    return element;
  }

  @Override
  public @NotNull TextRange getRangeInElement(@NotNull JsgfStringExp element) {
    PsiElement stringText = element.getFirstChild().getNextSibling();
    if (stringText == null) {
      return new TextRange(1, element.getTextLength());
    }
    if (stringText.getNode().getElementType() != JsgfBnfTypes.STRING_TEXT) {
      return new TextRange(1, 1);
    }
    return new TextRange(1, element.getTextLength() - 1);
  }
}
