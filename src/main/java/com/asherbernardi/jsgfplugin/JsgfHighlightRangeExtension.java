package com.asherbernardi.jsgfplugin;

import com.intellij.codeInsight.daemon.impl.HighlightRangeExtension;
import com.intellij.psi.PsiFile;
import com.asherbernardi.jsgfplugin.psi.JsgfFile;
import org.jetbrains.annotations.NotNull;

/**
 * In order to highlight tags even when they are misformatted
 */
public class JsgfHighlightRangeExtension implements HighlightRangeExtension {

  @Override
  public boolean isForceHighlightParents(@NotNull PsiFile psiFile) {
    return psiFile instanceof JsgfFile;
  }
}
