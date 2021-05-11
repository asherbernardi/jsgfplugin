package com.asherbernardi.jsgfplugin;

import com.asherbernardi.jsgfplugin.JsgfSyntaxHighlighter.JsgfHighlightType;
import com.asherbernardi.jsgfplugin.psi.*;
import com.asherbernardi.jsgfplugin.psi.reference.ImportNameReference;
import com.asherbernardi.jsgfplugin.psi.reference.JsgfResolveResult;
import com.asherbernardi.jsgfplugin.psi.reference.OtherFileNameReference;
import com.asherbernardi.jsgfplugin.psi.reference.RuleReferenceReference;
import com.intellij.lang.ASTNode;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.util.PsiTreeUtil;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;

/**
 * Provides semantic error highlighting for JSGF.
 * @author asherbernardi
 */
public class JsgfErrorAnnotator implements Annotator {
  @Override
  public void annotate(@NotNull final PsiElement element, @NotNull AnnotationHolder holder) {
    // Mark errors for whitespace before/after rule names. I was not able to get parser to do
    // that automatically... It will ignore whitespace between the angle brackets and the rulename
    if (element instanceof RuleName) {
      if (element.getNode().getTreeNext() == null
          || element.getNode().getTreeNext().getElementType() != JsgfBnfTypes.RANGLE) {
        holder.newAnnotation(HighlightSeverity.ERROR,
            "Rule name must be followed be a right angle bracket '>'")
            .range(element).create();
      }
      if (element.getNode().getTreePrev().getElementType() != JsgfBnfTypes.LANGLE) {
        holder.newAnnotation(HighlightSeverity.ERROR,
            "Rule name must be preceded by a left angle bracket '<'")
            .range(element).create();
      }
    }
    // Mark rule declarations that have that have already been defined as an error
    if (element instanceof JsgfRuleDeclarationName) {
      JsgfRuleDeclarationName ruleName = (JsgfRuleDeclarationName) element;
      if ("NULL".equals(ruleName.getRuleName()) || "VOID".equals(ruleName.getRuleName())) {
        holder.newAnnotation(HighlightSeverity.ERROR,
            "Cannot redefine reserved rule " + ruleName.getRuleName())
            .range(ruleName)
            .create();
      }
      if (!JsgfUtil.isFirstDeclarationInFile((JsgfFile) element.getContainingFile(), ruleName)) {
        holder.newAnnotation(HighlightSeverity.ERROR,
            "Rule <" + ruleName.getRuleName() + "> already defined")
            .range(ruleName)
            .create();
      }
    }
    // Mark unresolved rule references
    if (element instanceof JsgfRuleReferenceName) {
      JsgfRuleReferenceName ruleRef = (JsgfRuleReferenceName) element;
      // reserved special rules
      if ("NULL".equals(ruleRef.getRuleName()) || "VOID".equals(ruleRef.getRuleName()))
        return;
      RuleReferenceReference ref = ruleRef.getReference();
      JsgfResolveResult[] resolves = ref.multiResolve(false);
      if (resolves.length == 0) {
        holder.newAnnotation(HighlightSeverity.ERROR,
            "Cannot resolve reference to rule: "
                + ruleRef.getRuleName())
            .textAttributes(JsgfHighlightType.BAD_REFERENCE.getTextAttributesKey())
            .range(element)
            .create();
      } else if (resolves.length > 1) {
        holder.newAnnotation(HighlightSeverity.WARNING,
            "Rule has more than one declaration: "
                + ruleRef.getRuleName()).range(element).create();
      } else if (!resolves[0].isLocal()
          && !resolves[0].getElement().isPublicRule()) {
        holder.newAnnotation(HighlightSeverity.ERROR,
            "<" + ruleRef.getRuleName() + "> does not have public access in "
                + resolves[0].getElement().getContainingFile().getName())
            .textAttributes(JsgfHighlightType.BAD_REFERENCE.getTextAttributesKey())
            .range(element).create();
      }
    }
    if (element instanceof JsgfRuleImportName) {
      JsgfRuleImportName importName = (JsgfRuleImportName) element;
      if (importName.isStarImport()) {
        List<JsgfFile> files = JsgfUtil.findFilesByPackage(((JsgfRuleImportName) element));
        if (files.isEmpty()) {
          holder.newAnnotation(HighlightSeverity.ERROR,
              "File not found for imported rule: "
                  + ((JsgfRuleImportName) element).getRuleName())
              .range(element)
              .create();
        }
      }
      else {
        OtherFileNameReference ref = importName.getReference();
        JsgfResolveResult[] resolves = ref.multiResolve(false);
        if (resolves.length == 0) {
          holder.newAnnotation(HighlightSeverity.ERROR,
              "Cannot resolve reference to imported rule: "
                  + ((JsgfRuleImportName) element).getRuleName())
              .textAttributes(JsgfHighlightType.BAD_REFERENCE.getTextAttributesKey()).range(element).create();
        } else if (resolves.length > 1) {
          holder.newAnnotation(HighlightSeverity.ERROR,
              "Rule import refers to more than one declaration: "
                  + ((JsgfRuleImportName) element).getRuleName())
              .range(element)
              .create();
        } else if (!resolves[0].isLocal()
            && !resolves[0].getElement().isPublicRule()) {
          holder.newAnnotation(HighlightSeverity.ERROR,
              "<" + ref.getUnqualifiedName() + "> does not have public access in "
                  + resolves[0].getElement().getContainingFile().getName())
              .textAttributes(JsgfHighlightType.BAD_REFERENCE.getTextAttributesKey())
              .range(element).create();
        }
      }
    }
    // No viable alternative error
    if (element instanceof JsgfAlternatives) {
      List<JsgfSequence> sequences = ((JsgfAlternatives) element).getSequenceList();
      for (JsgfSequence alt : sequences) {
        if (PsiTreeUtil.findChildrenOfAnyType(alt, JsgfRuleReferenceName.class,
            JsgfTerminal.class, JsgfString.class, JsgfAlternatives.class).isEmpty()) {
          holder.newAnnotation(HighlightSeverity.ERROR,
              "Empty alternative: " + alt.getText())
              .range(alt)
              .create();
        }
      }
      if (!(((JsgfAlternatives) element).getOrSymbols().size() == sequences.size() - 1)) {
        holder.newAnnotation(HighlightSeverity.ERROR,
            "No viable alternative for input: " + element.getText())
            .range(element).create();
      }
    }
    // Hanging string
    if (element.getNode().getElementType() == JsgfBnfTypes.QUOTE) {
      ASTNode next = element.getNode().getTreeNext();
      if (next != null &&
          (
              next.getElementType() == JsgfBnfTypes.STRING_NL
              ||
              (next.getElementType() == JsgfBnfTypes.STRING_TEXT
                  && next.getTreeNext() != null
                  && next.getTreeNext().getElementType() == JsgfBnfTypes.STRING_NL)
          )
      ) {
        holder.newAnnotation(HighlightSeverity.ERROR,
            "Illegal line end in quoted token")
            .range(element).create();
      }
    }
    if (element.getNode().getElementType() == JsgfBnfTypes.STRING_TEXT) {
      ASTNode next = element.getNode().getTreeNext();
      if (next.getElementType() == JsgfBnfTypes.STRING_NL) {
        holder.newAnnotation(HighlightSeverity.ERROR,
            "Illegal line end in quoted token")
            .range(element).create();
      }
    }
    // No grammar declaration
    if (element instanceof JsgfFile) {
      if (((JsgfFile) element).getGrammarDeclaration() == null) {
        JsgfHeader header = ((JsgfFile) element).getHeader();
        TextRange range = null;
        if (header != null) {
          range = header.getTextRange();
        } else {
          Matcher firstNonWS = Pattern.compile("\\S").matcher(element.getText());
          if (firstNonWS.find()) {
            range = new TextRange(firstNonWS.start(), firstNonWS.start() + 1);
          }
        }
        if (range != null) {
          holder.newAnnotation(HighlightSeverity.WARNING,
              "No grammar declaration in file")
              .range(range).create();
        }
      }
    }
  }
}