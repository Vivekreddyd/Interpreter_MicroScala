
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//SyntaxTree.java

//SyntaxTree is a class to represent a node of a binary syntax tree.

class SyntaxTree {

private Object node;
private SyntaxTree left;
private SyntaxTree right;
private SyntaxTree thirdTree;
private List<SyntaxTree> trees;

// constructor functions

public SyntaxTree () { 
 this (null, null, null);
}

public SyntaxTree (Object nodeValue) {
 this (nodeValue, null, null);
 if(((String)nodeValue).startsWith("apply"))
	 this.trees=new ArrayList<SyntaxTree>();
}

public SyntaxTree (Object nodeValue, SyntaxTree leftTree) {
 this (nodeValue, leftTree, null);
}

public SyntaxTree (Object nodeValue, SyntaxTree leftTree, 
   SyntaxTree rightTree) {
 node  = nodeValue;
 left  = leftTree;
 right = rightTree;
}

public SyntaxTree (Object nodeValue, SyntaxTree leftTree, 
		   SyntaxTree rightTree,SyntaxTree thirdTree) {
		 this(nodeValue,leftTree,rightTree);
		 this.thirdTree=thirdTree;
		}
// selector functions

public String root ()      { return node . toString (); }
public List<SyntaxTree> getTrees() {
	return trees;
}

public String printId ()      { return "(id "+ node . toString ()+")"; }
private String printInt() { 
	if(node.toString().equals("Nil"))
		return node . toString ();
	else
		return "(intValue "+ node . toString ()+")";}
public SyntaxTree left ()  { return left; }
public SyntaxTree right () { return right; }
public SyntaxTree thirdTree () { return thirdTree; }
public int constValue () { return ((Integer) left () . node) . intValue (); }
public SyntaxTree procBody() { return left () . left () . left (); }

// print prints the tree in Cambridge Polish prefix notation with a heading.

public void print (String block_name) {
 System . out . println ("");
 System . out . println ("Syntax Tree for " + block_name);
 System . out . print ("----------------");
 for (int i = 0; i < block_name . length (); i++)
   System . out . print ("-");
 System . out . println ("");
 System . out . println (this);
}

// toString returns the tree in Cambridge Polish prefix notation.

public String toString () {
 if (root () . equals ("id"))
   return left () . printId (); // return only id name
 else if (root () . equals ("intValue"))
   if (left () . left () == null) // integer constant
     return left () . printInt ();  
   else                           // constant identifier
     return left () . left () . root ();
 else if(root () . startsWith("apply"))
	   return methodCallPrint();
 if (left == null) 
   return root ();
 else if (right == null)
   return "(" + root () + " " + left + ")";
 else if (thirdTree == null)
   return "(" + root () + " " + left + " " + right + ")";
 else
   return "(" + root () + " " + left + " " + right +" " + thirdTree + ")"; 
}

private String methodCallPrint() {
	String toPrint;
	toPrint="("+node.toString();
	Iterator<SyntaxTree> it=trees.iterator();
	SyntaxTree t=null;
	boolean isBracketOpen=false;
	/*if((t=it.next())!=null)
		toPrint=toPrint+" ["+t.toString();*/
	while(it.hasNext())
	{
		if(!isBracketOpen)
		{
			toPrint=toPrint+" [";
			isBracketOpen=true;
		}
		toPrint=toPrint+" ,"+it.next().toString();
	}
	if(isBracketOpen)
		toPrint=toPrint+" ]";
	toPrint=toPrint+")";
	return toPrint;
}



}