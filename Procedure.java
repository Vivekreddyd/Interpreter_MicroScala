import java.util.LinkedHashMap;
import java.util.TreeMap;

//Procedure.java

//Procedure is a class to represent the components of a procedure.

class Procedure {

private Environment env;
private SyntaxTree syntaxTree;
private LinkedHashMap<String, DenotableValue> parameters = new LinkedHashMap<String, DenotableValue>();
private int returnType;

public Procedure (Environment env, LinkedHashMap<String, DenotableValue> params, 
		SyntaxTree syntaxTree, int returnType)
{
 this . env         = env;
 this . parameters  = params;
 this . syntaxTree  = syntaxTree;
 this . returnType  = returnType;
}

public Environment env () { return env; }

public LinkedHashMap<String, DenotableValue> getParameters() {
	return parameters;
}

public SyntaxTree syntaxTree () { return syntaxTree; }

public int getReturnType() {
	return returnType;
}

public void print (String procedureName) {
 syntaxTree . print (procedureName);
 env . print (procedureName);
}

}