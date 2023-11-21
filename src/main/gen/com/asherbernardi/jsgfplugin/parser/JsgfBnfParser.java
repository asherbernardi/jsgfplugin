// This is a generated file. Not intended for manual editing.
package com.asherbernardi.jsgfplugin.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static com.asherbernardi.jsgfplugin.psi.JsgfBnfTypes.*;
import static com.intellij.lang.parser.GeneratedParserUtilBase.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class JsgfBnfParser implements PsiParser, LightPsiParser {

  public ASTNode parse(IElementType t, PsiBuilder b) {
    parseLight(t, b);
    return b.getTreeBuilt();
  }

  public void parseLight(IElementType t, PsiBuilder b) {
    boolean r;
    b = adapt_builder_(t, b, this, EXTENDS_SETS_);
    Marker m = enter_section_(b, 0, _COLLAPSE_, null);
    r = parse_root_(t, b);
    exit_section_(b, 0, m, t, r, true, TRUE_CONDITION);
  }

  protected boolean parse_root_(IElementType t, PsiBuilder b) {
    return parse_root_(t, b, 0);
  }

  static boolean parse_root_(IElementType t, PsiBuilder b, int l) {
    return jsgfFile(b, l + 1);
  }

  public static final TokenSet[] EXTENDS_SETS_ = new TokenSet[] {
    create_token_set_(EXPANSION, OPTIONAL_GROUP_EXP, PARENTHESES_GROUP_EXP, RULE_REFERENCE_EXP,
      SEQUENCE_EXP, STRING_EXP, TERMINAL_EXP, UNARY_OPERATION_EXP,
      UNWEIGHTED_ALTERNATIVES_EXP, WEIGHTED_ALTERNATIVES_EXP),
  };

  /* ********************************************************** */
  // unary_operation_exp
  //   | primary
  static boolean expansion_for_sequence_exp(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expansion_for_sequence_exp")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, null, "<expansion>");
    r = expansion(b, l + 1, 1);
    if (!r) r = expansion(b, l + 1, 2);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // sequence_exp
  //   | unary_operation_exp
  //   | primary
  static boolean expansion_for_weighted_alt(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expansion_for_weighted_alt")) return false;
    boolean r;
    r = expansion(b, l + 1, 0);
    if (!r) r = expansion(b, l + 1, 1);
    if (!r) r = expansion(b, l + 1, 2);
    return r;
  }

  /* ********************************************************** */
  // grammarDeclaration_with_recover SEMICOLON
  public static boolean grammarDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "grammarDeclaration")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, GRAMMAR_DECLARATION, "<grammar declaration>");
    r = grammarDeclaration_with_recover(b, l + 1);
    p = r; // pin = 1
    r = r && consumeToken(b, SEMICOLON);
    exit_section_(b, l, m, r, p, JsgfBnfParser::grammarDeclaration_top_recover);
    return r || p;
  }

  /* ********************************************************** */
  // !(SEMICOLON)
  static boolean grammarDeclaration_recover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "grammarDeclaration_recover")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !consumeToken(b, SEMICOLON);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // !(IMPORT|PUBLIC|LANGLE)
  static boolean grammarDeclaration_top_recover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "grammarDeclaration_top_recover")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !grammarDeclaration_top_recover_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // IMPORT|PUBLIC|LANGLE
  private static boolean grammarDeclaration_top_recover_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "grammarDeclaration_top_recover_0")) return false;
    boolean r;
    r = consumeToken(b, IMPORT);
    if (!r) r = consumeToken(b, PUBLIC);
    if (!r) r = consumeToken(b, LANGLE);
    return r;
  }

  /* ********************************************************** */
  // GRAMMAR grammarName
  static boolean grammarDeclaration_with_recover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "grammarDeclaration_with_recover")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, GRAMMAR);
    p = r; // pin = 1
    r = r && grammarName(b, l + 1);
    exit_section_(b, l, m, r, p, JsgfBnfParser::grammarDeclaration_recover);
    return r || p;
  }

  /* ********************************************************** */
  // IDENTIFIER
  public static boolean grammarName(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "grammarName")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    exit_section_(b, m, GRAMMAR_NAME, r);
    return r;
  }

  /* ********************************************************** */
  // header_with_recover SEMICOLON
  public static boolean header(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "header")) return false;
    if (!nextTokenIs(b, JSGF_IDENT)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, HEADER, null);
    r = header_with_recover(b, l + 1);
    p = r; // pin = 1
    r = r && consumeToken(b, SEMICOLON);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // header_top [grammarDeclaration]
  static boolean header_grammar(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "header_grammar")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = header_top(b, l + 1);
    p = r; // pin = 1
    r = r && header_grammar_1(b, l + 1);
    exit_section_(b, l, m, r, p, JsgfBnfParser::header_grammar_recover);
    return r || p;
  }

  // [grammarDeclaration]
  private static boolean header_grammar_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "header_grammar_1")) return false;
    grammarDeclaration(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // header_grammar importStatement*
  static boolean header_grammar_imports(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "header_grammar_imports")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = header_grammar(b, l + 1);
    p = r; // pin = 1
    r = r && header_grammar_imports_1(b, l + 1);
    exit_section_(b, l, m, r, p, JsgfBnfParser::header_grammar_imports_recover);
    return r || p;
  }

  // importStatement*
  private static boolean header_grammar_imports_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "header_grammar_imports_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!importStatement(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "header_grammar_imports_1", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // !(PUBLIC|LANGLE)
  static boolean header_grammar_imports_recover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "header_grammar_imports_recover")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !header_grammar_imports_recover_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // PUBLIC|LANGLE
  private static boolean header_grammar_imports_recover_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "header_grammar_imports_recover_0")) return false;
    boolean r;
    r = consumeToken(b, PUBLIC);
    if (!r) r = consumeToken(b, LANGLE);
    return r;
  }

  /* ********************************************************** */
  // header_grammar_imports ruleDefinition*
  static boolean header_grammar_imports_rules(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "header_grammar_imports_rules")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = header_grammar_imports(b, l + 1);
    r = r && header_grammar_imports_rules_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ruleDefinition*
  private static boolean header_grammar_imports_rules_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "header_grammar_imports_rules_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!ruleDefinition(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "header_grammar_imports_rules_1", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // !(IMPORT|IDENTIFIER|LANGLE|PUBLIC)
  static boolean header_grammar_recover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "header_grammar_recover")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !header_grammar_recover_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // IMPORT|IDENTIFIER|LANGLE|PUBLIC
  private static boolean header_grammar_recover_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "header_grammar_recover_0")) return false;
    boolean r;
    r = consumeToken(b, IMPORT);
    if (!r) r = consumeToken(b, IDENTIFIER);
    if (!r) r = consumeToken(b, LANGLE);
    if (!r) r = consumeToken(b, PUBLIC);
    return r;
  }

  /* ********************************************************** */
  // !(SEMICOLON)
  static boolean header_recover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "header_recover")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !consumeToken(b, SEMICOLON);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // [header]
  static boolean header_top(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "header_top")) return false;
    Marker m = enter_section_(b, l, _NONE_);
    header(b, l + 1);
    exit_section_(b, l, m, true, false, JsgfBnfParser::header_top_recover);
    return true;
  }

  /* ********************************************************** */
  // !(GRAMMAR|IMPORT|PUBLIC|LANGLE)
  static boolean header_top_recover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "header_top_recover")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !header_top_recover_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // GRAMMAR|IMPORT|PUBLIC|LANGLE
  private static boolean header_top_recover_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "header_top_recover_0")) return false;
    boolean r;
    r = consumeToken(b, GRAMMAR);
    if (!r) r = consumeToken(b, IMPORT);
    if (!r) r = consumeToken(b, PUBLIC);
    if (!r) r = consumeToken(b, LANGLE);
    return r;
  }

  /* ********************************************************** */
  // JSGF_IDENT VERSION IDENTIFIER*
  static boolean header_with_recover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "header_with_recover")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeTokens(b, 1, JSGF_IDENT, VERSION);
    p = r; // pin = 1
    r = r && header_with_recover_2(b, l + 1);
    exit_section_(b, l, m, r, p, JsgfBnfParser::header_recover);
    return r || p;
  }

  // IDENTIFIER*
  private static boolean header_with_recover_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "header_with_recover_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!consumeToken(b, IDENTIFIER)) break;
      if (!empty_element_parsed_guard_(b, "header_with_recover_2", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // importStatement_with_recover SEMICOLON
  public static boolean importStatement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "importStatement")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, IMPORT_STATEMENT, "<import statement>");
    r = importStatement_with_recover(b, l + 1);
    p = r; // pin = 1
    r = r && consumeToken(b, SEMICOLON);
    exit_section_(b, l, m, r, p, JsgfBnfParser::importStatement_loop_recover);
    return r || p;
  }

  /* ********************************************************** */
  // !(IMPORT|PUBLIC|LANGLE)
  static boolean importStatement_loop_recover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "importStatement_loop_recover")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !importStatement_loop_recover_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // IMPORT|PUBLIC|LANGLE
  private static boolean importStatement_loop_recover_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "importStatement_loop_recover_0")) return false;
    boolean r;
    r = consumeToken(b, IMPORT);
    if (!r) r = consumeToken(b, PUBLIC);
    if (!r) r = consumeToken(b, LANGLE);
    return r;
  }

  /* ********************************************************** */
  // !(SEMICOLON|IMPORT|PUBLIC|LANGLE)
  static boolean importStatement_recover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "importStatement_recover")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !importStatement_recover_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // SEMICOLON|IMPORT|PUBLIC|LANGLE
  private static boolean importStatement_recover_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "importStatement_recover_0")) return false;
    boolean r;
    r = consumeToken(b, SEMICOLON);
    if (!r) r = consumeToken(b, IMPORT);
    if (!r) r = consumeToken(b, PUBLIC);
    if (!r) r = consumeToken(b, LANGLE);
    return r;
  }

  /* ********************************************************** */
  // IMPORT ruleImport
  static boolean importStatement_with_recover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "importStatement_with_recover")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, IMPORT);
    p = r; // pin = 1
    r = r && ruleImport(b, l + 1);
    exit_section_(b, l, m, r, p, JsgfBnfParser::importStatement_recover);
    return r || p;
  }

  /* ********************************************************** */
  // header_grammar_imports_rules
  static boolean jsgfFile(PsiBuilder b, int l) {
    return header_grammar_imports_rules(b, l + 1);
  }

  /* ********************************************************** */
  // ruleDeclaration_with_recover RANGLE
  public static boolean ruleDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ruleDeclaration")) return false;
    if (!nextTokenIs(b, LANGLE)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, RULE_DECLARATION, null);
    r = ruleDeclaration_with_recover(b, l + 1);
    p = r; // pin = 1
    r = r && consumeToken(b, RANGLE);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // RULE_NAME_IDENTIFIER
  public static boolean ruleDeclarationName(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ruleDeclarationName")) return false;
    if (!nextTokenIs(b, RULE_NAME_IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, RULE_NAME_IDENTIFIER);
    exit_section_(b, m, RULE_DECLARATION_NAME, r);
    return r;
  }

  /* ********************************************************** */
  // !(EQUALS|RANGLE|SEMICOLON|PUBLIC|LBRACE)
  static boolean ruleDeclaration_recover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ruleDeclaration_recover")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !ruleDeclaration_recover_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // EQUALS|RANGLE|SEMICOLON|PUBLIC|LBRACE
  private static boolean ruleDeclaration_recover_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ruleDeclaration_recover_0")) return false;
    boolean r;
    r = consumeToken(b, EQUALS);
    if (!r) r = consumeToken(b, RANGLE);
    if (!r) r = consumeToken(b, SEMICOLON);
    if (!r) r = consumeToken(b, PUBLIC);
    if (!r) r = consumeToken(b, LBRACE);
    return r;
  }

  /* ********************************************************** */
  // LANGLE ruleDeclarationName
  static boolean ruleDeclaration_with_recover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ruleDeclaration_with_recover")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, LANGLE);
    p = r; // pin = 1
    r = r && ruleDeclarationName(b, l + 1);
    exit_section_(b, l, m, r, p, JsgfBnfParser::ruleDeclaration_recover);
    return r || p;
  }

  /* ********************************************************** */
  // ruleDefinition_with_recover SEMICOLON
  public static boolean ruleDefinition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ruleDefinition")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, RULE_DEFINITION, "<rule definition>");
    r = ruleDefinition_with_recover(b, l + 1);
    p = r; // pin = 1
    r = r && consumeToken(b, SEMICOLON);
    exit_section_(b, l, m, r, p, JsgfBnfParser::ruleDefinition_loop_recover);
    return r || p;
  }

  /* ********************************************************** */
  // !(PUBLIC|LANGLE)
  static boolean ruleDefinition_loop_recover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ruleDefinition_loop_recover")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !ruleDefinition_loop_recover_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // PUBLIC|LANGLE
  private static boolean ruleDefinition_loop_recover_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ruleDefinition_loop_recover_0")) return false;
    boolean r;
    r = consumeToken(b, PUBLIC);
    if (!r) r = consumeToken(b, LANGLE);
    return r;
  }

  /* ********************************************************** */
  // !(SEMICOLON)
  static boolean ruleDefinition_recover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ruleDefinition_recover")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !consumeToken(b, SEMICOLON);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // [PUBLIC] ruleDeclaration EQUALS expansion
  static boolean ruleDefinition_with_recover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ruleDefinition_with_recover")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = ruleDefinition_with_recover_0(b, l + 1);
    r = r && ruleDeclaration(b, l + 1);
    r = r && consumeToken(b, EQUALS);
    p = r; // pin = 3
    r = r && expansion(b, l + 1, -1);
    exit_section_(b, l, m, r, p, JsgfBnfParser::ruleDefinition_recover);
    return r || p;
  }

  // [PUBLIC]
  private static boolean ruleDefinition_with_recover_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ruleDefinition_with_recover_0")) return false;
    consumeToken(b, PUBLIC);
    return true;
  }

  /* ********************************************************** */
  // ruleImport_with_recover RANGLE
  public static boolean ruleImport(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ruleImport")) return false;
    if (!nextTokenIs(b, LANGLE)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, RULE_IMPORT, null);
    r = ruleImport_with_recover(b, l + 1);
    p = r; // pin = 1
    r = r && consumeToken(b, RANGLE);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // RULE_NAME_IDENTIFIER (PERIOD RULE_NAME_IDENTIFIER)* [PERIOD STAR]
  public static boolean ruleImportName(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ruleImportName")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, RULE_IMPORT_NAME, "<rule import name>");
    r = consumeToken(b, RULE_NAME_IDENTIFIER);
    r = r && ruleImportName_1(b, l + 1);
    r = r && ruleImportName_2(b, l + 1);
    exit_section_(b, l, m, r, false, JsgfBnfParser::ruleImport_recover);
    return r;
  }

  // (PERIOD RULE_NAME_IDENTIFIER)*
  private static boolean ruleImportName_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ruleImportName_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!ruleImportName_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "ruleImportName_1", c)) break;
    }
    return true;
  }

  // PERIOD RULE_NAME_IDENTIFIER
  private static boolean ruleImportName_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ruleImportName_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, PERIOD, RULE_NAME_IDENTIFIER);
    exit_section_(b, m, null, r);
    return r;
  }

  // [PERIOD STAR]
  private static boolean ruleImportName_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ruleImportName_2")) return false;
    parseTokens(b, 0, PERIOD, STAR);
    return true;
  }

  /* ********************************************************** */
  // !(RANGLE|SEMICOLON|IMPORT|PUBLIC|LANGLE)
  static boolean ruleImport_recover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ruleImport_recover")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !ruleImport_recover_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // RANGLE|SEMICOLON|IMPORT|PUBLIC|LANGLE
  private static boolean ruleImport_recover_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ruleImport_recover_0")) return false;
    boolean r;
    r = consumeToken(b, RANGLE);
    if (!r) r = consumeToken(b, SEMICOLON);
    if (!r) r = consumeToken(b, IMPORT);
    if (!r) r = consumeToken(b, PUBLIC);
    if (!r) r = consumeToken(b, LANGLE);
    return r;
  }

  /* ********************************************************** */
  // LANGLE ruleImportName
  static boolean ruleImport_with_recover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ruleImport_with_recover")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, LANGLE);
    p = r; // pin = 1
    r = r && ruleImportName(b, l + 1);
    exit_section_(b, l, m, r, p, JsgfBnfParser::ruleImport_recover);
    return r || p;
  }

  /* ********************************************************** */
  // RULE_NAME_IDENTIFIER (PERIOD RULE_NAME_IDENTIFIER)*
  public static boolean ruleReferenceName(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ruleReferenceName")) return false;
    if (!nextTokenIs(b, RULE_NAME_IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, RULE_NAME_IDENTIFIER);
    r = r && ruleReferenceName_1(b, l + 1);
    exit_section_(b, m, RULE_REFERENCE_NAME, r);
    return r;
  }

  // (PERIOD RULE_NAME_IDENTIFIER)*
  private static boolean ruleReferenceName_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ruleReferenceName_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!ruleReferenceName_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "ruleReferenceName_1", c)) break;
    }
    return true;
  }

  // PERIOD RULE_NAME_IDENTIFIER
  private static boolean ruleReferenceName_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ruleReferenceName_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, PERIOD, RULE_NAME_IDENTIFIER);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // &(RULE_NAME_IDENTIFIER|PERIOD)
  static boolean ruleReference_recover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ruleReference_recover")) return false;
    if (!nextTokenIs(b, "", PERIOD, RULE_NAME_IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _AND_);
    r = ruleReference_recover_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // RULE_NAME_IDENTIFIER|PERIOD
  private static boolean ruleReference_recover_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ruleReference_recover_0")) return false;
    boolean r;
    r = consumeToken(b, RULE_NAME_IDENTIFIER);
    if (!r) r = consumeToken(b, PERIOD);
    return r;
  }

  /* ********************************************************** */
  // &(RULE_NAME_IDENTIFIER|PERIOD|RANGLE)
  static boolean ruleReference_top_recover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ruleReference_top_recover")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _AND_);
    r = ruleReference_top_recover_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // RULE_NAME_IDENTIFIER|PERIOD|RANGLE
  private static boolean ruleReference_top_recover_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ruleReference_top_recover_0")) return false;
    boolean r;
    r = consumeToken(b, RULE_NAME_IDENTIFIER);
    if (!r) r = consumeToken(b, PERIOD);
    if (!r) r = consumeToken(b, RANGLE);
    return r;
  }

  /* ********************************************************** */
  // LANGLE ruleReferenceName
  static boolean ruleReference_with_recover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ruleReference_with_recover")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, LANGLE);
    p = r; // pin = 1
    r = r && ruleReferenceName(b, l + 1);
    exit_section_(b, l, m, r, p, JsgfBnfParser::ruleReference_recover);
    return r || p;
  }

  /* ********************************************************** */
  // &(STRING_TEXT)
  static boolean string_recover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "string_recover")) return false;
    if (!nextTokenIs(b, STRING_TEXT)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _AND_);
    r = consumeToken(b, STRING_TEXT);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // QUOTE [STRING_TEXT]
  static boolean string_with_recover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "string_with_recover")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, QUOTE);
    p = r; // pin = 1
    r = r && string_with_recover_1(b, l + 1);
    exit_section_(b, l, m, r, p, JsgfBnfParser::string_recover);
    return r || p;
  }

  // [STRING_TEXT]
  private static boolean string_with_recover_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "string_with_recover_1")) return false;
    consumeToken(b, STRING_TEXT);
    return true;
  }

  /* ********************************************************** */
  // tag_with_recover RBRACE
  public static boolean tag(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "tag")) return false;
    if (!nextTokenIs(b, LBRACE)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, TAG, null);
    r = tag_with_recover(b, l + 1);
    p = r; // pin = 1
    r = r && consumeToken(b, RBRACE);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // !(RBRACE)
  static boolean tag_recover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "tag_recover")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !consumeToken(b, RBRACE);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // LBRACE TAG_TOKEN*
  static boolean tag_with_recover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "tag_with_recover")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, LBRACE);
    p = r; // pin = 1
    r = r && tag_with_recover_1(b, l + 1);
    exit_section_(b, l, m, r, p, JsgfBnfParser::tag_recover);
    return r || p;
  }

  // TAG_TOKEN*
  private static boolean tag_with_recover_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "tag_with_recover_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!consumeToken(b, TAG_TOKEN)) break;
      if (!empty_element_parsed_guard_(b, "tag_with_recover_1", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // WEIGHT expansion_for_weighted_alt
  static boolean weighted_expansion(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "weighted_expansion")) return false;
    if (!nextTokenIs(b, WEIGHT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, WEIGHT);
    r = r && expansion_for_weighted_alt(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // Expression root: expansion
  // Operator priority table:
  // 0: N_ARY(unweighted_alternatives_exp) ATOM(weighted_alternatives_exp)
  // 1: POSTFIX(sequence_exp)
  // 2: POSTFIX(unary_operation_exp)
  // 3: ATOM(terminal_exp) ATOM(string_exp) ATOM(ruleReference_exp) PREFIX(parentheses_group_exp)
  //    PREFIX(optional_group_exp)
  public static boolean expansion(PsiBuilder b, int l, int g) {
    if (!recursion_guard_(b, l, "expansion")) return false;
    addVariant(b, "<expansion>");
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, "<expansion>");
    r = weighted_alternatives_exp(b, l + 1);
    if (!r) r = terminal_exp(b, l + 1);
    if (!r) r = string_exp(b, l + 1);
    if (!r) r = ruleReference_exp(b, l + 1);
    if (!r) r = parentheses_group_exp(b, l + 1);
    if (!r) r = optional_group_exp(b, l + 1);
    p = r;
    r = r && expansion_0(b, l + 1, g);
    exit_section_(b, l, m, null, r, p, null);
    return r || p;
  }

  public static boolean expansion_0(PsiBuilder b, int l, int g) {
    if (!recursion_guard_(b, l, "expansion_0")) return false;
    boolean r = true;
    while (true) {
      Marker m = enter_section_(b, l, _LEFT_, null);
      if (g < 0 && consumeTokenSmart(b, OR)) {
        while (true) {
          r = report_error_(b, expansion(b, l, 0));
          if (!consumeTokenSmart(b, OR)) break;
        }
        exit_section_(b, l, m, UNWEIGHTED_ALTERNATIVES_EXP, r, true, null);
      }
      else if (g < 1 && sequence_exp_0(b, l + 1)) {
        r = true;
        exit_section_(b, l, m, SEQUENCE_EXP, r, true, null);
      }
      else if (g < 2 && unary_operation_exp_0(b, l + 1)) {
        r = true;
        exit_section_(b, l, m, UNARY_OPERATION_EXP, r, true, null);
      }
      else {
        exit_section_(b, l, m, null, false, false, null);
        break;
      }
    }
    return r;
  }

  // weighted_expansion (OR weighted_expansion)+
  public static boolean weighted_alternatives_exp(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "weighted_alternatives_exp")) return false;
    if (!nextTokenIsSmart(b, WEIGHT)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, WEIGHTED_ALTERNATIVES_EXP, "<expansion>");
    r = weighted_expansion(b, l + 1);
    p = r; // pin = 1
    r = r && weighted_alternatives_exp_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (OR weighted_expansion)+
  private static boolean weighted_alternatives_exp_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "weighted_alternatives_exp_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = weighted_alternatives_exp_1_0(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!weighted_alternatives_exp_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "weighted_alternatives_exp_1", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  // OR weighted_expansion
  private static boolean weighted_alternatives_exp_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "weighted_alternatives_exp_1_0")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeTokenSmart(b, OR);
    p = r; // pin = 1
    r = r && weighted_expansion(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // expansion_for_sequence_exp+
  private static boolean sequence_exp_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sequence_exp_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = expansion_for_sequence_exp(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!expansion_for_sequence_exp(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "sequence_exp_0", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  // STAR|PLUS|tag
  private static boolean unary_operation_exp_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unary_operation_exp_0")) return false;
    boolean r;
    r = consumeTokenSmart(b, STAR);
    if (!r) r = consumeTokenSmart(b, PLUS);
    if (!r) r = tag(b, l + 1);
    return r;
  }

  // TERMINAL_IDENTIFIER | IDENTIFIER
  public static boolean terminal_exp(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "terminal_exp")) return false;
    if (!nextTokenIsSmart(b, IDENTIFIER, TERMINAL_IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, TERMINAL_EXP, "<expansion>");
    r = consumeTokenSmart(b, TERMINAL_IDENTIFIER);
    if (!r) r = consumeTokenSmart(b, IDENTIFIER);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // string_with_recover QUOTE
  public static boolean string_exp(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "string_exp")) return false;
    if (!nextTokenIsSmart(b, QUOTE)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, STRING_EXP, "<expansion>");
    r = string_with_recover(b, l + 1);
    p = r; // pin = 1
    r = r && consumeToken(b, QUOTE);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // ruleReference_with_recover RANGLE
  public static boolean ruleReference_exp(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ruleReference_exp")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, RULE_REFERENCE_EXP, "<expansion>");
    r = ruleReference_with_recover(b, l + 1);
    p = r; // pin = 1
    r = r && consumeToken(b, RANGLE);
    exit_section_(b, l, m, r, p, JsgfBnfParser::ruleReference_top_recover);
    return r || p;
  }

  public static boolean parentheses_group_exp(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parentheses_group_exp")) return false;
    if (!nextTokenIsSmart(b, LPAREN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, null);
    r = consumeTokenSmart(b, LPAREN);
    p = r;
    r = p && expansion(b, l, -1);
    r = p && report_error_(b, consumeToken(b, RPAREN)) && r;
    exit_section_(b, l, m, PARENTHESES_GROUP_EXP, r, p, null);
    return r || p;
  }

  public static boolean optional_group_exp(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "optional_group_exp")) return false;
    if (!nextTokenIsSmart(b, LBRACK)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, null);
    r = consumeTokenSmart(b, LBRACK);
    p = r;
    r = p && expansion(b, l, -1);
    r = p && report_error_(b, consumeToken(b, RBRACK)) && r;
    exit_section_(b, l, m, OPTIONAL_GROUP_EXP, r, p, null);
    return r || p;
  }

}
