package com.asherbernardi.jsgfplugin.formatting;

import com.intellij.formatting.Block;
import com.intellij.formatting.Indent;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class LeafBlock extends JsgfBlock<PsiElement> {

  private Indent indent;

  protected LeafBlock(@NotNull PsiElement element, Indent indent) {
    super(element, null, null);
    this.indent = indent;
  }

  @Override
  protected List<Block> buildChildren() {
    return Collections.emptyList();
  }

  @Override
  public Indent getIndent() {
    return indent;
  }

  public void setIndent(Indent indent) {
    this.indent = indent;
  }

  @Override
  public boolean isLeaf() {
    return true;
  }
}
