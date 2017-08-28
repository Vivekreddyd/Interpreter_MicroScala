/*
Name: Venugopal Gonela
CSE machine name:cse01.cse.unt.edu
SCALA Interpreter
This is the starting class for the interpreter to interpret scala programs*/

public class SCALAInt {

	public static void main (String args []) throws java.io.IOException {

	    System . out . println ("Source Program");
	    System . out . println ("--------------");
	    System . out . println ("");

	    Parser pl0 = new Parser ();
	    Environment env = new Environment ();
	    SyntaxTree syntaxTree=pl0 . program (env);

	    //System . out . println ("");
	    //syntaxTree . print ("main");
	    
	    env . print ("total program");
	    System . out . println ("");
	    
	    Interpreter interpreter = 
	    		   new Interpreter ();
	    Procedure mainProgram=(Procedure)env.accessEnv("main").value();
	    
	    interpreter . m (mainProgram.env(),syntaxTree);
	    System . out . println ("");
	    System.out.println("Output of the interpreter is :");
	    for(Integer r: interpreter.getResult())
	    {
	    	System.out.println(r);
	    }
	  }
}