package com.asherbernardi.jsgfplugin;

import com.asherbernardi.jsgfplugin.JsgfSyntaxHighlighter.JsgfHighlightType;
import com.asherbernardi.jsgfplugin.psi.JsgfTag;
import com.asherbernardi.jsgfplugin.psi.JsgfTerminal;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

/**
 * Improves and expands syntax highlighting for elements which cannot
 * be accounted for by the lexer tokens alone. Thankfully, ANTLR lexing
 * is powerful enough that almost everything can be highlighted without
 * this annotator by the JsgfSyntaxHighlighter
 * @author asherbernardi
 */
public class JsgfAnnotator implements Annotator {
  @Override
  public void annotate(@NotNull final PsiElement element, @NotNull AnnotationHolder holder) {
//    // Change syntax color of terminal symbols
//    if (element instanceof JsgfTerminal) {
//      holder.newSilentAnnotation(HighlightSeverity.INFORMATION).range(element)
//          .textAttributes(JsgfHighlightType.TERMINAL.getTextAttributesKey()).create();
//    }
    // Annotate documentation comments
    if (element.getNode().getElementType() == JsgfParserDefinition.DOC_COMMENT) {
      int fileOffset = element.getTextOffset();
      String text = element.getText();
      String line;
      for (int offset = 0; offset != -1 && offset < text.length();
          offset += (line.indexOf('\n') == -1 ? line.length() : line.indexOf('\n')) + 1) {
        line = text.substring(offset);
        int at = line.indexOf('@');
        if (at == -1 || !line.substring(0,at).matches("[\\s*]*"))
          continue;
        for (String keywordTag : JsgfSyntaxHighlighter.keywordTags) {
          if (line.substring(at).startsWith(keywordTag)) {
            int rangeOffset = fileOffset + offset + line.indexOf('@');
            TextRange range = new TextRange(rangeOffset, rangeOffset + keywordTag.length());
            holder.newSilentAnnotation(HighlightSeverity.INFORMATION).range(range)
                .textAttributes(JsgfHighlightType.DOC_TAG.getTextAttributesKey()).create();
          }
        }
      }
    }
    // Tags like injected language
    if (element instanceof JsgfTag) {
      holder.newSilentAnnotation(HighlightSeverity.INFORMATION).range(element)
          .textAttributes(JsgfHighlightType.TAG.getTextAttributesKey()).create();
    }
  }
}