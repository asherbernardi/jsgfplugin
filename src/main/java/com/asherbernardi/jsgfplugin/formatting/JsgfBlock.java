package com.asherbernardi.jsgfplugin.formatting;

import com.intellij.formatting.Alignment;
import com.intellij.formatting.Block;
import com.intellij.formatting.Indent;
import com.intellij.formatting.Indent.Type;
import com.intellij.formatting.Spacing;
import com.intellij.formatting.SpacingBuilder;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.formatter.common.AbstractBlock;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class JsgfBlock<E extends PsiElement> extends AbstractBlock {

  private final SpacingBuilder spacingBuilder;
  private final CodeStyleSettings settings;
  private final E element;
  protected static final Indent NORMAL_INDENT = Indent.getIndent(Type.NORMAL, false, true);
  protected static final Indent NONE_INDENT = Indent.getIndent(Type.NONE, false, true);

  protected JsgfBlock(@NotNull E element, SpacingBuilder spacingBuilder,
      CodeStyleSettings settings, Alignment alignment) {
    super(element.getNode(), null, alignment);
    this.spacingBuilder = spacingBuilder;
    this.settings = settings;
    this.element = element;
  }

  protected JsgfBlock(@NotNull E element, SpacingBuilder spacingBuilder, CodeStyleSettings settings) {
    this(element, spacingBuilder, settings, null);
  }

  public SpacingBuilder getSpacingBuilder() {
    return spacingBuilder;
  }

  public CodeStyleSettings getSettings() {
    return settings;
  }

  public E getElement() {
    return element;
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

  protected Indent normalIndent() {
    return NORMAL_INDENT;
  }

  protected Indent noneIndent() {
    return NONE_INDENT;
  }

  @Override
  public abstract Indent getIndent();

  protected Block createSimpleBlock(PsiElement child, Indent indent) {
    return FormattingUtil.createSimpleBlock(child, indent, getSpacingBuilder());
  }

  @Override
  public boolean isLeaf() {
    return myNode.getFirstChildNode() == null;
  }
}
