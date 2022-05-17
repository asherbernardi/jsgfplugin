// This is a generated file. Not intended for manual editing.
package com.asherbernardi.jsgfplugin.psi;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiElement;

public class JsgfVisitor extends PsiElementVisitor {

  public void visitAlternativesExp(@NotNull JsgfAlternativesExp o) {
    visitExpansion(o);
  }

  public void visitExpansion(@NotNull JsgfExpansion o) {
    visitPsiElement(o);
  }

  public void visitExpansionWithTagExp(@NotNull JsgfExpansionWithTagExp o) {
    visitExpansion(o);
  }

  public void visitGrammarDeclaration(@NotNull JsgfGrammarDeclaration o) {
    visitPsiElement(o);
  }

  public void visitGrammarName(@NotNull JsgfGrammarName o) {
    visitGrammarName(o);
  }

  public void visitGroupExp(@NotNull JsgfGroupExp o) {
    visitExpansion(o);
  }

  public void visitHeader(@NotNull JsgfHeader o) {
    visitPsiElement(o);
  }

  public void visitImportStatement(@NotNull JsgfImportStatement o) {
    visitPsiElement(o);
  }

  public void visitOptionalGroupExp(@NotNull JsgfOptionalGroupExp o) {
    visitGroupExp(o);
  }

  public void visitParenthesesGroupExp(@NotNull JsgfParenthesesGroupExp o) {
    visitGroupExp(o);
  }

  public void visitRuleDeclaration(@NotNull JsgfRuleDeclaration o) {
    visitPsiElement(o);
  }

  public void visitRuleDeclarationName(@NotNull JsgfRuleDeclarationName o) {
    visitRuleName(o);
  }

  public void visitRuleDefinition(@NotNull JsgfRuleDefinition o) {
    visitPsiElement(o);
  }

  public void visitRuleImport(@NotNull JsgfRuleImport o) {
    visitPsiElement(o);
  }

  public void visitRuleImportName(@NotNull JsgfRuleImportName o) {
    visitRuleName(o);
  }

  public void visitRuleReferenceName(@NotNull JsgfRuleReferenceName o) {
    visitRuleName(o);
  }

  public void visitRuleReferenceExp(@NotNull JsgfRuleReferenceExp o) {
    visitExpansion(o);
  }

  public void visitSequenceExp(@NotNull JsgfSequenceExp o) {
    visitExpansion(o);
  }

  public void visitStringExp(@NotNull JsgfStringExp o) {
    visitExpansion(o);
  }

  public void visitTag(@NotNull JsgfTag o) {
    visitPsiElement(o);
  }

  public void visitTerminalExp(@NotNull JsgfTerminalExp o) {
    visitExpansion(o);
  }

  public void visitUnaryOperationExp(@NotNull JsgfUnaryOperationExp o) {
    visitExpansion(o);
  }

  public void visitUnweightedAlternativesExp(@NotNull JsgfUnweightedAlternativesExp o) {
    visitAlternativesExp(o);
  }

  public void visitWeightedAlternativesExp(@NotNull JsgfWeightedAlternativesExp o) {
    visitAlternativesExp(o);
  }

  public void visitRuleName(@NotNull RuleName o) {
    visitElement(o);
  }

  public void visitPsiElement(@NotNull PsiElement o) {
    visitElement(o);
  }

}
