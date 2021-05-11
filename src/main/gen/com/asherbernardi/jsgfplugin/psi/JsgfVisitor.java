// This is a generated file. Not intended for manual editing.
package com.asherbernardi.jsgfplugin.psi;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiElement;

public class JsgfVisitor extends PsiElementVisitor {

  public void visitAlternatives(@NotNull JsgfAlternatives o) {
    visitPsiElement(o);
  }

  public void visitGrammarDeclaration(@NotNull JsgfGrammarDeclaration o) {
    visitPsiElement(o);
  }

  public void visitGrammarName(@NotNull JsgfGrammarName o) {
    visitGrammarName(o);
  }

  public void visitGroup(@NotNull JsgfGroup o) {
    visitPsiElement(o);
  }

  public void visitHeader(@NotNull JsgfHeader o) {
    visitPsiElement(o);
  }

  public void visitImportStatement(@NotNull JsgfImportStatement o) {
    visitPsiElement(o);
  }

  public void visitRuleDeclaration(@NotNull JsgfRuleDeclaration o) {
    visitPsiElement(o);
  }

  public void visitRuleDeclarationName(@NotNull JsgfRuleDeclarationName o) {
    visitRuleDeclarationName(o);
  }

  public void visitRuleDefinition(@NotNull JsgfRuleDefinition o) {
    visitPsiElement(o);
  }

  public void visitRuleImport(@NotNull JsgfRuleImport o) {
    visitPsiElement(o);
  }

  public void visitRuleImportName(@NotNull JsgfRuleImportName o) {
    visitImportName(o);
  }

  public void visitRuleReference(@NotNull JsgfRuleReference o) {
    visitPsiElement(o);
  }

  public void visitRuleReferenceName(@NotNull JsgfRuleReferenceName o) {
    visitRuleName(o);
  }

  public void visitSequence(@NotNull JsgfSequence o) {
    visitPsiElement(o);
  }

  public void visitString(@NotNull JsgfString o) {
    visitPsiElement(o);
  }

  public void visitTag(@NotNull JsgfTag o) {
    visitPsiElement(o);
  }

  public void visitTerminal(@NotNull JsgfTerminal o) {
    visitPsiElement(o);
  }

  public void visitImportName(@NotNull ImportName o) {
    visitElement(o);
  }

  public void visitRuleName(@NotNull RuleName o) {
    visitElement(o);
  }

  public void visitPsiElement(@NotNull PsiElement o) {
    visitElement(o);
  }

}
