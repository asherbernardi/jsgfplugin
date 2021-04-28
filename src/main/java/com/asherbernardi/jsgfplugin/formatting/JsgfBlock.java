package com.asherbernardi.jsgfplugin.formatting;

import com.intellij.formatting.Alignment;
import com.intellij.formatting.Block;
import com.intellij.formatting.Indent;
import com.intellij.formatting.Spacing;
import com.intellij.formatting.SpacingBuilder;
import com.intellij.lang.ASTNode;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.formatter.common.AbstractBlock;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class JsgfBlock extends AbstractBlock {

  private final SpacingBuilder spacingBuilder;
  private final CodeStyleSettings settings;

  protected JsgfBlock(@NotNull ASTNode node, SpacingBuilder spacingBuilder,
      CodeStyleSettings settings, Alignment alignment) {
    super(node, null, alignment);
    this.spacingBuilder = spacingBuilder;
    this.settings = settings;
  }

  protected JsgfBlock(@NotNull ASTNode node, SpacingBuilder spacingBuilder, CodeStyleSettings settings) {
    this(node, spacingBuilder, settings, null);
  }

  public SpacingBuilder getSpacingBuilder() {
    return spacingBuilder;
  }

  public CodeStyleSettings getSettings() {
    return settings;
  }

  @Override
  public @Nullable Spacing getSpacing(@Nullable Block child1, @NotNull Block child2) {
    return getSpacingBuilder().getSpacing(this, child1, child2);
  }

  protected boolean isAfter(final int newChildIndex, final IElementType... elementTypes) {
    if (newChildIndex == 0) return false;
    final Block previousBlock = getSubBlocks().get(newChildIndex - 1);
    if (!(previousBlock instanceof AbstractBlock)) return false;
    final IElementType previousElementType = ((AbstractBlock)previousBlock).getNode().getElementType();
    for (IElementType elementType : elementTypes) {
      if (previousElementType == elementType) return true;
    }
    return false;
  }

  @SafeVarargs
  protected final boolean isAfter(final int newChildIndex,
      @NotNull final Class<? extends Block>... blockTypes) {
    if (newChildIndex == 0) return false;
    final Block previousBlock = getSubBlocks().get(newChildIndex - 1);
    if (!(previousBlock instanceof AbstractBlock)) return false;
    final Class<? extends Block> previousBlockType = ((AbstractBlock)previousBlock).getClass();
    for (Class<? extends Block> blockType : blockTypes) {
      if (previousBlockType == blockType) return true;
    }
    return false;
  }

  @Override
  protected @Nullable Indent getChildIndent() {
    return Indent.getNoneIndent();
  }

  @Override
  public abstract Indent getIndent();

  protected Block createSimpleBlock(ASTNode child, Indent indent) {
    return FormattingUtil.createSimpleBlock(child, indent, getSpacingBuilder());
  }

}
