package com.asherbernardi.jsgfplugin;

import com.asherbernardi.jsgfplugin.JsgfSyntaxHighlighter.JsgfHighlightType;
import com.asherbernardi.jsgfplugin.psi.*;
import com.asherbernardi.jsgfplugin.psi.reference.JsgfResolveResultGrammar;
import com.asherbernardi.jsgfplugin.psi.reference.JsgfResolveResultRule;
import com.asherbernardi.jsgfplugin.psi.reference.OtherFileGrammarNameReference;
import com.asherbernardi.jsgfplugin.psi.reference.OtherFileReferencePair;
import com.asherbernardi.jsgfplugin.psi.reference.OtherFileRuleNameReference;
import com.asherbernardi.jsgfplugin.psi.reference.RuleNameReference;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.search.searches.ReferencesSearch;
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
    String myText = element.getText();
    TextRange myRange = element.getTextRange();
    // Mark errors for whitespace before/after rule names. I was not able to get parser to do
    // that automatically... It will ignore whitespace between the angle brackets and the rulename
    if (element instanceof RuleName) {
      if (element.getNode().getTreeNext() == null
          || element.getNode().getTreeNext().getElementType() != JsgfBnfTypes.RANGLE) {
        holder.newAnnotation(HighlightSeverity.ERROR,
            "Rule name must be followed be a right angle bracket '>'")
            .range(myRange).create();
      }
      if (element.getNode().getTreePrev().getElementType() != JsgfBnfTypes.LANGLE) {
        holder.newAnnotation(HighlightSeverity.ERROR,
            "Rule name must be preceded by a left angle bracket '<'")
            .range(myRange).create();
      }
    }
    // Mark rule declarations that have already been defined as an error
    if (element instanceof JsgfRuleDeclarationName) {
      JsgfRuleDeclarationName ruleName = (JsgfRuleDeclarationName) element;
      if ("NULL".equals(ruleName.getRuleName()) || "VOID".equals(ruleName.getRuleName())) {
        holder.newAnnotation(HighlightSeverity.ERROR,
            "Cannot redefine reserved rule " + ruleName.getRuleName())
            .range(myRange)
            .create();
      }
      if (!JsgfUtil.isFirstDeclarationInFile(ruleName)) {
        holder.newAnnotation(HighlightSeverity.ERROR, "Rule <" + ruleName.getRuleName() + "> already defined")
            .range(myRange)
            .create();
      }
      if (!ruleName.isPublicRule() && ReferencesSearch.search(ruleName).findFirst() == null) {
        holder.newAnnotation(HighlightSeverity.WARNING,
                "Unused rule")
            .range(myRange)
            .create();
      }
    }
    // Mark unresolved rule references
    if (element instanceof JsgfRuleReferenceName) {
      JsgfRuleReferenceName ruleRef = (JsgfRuleReferenceName) element;
      // reserved special rules
      if ("NULL".equals(myText) || "VOID".equals(myText))
        return;
      RuleNameReference ref = ruleRef.getReferencePair().getRuleReference();
      JsgfResolveResultRule[] resolves = ref.multiResolve(false);
      if (resolves.length == 0) {
        holder.newAnnotation(HighlightSeverity.ERROR, "Cannot resolve reference to rule: "
            + myText)
            .textAttributes(JsgfHighlightType.BAD_REFERENCE.getTextAttributesKey())
            .range(myRange)
            .create();
      } else if (resolves.length > 1) {
        holder.newAnnotation(HighlightSeverity.WARNING, "Rule has more than one declaration: "
            + myText).range(myRange).create();
      } else if (!resolves[0].isLocal()
          && !resolves[0].getElement().isPublicRule()) {
        holder.newAnnotation(HighlightSeverity.ERROR,
            "<" + myText + "> does not have public access in "
                + resolves[0].getElement().getContainingFile().getName())
            .textAttributes(JsgfHighlightType.BAD_REFERENCE.getTextAttributesKey())
            .range(myRange)
            .create();
      }
    }
    if (element instanceof JsgfRuleImportName) {
      JsgfRuleImportName importName = (JsgfRuleImportName) element;
      OtherFileReferencePair refPair = importName.getReferencePair();
      // Resolving the grammar name
      OtherFileGrammarNameReference refGrammar = refPair.getGrammarReference();
      JsgfResolveResultGrammar[] resolvesGrammar = refGrammar.multiResolve(false);
      if (resolvesGrammar.length == 0) {
        holder.newAnnotation(HighlightSeverity.ERROR, "Grammar not found for imported rule: "
                + importName.getSimpleGrammarName())
            .range(refGrammar.getAbsoluteRange())
            .create();
      } else if (resolvesGrammar.length > 1) {
        holder.newAnnotation(HighlightSeverity.WARNING, "Can't resolve multiple grammar name matches: "
                + importName.getSimpleGrammarName())
            .range(refGrammar.getAbsoluteRange())
            .create();
      }
      // Resolving the rule name
      if (!importName.isStarImport()) {
        OtherFileRuleNameReference refRule = refPair.getRuleReference();
        JsgfResolveResultRule[] resolvesRule = refRule.multiResolve(false);
        if (resolvesRule.length == 0) {
          holder.newAnnotation(HighlightSeverity.ERROR,
              "Cannot resolve reference to imported rule: " + myText)
              .textAttributes(JsgfHighlightType.BAD_REFERENCE.getTextAttributesKey())
              .range(refRule.getAbsoluteRange())
              .create();
        } else if (resolvesRule.length > 1) {
          holder.newAnnotation(HighlightSeverity.ERROR,
              "Rule import refers to more than one declaration: " + myText)
              .range(refRule.getAbsoluteRange())
              .create();
        } else if (!resolvesRule[0].isLocal()
            && !resolvesRule[0].getElement().isPublicRule()) {
          holder.newAnnotation(HighlightSeverity.ERROR,
              "<" + refRule.getUnqualifiedName() + "> does not have public access in "
                  + resolvesRule[0].getElement().getContainingFile().getName())
              .textAttributes(JsgfHighlightType.BAD_REFERENCE.getTextAttributesKey())
              .range(refRule.getAbsoluteRange())
              .create();
        }
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
          Matcher firstNonWS = Pattern.compile("\\S").matcher(myText);
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
    if (element instanceof JsgfGroupExp) {
      if (((JsgfGroupExp) element).getExpansion() == null) {
        holder.newAnnotation(HighlightSeverity.ERROR,
                "Empty group")
            .range(myRange)
            .create();
      }
    }
  }
}