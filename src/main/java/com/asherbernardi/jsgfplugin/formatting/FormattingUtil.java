package com.asherbernardi.jsgfplugin.formatting;

import com.asherbernardi.jsgfplugin.JsgfParserDefinition;
import com.asherbernardi.jsgfplugin.psi.JsgfBnfTypes;
import com.intellij.formatting.Block;
import com.intellij.formatting.Indent;
import com.intellij.formatting.SpacingBuilder;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public interface FormattingUtil {

  static Block createSimpleBlock(ASTNode child, Indent indent, SpacingBuilder spacingBuilder) {
    if (child.getFirstChildNode() == null
        || child.getElementType() == JsgfBnfTypes.STRING
        || child.getElementType() == JsgfBnfTypes.GRAMMAR_NAME
        || child.getElementType() == JsgfBnfTypes.IMPORT
        || child.getElementType() == JsgfBnfTypes.RULE_DECLARATION
        || child.getElementType() == JsgfBnfTypes.TERMINAL
        || child.getElementType() == JsgfBnfTypes.RULE_REFERENCE) {
      return new LeafBlock(child, indent);
    } else {
      return new SimpleBlock(child, spacingBuilder);
    }
  }

  TokenSet ALTERNATIVES_CHILDREN = TokenSet.create(JsgfBnfTypes.SEQUENCE,
      JsgfBnfTypes.OR, JsgfBnfTypes.WEIGHT, JsgfParserDefinition.BLOCK_COMMENT,
      JsgfParserDefinition.LINE_COMMENT, JsgfParserDefinition.DOC_COMMENT);
  TokenSet SEQUENCE_CHILDREN = TokenSet.create(JsgfBnfTypes.RULE_REFERENCE,
      JsgfBnfTypes.TERMINAL, JsgfBnfTypes.STRING, JsgfBnfTypes.GROUP, JsgfBnfTypes.STAR,
      JsgfBnfTypes.PLUS, JsgfBnfTypes.TAG, JsgfParserDefinition.BLOCK_COMMENT,
      JsgfParserDefinition.LINE_COMMENT, JsgfParserDefinition.DOC_COMMENT);

  static List<ASTNode> expandCompositionElements(ASTNode node) {
    if (node.getElementType() == JsgfBnfTypes.ALTERNATIVES) {
      List<ASTNode> sequences = Arrays.asList(node.getChildren(TokenSet.ANY));
      return sequences.stream().flatMap(n -> expandCompositionElements(n).stream()).collect(
          Collectors.toList());
    } else if (node.getElementType() == JsgfBnfTypes.SEQUENCE) {
      List<ASTNode> sequences = Arrays.asList(node.getChildren(TokenSet.ANY));
      return sequences.stream().flatMap(n -> expandCompositionElements(n).stream()).collect(
          Collectors.toList());
    }
    return Arrays.asList(node);
  }

  static List<List<ASTNode>> groupByLine(List<ASTNode> nodes) {
    List<List<ASTNode>> result = new ArrayList<>();
    List<ASTNode> nextGroup = new ArrayList<>();
    for (final ASTNode node : nodes) {
      nextGroup.add(node);
      ASTNode next = node.getTreeNext();
      ASTNode parentNode = node;
      while (next == null) {
        parentNode = parentNode.getTreeParent();
        if (parentNode == null) {
          break;
        }
        next = parentNode.getTreeNext();
      }
      if (parentNode == null || next.getText().contains("\n") || node == nodes.get(nodes.size() - 1)) {
        result.add(nextGroup);
        nextGroup = new ArrayList<>();
      }
    }
    return result;
  }
}
