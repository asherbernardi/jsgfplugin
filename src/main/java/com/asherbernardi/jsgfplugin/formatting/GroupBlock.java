package com.asherbernardi.jsgfplugin.formatting;

import com.intellij.formatting.Block;
import com.intellij.formatting.ChildAttributes;
import com.intellij.formatting.Indent;
import com.intellij.formatting.SpacingBuilder;
import com.intellij.lang.ASTNode;
import com.intellij.psi.TokenType;
import com.asherbernardi.jsgfplugin.psi.JsgfBnfTypes;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.tree.TokenSet;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class GroupBlock extends JsgfBlock {

  private final boolean indent;

  protected GroupBlock(@NotNull ASTNode node,
      SpacingBuilder spacingBuilder, CodeStyleSettings settings, boolean indent) {
    super(node, spacingBuilder, settings);
    this.indent = indent;
  }

  @Override
  protected List<Block> buildChildren() {
    List<Block> blocks = new ArrayList<>();
    ASTNode child = myNode.getFirstChildNode();
    while (child != null) {
      if (child.getTextLength() != 0) {
        if (child.getElementType() == JsgfBnfTypes.ALTERNATIVES) {
          List<ASTNode> expanded = FormattingUtil.expandCompositionElements(child);
          List<List<ASTNode>> grouped = FormattingUtil.groupByLine(expanded);
          for (List<ASTNode> lineGroup : grouped) {
            blocks.add(new LineGroupBlock(lineGroup, getSpacingBuilder(), getSettings(), Indent.getNormalIndent()));
          }
        } else if (BLOCK_PARENS.contains(child.getElementType())) {
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
  public Indent getIndent() {
    return indent ? Indent.getNormalIndent() : Indent.getNoneIndent();
  }

  private static final TokenSet BLOCK_PARENS = TokenSet.create(JsgfBnfTypes.LPAREN,
      JsgfBnfTypes.RPAREN, JsgfBnfTypes.LBRACK, JsgfBnfTypes.RBRACK);

  @Override
  public @NotNull ChildAttributes getChildAttributes(int newChildIndex) {
    // Indent all Group sub-blocks except the opening and closing parentheses
    return new ChildAttributes(Indent.getNormalIndent(), null);
  }

  @Override
  public boolean isLeaf() {
    return false;
  }
}
