
/*
Name: Venugopal Gonela
CSE machine name:cse01.cse.unt.edu
TokenClass enumeration definition
TokenClass is an enumeration to represent lexical token classes in the 
MicroScala programming language.*/

public enum TokenClass {
  EOF, 
  // keywords
  OBJECT, DEF, MAIN, ARGS, ARRAY, STRING, RETURN, VAR, INT, LIST, IF, ELSE, WHILE, PRINTLN, HEAD, TAIL, ISEMPTY, NIL,
  // punctuation
  COMMA, PERIOD, SEMICOLON, LBRACE, RBRACE, LBRACKET, RBRACKET, COLON, COLON_COLON,
  // operators
  LPAREN, RPAREN, ASSIGN, PLUS, MINUS, TIMES, SLASH, EQ, LT, LE, GT, GE, NE, AND, OR, NOT, 
  // ids and integers
  ID, INTEGER
}
