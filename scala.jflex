/*
Name: Venugopal Gonela
CSE machine name:cse01.cse.unt.edu
scala.jflex
This jflex creates a lexical analyser to analyze lexmes of scala source code*/
%%
%{
  private void echo () { System . out . print (yytext ()); }

  public int position () { return yycolumn; }
%}

%class    SCALALexer
%function nextToken
%type	  Token
%unicode
%line
%column
%eofval{
  { return new Token (TokenClass . EOF); }
%eofval}

Identifier = [:letter:]([:letter:] | [:digit:])*([_]?([:letter:] | [:digit:])+)*
Integer    = [:digit:] [:digit:]*
Comments= "//"(.)*
%%
{Comments}		{ echo (); }
[ \t\n\r]		{ echo (); }
";"		{ echo (); return new Token (TokenClass . SEMICOLON, yytext ()); }
"."		{ echo (); return new Token (TokenClass . PERIOD, yytext ()); }
","		{ echo (); return new Token (TokenClass . COMMA, yytext ()); }
"{"		{ echo (); return new Token (TokenClass . LBRACE, yytext ()); }
"}"		{ echo (); return new Token (TokenClass . RBRACE, yytext ()); }
"["		{ echo (); return new Token (TokenClass . LBRACKET, yytext ()); }
"]"		{ echo (); return new Token (TokenClass . RBRACKET, yytext ()); }
":"		{ echo (); return new Token (TokenClass . COLON, yytext ()); }
"::"		{ echo (); return new Token (TokenClass . COLON_COLON, yytext ()); }
"<"		{ echo (); return new Token (TokenClass . LT, yytext ()); }
"<="		{ echo (); return new Token (TokenClass . LE, yytext ()); }
">"		{ echo (); return new Token (TokenClass . GT, yytext ()); }
">="		{ echo (); return new Token (TokenClass . GE, yytext ()); }
"=="		{ echo (); return new Token (TokenClass . EQ, yytext ()); }
"!="		{ echo (); return new Token (TokenClass . NE, yytext ()); }
"("		{ echo (); return new Token (TokenClass . LPAREN, yytext ()); }
")"		{ echo (); return new Token (TokenClass . RPAREN, yytext ()); }
"+"		{ echo (); return new Token (TokenClass . PLUS, yytext ()); }
"-"		{ echo (); return new Token (TokenClass . MINUS, yytext ()); }
"*"		{ echo (); return new Token (TokenClass . TIMES, yytext ()); }
"/"		{ echo (); return new Token (TokenClass . SLASH, yytext ()); }
"="		{ echo (); return new Token (TokenClass . ASSIGN, yytext ()); }
"&&"		{ echo (); return new Token (TokenClass . AND, yytext ()); }
"||"		{ echo (); return new Token (TokenClass . OR, yytext ()); }
"!"		{ echo (); return new Token (TokenClass . NOT, yytext ()); }
object		{ echo (); return new Token (TokenClass . OBJECT, yytext ()); }
def		{ echo (); return new Token (TokenClass . DEF, yytext ()); }
main		{ echo (); return new Token (TokenClass . MAIN, yytext ()); }
args		{ echo (); return new Token (TokenClass . ARGS, yytext ()); }
Array		{ echo (); return new Token (TokenClass . ARRAY, yytext ()); }
String		{ echo (); return new Token (TokenClass . STRING, yytext ()); }
return		{ echo (); return new Token (TokenClass . RETURN, yytext ()); }
var		{ echo (); return new Token (TokenClass . VAR, yytext ()); }
Int		{ echo (); return new Token (TokenClass . INT, yytext ()); }
List		{ echo (); return new Token (TokenClass . LIST, yytext ()); }
if		{ echo (); return new Token (TokenClass . IF, yytext ()); }
else		{ echo (); return new Token (TokenClass . ELSE, yytext ()); }
println		{ echo (); return new Token (TokenClass . PRINTLN, yytext ()); }
head	{ echo (); return new Token (TokenClass . HEAD, yytext ()); }
tail		{ echo (); return new Token (TokenClass . TAIL, yytext ()); }
isEmpty		{ echo (); return new Token (TokenClass . ISEMPTY, yytext ()); }
Nil		{ echo (); return new Token (TokenClass . NIL, yytext ()); }
while		{ echo (); return new Token (TokenClass . WHILE, yytext ()); }
{Integer}	{ echo (); return new Token (TokenClass . INTEGER, yytext ()); }
{Identifier}	{ echo (); return new Token (TokenClass . ID, yytext ()); }
.		{ echo (); ErrorMessage . print (yychar, "Illegal character"); }