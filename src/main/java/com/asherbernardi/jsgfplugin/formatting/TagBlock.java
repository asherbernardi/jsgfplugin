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

  private final Indent indent;

  protected TagBlock(@NotNull JsgfTag element, SpacingBuilder spacingBuilder, CodeStyleSettings settings, Indent indent) {
    super(element,  spacingBuilder, settings);
    this.indent = indent;
  }

  @Override
  protected List<Block> buildChildren() {
    List<Block> blocks = new ArrayList<>();
    for (PsiElement child : FormattingUtil.iterableOfChildren(getElement())) {
      blocks.add(createSimpleBlock(child, normalIndent()));
    }
    return blocks;
  }

  @Override
  public @NotNull ChildAttributes getChildAttributes(int newChildIndex) {
    if (isAfter(newChildIndex,
        JsgfBnfTypes.LBRACE, JsgfBnfTypes.TAG_TOKEN)) {
      return new ChildAttributes(normalIndent(), null);
    }
    return new ChildAttributes(noneIndent(), null);
  }

  @Override
  public Indent getIndent() {
    return indent;
  }
}
