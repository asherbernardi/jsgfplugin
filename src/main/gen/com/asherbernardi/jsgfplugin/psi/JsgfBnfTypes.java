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

  IElementType ALTERNATIVES = new JsgfElementType("ALTERNATIVES");
  IElementType GRAMMAR_DECLARATION = new JsgfElementType("GRAMMAR_DECLARATION");
  IElementType GRAMMAR_NAME = GrammarNameStubElementType.getInstance("GRAMMAR_NAME");
  IElementType GROUP = new JsgfElementType("GROUP");
  IElementType HEADER = new JsgfElementType("HEADER");
  IElementType IMPORT_STATEMENT = new JsgfElementType("IMPORT_STATEMENT");
  IElementType RULE_DECLARATION = new JsgfElementType("RULE_DECLARATION");
  IElementType RULE_DECLARATION_NAME = RuleDeclarationStubElementType.getInstance("RULE_DECLARATION_NAME");
  IElementType RULE_DEFINITION = new JsgfElementType("RULE_DEFINITION");
  IElementType RULE_IMPORT = new JsgfElementType("RULE_IMPORT");
  IElementType RULE_IMPORT_NAME = ImportStubElementType.getInstance("RULE_IMPORT_NAME");
  IElementType RULE_REFERENCE = new JsgfElementType("RULE_REFERENCE");
  IElementType RULE_REFERENCE_NAME = new JsgfElementType("RULE_REFERENCE_NAME");
  IElementType SEQUENCE = new JsgfElementType("SEQUENCE");
  IElementType STRING = new JsgfElementType("STRING");
  IElementType TAG = new JsgfElementType("TAG");
  IElementType TERMINAL = new JsgfElementType("TERMINAL");

  IElementType AND = new JsgfTokenType("AND");
  IElementType AND_AND = new JsgfTokenType("AND_AND");
  IElementType AND_END = new JsgfTokenType("AND_END");
  IElementType AND_START = new JsgfTokenType("AND_START");
  IElementType BIAS = new JsgfTokenType("BIAS");
  IElementType EQUALS = new JsgfTokenType("=");
  IElementType GRAMMAR = new JsgfTokenType("grammar");
  IElementType IDENTIFIER = new JsgfTokenType("IDENTIFIER");
  IElementType IMPORT = new JsgfTokenType("import");
  IElementType INCLUDE = new JsgfTokenType("include");
  IElementType JSGF_IDENT = new JsgfTokenType("#JSGF");
  IElementType LANGLE = new JsgfTokenType("<");
  IElementType LBRACE = new JsgfTokenType("{");
  IElementType LBRACK = new JsgfTokenType("[");
  IElementType LPAREN = new JsgfTokenType("(");
  IElementType OR = new JsgfTokenType("|");
  IElementType PARSING_KEYWORD = new JsgfTokenType("PARSING_KEYWORD");
  IElementType PERIOD = new JsgfTokenType(".");
  IElementType PLUS = new JsgfTokenType("+");
  IElementType PUBLIC = new JsgfTokenType("public");
  IElementType QUOTE_CLOSE = new JsgfTokenType("'");
  IElementType QUOTE_OPEN = new JsgfTokenType("\"");
  IElementType RANGLE = new JsgfTokenType(">");
  IElementType RBRACE = new JsgfTokenType("}");
  IElementType RBRACK = new JsgfTokenType("]");
  IElementType RPAREN = new JsgfTokenType(")");
  IElementType RULE_NAME_IDENTIFIER = new JsgfTokenType("RULE_NAME_IDENTIFIER");
  IElementType SEMICOLON = new JsgfTokenType(";");
  IElementType SLASH = new JsgfTokenType("/");
  IElementType STAR = new JsgfTokenType("*");
  IElementType STRING_NL = new JsgfTokenType("STRING_NL");
  IElementType STRING_TEXT = new JsgfTokenType("STRING_TEXT");
  IElementType TAG_TOKEN = new JsgfTokenType("TAG_TOKEN");
  IElementType TERMINAL_IDENTIFIER = new JsgfTokenType("TERMINAL_IDENTIFIER");
  IElementType VERSION = new JsgfTokenType("VERSION");
  IElementType WEIGHT = new JsgfTokenType("WEIGHT");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == ALTERNATIVES) {
        return new JsgfAlternativesImpl(node);
      }
      else if (type == GRAMMAR_DECLARATION) {
        return new JsgfGrammarDeclarationImpl(node);
      }
      else if (type == GRAMMAR_NAME) {
        return new JsgfGrammarNameImpl(node);
      }
      else if (type == GROUP) {
        return new JsgfGroupImpl(node);
      }
      else if (type == HEADER) {
        return new JsgfHeaderImpl(node);
      }
      else if (type == IMPORT_STATEMENT) {
        return new JsgfImportStatementImpl(node);
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
      else if (type == RULE_REFERENCE) {
        return new JsgfRuleReferenceImpl(node);
      }
      else if (type == RULE_REFERENCE_NAME) {
        return new JsgfRuleReferenceNameImpl(node);
      }
      else if (type == SEQUENCE) {
        return new JsgfSequenceImpl(node);
      }
      else if (type == STRING) {
        return new JsgfStringImpl(node);
      }
      else if (type == TAG) {
        return new JsgfTagImpl(node);
      }
      else if (type == TERMINAL) {
        return new JsgfTerminalImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
