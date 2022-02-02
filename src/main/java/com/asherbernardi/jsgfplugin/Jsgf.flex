package com.asherbernardi.jsgfplugin;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

import static com.intellij.psi.TokenType.*;
import static com.asherbernardi.jsgfplugin.psi.JsgfBnfTypes.*;
import static com.asherbernardi.jsgfplugin.JsgfParserDefinition.*;

%%

%class JsgfLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType
%eof{  return;
%eof}

%{
  private int previousState = YYINITIAL;

  public void yyHopOver(int newState) {
    previousState = yystate();
    yybegin(newState);
  }

  public void yyHopBack() {
    yybegin(previousState);
    previousState = YYINITIAL;
  }
%}

// White space
NL = \r|\n|\r\n|\R
INPUT_CHAR = [^\r\n\ufeff]
WHITE_SPACE = [\ \t\f]
// Comments
LineComment = "//" {INPUT_CHAR}*
DocumentationComment = "/**" "*"* [^/*] ~"*/"
BlockComment = "/*" [^*] ~"*/" | "/*" "*"+ "/"
// Identifiers
WEIGHT = "/" ({FLOAT}|{INTEGER}) "/"
IDENTIFIER = ({LETTER} | {DIGIT} | "_" | "$" | "." | "-")+
TERMINAL = ({TERMINAL_CHAR} | "/" {TERMINAL_CHAR})+ "/"?
RULE_NAME_IDENTIFIER = ({LETTER_EXT} | {DIGIT_EXT} | {OTHER_PUNCTUATION})+
// Numbers
CHINESE_ZERO = \u3007
SUPERSCRIPT_DIGIT = \u00B2..\u00B3
DIGIT = [0-9]
DIGIT_EXT = {CHINESE_ZERO} | {SUPERSCRIPT_DIGIT} | {DIGIT}
  | [\u0660-\u0669] | [\u06F0-\u06F9] | [\u0966-\u096F] | [\u09E6-\u09EF]
  | [\u0A66-\u0A6F] | [\u0AE6-\u0AEF] | [\u0B66-\u0B6F] | [\u0BE7-\u0BEF]
  | [\u0C66-\u0C6F] | [\u0CE6-\u0CEF] | [\u0D66-\u0D6F] | [\u0E50-\u0E59]
  | [\u0ED0-\u0ED9] | [\u1040-\u1049]
FLOAT = {DIGIT}* "." {DIGIT}+ {EXPONENT}?
INTEGER = {DIGIT}+
EXPONENT = [eE] [+-]? {DIGIT}+
TERMINAL_CHAR = [^ \n\t\r\ufeff\";=|*+<>()\[\]{}/]
LETTER = [a-zA-Z]
LETTER_EXT =
  {LETTER}
  // degree
  | \u00B0
  // micro
  | \u00B5
  // International letters
  | [\u00C0-\u00D6]
  | [\u00D8-\u00F6]
  | [\u00F8-\u00FF]
  | [\u0100-\u1FFF]
  | [\u3040-\u318F]
  | [\u3300-\u337F]
  | [\u3400-\u3D2D]
  | [\u4E00-\u9FFF]
  | [\uF900-\uFAFF]
// Other characters
OTHER_PUNCTUATION = "_" | "+" | "-" | ":" | ";" | "," | "=" | "|" | "/" | \\ | "(" | ")"
                  | "[" | "]" | "@" | "#" | "%" | "!" | "^" | "&" | "~" | "$"

%state RULE_EXPANSION, TAG, JAVA_SCRIPT
%xstate STRING, IN_DOC_COMMENT, IN_BLOCK_COMMENT, RULE_NAME

%%

<YYINITIAL> {
  "#JSGF"                                         { return JSGF_IDENT; }
  "V" \d+ ("."\d+)*                               { return VERSION; }
  "grammar"                                       { return GRAMMAR; }
  "import"                                        { return IMPORT; }
  "include"                                       { return INCLUDE; }
  "public"                                        { return PUBLIC; }
  {IDENTIFIER}                                    { return IDENTIFIER; }
}

<RULE_EXPANSION> {
  "|"                                             { return OR; }
  {WEIGHT}                                        { return WEIGHT; }
  {TERMINAL} | {IDENTIFIER}                       { return TERMINAL_IDENTIFIER; }
}

<YYINITIAL, RULE_EXPANSION> {
  {LineComment}                                   { return LINE_COMMENT; }
  "/**"                                           { yyHopOver(IN_DOC_COMMENT); }
  "/*"                                            { yyHopOver(IN_BLOCK_COMMENT); }
  "."                                             { return PERIOD; }
  "*"                                             { return STAR; }
  "="                                             { yybegin(RULE_EXPANSION); return EQUALS; }
  ";"                                             { yybegin(YYINITIAL); return SEMICOLON; }
  "<"                                             { yyHopOver(RULE_NAME); return LANGLE; }
  ">"                                             { return RANGLE; }
  "/"                                             { return SLASH; }
}

<RULE_EXPANSION> {
  "("                                             { return LPAREN; }
  ")"                                             { return RPAREN; }
  "{"                                             { yybegin(TAG); return LBRACE; }
  "}"                                             { return RBRACE; }
  "["                                             { return LBRACK; }
  "]"                                             { return RBRACK; }
  \"                                              { yybegin(STRING); return QUOTE_OPEN; }
  "|"                                             { return OR; }
  "+"                                             { return PLUS; }
}

<RULE_NAME> {
  "."                                             { return PERIOD; }
  "*"                                             { return STAR; }
  {RULE_NAME_IDENTIFIER}                          { return RULE_NAME_IDENTIFIER; }
  "<"                                             { return LANGLE; }
  ">"                                             { yyHopBack(); return RANGLE; }
  {NL}                                            { yyHopBack(); return WHITE_SPACE; }
  {WHITE_SPACE}                                   { return WHITE_SPACE; }
  [^]                                             { return BAD_CHARACTER; }
}

<STRING> {
  ( [^\"\n\r\ufeff] | \\\" )+                     { return STRING_TEXT;}
  \"                                              { yybegin(RULE_EXPANSION); return QUOTE_CLOSE;}
  {NL}                                            { yybegin(RULE_EXPANSION); return STRING_NL;}
  [^]                                             { yybegin(RULE_EXPANSION); return BAD_CHARACTER; }
}

<IN_BLOCK_COMMENT> {
  "*/"                                            { yyHopBack(); return BLOCK_COMMENT; }
  <<EOF>>                                         { yyHopBack(); return BLOCK_COMMENT; }
  [^]                                             { }
}

<IN_DOC_COMMENT> {
  "*/"                                            { yyHopBack(); return DOC_COMMENT; }
  <<EOF>>                                         { yyHopBack(); return DOC_COMMENT; }
  [^]                                             { }
}

<TAG> {
  "}"                                             { yybegin(RULE_EXPANSION); return RBRACE; }
  <<EOF>>                                         { yybegin(RULE_EXPANSION); return TAG_TOKEN; }
  \\\}                                            { return TAG_TOKEN; }
  \\\\                                            { return TAG_TOKEN; }
  ({NL}|{WHITE_SPACE})+                           { return WHITE_SPACE; }
  [^ \\\}\r\n\ufeff\t\f]+                         { return TAG_TOKEN; }
}

({NL}|{WHITE_SPACE})+                             { return WHITE_SPACE; }

[^]                                               { return BAD_CHARACTER; }
