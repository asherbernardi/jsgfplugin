package com.asherbernardi.jsgfplugin.formatting;

import com.intellij.formatting.Block;
import com.intellij.formatting.Indent;
import com.intellij.formatting.Spacing;
import com.intellij.formatting.SpacingBuilder;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.TokenType;
import com.intellij.psi.util.PsiTreeUtil;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SimpleBlock extends JsgfBlock<PsiElement> {

  private final Indent indent;

  protected SimpleBlock(@NotNull PsiElement element, SpacingBuilder spacingBuilder, Indent indent) {
    super(element, spacingBuilder, null);
    this.indent = indent;
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
  public Indent getIndent() {
    return indent;
  }

  @Override
  public @Nullable Spacing getSpacing(@Nullable Block child1, @NotNull Block child2) {
    if (child2 instanceof TagBlock) {
      if (PsiTreeUtil.findChildOfType(((TagBlock) child2).getElement(),
          PsiWhiteSpace.class) != null) {
        return Spacing.createSpacing(1, 1, 0, true, 0);
      } else {
        return Spacing.createSpacing(0, 0, 0, false, 0);
      }
    }
    return super.getSpacing(child1, child2);
  }
}
