package com.asherbernardi.jsgfplugin;

import com.asherbernardi.jsgfplugin.JsgfLexer;
import com.asherbernardi.jsgfplugin.psi.JsgfFile;
import com.asherbernardi.jsgfplugin.psi.JsgfTypes;
import com.asherbernardi.jsgfplugin.psi.impl.*;
import com.asherbernardi.jsgfplugin.psi.reference.ImportNameReference;
import com.asherbernardi.jsgfplugin.psi.reference.RuleReferenceReference;
import com.intellij.extapi.psi.ASTDelegatePsiElement;
import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveResult;
import com.asherbernardi.jsgfplugin.psi.impl.ImportNameElement;
import com.asherbernardi.jsgfplugin.psi.impl.RuleDeclarationNameElement;
import com.asherbernardi.jsgfplugin.psi.impl.RuleNameElement;
import com.intellij.psi.util.PsiTreeUtil;
import java.util.List;
import javax.swing.GroupLayout.SequentialGroup;
import org.antlr.intellij.adaptor.lexer.TokenIElementType;
import org.jetbrains.annotations.NotNull;

/**
 * Provides semantic error highlighting for JSGF.
 * @author asherbernardi
 */
public class JsgfGrammarErrorAnnotator implements Annotator {
  @Override
  public void annotate(@NotNull final PsiElement element, @NotNull AnnotationHolder holder) {
    // Mark errors for whitespace before/after rule names. I was not able to get parser to do
    // that automatically... It will ignore whitespace between the angle brackets and the rulename
    if (element instanceof RuleNameElement) {
      if (!(element.getNode().getTreeNext().getElementType() instanceof TokenIElementType) ||
          ((TokenIElementType) element.getNode().getTreeNext().getElementType()).getANTLRTokenType()
            != JsgfLexer.CLOSEARROW) {
        holder.createErrorAnnotation(element.getNode().getTreeNext().getPsi(),
            "Rule name must be followed be a right angle brackets '>'");
      }
      if (!(element.getNode().getTreePrev().getElementType() instanceof TokenIElementType) ||
          ((TokenIElementType) element.getNode().getTreePrev().getElementType()).getANTLRTokenType()
              != JsgfLexer.OPENARROW) {
        holder.createErrorAnnotation(element.getNode().getTreePrev().getPsi(),
            "Rule name must be preceded by a left angle brackets '<'");
      }
    }
    // Mark rule declarations that have that have already been defined as an error
    if (element instanceof RuleDeclarationNameElement) {
      RuleDeclarationNameElement ruleName = (RuleDeclarationNameElement) element;
      if ("NULL".equals(ruleName.getName()) || "VOID".equals(ruleName.getName()))
        holder.createErrorAnnotation(ruleName, "Cannot redefine reserved rule " + ruleName.getName());
      if (!JsgfUtil.isFirstDeclarationInFile((JsgfFile) element.getContainingFile(), ruleName))
        holder.createErrorAnnotation(ruleName, "Rule <" + ruleName.getName() + "> already defined");
    }
    // Mark unresolved rule references
    if (element instanceof RuleReferenceElement) {
      RuleReferenceElement ruleRef = (RuleReferenceElement) element;
      // reserved special rules
      if ("NULL".equals(ruleRef.getName()) || "VOID".equals(ruleRef.getName()))
        return;
      ResolveResult[] resolves = ((RuleReferenceReference) ruleRef.getReference()).multiResolve(false);
      if (resolves.length == 0) {
        Annotation annotation = holder.createErrorAnnotation(element, "Cannot resolve reference to rule: "
            + ruleRef.getName());
        annotation.setTextAttributes(JsgfSyntaxHighlighter.BAD_REFERENCE);
      }
      else if (resolves.length > 1){
        holder.createWarningAnnotation(element, "Rule has more than one declaration: "
            + ruleRef.getName());
      }
    }
    if (element instanceof ImportNameElement) {
      if (((ImportNameElement) element).isStarImport()) {
        List<JsgfFile> files = JsgfUtil.findFilesByPackage(element.getProject(), ((ImportNameElement) element));
        if (files.isEmpty()) {
          holder.createErrorAnnotation(element, "File not found for imported rule: "
                  + ((ImportNameElement) element).getName());
        }
      }
      else {
        ResolveResult[] resolves = ((ImportNameReference) element.getReference())
            .multiResolve(false);
        if (resolves.length == 0) {
          Annotation annotation = holder
              .createErrorAnnotation(element, "Cannot resolve reference to imported rule: "
                  + ((ImportNameElement) element).getName());
          annotation.setTextAttributes(JsgfSyntaxHighlighter.BAD_REFERENCE);
        } else if (resolves.length > 1) {
          holder
              .createWarningAnnotation(element, "Rule import refers to more than one declaration: "
                  + ((ImportNameElement) element).getName());
        }
      }
    }
    // No viable alternative error
    if (element instanceof AlternativesElement) {
      SequenceElement[] sequences = ((AlternativesElement) element).getAlternatives();
      for (SequenceElement alt : sequences) {
        if (PsiTreeUtil.findChildrenOfAnyType(alt, RuleReferenceElement.class,
            TerminalElement.class, StringElement.class, AlternativesElement.class).isEmpty()) {
          holder.createErrorAnnotation(alt, "Empty alternative: " + alt.getText());
        }
      }
      if (!(((AlternativesElement) element).getOrSymbols().length == sequences.length - 1)) {
        holder.createErrorAnnotation(element, "No viable alternative for input: " + element.getText());
      }
    }
  }
}