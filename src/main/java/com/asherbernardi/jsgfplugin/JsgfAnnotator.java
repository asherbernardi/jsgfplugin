package com.asherbernardi.jsgfplugin;

import com.asherbernardi.jsgfplugin.psi.impl.TerminalElement;
import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import org.antlr.intellij.adaptor.lexer.TokenIElementType;
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
    // Change syntax color of terminal symbols
    if (element instanceof TerminalElement) {
      Annotation annotation = holder.createInfoAnnotation(element, null);
      annotation.setTextAttributes(JsgfSyntaxHighlighter.TERMINAL);
    }
    // Annotate documentation comments
    else if (element.getNode().getElementType() instanceof TokenIElementType) {
      int ttype = ((TokenIElementType) element.getNode().getElementType()).getANTLRTokenType();
      if (ttype == JsgfLexer.DOCCOMMENT) {
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
              Annotation annotation = holder.createInfoAnnotation(range, null);
              annotation.setTextAttributes(JsgfSyntaxHighlighter.DOC_TAG);
            }
          }
        }
      }
    }
  }
}