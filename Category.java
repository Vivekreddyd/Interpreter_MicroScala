
//Category.java

//Category is a class to represent the categories of identifiers in a PL/0
//program.

public class Category {

public static final int INTVAR  = 0;
public static final int LISTVAR  = 1;
public static final int FUNCTIONID = 2;
public static final int BOOLEANVAR = 3;

public static String toString (int category) {
 switch (category) {
   case INTVAR  : return "Int ";
   case LISTVAR  : return "List ";
   case FUNCTIONID : return "Function";
   case BOOLEANVAR : return "Boolean";
   default        : return null;
 }
}

}