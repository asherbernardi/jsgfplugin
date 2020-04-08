lexer grammar JsgfLexer;

@header{
/**
 * A Lexer for the Jsgf plugin for intellij.
 * @author asherbernardi
 */
}

//Tokens
// Whitespaces
NL: [\n\t\r\f\ufeff]+                               -> channel(HIDDEN);
SPACE: ' '                                          -> channel(HIDDEN);
COMMENT: '//' ~('\n' | '\r')*                       -> channel(HIDDEN);
DOCCOMMENT: '/**' .*? ('*/' | EOF);
BLOCKCOMMENT: '/*' .*? ('*/' | EOF)                 -> channel(HIDDEN);
// Primary tokens
JSGF_IDENT: '#JSGF';
VERSION: 'V1.0';
GRAMMAR: 'grammar';
IMPORT: 'import';
PUBLIC: 'public';
// Identifiers
WEIGHT: SLASH (FLOAT|INTEGER) SLASH;
IDENTIFIER: (LETTER | DIGIT | '_' | '$' | '.' | '-')+;
TERMINAL: (TERMINAL_CHAR | '/' TERMINAL_CHAR)+ '/'?;
// Rule names can have more symbols then your average identifier and can only be recognized in RULE_NAME_MODE
fragment RULE_IDENTIFIER: (LETTER_EXT | DIGIT_EXT | OTHER_PUNCTUATION)+;
// Simple tokens
PERIOD: '.';
SLASH: '/';
BACKSLASH: '\\';
STAR: '*';
OPENARROW: '<'                                      -> pushMode(RULE_NAME_MODE);
CLOSEARROW: '>';
EQUALS: '=';
SEMICOLON: ';';
OPENCURLY: '{'                                      -> pushMode(TAG_MODE);
CLOSECURLY: '}';
OPENPARAN: '(';
CLOSEPARAN: ')';
OPENBRACKET: '[';
CLOSEBRACKET: ']';
QUOTE: '"'                                          -> pushMode(STRING_MODE);
ORSYMBOL: '|';
PLUS: '+';
// fragments as helpers
fragment EXPONENT: ('e' | 'E') ('+' | '-')? ('0'..'9')+;
fragment LETTER: ('a'..'z' | 'A'..'Z');
fragment LETTER_EXT:
  LETTER
  // degree
  | '\u00B0'
  // micro
  | '\u00B5'
  // International letters
  | '\u00C0'..'\u00D6'
  | '\u00D8'..'\u00F6'
  | '\u00F8'..'\u00FF'
  | '\u0100'..'\u1FFF'
  | '\u3040'..'\u318F'
  | '\u3300'..'\u337F'
  | '\u3400'..'\u3D2D'
  | '\u4E00'..'\u9FFF'
  | '\uF900'..'\uFAFF';
// Taken from http://java.coe.psu.ac.th/Extension/JavaSpeech1.0/JSGF.pdf
fragment OTHER_PUNCTUATION : '_' | '+' | '-' | ':' | ';' | ',' | '=' | '|' | '/' | '\\' | '(' | ')' | '[' | ']' | '@' | '#' | '%' | '!' | '^' | '&' | '~';
fragment CHINESE_ZERO: '\u3007';
fragment SUPERSCRIPT_DIGIT: '\u00B2'..'\u00B3';
fragment DIGIT: ('0'..'9');
fragment DIGIT_EXT: CHINESE_ZERO | SUPERSCRIPT_DIGIT | DIGIT
  | '\u0660'..'\u0669' | '\u06F0'..'\u06F9' | '\u0966'..'\u096F' | '\u09E6'..'\u09EF'
  | '\u0A66'..'\u0A6F' | '\u0AE6'..'\u0AEF' | '\u0B66'..'\u0B6F' | '\u0BE7'..'\u0BEF'
  | '\u0C66'..'\u0C6F' | '\u0CE6'..'\u0CEF' | '\u0D66'..'\u0D6F' | '\u0E50'..'\u0E59'
  | '\u0ED0'..'\u0ED9' | '\u1040'..'\u1049';
fragment DASH: '-';
fragment FLOAT: ('0'..'9')* '.' ('0'..'9')+ EXPONENT?;
fragment TERMINAL_CHAR: ~(' ' | '\n' | '\t' | '\r' | '\ufeff'
                        | '"' | ';' | '=' | '|' | '*' | '+'
                        | '<' | '>' | '(' | ')' | '[' | ']' | '{' | '}' | '/');
fragment INTEGER: DIGIT+;
// if nothing is matched, there was a mistake
BAD_CHARACTER: .                                    -> channel(HIDDEN);

mode RULE_NAME_MODE;
R_SPACE: SPACE                                      -> type(SPACE);
R_NL: [\n\t\r\f\ufeff]+                             -> type(NL), channel(HIDDEN);
R_STAR: STAR                                        -> type(STAR);
R_PERIOD: PERIOD                                    -> type(PERIOD);
RULE_NAME_IDENTIFIER: RULE_IDENTIFIER;
// End of mode
RR_CLOSEARROW: CLOSEARROW                           -> type(CLOSEARROW), popMode;
RR_BAD_CHARACTER: .                                 -> type(BAD_CHARACTER), channel(HIDDEN), popMode;

mode STRING_MODE;
STRING_TEXT:
    (
        ~('"' | '\n' | '\r')
        |
        '\\' ('"')
    )+
;
// End of mode
S_QUOTE: QUOTE                                      -> type(QUOTE), popMode;
S_BAD_CHARACTER: .                                  -> type(BAD_CHARACTER), channel(HIDDEN), popMode;

mode TAG_MODE;
fragment T_ESCAPED_CLOSECURLY: '\\}';
fragment T_TAG_CHAR: ~('}');
TAG_TEXT: (T_ESCAPED_CLOSECURLY | T_TAG_CHAR)+;
// End of mode
T_CLOSECURLY: '}'                                   -> type(CLOSECURLY), popMode;
// Should never reach hereS
T_BAD_CHARACTER: .                                  -> type(BAD_CHARACTER), popMode;
