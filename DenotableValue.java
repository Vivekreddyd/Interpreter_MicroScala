//DenotableValue.java

//DenotableValue is a class to represent the denotable values of identifiers
//in a PL/0 program.

public class DenotableValue implements Cloneable{

private int category;
private Object value;

public DenotableValue (int category, Object value) {
 this . category = category;
 this . value = value;
}

public int category () { return category; }

public Object value () { return value; }

public void setValue (Object val) { value=val; }

public String toString () {
 String printString = Category . toString (category);
 if (category == Category . INTVAR || category == Category . LISTVAR)
   printString = printString + "(" + value + ")";
 return printString;
}
}
