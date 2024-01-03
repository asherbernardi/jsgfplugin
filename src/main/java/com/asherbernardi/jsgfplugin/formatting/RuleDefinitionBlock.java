package com.asherbernardi.jsgfplugin.formatting;

import com.asherbernardi.jsgfplugin.codeStyle.JsgfCodeStyleSettings;
import com.asherbernardi.jsgfplugin.psi.JsgfExpansion;
import com.asherbernardi.jsgfplugin.psi.JsgfGroupExp;
import com.asherbernardi.jsgfplugin.psi.JsgfRuleDefinition;
import com.intellij.formatting.Block;
import com.intellij.formatting.ChildAttributes;
import com.intellij.formatting.Indent;
import com.intellij.formatting.SpacingBuilder;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.TokenType;
import com.asherbernardi.jsgfplugin.psi.JsgfBnfTypes;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.tree.IElementType;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class RuleDefinitionBlock extends JsgfBlock<JsgfRuleDefinition> {

  private Indent myChildIndent = null;

  protected RuleDefinitionBlock(@NotNull JsgfRuleDefinition element, SpacingBuilder spacingBuilder, CodeStyleSettings settings) {
    super(element, spacingBuilder, settings);
  }

  @Override
  protected List<Block> buildChildren() {
    List<Block> blocks = new ArrayList<>();
    List<Block> simpleBlocks = new ArrayList<>();
    for (PsiElement child : FormattingUtil.iterableOfChildren(getElement())) {
      if (isSimpleChild(child)) {
        blocks.add(createSimpleBlock(child, Indent.getNoneIndent()));
      } else if (child instanceof JsgfGroupExp) {
        myChildIndent = getSettings().getCustomSettings(JsgfCodeStyleSettings.class).INDENT_SINGLE_GROUP_RULES
            ? normalIndent() : noneIndent();
        blocks.add(new ExpansionBlock((JsgfExpansion) child, getSpacingBuilder(), getSettings(), myChildIndent));
      } else if (child instanceof JsgfExpansion) {
        myChildIndent = getSettings().getCustomSettings(JsgfCodeStyleSettings.class).INDENT_SINGLE_ALTERNATIVES_RULES
            ? normalIndent() : noneIndent();
        blocks.add(new ExpansionBlock((JsgfExpansion) child, getSpacingBuilder(), getSettings(),
            myChildIndent));
      } else if (child.getNode().getElementType() != TokenType.WHITE_SPACE){
        Block simple = createSimpleBlock(child, Indent.getNoneIndent());
        blocks.add(simple);
        simpleBlocks.add(simple);
      }
    }
    // The indent for the child of this block, not determined until we reach the Alternatives
    // child element
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
    return noneIndent();
  }

  private boolean isSimpleChild(PsiElement child) {
    IElementType type = child.getNode().getElementType();
    return type == JsgfBnfTypes.PUBLIC
        || type == JsgfBnfTypes.RULE_DECLARATION
        || type == JsgfBnfTypes.EQUALS
        || type == JsgfBnfTypes.SEMICOLON;
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
