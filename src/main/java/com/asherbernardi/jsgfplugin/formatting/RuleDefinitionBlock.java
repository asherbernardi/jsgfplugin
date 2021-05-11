package com.asherbernardi.jsgfplugin.formatting;

import com.asherbernardi.jsgfplugin.codeStyle.JsgfCodeStyleSettings;
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

public class RuleDefinitionBlock extends JsgfBlock {

  private Indent myChildIndent = null;

  protected RuleDefinitionBlock(@NotNull ASTNode node, SpacingBuilder spacingBuilder, CodeStyleSettings settings) {
    super(node, spacingBuilder, settings);
  }

  @Override
  protected List<Block> buildChildren() {
    List<Block> blocks = new ArrayList<>();
    ASTNode child = myNode.getFirstChildNode();
    List<Block> simpleBlocks = new ArrayList<>();
    // The indent for the child of this block, not determined until we reach the Alternatives
    // child element
    while (child != null) {
      if (child.getTextLength() != 0) {
        if (child.getElementType() == JsgfBnfTypes.ALTERNATIVES) {
          List<ASTNode> expanded = FormattingUtil.expandCompositionElements(child);
          List<List<ASTNode>> grouped = FormattingUtil.groupByLine(expanded);
          for (List<ASTNode> lineGroup : grouped) {
            // if this rule definition consists of just one group, don't indent it
            if (grouped.size() == 1 && grouped.get(0).size() == 1
                && grouped.get(0).get(0).getElementType() == JsgfBnfTypes.GROUP) {
              myChildIndent = getSettings().getCustomSettings(JsgfCodeStyleSettings.class).INDENT_SINGLE_GROUP_RULES
                  ? Indent.getNormalIndent() : Indent.getNoneIndent();
            } else {
              myChildIndent = getSettings().getCustomSettings(JsgfCodeStyleSettings.class).INDENT_SINGLE_ALTERNATIVES_RULES
                  ? Indent.getNormalIndent() : Indent.getNoneIndent();
            }
            blocks.add(new LineGroupBlock(lineGroup, getSpacingBuilder(), getSettings(),
                myChildIndent));
          }
        } else if (child.getElementType() == JsgfBnfTypes.PUBLIC
            || child.getElementType() == JsgfBnfTypes.RULE_DECLARATION
            || child.getElementType() == JsgfBnfTypes.EQUALS
            || child.getElementType() == JsgfBnfTypes.SEMICOLON) {
          blocks.add(createSimpleBlock(child, Indent.getNoneIndent()));
        } else if (child.getElementType() != TokenType.WHITE_SPACE) {
          Block simple = createSimpleBlock(child, Indent.getNoneIndent());
          blocks.add(simple);
          simpleBlocks.add(simple);
        }
      }
      child = child.getTreeNext();
    }
    if (myChildIndent != null) {
      for (Block simpleBlock : simpleBlocks) {
        if (simpleBlock instanceof LeafBlock) {
          ((LeafBlock) simpleBlock).setIndent(myChildIndent);
        }
      }
    }
    return blocks;
  }

  @Override
  public Indent getIndent() {
    return Indent.getNoneIndent();
  }

  @Override
  public @NotNull ChildAttributes getChildAttributes(int newChildIndex) {
    if (myChildIndent != null) return new ChildAttributes(myChildIndent, null);
    if (!isAfter(newChildIndex, JsgfBnfTypes.RULE_DECLARATION)) {
      return new ChildAttributes(Indent.getNormalIndent(), null);
    }
    return new ChildAttributes(Indent.getNoneIndent(), null);
  }

  @Override
  public boolean isLeaf() {
    return false;
  }
}
