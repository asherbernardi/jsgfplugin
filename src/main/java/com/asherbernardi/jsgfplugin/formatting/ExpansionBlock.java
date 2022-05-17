package com.asherbernardi.jsgfplugin.formatting;

import com.intellij.formatting.Block;
import com.intellij.formatting.ChildAttributes;
import com.intellij.formatting.Indent;
import com.intellij.formatting.SpacingBuilder;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.asherbernardi.jsgfplugin.psi.JsgfExpansion;
import com.asherbernardi.jsgfplugin.psi.JsgfGroupExp;
import com.asherbernardi.jsgfplugin.psi.JsgfTag;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ExpansionBlock extends JsgfBlock<JsgfExpansion> {

  private final Indent indent;

  public ExpansionBlock(@Nonnull JsgfExpansion element, SpacingBuilder spacingBuilder, CodeStyleSettings settings, Indent indent) {
    super(element, spacingBuilder, settings);
    this.indent = indent;
  }

  @Override
  protected List<Block> buildChildren() {
    List<Block> blocks = new ArrayList<>();
    for (PsiElement child : FormattingUtil.iterableOfChildren(getElement())) {
      if (child instanceof JsgfExpansion) {
        Indent childIndent;
        if (getElement() instanceof JsgfGroupExp) {
          childIndent = normalIndent();
        } else {
          childIndent = noneIndent();
        }
        blocks.add(new ExpansionBlock((JsgfExpansion) child, getSpacingBuilder(), getSettings(), childIndent));
      } else if (child instanceof JsgfTag) {
        blocks.add(new TagBlock((JsgfTag) child, getSpacingBuilder(), getSettings()));
      } else if (child instanceof PsiComment && getElement() instanceof JsgfGroupExp) {
        blocks.add(createSimpleBlock(child, normalIndent()));
      } else if (!(child instanceof PsiWhiteSpace)) {
        blocks.add(createSimpleBlock(child, noneIndent()));
      }
    }
    return blocks;
  }

  @Override
  public @Nullable Indent getIndent() {
    return indent;
  }

  @Override
  public @NotNull ChildAttributes getChildAttributes(int newChildIndex) {
    if (getElement() instanceof JsgfGroupExp) {
      return new ChildAttributes(normalIndent(), null);
    }
    return new ChildAttributes(noneIndent(), null);
  }
}
