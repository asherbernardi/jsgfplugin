package com.asherbernardi.jsgfplugin.formatting;

import com.intellij.formatting.Block;
import com.intellij.formatting.ChildAttributes;
import com.intellij.formatting.Indent;
import com.intellij.formatting.SpacingBuilder;
import com.intellij.lang.ASTNode;
import com.intellij.psi.TokenType;
import com.asherbernardi.jsgfplugin.psi.JsgfBnfTypes;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class TagBlock extends JsgfBlock {

  protected TagBlock(@NotNull ASTNode node, SpacingBuilder spacingBuilder, CodeStyleSettings settings) {
    super(node, spacingBuilder, settings);
  }

  @Override
  protected List<Block> buildChildren() {
    List<Block> blocks = new ArrayList<>();
    ASTNode child = myNode.getFirstChildNode();
    while (child != null) {
      if (child.getTextLength() != 0) {
        if (child.getElementType() == JsgfBnfTypes.LBRACE
            || child.getElementType() == JsgfBnfTypes.RBRACE) {
          blocks.add(createSimpleBlock(child, Indent.getNoneIndent()));
        } else if (child.getElementType() != TokenType.WHITE_SPACE) {
          blocks.add(createSimpleBlock(child, Indent.getNormalIndent()));
        }
      }
      child = child.getTreeNext();
    }
    return blocks;
  }

  @Override
  public @NotNull
  ChildAttributes getChildAttributes(int newChildIndex) {
    return new ChildAttributes(Indent.getNoneIndent(), null);
  }

  @Override
  public Indent getIndent() {
    return Indent.getNoneIndent();
  }

  @Override
  public boolean isLeaf() {
    return myNode.getFirstChildNode() == null;
  }
}
