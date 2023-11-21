package com.asherbernardi.jsgfplugin.formatting;

import com.asherbernardi.jsgfplugin.JsgfParserDefinition;
import com.asherbernardi.jsgfplugin.psi.JsgfBnfTypes;
import com.intellij.formatting.Block;
import com.intellij.formatting.Indent;
import com.intellij.formatting.SpacingBuilder;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.TokenType;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.tree.TokenSet;
import com.asherbernardi.jsgfplugin.psi.JsgfGrammarName;
import com.asherbernardi.jsgfplugin.psi.JsgfRuleDeclaration;
import com.asherbernardi.jsgfplugin.psi.JsgfRuleImport;
import com.asherbernardi.jsgfplugin.psi.JsgfStringExp;
import com.asherbernardi.jsgfplugin.psi.JsgfTypes;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public interface FormattingUtil {

  static Block createSimpleBlock(PsiElement child, Indent indent, SpacingBuilder spacingBuilder) {
    if (child instanceof LeafPsiElement
        || child.getFirstChild() == null
        || child instanceof JsgfStringExp
        || child instanceof JsgfGrammarName
        || child instanceof JsgfRuleImport
        || child instanceof JsgfRuleDeclaration) {
      return new LeafBlock(child, indent);
    } else {
      return new SimpleBlock(child, spacingBuilder, indent);
    }
  }

  TokenSet ALTERNATIVES_CHILDREN = TokenSet.create(JsgfBnfTypes.SEQUENCE_EXP,
      JsgfBnfTypes.OR, JsgfBnfTypes.WEIGHT, JsgfTypes.BLOCK_COMMENT,
      JsgfTypes.LINE_COMMENT, JsgfTypes.DOC_COMMENT);
  TokenSet SEQUENCE_CHILDREN = TokenSet.create(JsgfBnfTypes.RULE_REFERENCE_EXP,
      JsgfBnfTypes.TERMINAL_EXP, JsgfBnfTypes.STRING_EXP, JsgfBnfTypes.PARENTHESES_GROUP_EXP,
      JsgfBnfTypes.OPTIONAL_GROUP_EXP, JsgfBnfTypes.STAR,
      JsgfBnfTypes.PLUS, JsgfBnfTypes.TAG, JsgfTypes.BLOCK_COMMENT,
      JsgfTypes.LINE_COMMENT, JsgfTypes.DOC_COMMENT);
  TokenSet NON_WHITE_SPACE = TokenSet.andNot(TokenSet.ANY, TokenSet.create(TokenType.WHITE_SPACE));

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

  static Iterable<PsiElement> iterableOfChildren(PsiElement parent) {
    return () -> new Iterator<>() {
      PsiElement current = parent.getFirstChild();

      @Override
      public boolean hasNext() {
        return current != null;
      }

      @Override
      public PsiElement next() {
        PsiElement oldCurrent = current;
        current = current.getNextSibling();
        return oldCurrent;
      }
    };
  }
}
