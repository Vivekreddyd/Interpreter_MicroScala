import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.TreeMap;

//Name: Venugopal Gonela
//CSE machine name:cse01.cse.unt.edu
//Parser implementation
//Parser is a class to represent a recursive descent parser for the SCALA
//programming language, described in the given assignments.

class Parser {

  protected SCALALexer lexer;	// lexical analyzer
  protected Token token;	// current token

  public Parser () throws java.io.IOException {
	lexer = new SCALALexer (System . in);
    getToken ();
  }

  private void getToken () throws java.io.IOException { 
    token = lexer . nextToken (); 
  }

    public SyntaxTree program (Environment env) throws java.io.IOException {
    	SyntaxTree programTree=null;
	  if (token . symbol () != TokenClass . OBJECT)
		ErrorMessage . print (lexer . position (), TokenClass.OBJECT+" EXPECTED");
	  getToken();
	  if (token . symbol () != TokenClass . ID) 
		  ErrorMessage . print (lexer . position (), "ID EXPECTED");
	  getToken();
	  if (token . symbol () != TokenClass . LBRACE) 
		  ErrorMessage . print (lexer . position (), "{ EXPECTED");
	  getToken ();
	  
	  while(token . symbol ()== TokenClass . VAR || token . symbol ()== TokenClass . DEF)
	  {
		  if(token . symbol ()== TokenClass . VAR)
		  {
			  varDef(env);			  
		  }
		  else
		  {
			  getToken();
			  if(token . symbol () == TokenClass . ID)
			  {
				  //Environment defEnv = env.newFunction();
				  //String defId=token.lexeme();
				  SyntaxTree defTree=def(env);
				  //env.updateEnvFunc(defId, defTree, defEnv);
				  //defTree.print(defId);
			  }
			  else
				  break;
		  }
		  getToken();
	  }
	  if ( token . symbol () == TokenClass . MAIN)
	  {
		  Environment mainEnv=env.newFunction();
		  programTree=maindef(mainEnv);
		  env.updateEnvFunc("main", programTree, mainEnv, null, -1);
		  //maindef();
	  }		  
	  else
		  ErrorMessage . print (lexer . position (), "def main  EXPECTED");
	  getToken();
	  if (token . symbol () != TokenClass . RBRACE) 
		  ErrorMessage . print (lexer . position (), "} EXPECTED");
	return programTree;    
  }

private SyntaxTree def(Environment env) throws IOException {
	SyntaxTree defTree=null,statementTree = null,listExprTree;
	LinkedHashMap<String, DenotableValue> params = new LinkedHashMap<String, DenotableValue>();
	Environment funcEnv= env.newFunction();
	
	String funcId = token . lexeme();
	if (token . symbol () != TokenClass . ID)
		ErrorMessage . print (lexer . position (), "ID EXPECTED");
	getToken();
	if (token . symbol () != TokenClass . LPAREN)
		ErrorMessage . print (lexer . position (), "( EXPECTED");
	getToken();
	if (token . symbol () == TokenClass . ID)
	{
		TokenClass t1=null;
		String paramId=null;
		String paramType=null;
		do {
			paramId= token . lexeme();
			if (token . symbol () != TokenClass . ID)
				ErrorMessage . print (lexer . position (), "ID EXPECTED");
			getToken();
			if (token . symbol () != TokenClass . COLON)
				ErrorMessage . print (lexer . position (), ": EXPECTED");
			getToken();
			paramType=type();
			getToken();
			t1=token . symbol ();
			if(t1 == TokenClass . COMMA)
				getToken();
			
			if(paramType .equals("Int"))
				params.put(paramId, new DenotableValue( Category.INTVAR, null));
			else
				params.put(paramId, new DenotableValue( Category.LISTVAR, new ArrayList()));
			
		} while (t1 == TokenClass . COMMA);
	}
	if (token . symbol () != TokenClass . RPAREN)
		ErrorMessage . print (lexer . position (), ") EXPECTED");
	getToken();
	if (token . symbol () != TokenClass . COLON)
		ErrorMessage . print (lexer . position (), ": EXPECTED");
	getToken();
	String returnType1=type();
	int returnType;
	if(returnType1 .equals("Int"))
		returnType=Category.INTVAR;
	else
		returnType=Category.LISTVAR;
	
	getToken();
	if (token . symbol () != TokenClass . ASSIGN)
		ErrorMessage . print (lexer . position (), "= EXPECTED");
	getToken();
	if (token . symbol () != TokenClass . LBRACE)
		ErrorMessage . print (lexer . position (), "{ EXPECTED");
	getToken();
	while (token . symbol () == TokenClass . VAR)
	{
		varDef(funcEnv);
		getToken();
	}
	boolean isPassed=true;
	TokenClass tokenVal=null;
	while(token . symbol () == TokenClass . IF
			|| token . symbol () == TokenClass . WHILE
			|| token . symbol () == TokenClass . ID
			|| token . symbol () == TokenClass . PRINTLN
			|| token . symbol () == TokenClass . LBRACE)
	{
		tokenVal=token.symbol();
		if(isPassed)
		{
			statementTree =statement();
			isPassed=false;
			tokenVal=null;
		}		
		else 
			statementTree=new SyntaxTree(":",statementTree,statement());		
	}
	
	if (token . symbol () != TokenClass . RETURN)
		ErrorMessage . print (lexer . position (), "return EXPECTED");
	getToken();
	
	listExprTree=listExpr();
	//statementTree=new SyntaxTree("Return",statementTree,listExprTree);
	statementTree=new SyntaxTree(":",statementTree,new SyntaxTree("return",listExprTree));
	defTree=statementTree;
	
	if (token . symbol () != TokenClass . SEMICOLON)
		ErrorMessage . print (lexer . position (), "; EXPECTED");
	getToken();
	if (token . symbol () != TokenClass . RBRACE)
		ErrorMessage . print (lexer . position (), "} EXPECTED");
	
	env.updateEnvFunc(funcId, defTree, funcEnv, params, returnType);
	return defTree;
		
}

private SyntaxTree maindef(Environment env) throws IOException {
	SyntaxTree maindefTree=null,statementTree;
	if (token . symbol () != TokenClass . MAIN)
		ErrorMessage . print (lexer . position (), "main EXPECTED");
	getToken();
	if (token . symbol () != TokenClass . LPAREN)
		ErrorMessage . print (lexer . position (), "( EXPECTED");
	getToken();
	if (token . symbol () != TokenClass . ARGS) 
		ErrorMessage . print (lexer . position (), "ARGS EXPECTED");
	getToken();
	if (token . symbol () != TokenClass . COLON) 
		ErrorMessage . print (lexer . position (), ": EXPECTED");
	getToken();
	if (token . symbol () != TokenClass . ARRAY) 
		ErrorMessage . print (lexer . position (), "ARRAY EXPECTED");
	getToken();
	if (token . symbol () != TokenClass . LBRACKET) 
		ErrorMessage . print (lexer . position (), "[ EXPECTED");
	getToken();
	if (token . symbol () != TokenClass . STRING) 
		ErrorMessage . print (lexer . position (), "STRING EXPECTED");
	getToken();
	if (token . symbol () != TokenClass . RBRACKET) 
		ErrorMessage . print (lexer . position (), "] EXPECTED");
	getToken();
	if (token . symbol () != TokenClass . RPAREN)
		ErrorMessage . print (lexer . position (), ") EXPECTED");
	getToken();
	if (token . symbol () != TokenClass . LBRACE) 
		  ErrorMessage . print (lexer . position (), "{ EXPECTED");
	getToken();
	while(token . symbol () == TokenClass . VAR)
	{
		varDef(env);
		getToken();
	}
	statementTree =statement();
	while(token . symbol () == TokenClass . IF
			|| token . symbol () == TokenClass . WHILE
			|| token . symbol () == TokenClass . ID
			|| token . symbol () == TokenClass . PRINTLN
			|| token . symbol () == TokenClass . LBRACE)
	{
		/*tokenVal=token.symbol();
		if(token.symbol()==TokenClass.ID || token.symbol()==TokenClass.PRINTLN)
			statementTree=new SyntaxTree(";",statementTree,statement());
		else
			statementTree=new SyntaxTree("NIL",statementTree,statement());*/
		statementTree=new SyntaxTree(":",statementTree,statement());
	}
	/*if(tokenVal!=null && (tokenVal==TokenClass.ID || tokenVal==TokenClass.PRINTLN))
		statementTree=new SyntaxTree(";",statementTree,new SyntaxTree("NIL"));*/
	maindefTree=statementTree;
	if (token . symbol () != TokenClass . RBRACE) 
		  ErrorMessage . print (lexer . position (), "} EXPECTED");
	return maindefTree;
	
}

private SyntaxTree statement() throws IOException {
	SyntaxTree statementTree = null, expTree,statementTree2;
	switch (token . symbol() ) {
	
	case IF :
		getToken();
		if (token . symbol () != TokenClass . LPAREN) 
			  ErrorMessage . print (lexer . position (), "( EXPECTED");
		getToken();
		expTree=expr();
		if (token . symbol () != TokenClass . RPAREN) 
			  ErrorMessage . print (lexer . position (), ") EXPECTED");
		getToken ();
		statementTree2=statement();
		statementTree=new SyntaxTree("if",expTree,statementTree2);
		if (token . symbol () == TokenClass . ELSE)
		{
			getToken();
			//statement();
			statementTree=new SyntaxTree("if",expTree,statementTree2,statement());
		}
		break;
	case WHILE:
		getToken();
		if (token . symbol () != TokenClass . LPAREN) 
			  ErrorMessage . print (lexer . position (), "( EXPECTED");
		getToken();
		expTree=expr();
		if (token . symbol () != TokenClass . RPAREN) 
			  ErrorMessage . print (lexer . position (), ") EXPECTED");
		getToken ();
		statementTree2=statement();
		statementTree=new SyntaxTree("WHILE",expTree,statementTree2);
		break;
	case ID:
		String id=token.lexeme();
		getToken();
		if (token . symbol () != TokenClass . ASSIGN) 
			  ErrorMessage . print (lexer . position (), "= EXPECTED");
		getToken();
		statementTree=listExpr();
		statementTree=new SyntaxTree("=",new SyntaxTree("id", new SyntaxTree(id)),statementTree);
		if (token . symbol () != TokenClass . SEMICOLON) 
			  ErrorMessage . print (lexer . position (), "; EXPECTED");
		getToken();
		break;
	case PRINTLN:
		getToken();
		if (token . symbol () != TokenClass . LPAREN) 
			  ErrorMessage . print (lexer . position (), "( EXPECTED");
		getToken();
		expTree=listExpr();
		statementTree=new SyntaxTree("println",expTree);
		if (token . symbol () != TokenClass . RPAREN) 
			  ErrorMessage . print (lexer . position (), ") EXPECTED");
		getToken();
		if (token . symbol () != TokenClass . SEMICOLON) 
			  ErrorMessage . print (lexer . position (), "; EXPECTED");
		getToken();
		break;
	case LBRACE:
		getToken();
		statementTree=statement();
		while(token . symbol () == TokenClass . IF
				|| token . symbol () == TokenClass . WHILE
				|| token . symbol () == TokenClass . ID
				|| token . symbol () == TokenClass . PRINTLN
				|| token . symbol () == TokenClass . LBRACE)
		{
			statementTree2=statement();
			statementTree=new SyntaxTree(":",statementTree,statementTree2);
		}
		if (token . symbol () != TokenClass . RBRACE) 
			  ErrorMessage . print (lexer . position (), "} EXPECTED");
		getToken();
		break;
	default:
		ErrorMessage . print (lexer . position (), "STATEMENT EXPECTED");	
	}
	return statementTree;
}

private SyntaxTree listExpr() throws IOException {
	SyntaxTree listExprTree=null,listExprTree2;
	listExprTree=addExpr();
	if (token . symbol () == TokenClass . COLON_COLON) 
	{
		getToken();
		listExprTree2=listExpr();
		listExprTree=new SyntaxTree("::",listExprTree,listExprTree2);
	}	
	return listExprTree;
}

private SyntaxTree addExpr() throws IOException {
	SyntaxTree addExprTree=null,addExprTree2;
	addExprTree=mulExpr();
	while(token . symbol () == TokenClass.PLUS || token . symbol () == TokenClass.MINUS)
	{
		String op=token.lexeme();
		getToken();
		addExprTree2=mulExpr();
		addExprTree=new SyntaxTree(op,addExprTree,addExprTree2);
	}
	return addExprTree;
}

private SyntaxTree mulExpr() throws IOException {
	SyntaxTree mulExprTree=null,mulExprTree2;
	mulExprTree=prefixExpr();
	while(token . symbol () == TokenClass.TIMES || token . symbol () == TokenClass.SLASH)
	{
		String op=token.lexeme();
		getToken();
		mulExprTree2=prefixExpr();
		mulExprTree=new SyntaxTree(op,mulExprTree,mulExprTree2);
	}
	return mulExprTree;
}

private SyntaxTree prefixExpr() throws IOException {
	SyntaxTree prefixExprTree=null,prefixExprTree2;
	String tok=null;
	if(token . symbol () == TokenClass.PLUS || token . symbol () == TokenClass.MINUS)
	{
		tok=token.lexeme();
		getToken();
	}
	if(tok!=null)
		prefixExprTree=new SyntaxTree(tok, simpleExpr()) ;
	else
		prefixExprTree=simpleExpr();
	
	while(token . symbol () == TokenClass.PERIOD)
	{
		getToken();
		prefixExprTree2=listMethodCall();
		getToken();
		prefixExprTree=new SyntaxTree(prefixExprTree2.root(),prefixExprTree);
	}
	return prefixExprTree;
}

private SyntaxTree listMethodCall() {
	if(!(token . symbol () == TokenClass.HEAD
			|| token . symbol () == TokenClass.TAIL
			|| token . symbol () == TokenClass.ISEMPTY))
		ErrorMessage . print (lexer . position (), "METHODCALL EXPECTED");
	return new SyntaxTree(token.lexeme());
		
}

private SyntaxTree simpleExpr() throws IOException {
	SyntaxTree simplexTree=null,listExpr,listExpr1;
	if(token . symbol () == TokenClass.INTEGER || token . symbol () == TokenClass.NIL)
	{
		simplexTree= new SyntaxTree("intValue", new SyntaxTree(token.lexeme()));
		literal();
		getToken();
	}
	else if(token . symbol () == TokenClass.LPAREN)
	{
		getToken();
		simplexTree=expr();
		if(token . symbol () != TokenClass.RPAREN)
			ErrorMessage . print (lexer . position (), ") EXPECTED");
		getToken();
	}
	else if(token . symbol () == TokenClass.ID)
	{
		String id=token.lexeme();
		simplexTree=new SyntaxTree("id", new SyntaxTree(token.lexeme()));
		getToken();
		if(token . symbol () == TokenClass.LPAREN)
		{
			simplexTree=new SyntaxTree("apply "+id);
			getToken();
			if(token . symbol () == TokenClass.PLUS || token . symbol () == TokenClass.MINUS
			   || token . symbol () == TokenClass.INTEGER || token . symbol () == TokenClass.NIL
			   || token . symbol () == TokenClass.LPAREN || token . symbol () == TokenClass.ID)
			{
				listExpr=listExpr();
				simplexTree.getTrees().add(listExpr);
				while(token . symbol () == TokenClass.COMMA)
				{
					getToken();
					listExpr1=listExpr();
					simplexTree.getTrees().add(listExpr1);
				}				
			}
			if(token . symbol () != TokenClass.RPAREN)
				ErrorMessage . print (lexer . position (), ") EXPECTED");
			getToken();
		}
	}
	else
		ErrorMessage . print (lexer . position (), "Expresion EXPECTED");
	return simplexTree;
}

private SyntaxTree expr() throws IOException {
	SyntaxTree exprTree=null, exprTree2;
	exprTree=andExpr();
	while(token . symbol () == TokenClass . OR)
	{
		String op="||";
		getToken();
		exprTree2=andExpr();
		exprTree=new SyntaxTree(op,exprTree,exprTree2);
	}
	return exprTree;
}

private SyntaxTree andExpr() throws IOException {
	SyntaxTree andExprTree=null,andExprTree2;
	andExprTree=relExpr();
	while(token . symbol () == TokenClass . AND)
	{
		String op="&&";
		getToken();
		andExprTree2=relExpr();
		andExprTree=new SyntaxTree(op,andExprTree,andExprTree2);
	}
	return andExprTree;
}

private SyntaxTree relExpr() throws IOException {	
	SyntaxTree relExprTree=null,relExprTree2;	
	String notLex=null;
	if (token . symbol () == TokenClass . NOT)
	{
		notLex=token.lexeme();
		getToken();
	}
	if(notLex!=null)
		relExprTree=new SyntaxTree(notLex, listExpr());
	else
		relExprTree=listExpr();
	if(token . symbol () == TokenClass . LT
			|| token . symbol () == TokenClass . LE
			|| token . symbol () == TokenClass . GT
			|| token . symbol () == TokenClass . GE
			|| token . symbol () == TokenClass . EQ
			|| token . symbol () == TokenClass . NE)
	{
		String op=token.lexeme();
		getToken();
		relExprTree2=listExpr();
		relExprTree=new SyntaxTree(op,relExprTree,relExprTree2);
	}	
	return relExprTree;
}

private void varDef(Environment env) throws IOException {
	if (token . symbol () != TokenClass . VAR) 
		ErrorMessage . print (lexer . position (), "VAR EXPECTED");
	getToken();
	String id=token . lexeme();
	if (token . symbol () != TokenClass . ID) 
		ErrorMessage . print (lexer . position (), "ID EXPECTED");
	getToken();
	if (token . symbol () != TokenClass . COLON) 
		ErrorMessage . print (lexer . position (), ": EXPECTED");
	getToken();
	String type=type();
	getToken();
	if (token . symbol () != TokenClass . ASSIGN) 
		ErrorMessage . print (lexer . position (), "= EXPECTED");
	getToken();
	String literal=token . lexeme();
	literal();
	if(type.equals("Int"))
	{
		if(literal.equals("0"))
			env.updateEnvIntVar(id, 0);
		else
			ErrorMessage . print (lexer . position (), "0 EXPECTED");
	} else 
		if(literal.equals("Nil"))
			env.updateEnvListVar(id);
		else
			ErrorMessage . print (lexer . position (), "Nil EXPECTED");
	
	getToken();
	if (token . symbol () != TokenClass . SEMICOLON) 
		ErrorMessage . print (lexer . position (), "; EXPECTED");
}

private void literal() throws IOException {
	if (!(token . symbol () == TokenClass . INTEGER || token . symbol () == TokenClass . NIL)) 
		ErrorMessage . print (lexer . position (), "INTEGER or NIL EXPECTED");
}

private String type() throws IOException {
	String type=token . lexeme();
	if (token . symbol () == TokenClass . INT);
	else if (token . symbol () == TokenClass . LIST)
	{
		getToken();
		if(token . symbol () != TokenClass . LBRACKET)
			ErrorMessage . print (lexer . position (), "[ EXPECTED");
		getToken();
		if(token . symbol () != TokenClass . INT)
			ErrorMessage . print (lexer . position (), "Int EXPECTED");
		getToken();
		if(token . symbol () != TokenClass . RBRACKET)
			ErrorMessage . print (lexer . position (), "] EXPECTED");
	}
	else
		ErrorMessage . print (lexer . position (), "INT OR LIST EXPECTED");	
	return type;
}

}