package com.asherbernardi.jsgfplugin.formatting;

import com.intellij.formatting.Block;
import com.intellij.formatting.Indent;
import com.intellij.formatting.SpacingBuilder;
import com.intellij.lang.ASTNode;
import com.intellij.psi.TokenType;
import com.asherbernardi.jsgfplugin.psi.JsgfBnfTypes;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class JsgfGrammarBlock extends JsgfBlock {

  protected JsgfGrammarBlock(@NotNull ASTNode node, SpacingBuilder spacingBuilder, CodeStyleSettings settings) {
    super(node, spacingBuilder, settings);
  }

  @Override
  protected List<Block> buildChildren() {
    List<Block> blocks = new ArrayList<>();
    ASTNode child = myNode.getFirstChildNode();
    while (child != null) {
      if (child.getTextLength() != 0) {
        if (child.getElementType() == JsgfBnfTypes.RULE_DEFINITION) {
          blocks.add(new RuleDefinitionBlock(child, getSpacingBuilder(), getSettings()));
        } else if (child.getElementType() != TokenType.WHITE_SPACE) {
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
  public boolean isLeaf() {
    return false;
  }
}
