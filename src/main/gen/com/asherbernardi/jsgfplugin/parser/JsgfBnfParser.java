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
    b = adapt_builder_(t, b, this, null);
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

  /* ********************************************************** */
  // unweighted_alternatives | weighted_alternatives
  public static boolean alternatives(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "alternatives")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ALTERNATIVES, "<alternatives>");
    r = unweighted_alternatives(b, l + 1);
    if (!r) r = weighted_alternatives(b, l + 1);
    exit_section_(b, l, m, r, false, JsgfBnfParser::alternatives_recover);
    return r;
  }

  /* ********************************************************** */
  // !(SEMICOLON | RPAREN | RBRACK)
  static boolean alternatives_recover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "alternatives_recover")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !alternatives_recover_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // SEMICOLON | RPAREN | RBRACK
  private static boolean alternatives_recover_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "alternatives_recover_0")) return false;
    boolean r;
    r = consumeToken(b, SEMICOLON);
    if (!r) r = consumeToken(b, RPAREN);
    if (!r) r = consumeToken(b, RBRACK);
    return r;
  }

  /* ********************************************************** */
  // brack_group_with_recover RBRACK
  static boolean brack_group(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "brack_group")) return false;
    if (!nextTokenIs(b, LBRACK)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = brack_group_with_recover(b, l + 1);
    p = r; // pin = 1
    r = r && consumeToken(b, RBRACK);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // !(RBRACK | SEMICOLON)
  static boolean brack_group_recover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "brack_group_recover")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !brack_group_recover_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // RBRACK | SEMICOLON
  private static boolean brack_group_recover_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "brack_group_recover_0")) return false;
    boolean r;
    r = consumeToken(b, RBRACK);
    if (!r) r = consumeToken(b, SEMICOLON);
    return r;
  }

  /* ********************************************************** */
  // LBRACK alternatives
  static boolean brack_group_with_recover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "brack_group_with_recover")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, LBRACK);
    p = r; // pin = 1
    r = r && alternatives(b, l + 1);
    exit_section_(b, l, m, r, p, JsgfBnfParser::brack_group_recover);
    return r || p;
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
  // !(IMPORT|INCLUDE|PUBLIC|ruleDeclaration EQUALS)
  static boolean grammarDeclaration_top_recover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "grammarDeclaration_top_recover")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !grammarDeclaration_top_recover_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // IMPORT|INCLUDE|PUBLIC|ruleDeclaration EQUALS
  private static boolean grammarDeclaration_top_recover_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "grammarDeclaration_top_recover_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IMPORT);
    if (!r) r = consumeToken(b, INCLUDE);
    if (!r) r = consumeToken(b, PUBLIC);
    if (!r) r = grammarDeclaration_top_recover_0_3(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ruleDeclaration EQUALS
  private static boolean grammarDeclaration_top_recover_0_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "grammarDeclaration_top_recover_0_3")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = ruleDeclaration(b, l + 1);
    r = r && consumeToken(b, EQUALS);
    exit_section_(b, m, null, r);
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
  // paren_group | brack_group
  public static boolean group(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "group")) return false;
    if (!nextTokenIs(b, "<group>", LBRACK, LPAREN)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, GROUP, "<group>");
    r = paren_group(b, l + 1);
    if (!r) r = brack_group(b, l + 1);
    exit_section_(b, l, m, r, false, null);
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
  // !(PUBLIC|ruleDeclaration EQUALS)
  static boolean header_grammar_imports_recover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "header_grammar_imports_recover")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !header_grammar_imports_recover_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // PUBLIC|ruleDeclaration EQUALS
  private static boolean header_grammar_imports_recover_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "header_grammar_imports_recover_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PUBLIC);
    if (!r) r = header_grammar_imports_recover_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ruleDeclaration EQUALS
  private static boolean header_grammar_imports_recover_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "header_grammar_imports_recover_0_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = ruleDeclaration(b, l + 1);
    r = r && consumeToken(b, EQUALS);
    exit_section_(b, m, null, r);
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
  // !(IMPORT|INCLUDE|IDENTIFIER|LANGLE|PUBLIC)
  static boolean header_grammar_recover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "header_grammar_recover")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !header_grammar_recover_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // IMPORT|INCLUDE|IDENTIFIER|LANGLE|PUBLIC
  private static boolean header_grammar_recover_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "header_grammar_recover_0")) return false;
    boolean r;
    r = consumeToken(b, IMPORT);
    if (!r) r = consumeToken(b, INCLUDE);
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
  // !(GRAMMAR|IMPORT|INCLUDE|PUBLIC|ruleDeclaration)
  static boolean header_top_recover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "header_top_recover")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !header_top_recover_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // GRAMMAR|IMPORT|INCLUDE|PUBLIC|ruleDeclaration
  private static boolean header_top_recover_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "header_top_recover_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, GRAMMAR);
    if (!r) r = consumeToken(b, IMPORT);
    if (!r) r = consumeToken(b, INCLUDE);
    if (!r) r = consumeToken(b, PUBLIC);
    if (!r) r = ruleDeclaration(b, l + 1);
    exit_section_(b, m, null, r);
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
  // !(INCLUDE|IMPORT|PUBLIC|ruleDeclaration EQUALS)
  static boolean importStatement_loop_recover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "importStatement_loop_recover")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !importStatement_loop_recover_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // INCLUDE|IMPORT|PUBLIC|ruleDeclaration EQUALS
  private static boolean importStatement_loop_recover_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "importStatement_loop_recover_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, INCLUDE);
    if (!r) r = consumeToken(b, IMPORT);
    if (!r) r = consumeToken(b, PUBLIC);
    if (!r) r = importStatement_loop_recover_0_3(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ruleDeclaration EQUALS
  private static boolean importStatement_loop_recover_0_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "importStatement_loop_recover_0_3")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = ruleDeclaration(b, l + 1);
    r = r && consumeToken(b, EQUALS);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // !(SEMICOLON|PUBLIC|INCLUDE|IMPORT|ruleDeclaration EQUALS)
  static boolean importStatement_recover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "importStatement_recover")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !importStatement_recover_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // SEMICOLON|PUBLIC|INCLUDE|IMPORT|ruleDeclaration EQUALS
  private static boolean importStatement_recover_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "importStatement_recover_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, SEMICOLON);
    if (!r) r = consumeToken(b, PUBLIC);
    if (!r) r = consumeToken(b, INCLUDE);
    if (!r) r = consumeToken(b, IMPORT);
    if (!r) r = importStatement_recover_0_4(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ruleDeclaration EQUALS
  private static boolean importStatement_recover_0_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "importStatement_recover_0_4")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = ruleDeclaration(b, l + 1);
    r = r && consumeToken(b, EQUALS);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // (IMPORT|INCLUDE) ruleImport
  static boolean importStatement_with_recover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "importStatement_with_recover")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = importStatement_with_recover_0(b, l + 1);
    p = r; // pin = 1
    r = r && ruleImport(b, l + 1);
    exit_section_(b, l, m, r, p, JsgfBnfParser::importStatement_recover);
    return r || p;
  }

  // IMPORT|INCLUDE
  private static boolean importStatement_with_recover_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "importStatement_with_recover_0")) return false;
    boolean r;
    r = consumeToken(b, IMPORT);
    if (!r) r = consumeToken(b, INCLUDE);
    return r;
  }

  /* ********************************************************** */
  // (ruleReference | terminal | string | group) [STAR | PLUS] tag*
  static boolean item(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "item")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = item_0(b, l + 1);
    r = r && item_1(b, l + 1);
    r = r && item_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ruleReference | terminal | string | group
  private static boolean item_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "item_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = ruleReference(b, l + 1);
    if (!r) r = terminal(b, l + 1);
    if (!r) r = string(b, l + 1);
    if (!r) r = group(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [STAR | PLUS]
  private static boolean item_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "item_1")) return false;
    item_1_0(b, l + 1);
    return true;
  }

  // STAR | PLUS
  private static boolean item_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "item_1_0")) return false;
    boolean r;
    r = consumeToken(b, STAR);
    if (!r) r = consumeToken(b, PLUS);
    return r;
  }

  // tag*
  private static boolean item_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "item_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!tag(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "item_2", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // header_grammar_imports_rules
  static boolean jsgfFile(PsiBuilder b, int l) {
    return header_grammar_imports_rules(b, l + 1);
  }

  /* ********************************************************** */
  // paren_group_with_recover RPAREN
  static boolean paren_group(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "paren_group")) return false;
    if (!nextTokenIs(b, LPAREN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = paren_group_with_recover(b, l + 1);
    p = r; // pin = 1
    r = r && consumeToken(b, RPAREN);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // !(RPAREN | SEMICOLON)
  static boolean paren_group_recover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "paren_group_recover")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !paren_group_recover_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // RPAREN | SEMICOLON
  private static boolean paren_group_recover_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "paren_group_recover_0")) return false;
    boolean r;
    r = consumeToken(b, RPAREN);
    if (!r) r = consumeToken(b, SEMICOLON);
    return r;
  }

  /* ********************************************************** */
  // LPAREN alternatives
  static boolean paren_group_with_recover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "paren_group_with_recover")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, LPAREN);
    p = r; // pin = 1
    r = r && alternatives(b, l + 1);
    exit_section_(b, l, m, r, p, JsgfBnfParser::paren_group_recover);
    return r || p;
  }

  /* ********************************************************** */
  // LANGLE ruleDeclarationName RANGLE
  public static boolean ruleDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ruleDeclaration")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, RULE_DECLARATION, "<rule declaration>");
    r = consumeToken(b, LANGLE);
    p = r; // pin = 1
    r = r && report_error_(b, ruleDeclarationName(b, l + 1));
    r = p && consumeToken(b, RANGLE) && r;
    exit_section_(b, l, m, r, p, JsgfBnfParser::ruleDeclaration_recover);
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
  // !(EQUALS | SEMICOLON)
  static boolean ruleDeclaration_recover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ruleDeclaration_recover")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !ruleDeclaration_recover_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // EQUALS | SEMICOLON
  private static boolean ruleDeclaration_recover_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ruleDeclaration_recover_0")) return false;
    boolean r;
    r = consumeToken(b, EQUALS);
    if (!r) r = consumeToken(b, SEMICOLON);
    return r;
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
  // !(PUBLIC|ruleDeclaration EQUALS)
  static boolean ruleDefinition_loop_recover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ruleDefinition_loop_recover")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !ruleDefinition_loop_recover_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // PUBLIC|ruleDeclaration EQUALS
  private static boolean ruleDefinition_loop_recover_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ruleDefinition_loop_recover_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PUBLIC);
    if (!r) r = ruleDefinition_loop_recover_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ruleDeclaration EQUALS
  private static boolean ruleDefinition_loop_recover_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ruleDefinition_loop_recover_0_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = ruleDeclaration(b, l + 1);
    r = r && consumeToken(b, EQUALS);
    exit_section_(b, m, null, r);
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
  // [PUBLIC] ruleDeclaration EQUALS alternatives
  static boolean ruleDefinition_with_recover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ruleDefinition_with_recover")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = ruleDefinition_with_recover_0(b, l + 1);
    r = r && ruleDeclaration(b, l + 1);
    p = r; // pin = 2
    r = r && report_error_(b, consumeToken(b, EQUALS));
    r = p && alternatives(b, l + 1) && r;
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
  // LANGLE ruleImportName RANGLE
  public static boolean ruleImport(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ruleImport")) return false;
    if (!nextTokenIs(b, LANGLE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LANGLE);
    r = r && ruleImportName(b, l + 1);
    r = r && consumeToken(b, RANGLE);
    exit_section_(b, m, RULE_IMPORT, r);
    return r;
  }

  /* ********************************************************** */
  // RULE_NAME_IDENTIFIER (PERIOD RULE_NAME_IDENTIFIER)* [PERIOD STAR]
  public static boolean ruleImportName(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ruleImportName")) return false;
    if (!nextTokenIs(b, RULE_NAME_IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, RULE_NAME_IDENTIFIER);
    r = r && ruleImportName_1(b, l + 1);
    r = r && ruleImportName_2(b, l + 1);
    exit_section_(b, m, RULE_IMPORT_NAME, r);
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
  // ruleReference_with_recover RANGLE
  public static boolean ruleReference(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ruleReference")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, RULE_REFERENCE, "<rule reference>");
    r = ruleReference_with_recover(b, l + 1);
    p = r; // pin = 1
    r = r && consumeToken(b, RANGLE);
    exit_section_(b, l, m, r, p, JsgfBnfParser::ruleReference_top_recover);
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
  // !(RANGLE|STAR|PLUS|LBRACE|BIAS|LANGLE|TERMINAL_IDENTIFIER|IDENTIFIER|PARSING_KEYWORD|QUOTE|LPAREN|LBRACK
  //   |OR|AND|AND_AND|AND_END|AND_START|SEMICOLON|RPAREN|RBRACK|WEIGHT)
  static boolean ruleReference_recover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ruleReference_recover")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !ruleReference_recover_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // RANGLE|STAR|PLUS|LBRACE|BIAS|LANGLE|TERMINAL_IDENTIFIER|IDENTIFIER|PARSING_KEYWORD|QUOTE|LPAREN|LBRACK
  //   |OR|AND|AND_AND|AND_END|AND_START|SEMICOLON|RPAREN|RBRACK|WEIGHT
  private static boolean ruleReference_recover_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ruleReference_recover_0")) return false;
    boolean r;
    r = consumeToken(b, RANGLE);
    if (!r) r = consumeToken(b, STAR);
    if (!r) r = consumeToken(b, PLUS);
    if (!r) r = consumeToken(b, LBRACE);
    if (!r) r = consumeToken(b, BIAS);
    if (!r) r = consumeToken(b, LANGLE);
    if (!r) r = consumeToken(b, TERMINAL_IDENTIFIER);
    if (!r) r = consumeToken(b, IDENTIFIER);
    if (!r) r = consumeToken(b, PARSING_KEYWORD);
    if (!r) r = consumeToken(b, QUOTE);
    if (!r) r = consumeToken(b, LPAREN);
    if (!r) r = consumeToken(b, LBRACK);
    if (!r) r = consumeToken(b, OR);
    if (!r) r = consumeToken(b, AND);
    if (!r) r = consumeToken(b, AND_AND);
    if (!r) r = consumeToken(b, AND_END);
    if (!r) r = consumeToken(b, AND_START);
    if (!r) r = consumeToken(b, SEMICOLON);
    if (!r) r = consumeToken(b, RPAREN);
    if (!r) r = consumeToken(b, RBRACK);
    if (!r) r = consumeToken(b, WEIGHT);
    return r;
  }

  /* ********************************************************** */
  // !(STAR|PLUS|LBRACE|BIAS|LANGLE|TERMINAL_IDENTIFIER|IDENTIFIER|PARSING_KEYWORD|QUOTE|LPAREN|LBRACK
  //   |OR|AND|AND_AND|AND_END|AND_START|SEMICOLON|RPAREN|RBRACK|WEIGHT)
  static boolean ruleReference_top_recover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ruleReference_top_recover")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !ruleReference_top_recover_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // STAR|PLUS|LBRACE|BIAS|LANGLE|TERMINAL_IDENTIFIER|IDENTIFIER|PARSING_KEYWORD|QUOTE|LPAREN|LBRACK
  //   |OR|AND|AND_AND|AND_END|AND_START|SEMICOLON|RPAREN|RBRACK|WEIGHT
  private static boolean ruleReference_top_recover_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ruleReference_top_recover_0")) return false;
    boolean r;
    r = consumeToken(b, STAR);
    if (!r) r = consumeToken(b, PLUS);
    if (!r) r = consumeToken(b, LBRACE);
    if (!r) r = consumeToken(b, BIAS);
    if (!r) r = consumeToken(b, LANGLE);
    if (!r) r = consumeToken(b, TERMINAL_IDENTIFIER);
    if (!r) r = consumeToken(b, IDENTIFIER);
    if (!r) r = consumeToken(b, PARSING_KEYWORD);
    if (!r) r = consumeToken(b, QUOTE);
    if (!r) r = consumeToken(b, LPAREN);
    if (!r) r = consumeToken(b, LBRACK);
    if (!r) r = consumeToken(b, OR);
    if (!r) r = consumeToken(b, AND);
    if (!r) r = consumeToken(b, AND_AND);
    if (!r) r = consumeToken(b, AND_END);
    if (!r) r = consumeToken(b, AND_START);
    if (!r) r = consumeToken(b, SEMICOLON);
    if (!r) r = consumeToken(b, RPAREN);
    if (!r) r = consumeToken(b, RBRACK);
    if (!r) r = consumeToken(b, WEIGHT);
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
  // sequence_with_recover+
  public static boolean sequence(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sequence")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, SEQUENCE, "<sequence>");
    r = sequence_with_recover(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!sequence_with_recover(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "sequence", c)) break;
    }
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // !(SEMICOLON | item | OR | RPAREN | RBRACK)
  static boolean sequence_recover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sequence_recover")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !sequence_recover_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // SEMICOLON | item | OR | RPAREN | RBRACK
  private static boolean sequence_recover_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sequence_recover_0")) return false;
    boolean r;
    r = consumeToken(b, SEMICOLON);
    if (!r) r = item(b, l + 1);
    if (!r) r = consumeToken(b, OR);
    if (!r) r = consumeToken(b, RPAREN);
    if (!r) r = consumeToken(b, RBRACK);
    return r;
  }

  /* ********************************************************** */
  // item
  static boolean sequence_with_recover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sequence_with_recover")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_);
    r = item(b, l + 1);
    exit_section_(b, l, m, r, false, JsgfBnfParser::sequence_recover);
    return r;
  }

  /* ********************************************************** */
  // string_with_recover QUOTE
  public static boolean string(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "string")) return false;
    if (!nextTokenIs(b, QUOTE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = string_with_recover(b, l + 1);
    r = r && consumeToken(b, QUOTE);
    exit_section_(b, m, STRING, r);
    return r;
  }

  /* ********************************************************** */
  // !(QUOTE | STRING_NL)
  static boolean string_recover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "string_recover")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !string_recover_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // QUOTE | STRING_NL
  private static boolean string_recover_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "string_recover_0")) return false;
    boolean r;
    r = consumeToken(b, QUOTE);
    if (!r) r = consumeToken(b, STRING_NL);
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
    boolean r;
    Marker m = enter_section_(b);
    r = tag_with_recover(b, l + 1);
    r = r && consumeToken(b, RBRACE);
    exit_section_(b, m, TAG, r);
    return r;
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
  // TERMINAL_IDENTIFIER | IDENTIFIER
  public static boolean terminal(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "terminal")) return false;
    if (!nextTokenIs(b, "<terminal>", IDENTIFIER, TERMINAL_IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, TERMINAL, "<terminal>");
    r = consumeToken(b, TERMINAL_IDENTIFIER);
    if (!r) r = consumeToken(b, IDENTIFIER);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // sequence (OR sequence)*
  static boolean unweighted_alternatives(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unweighted_alternatives")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = sequence(b, l + 1);
    r = r && unweighted_alternatives_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (OR sequence)*
  private static boolean unweighted_alternatives_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unweighted_alternatives_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!unweighted_alternatives_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "unweighted_alternatives_1", c)) break;
    }
    return true;
  }

  // OR sequence
  private static boolean unweighted_alternatives_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unweighted_alternatives_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, OR);
    r = r && sequence(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // WEIGHT sequence (OR WEIGHT sequence)+
  static boolean weighted_alternatives(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "weighted_alternatives")) return false;
    if (!nextTokenIs(b, WEIGHT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, WEIGHT);
    r = r && sequence(b, l + 1);
    r = r && weighted_alternatives_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (OR WEIGHT sequence)+
  private static boolean weighted_alternatives_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "weighted_alternatives_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = weighted_alternatives_2_0(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!weighted_alternatives_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "weighted_alternatives_2", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  // OR WEIGHT sequence
  private static boolean weighted_alternatives_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "weighted_alternatives_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, OR, WEIGHT);
    r = r && sequence(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

}
