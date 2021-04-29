package com.asherbernardi.jsgfplugin.formatting;

import com.intellij.formatting.ASTBlock;
import com.intellij.formatting.Block;
import com.intellij.formatting.Spacing;
import com.intellij.formatting.SpacingBuilder;
import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ASTGroupSpacingBuilder extends SpacingBuilder {

  public ASTGroupSpacingBuilder(
      @NotNull CodeStyleSettings codeStyleSettings,
      @NotNull Language language) {
    super(codeStyleSettings, language);
  }

  @Override
  public @Nullable Spacing getSpacing(@Nullable Block parent, @Nullable Block child1,
      @Nullable Block child2) {
    if (parent instanceof ASTBlock && child1 instanceof ASTGroupBlock && child2 instanceof ASTNode) {
      return getSpacing(parent, ASTBlock.getElementType(parent),
          ((ASTGroupBlock) child1).getLastNode().getElementType(),
          ASTBlock.getElementType(child2));
    }
    return super.getSpacing(parent, child1, child2);
  }
}
