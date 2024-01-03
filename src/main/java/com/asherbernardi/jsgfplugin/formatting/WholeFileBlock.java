package com.asherbernardi.jsgfplugin.formatting;

import com.intellij.formatting.Block;
import com.intellij.formatting.Indent;
import com.intellij.formatting.SpacingBuilder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.asherbernardi.jsgfplugin.psi.JsgfFile;
import com.asherbernardi.jsgfplugin.psi.JsgfRuleDefinition;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class WholeFileBlock extends JsgfBlock<JsgfFile> {

  protected WholeFileBlock(@NotNull JsgfFile element, SpacingBuilder spacingBuilder, CodeStyleSettings settings) {
    super(element, spacingBuilder, settings);
  }

  @Override
  protected List<Block> buildChildren() {
    List<Block> blocks = new ArrayList<>();
    for (PsiElement child : FormattingUtil.iterableOfChildren(getElement())) {
      if (child instanceof JsgfRuleDefinition) {
        blocks.add(new RuleDefinitionBlock((JsgfRuleDefinition) child, getSpacingBuilder(), getSettings()));
      } else if (!(child instanceof PsiWhiteSpace)) {
        blocks.add(createSimpleBlock(child, Indent.getNoneIndent()));
      }
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
