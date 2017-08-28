
/*
Name: Venugopal Gonela
CSE machine name:cse01.cse.unt.edu
Token class definition
Token is a class to represent lexical tokens in the MicroScala programming 
language described in grammer in given assignment */

public class Token {

  private TokenClass symbol;	// current token
  private String lexeme;	// lexeme

  public Token () { }

  public Token (TokenClass symbol) {
    this (symbol, null);
  }

  public Token (TokenClass symbol, String lexeme) {
    this . symbol = symbol;
    this . lexeme  = lexeme;
  }

  public TokenClass symbol () { return symbol; }

  public String lexeme () { return lexeme; }

  public String toString () {
    switch (symbol) {
      case OBJECT :     return "(keyword, object) ";
      case DEF :      return "(keyword, def) ";
      case MAIN :     return "(keyword, main) ";
      case ARGS :        return "(keyword, args) ";
      case ARRAY :       return "(keyword, Array) ";
      case STRING :        return "(keyword, String) ";
      case RETURN :       return "(keyword, return) ";
      case VAR :      return "(keyword, var) ";
      case INT :      return "(keyword, Int) ";
      case LIST :       return "(keyword, List) ";
	  case IF :     return "(keyword, if) ";
	  case ELSE :     return "(keyword, else) ";
	  case WHILE :     return "(keyword, while) ";
	  case PRINTLN :     return "(keyword, println) ";
	  case HEAD :     return "(keyword, head) ";
	  case TAIL :     return "(keyword, tail) ";
	  case ISEMPTY :     return "(keyword, isEmpty) ";
      case NIL :     return "(keyword, Nil) ";
      case ASSIGN :    return "(operator, =) ";
      case PLUS :      return "(operator, +) ";
      case MINUS :     return "(operator, -) ";
      case TIMES :     return "(operator, *) ";
      case SLASH :     return "(operator, /) ";
      case EQ :        return "(operator, ==) ";
      case LT :        return "(operator, <) ";
      case GT :        return "(operator, >) ";
      case NE :        return "(operator, !=) ";
      case LE :        return "(operator, <=) ";
      case GE :        return "(operator, >=) ";
      case LPAREN :    return "(operator, () ";
      case RPAREN :    return "(operator, )) ";
	  case AND :    return "(operator, &&) ";
	  case OR :    return "(operator, ||) ";
	  case NOT :    return "(operator, !) ";
      case COMMA :     return "(punctuation, ,) ";
      case PERIOD :    return "(punctuation, .) ";
      case SEMICOLON : return "(punctuation, ;) ";
	  case LBRACE : return "(punctuation, {) ";
	  case RBRACE : return "(punctuation, }) ";
	  case LBRACKET : return "(punctuation, [) ";
	  case RBRACKET : return "(punctuation, ]) ";
	  case COLON : return "(punctuation, :) ";
	  case COLON_COLON : return "(punctuation, ::) ";
      case ID :        return "(identifier, " + lexeme + ") ";
      case INTEGER :   return "(integer, " + lexeme + ") ";
      default : 
	ErrorMessage . print (0, "Unrecognized token");
        return null;
    }
  }

}