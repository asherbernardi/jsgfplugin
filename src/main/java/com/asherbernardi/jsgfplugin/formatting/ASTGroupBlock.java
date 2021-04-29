package com.asherbernardi.jsgfplugin.formatting;

import com.intellij.formatting.ASTBlock;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A Block which spans multiple ASTNodes
 *
 * @author asherbernardi
 */
public interface ASTGroupBlock extends ASTBlock {

  @Override
  @Nullable
  default ASTNode getNode() {
    return getFirstNode();
  }

  @NotNull
  ASTNode getFirstNode();

  @NotNull
  ASTNode getLastNode();

  @Override
  @NotNull
  default TextRange getTextRange() {
    return new TextRange(getFirstNode().getStartOffset(),
        getLastNode().getTextRange().getEndOffset());
  }

}
