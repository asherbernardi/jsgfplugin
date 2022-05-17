package com.asherbernardi.jsgfplugin;

import com.asherbernardi.jsgfplugin.parser.JsgfBnfParser;
import com.asherbernardi.jsgfplugin.psi.JsgfBnfTypes;
import com.asherbernardi.jsgfplugin.psi.JsgfFile;
import com.asherbernardi.jsgfplugin.psi.JsgfTokenType;
import com.asherbernardi.jsgfplugin.psi.JsgfTypes;
import com.asherbernardi.jsgfplugin.psi.stub.JsgfStubElementTypes;
import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;

/**
 * Defines the parser that Intellij will use on Jsgf.
 * @author asherbernardi
 */
public class JsgfParserDefinition implements ParserDefinition {

  @NotNull
  @Override
  public Lexer createLexer(Project project) {
    return new JsgfLexerAdapter();
  }

  @NotNull
  @Override
  public TokenSet getCommentTokens() {
    return JsgfTypes.COMMENTS;
  }

  @NotNull
  @Override
  public TokenSet getStringLiteralElements() {
    return JsgfTypes.STRINGS;
  }

  @NotNull
  @Override
  public PsiParser createParser(final Project project) {
    return new JsgfBnfParser();
  }

  @NotNull
  @Override
  public IFileElementType getFileNodeType() {
    return JsgfStubElementTypes.FILE_ELEMENT_TYPE;
  }

  @NotNull
  @Override
  public PsiFile createFile(@NotNull FileViewProvider viewProvider) {
    return new JsgfFile(viewProvider);
  }

  @NotNull
  @Override
  public SpaceRequirements spaceExistenceTypeBetweenTokens(ASTNode left, ASTNode right) {
    return SpaceRequirements.MUST_NOT;
  }

  @NotNull
  @Override
  public PsiElement createElement(ASTNode node) {
    return JsgfBnfTypes.Factory.createElement(node);
  }
}