package com.asherbernardi.jsgfplugin.formatting;

import com.asherbernardi.jsgfplugin.psi.JsgfBnfTypes;
import com.intellij.formatting.ASTBlock;
import com.intellij.formatting.Alignment;
import com.intellij.formatting.Block;
import com.intellij.formatting.ChildAttributes;
import com.intellij.formatting.Indent;
import com.intellij.formatting.Spacing;
import com.intellij.formatting.SpacingBuilder;
import com.intellij.formatting.Wrap;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LineGroupBlock implements ASTGroupBlock {

  private final List<ASTNode> groupNodes;
  private final Indent indent;
  private final SpacingBuilder spacingBuilder;
  private final CodeStyleSettings settings;

  public LineGroupBlock(@Nonnull List<ASTNode> groupNodes, SpacingBuilder spacingBuilder, CodeStyleSettings settings, Indent indent) {
    if (groupNodes.size() == 0 || groupNodes.stream().anyMatch(Objects::isNull)) {
      throw new IllegalArgumentException("no group nodes provided");
    }
    this.groupNodes = groupNodes;
    this.indent = indent;
    this.spacingBuilder = spacingBuilder;
    this.settings = settings;
  }

  @Override
  public @NotNull List<Block> getSubBlocks() {
    List<Block> blocks = new ArrayList<>();
    for (ASTNode node : groupNodes) {
      if (node.getElementType() == JsgfBnfTypes.GROUP) {
        blocks.add(new GroupBlock(node, getSpacingBuilder(), getSettings(), false));
      } else if (node.getElementType() == JsgfBnfTypes.TAG) {
        blocks.add(new TagBlock(node, getSpacingBuilder(), getSettings()));
      } else {
        blocks.add(FormattingUtil.createSimpleBlock(node, Indent.getNoneIndent(), getSpacingBuilder()));
      }
    }
    return blocks;
  }

  @Override
  public @Nullable Wrap getWrap() {
    return null;
  }

  @Override
  public @Nullable Indent getIndent() {
    return indent;
  }

  @Override
  public @Nullable Alignment getAlignment() {
    return null;
  }

  @Override
  public @Nullable Spacing getSpacing(@Nullable Block block, @NotNull Block block1) {
    return getSpacingBuilder().getSpacing(this, block, block1);
  }

  public SpacingBuilder getSpacingBuilder() {
    return spacingBuilder;
  }

  public CodeStyleSettings getSettings() {
    return settings;
  }

  @Override
  public @NotNull ChildAttributes getChildAttributes(int i) {
    return new ChildAttributes(null, null);
  }

  @Override
  public boolean isIncomplete() {
    return false;
  }

  @Override
  public boolean isLeaf() {
    return false;
  }

  @NotNull
  public ASTNode getFirstNode() {
    return groupNodes.get(0);
  }

  @NotNull
  public ASTNode getLastNode() {
    return groupNodes.get(groupNodes.size() - 1);
  }
}
