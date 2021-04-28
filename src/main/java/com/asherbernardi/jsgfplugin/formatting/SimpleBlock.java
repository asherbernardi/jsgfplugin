package com.asherbernardi.jsgfplugin.formatting;

import com.intellij.formatting.Block;
import com.intellij.formatting.Indent;
import com.intellij.formatting.Spacing;
import com.intellij.formatting.SpacingBuilder;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.TokenType;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.util.PsiTreeUtil;
import com.asherbernardi.jsgfplugin.psi.JsgfBnfTypes;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SimpleBlock extends JsgfBlock {

  protected SimpleBlock(@NotNull ASTNode node, SpacingBuilder spacingBuilder) {
    super(node, spacingBuilder, null);
  }

  @Override
  protected List<Block> buildChildren() {
    List<Block> blocks = new ArrayList<>();
    ASTNode child = myNode.getFirstChildNode();
    while (child != null) {
      if (child.getTextLength() != 0) {
        if (child.getElementType() != TokenType.WHITE_SPACE) {
          blocks.add(createSimpleBlock(child, Indent.getNoneIndent()));
        }
      }
      child = child.getTreeNext();
    }
    return blocks;
  }

  @Override
  public Indent getIndent() {
    return Indent.getNoneIndent();
  }

  @Override
  public @Nullable Spacing getSpacing(@Nullable Block child1, @NotNull Block child2) {
    if (child2 instanceof TagBlock) {
      if (PsiTreeUtil.findChildOfType(((TagBlock) child2).getNode().getPsi(),
          PsiWhiteSpace.class) != null) {
        return Spacing.createSpacing(1, 1, 0, true, 0);
      } else {
        return Spacing.createSpacing(0, 0, 0, false, 0);
      }
    }
    return super.getSpacing(child1, child2);
  }

  @Override
  public boolean isLeaf() {
    return myNode.getFirstChildNode() == null;
  }
}
