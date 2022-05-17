package com.asherbernardi.jsgfplugin.formatting;

import com.asherbernardi.jsgfplugin.psi.JsgfTag;
import com.intellij.formatting.Block;
import com.intellij.formatting.ChildAttributes;
import com.intellij.formatting.Indent;
import com.intellij.formatting.SpacingBuilder;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.TokenType;
import com.asherbernardi.jsgfplugin.psi.JsgfBnfTypes;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class TagBlock extends JsgfBlock<JsgfTag> {

  protected TagBlock(@NotNull JsgfTag element, SpacingBuilder spacingBuilder, CodeStyleSettings settings) {
    super(element,  spacingBuilder, settings);
  }

  @Override
  protected List<Block> buildChildren() {
    List<Block> blocks = new ArrayList<>();
    for (PsiElement child : FormattingUtil.iterableOfChildren(getElement())) {
      if (!(child instanceof PsiWhiteSpace)) {
        blocks.add(createSimpleBlock(child, noneIndent()));
      }
    }
    return blocks;
  }

  @Override
  public @NotNull ChildAttributes getChildAttributes(int newChildIndex) {
    if (isAfter(newChildIndex,
        JsgfBnfTypes.LBRACE)) {
      return new ChildAttributes(Indent.getNormalIndent(), null);
    }
    return new ChildAttributes(Indent.getNoneIndent(), null);
  }

  @Override
  public Indent getIndent() {
    return Indent.getNoneIndent();
  }
}
