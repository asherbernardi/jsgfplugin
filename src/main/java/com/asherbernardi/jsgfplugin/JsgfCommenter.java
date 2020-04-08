package com.asherbernardi.jsgfplugin;

import com.intellij.lang.Commenter;
import org.jetbrains.annotations.Nullable;

/**
 * Auto-commenting with CTRL-?
 * @author asherbernardi
 */
public class JsgfCommenter implements Commenter {
  @Nullable
  @Override
  public String getLineCommentPrefix() {
    return "//";
  }

  @Nullable
  @Override
  public String getBlockCommentPrefix() {
    return "/*";
  }

  @Nullable
  @Override
  public String getBlockCommentSuffix() {
    return "*/";
  }

  @Nullable
  @Override
  public String getCommentedBlockCommentPrefix() {
    return getBlockCommentPrefix();
  }

  @Nullable
  @Override
  public String getCommentedBlockCommentSuffix() {
    return getBlockCommentSuffix();
  }
}
