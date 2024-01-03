// This is a generated file. Not intended for manual editing.
package com.asherbernardi.jsgfplugin.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import com.asherbernardi.jsgfplugin.psi.stub.GrammarNameStubElementType;
import com.asherbernardi.jsgfplugin.psi.stub.RuleDeclarationStubElementType;
import com.asherbernardi.jsgfplugin.psi.stub.ImportStubElementType;
import com.asherbernardi.jsgfplugin.psi.impl.*;

public interface JsgfBnfTypes {

  IElementType EXPANSION = new JsgfElementType("EXPANSION");
  IElementType GRAMMAR_DECLARATION = new JsgfElementType("GRAMMAR_DECLARATION");
  IElementType GRAMMAR_NAME = GrammarNameStubElementType.getInstance("GRAMMAR_NAME");
  IElementType HEADER = new JsgfElementType("HEADER");
  IElementType IMPORT_STATEMENT = new JsgfElementType("IMPORT_STATEMENT");
  IElementType OPTIONAL_GROUP_EXP = new JsgfElementType("OPTIONAL_GROUP_EXP");
  IElementType PARENTHESES_GROUP_EXP = new JsgfElementType("PARENTHESES_GROUP_EXP");
  IElementType RULE_DECLARATION = new JsgfElementType("RULE_DECLARATION");
  IElementType RULE_DECLARATION_NAME = RuleDeclarationStubElementType.getInstance("RULE_DECLARATION_NAME");
  IElementType RULE_DEFINITION = new JsgfElementType("RULE_DEFINITION");
  IElementType RULE_IMPORT = new JsgfElementType("RULE_IMPORT");
  IElementType RULE_IMPORT_NAME = ImportStubElementType.getInstance("RULE_IMPORT_NAME");
  IElementType RULE_REFERENCE_EXP = new JsgfElementType("RULE_REFERENCE_EXP");
  IElementType RULE_REFERENCE_NAME = new JsgfElementType("RULE_REFERENCE_NAME");
  IElementType SEQUENCE_EXP = new JsgfElementType("SEQUENCE_EXP");
  IElementType STRING_EXP = new JsgfElementType("STRING_EXP");
  IElementType TAG = new JsgfElementType("TAG");
  IElementType TERMINAL_EXP = new JsgfElementType("TERMINAL_EXP");
  IElementType UNARY_OPERATION_EXP = new JsgfElementType("UNARY_OPERATION_EXP");
  IElementType UNWEIGHTED_ALTERNATIVES_EXP = new JsgfElementType("UNWEIGHTED_ALTERNATIVES_EXP");
  IElementType WEIGHTED_ALTERNATIVES_EXP = new JsgfElementType("WEIGHTED_ALTERNATIVES_EXP");

  IElementType EQUALS = new JsgfTokenType("=");
  IElementType GRAMMAR = new JsgfTokenType("grammar");
  IElementType IDENTIFIER = new JsgfTokenType("IDENTIFIER");
  IElementType IMPORT = new JsgfTokenType("import");
  IElementType JSGF_IDENT = new JsgfTokenType("#JSGF");
  IElementType LANGLE = new JsgfTokenType("<");
  IElementType LBRACE = new JsgfTokenType("{");
  IElementType LBRACK = new JsgfTokenType("[");
  IElementType LPAREN = new JsgfTokenType("(");
  IElementType OR = new JsgfTokenType("|");
  IElementType PERIOD = new JsgfTokenType(".");
  IElementType PLUS = new JsgfTokenType("+");
  IElementType PUBLIC = new JsgfTokenType("public");
  IElementType QUOTE = new JsgfTokenType("\"");
  IElementType RANGLE = new JsgfTokenType(">");
  IElementType RBRACE = new JsgfTokenType("}");
  IElementType RBRACK = new JsgfTokenType("]");
  IElementType RPAREN = new JsgfTokenType(")");
  IElementType RULE_NAME_IDENTIFIER = new JsgfTokenType("RULE_NAME_IDENTIFIER");
  IElementType SEMICOLON = new JsgfTokenType(";");
  IElementType SLASH = new JsgfTokenType("/");
  IElementType STAR = new JsgfTokenType("*");
  IElementType STRING_TEXT = new JsgfTokenType("STRING_TEXT");
  IElementType TAG_TOKEN = new JsgfTokenType("TAG_TOKEN");
  IElementType TERMINAL_IDENTIFIER = new JsgfTokenType("TERMINAL_IDENTIFIER");
  IElementType VERSION = new JsgfTokenType("VERSION");
  IElementType WEIGHT = new JsgfTokenType("WEIGHT");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == GRAMMAR_DECLARATION) {
        return new JsgfGrammarDeclarationImpl(node);
      }
      else if (type == GRAMMAR_NAME) {
        return new JsgfGrammarNameImpl(node);
      }
      else if (type == HEADER) {
        return new JsgfHeaderImpl(node);
      }
      else if (type == IMPORT_STATEMENT) {
        return new JsgfImportStatementImpl(node);
      }
      else if (type == OPTIONAL_GROUP_EXP) {
        return new JsgfOptionalGroupExpImpl(node);
      }
      else if (type == PARENTHESES_GROUP_EXP) {
        return new JsgfParenthesesGroupExpImpl(node);
      }
      else if (type == RULE_DECLARATION) {
        return new JsgfRuleDeclarationImpl(node);
      }
      else if (type == RULE_DECLARATION_NAME) {
        return new JsgfRuleDeclarationNameImpl(node);
      }
      else if (type == RULE_DEFINITION) {
        return new JsgfRuleDefinitionImpl(node);
      }
      else if (type == RULE_IMPORT) {
        return new JsgfRuleImportImpl(node);
      }
      else if (type == RULE_IMPORT_NAME) {
        return new JsgfRuleImportNameImpl(node);
      }
      else if (type == RULE_REFERENCE_EXP) {
        return new JsgfRuleReferenceExpImpl(node);
      }
      else if (type == RULE_REFERENCE_NAME) {
        return new JsgfRuleReferenceNameImpl(node);
      }
      else if (type == SEQUENCE_EXP) {
        return new JsgfSequenceExpImpl(node);
      }
      else if (type == STRING_EXP) {
        return new JsgfStringExpImpl(node);
      }
      else if (type == TAG) {
        return new JsgfTagImpl(node);
      }
      else if (type == TERMINAL_EXP) {
        return new JsgfTerminalExpImpl(node);
      }
      else if (type == UNARY_OPERATION_EXP) {
        return new JsgfUnaryOperationExpImpl(node);
      }
      else if (type == UNWEIGHTED_ALTERNATIVES_EXP) {
        return new JsgfUnweightedAlternativesExpImpl(node);
      }
      else if (type == WEIGHTED_ALTERNATIVES_EXP) {
        return new JsgfWeightedAlternativesExpImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
