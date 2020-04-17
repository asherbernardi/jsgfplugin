parser grammar JsgfParser;
options {
  tokenVocab = JsgfLexer;
}

@header{
/**
 * A Parser for the Jsgf plugin for intellij.
 * @author asherbernardi
 */
}

// Rule used when renaming JSGF rules
parseJustRuleName
:
  OPENARROW
  (declarationName | ruleReference | importName)
  CLOSEARROW
;

getGrammar
:
  (
    jsgfIdent?
    DOCCOMMENT*
    initGram
    DOCCOMMENT*
    importStatement*
    (DOCCOMMENT* ruleDeclaration DOCCOMMENT*)*
    EOF
  )
;

jsgfIdent
:
  JSGF_IDENT
  VERSION
  IDENTIFIER*
  SEMICOLON
;

initGram
:
  (
    GRAMMAR
    grammarName
    SEMICOLON
  )
;

grammarName: IDENTIFIER;

importStatement
:
  IMPORT
  OPENARROW
  importName
  CLOSEARROW
  SEMICOLON
;

importName: RULE_NAME_IDENTIFIER (PERIOD RULE_NAME_IDENTIFIER)* (PERIOD STAR)?;

declarationName: RULE_NAME_IDENTIFIER;

ruleReference: RULE_NAME_IDENTIFIER (PERIOD RULE_NAME_IDENTIFIER)*;

terminal: TERMINAL | IDENTIFIER;

string: QUOTE STRING_TEXT? QUOTE;

ruleDeclaration
:
  PUBLIC?
  OPENARROW
  declarationName
  CLOSEARROW
  EQUALS
  jsgfAlternatives
  SEMICOLON
;

jsgfAlternatives
:
  (
    jsgfSequence
    (
      ORSYMBOL
      jsgfSequence
    )*
    |
    WEIGHT
    jsgfSequence
    (
      ORSYMBOL
      WEIGHT
      jsgfSequence
    )+
  )
;

jsgfSequence
:
  item+
;

item
:
  (
    OPENARROW
    ruleReference
    CLOSEARROW
    |
    terminal
    |
    string
    |
    OPENPARAN
    jsgfAlternatives
    CLOSEPARAN
    |
    OPENBRACKET
    jsgfAlternatives
    CLOSEBRACKET
  )
  (
    STAR
    |
    PLUS
  )?
  jsgfTag*
;

jsgfTag
:
  (
    OPENCURLY TAG_TEXT CLOSECURLY
    |
    OPENCURLY CLOSECURLY
  )
;
