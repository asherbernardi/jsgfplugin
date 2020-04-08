package com.asherbernardi.jsgfplugin;

import com.asherbernardi.jsgfplugin.psi.impl.AlternativesElement;
import com.asherbernardi.jsgfplugin.psi.impl.SequenceElement;
import com.asherbernardi.jsgfplugin.psi.impl.StringElement;
import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import com.asherbernardi.jsgfplugin.psi.JsgfFile;
import com.asherbernardi.jsgfplugin.psi.JsgfTypes;
import com.asherbernardi.jsgfplugin.psi.impl.GrammarNameElement;
import com.asherbernardi.jsgfplugin.psi.impl.ImportNameElement;
import com.asherbernardi.jsgfplugin.psi.impl.ImportStatementElement;
import com.asherbernardi.jsgfplugin.psi.impl.InitGramSubtree;
import com.asherbernardi.jsgfplugin.psi.impl.JsgfIdentSubtree;
import com.asherbernardi.jsgfplugin.psi.impl.TagElement;
import com.asherbernardi.jsgfplugin.psi.impl.RuleDeclarationNameElement;
import com.asherbernardi.jsgfplugin.psi.impl.RuleDeclarationSubtree;
import com.asherbernardi.jsgfplugin.psi.impl.RuleReferenceElement;
import com.asherbernardi.jsgfplugin.psi.impl.TerminalElement;
import java.util.List;
import org.antlr.intellij.adaptor.lexer.ANTLRLexerAdaptor;
import org.antlr.intellij.adaptor.lexer.PSIElementTypeFactory;
import org.antlr.intellij.adaptor.lexer.RuleIElementType;
import org.antlr.intellij.adaptor.lexer.TokenIElementType;
import org.antlr.intellij.adaptor.parser.ANTLRParserAdaptor;
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.tree.ParseTree;
import org.jetbrains.annotations.NotNull;

/**
 * Defines the parser that Intellij will use on Jsgf. It uses the ANTLR4 intellij adaptor.
 * @author asherbernardi
 */
public class JsgfParserDefinition implements ParserDefinition {
  public static final IFileElementType FILE = new IFileElementType(JsgfLanguage.INSTANCE);

  static {
    PSIElementTypeFactory.defineLanguageIElementTypes(JsgfLanguage.INSTANCE,
        JsgfParser.tokenNames,
        JsgfParser.ruleNames);
    List<TokenIElementType> tokenIElementTypes =
        PSIElementTypeFactory.getTokenIElementTypes(JsgfLanguage.INSTANCE);
  }


  @NotNull
  @Override
  public Lexer createLexer(Project project) {
    JsgfLexer lexer = new JsgfLexer(null);
    return new ANTLRLexerAdaptor(JsgfLanguage.INSTANCE, lexer);
  }

  @NotNull
  @Override
  public TokenSet getWhitespaceTokens() {
    return JsgfTypes.WHITE_SPACES;
  }

  @NotNull
  @Override
  public TokenSet getCommentTokens() {
    return JsgfTypes.COMMENTS;
  }

  @NotNull
  @Override
  public TokenSet getStringLiteralElements() {
    return JsgfTypes.STRING;
  }

  @NotNull
  @Override
  public PsiParser createParser(final Project project) {
    final JsgfParser parser = new JsgfParser(null);
    return new ANTLRParserAdaptor(JsgfLanguage.INSTANCE, parser) {
      @Override
      protected ParseTree parse(Parser parser, IElementType root) {
        if (root instanceof IFileElementType) {
          return ((JsgfParser) parser).getGrammar();
        }
        // When you rename an element, it does some strange things to change
        // the actual node by creating a new file then re-parsing that, and
        // in that case, it passes in a Token type to be parsed separately
        else if (root instanceof TokenIElementType) {
          switch (((TokenIElementType) root).getANTLRTokenType()) {
            case JsgfLexer.RULE_NAME_IDENTIFIER:
              return ((JsgfParser) parser).parseJustRuleName();
            default:
              return null;
          }
        }
        return null;
      }
    };
  }

  @Override
  public IFileElementType getFileNodeType() {
    return FILE;
  }

  @Override
  public PsiFile createFile(FileViewProvider viewProvider) {
    return new JsgfFile(viewProvider);
  }

  @Override
  public SpaceRequirements spaceExistenceTypeBetweenTokens(ASTNode left, ASTNode right) {
    return SpaceRequirements.MUST_NOT;
  }

  /** Convert from *NON-LEAF* parse node (AST they call it)
   *  to PSI node. Leaves are created in the AST factory.
   *  Rename re-factoring can cause this to be
   *  called on a TokenIElementType since we want to rename ID nodes.
   *  In that case, this method is called to create the root node
   *  but with ID type. Kind of strange, but we can simply create a
   *  ASTWrapperPsiElement to make everything work correctly.
   *
   *  RuleIElementType.  Ah! It's that ID is the root
   *  IElementType requested to parse, which means that the root
   *  node returned from parsetree->PSI conversion.  But, it
   *  must be a CompositeElement! The adaptor calls
   *  rootMarker.done(root) to finish off the PSI conversion.
   *  See {@link ANTLRParserAdaptor#parse(IElementType root,
   *  PsiBuilder)}
   *
   *  If you don't care to distinguish PSI nodes by type, it is
   *  sufficient to create a {@link ANTLRPsiNode} around
   *  the parse tree node
   */
  @NotNull
  @Override
  public PsiElement createElement(ASTNode node) {
    IElementType elType = node.getElementType();
    // for Token elements
    if (elType instanceof TokenIElementType) {
      TokenIElementType tokenElType = (TokenIElementType) elType;
      return new ANTLRPsiNode(node);
    }
    // all other elements are Rule elements
    else {
      RuleIElementType ruleElType = (RuleIElementType) elType;
      switch (ruleElType.getRuleIndex()) {
        case JsgfParser.RULE_declarationName:
          return new RuleDeclarationNameElement(node);
        case JsgfParser.RULE_importName:
          return new ImportNameElement(node);
        case JsgfParser.RULE_ruleReference:
          return new RuleReferenceElement(node);
        case JsgfParser.RULE_terminal:
          return new TerminalElement(node);
        case JsgfParser.RULE_grammarName:
          return new GrammarNameElement(node);
        case JsgfParser.RULE_jsgfIdent:
          return new JsgfIdentSubtree(node, elType);
        case JsgfParser.RULE_initGram:
          return new InitGramSubtree(node, elType);
        case JsgfParser.RULE_importStatement:
          return new ImportStatementElement(node, elType);
        case JsgfParser.RULE_ruleDeclaration:
          return new RuleDeclarationSubtree(node, elType);
        case JsgfParser.RULE_jsgfTag:
          return new TagElement(node);
        case JsgfParser.RULE_string:
          return new StringElement(node);
        case JsgfParser.RULE_jsgfAlternatives:
          return new AlternativesElement(node);
        case JsgfParser.RULE_jsgfSequence:
          return new SequenceElement(node);
        default:
          return new ANTLRPsiNode(node);
      }
    }
  }
}