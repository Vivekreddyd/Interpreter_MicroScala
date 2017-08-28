//Name: Venugopal Gonela
//CSE machine name:cse01.cse.unt.edu
//Interpreter implementation
//Interpreter is a class to represent a denotational semantics equations for the SCALA
//programming language, described in the given assignments.

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public class Interpreter {

	private List<Integer> result;
	
	public Interpreter() {
		this.result=new ArrayList<Integer>();
	}

	public List<Integer> getResult() {
		return result;
	}
	
	//Main program
	public void m(Environment env, SyntaxTree mainTree)
	{
		statement(env, mainTree);
	}

	private void statement(Environment env, SyntaxTree mainTree) {
		
		if (mainTree . root () . equals (":")) {
				statement (env, mainTree . left ());
				statement (env, mainTree . right ());
			 }
		else if (mainTree . root () . equals ("="))
			{
			 String id=mainTree . left() . left() . root();
			 DenotableValue dv = (DenotableValue) expression(env, mainTree . right());
			 int category=env.accessEnv(id).category();
			 if(category == dv . category())
			 {
				 env.updateEnvVal(id, dv);
			 }
			 else
					ErrorMessage . print ("type error");
		}
		else if (mainTree . root () . equals ("WHILE")) {
			DenotableValue dv = (DenotableValue) expression(env, mainTree . left());
			if(dv.category() == Category . BOOLEANVAR)
			{
				if ((boolean) dv . value()) {
					statement (env, mainTree . right ());	// execute body of while loop
					statement (env, mainTree);		// execute while loop recursively
				   }
			}
			else
				ErrorMessage . print ("type error");
		   
		}
		
		//if E S1 else S2
		else if (mainTree . root () . equals ("if")) {
			DenotableValue dv = (DenotableValue) expression(env, mainTree . left());
			if(dv.category() == Category . BOOLEANVAR)
			{
				if((Boolean)dv.value())
					statement (env, mainTree . right ());//S1 execution
				else
					if(mainTree.thirdTree()!=null)
					{
						   statement(env, mainTree . thirdTree()); //S2 execution
					}
			}
			else
				ErrorMessage . print ("type error");
		}
		
		else if (mainTree . root() .equals("println"))
		{
			DenotableValue dv = (DenotableValue) expression(env, mainTree . left());
			if (dv.category() == Category . INTVAR)
				this.result.add((Integer)dv.value());
			else
				ErrorMessage . print ("type error");
		}
		
		//check for return
		else if(mainTree . root() .equals("return"))
		{
			String id="return";
			DenotableValue dv = (DenotableValue) expression(env, mainTree . left());
			Object val=dv.value();//type checking to be done here
			int category=env.accessEnv(id).category();
			if(category == dv . category())
			{
				env.updateEnvVal(id, new DenotableValue( category,val));
			}
			else
				ErrorMessage . print ("type error");
		}
				
		
	}

	private Object expression(Environment env, SyntaxTree eTree) {
		String root=eTree . root();
		
		//arithmetic operator check
		if(root . matches("(\\+|-|\\*|/)"))
		{
			DenotableValue leftDV= (DenotableValue) expression(env, eTree.left());
			SyntaxTree rightTree = eTree . right();
			DenotableValue rightDV=null;
			if(rightTree!=null)
			{
				rightDV = (DenotableValue) expression(env, rightTree);
				if(leftDV.category() == Category.INTVAR && rightDV.category() == Category.INTVAR)
				{
					if(root .equals("+"))
						return new DenotableValue(Category . INTVAR,
								(Integer)leftDV.value()+(Integer)rightDV.value());
					else if(root .equals("-"))
						return new DenotableValue(Category . INTVAR,
								(Integer)leftDV.value()-(Integer)rightDV.value());
					else if(root .equals("*"))
					{
						return new DenotableValue(Category . INTVAR,
								(Integer)leftDV.value()*(Integer)rightDV.value());
					}
					else if(root .equals("/"))
					{
						Integer rightVal=(Integer)rightDV.value();
						if(rightVal!=0)
							return new DenotableValue(Category . INTVAR,
									(Integer)leftDV.value()/rightVal);
						else
						{
							ErrorMessage . print ("Divide by zero error");
							return null;
						}
					}
					else
						return null;
				}
				else
				{
					ErrorMessage . print ("type error");
					return null;
				}
			}
			else
			{
				if(leftDV.category() == Category.INTVAR)
				{
					if(root .equals("+"))
						return new DenotableValue(Category . INTVAR,
								(Integer)leftDV.value());
					else if(root .equals("-"))
						return new DenotableValue(Category . INTVAR,
								0-(Integer)leftDV.value());
					else
						return null;
				}
				else
				{
					ErrorMessage . print ("type error");
					return null;
				}
			}
			
		}
		
		//boolean comparision operator check
		else if(root . equals("&&"))
		{
			DenotableValue leftDV= (DenotableValue) expression(env, eTree.left());
			if(leftDV.category() == Category . BOOLEANVAR)
			{
				if((Boolean)leftDV.value())
				{
					DenotableValue rightDV = (DenotableValue) expression(env, eTree.right());
					if(rightDV.category() == Category . BOOLEANVAR)
					{
						return rightDV;
					}
					else
					{
						ErrorMessage . print ("type error");
						return null;
					}
				}
				else
					return leftDV;
			}
			else
			{
				ErrorMessage . print ("type error");
				return null;
			}
		}
		else if(root . equals("||"))
		{
			DenotableValue leftDV= (DenotableValue) expression(env, eTree.left());
			//DenotableValue rightDV = (DenotableValue) expression(env, eTree.right());
			if(leftDV.category() == Category . BOOLEANVAR)
			{
				if(!(Boolean)leftDV.value())
				{
					DenotableValue rightDV = (DenotableValue) expression(env, eTree.right());
					if(rightDV.category() == Category . BOOLEANVAR)
					{
						return rightDV;
					}
					else
					{
						ErrorMessage . print ("type error");
						return null;
					}
				}
				else
					return leftDV;
			}
			else
			{
				ErrorMessage . print ("type error");
				return null;
			}
		}
		else if(root . equals("!"))
		{
			DenotableValue leftDV= (DenotableValue) expression(env, eTree.left());
			if(leftDV.category() == Category . BOOLEANVAR)
			{
				if((Boolean)leftDV.value())
				{
					return new DenotableValue(Category . BOOLEANVAR, false);
				}
				else
					return new DenotableValue(Category . BOOLEANVAR, true);
			}
			else
			{
				ErrorMessage . print ("type error");
				return null;
			}
		}
		
		// == & != operations check
		else if(root . matches("(==|!=)"))
		{
			DenotableValue leftDV= (DenotableValue) expression(env, eTree.left());
			DenotableValue rightDV = (DenotableValue) expression(env, eTree.right());
			if((leftDV.category() == Category.INTVAR && rightDV.category() == Category.INTVAR) ||
					(leftDV.category() == Category.LISTVAR && rightDV.category() == Category.LISTVAR))
			{
				if(root .equals("=="))
					return new DenotableValue(Category . BOOLEANVAR,
							leftDV.value().equals(rightDV.value()));
				else if(root .equals("!="))
					return new DenotableValue(Category . BOOLEANVAR,
							!leftDV.value().equals(rightDV.value()));
				else
					return null;
			}
			else
			{
				ErrorMessage . print ("type error");
				return null;
			}
		}
		
		//Relational operators check
		else if(root . matches("(<|<=|>|>=)"))
		{
			DenotableValue leftDV= (DenotableValue) expression(env, eTree.left());
			DenotableValue rightDV = (DenotableValue) expression(env, eTree.right());
			if(leftDV.category() == Category.INTVAR && rightDV.category() == Category.INTVAR)
			{
				if(root .equals("<"))
					return new DenotableValue(Category . BOOLEANVAR,
							(Integer)leftDV.value()<(Integer)rightDV.value());
				else if(root .equals("<="))
					return new DenotableValue(Category . BOOLEANVAR,
							(Integer)leftDV.value()<=(Integer)rightDV.value());
				else if(root .equals(">"))
					return new DenotableValue(Category . BOOLEANVAR,
							(Integer)leftDV.value()>(Integer)rightDV.value());
				else if(root .equals(">="))
					return new DenotableValue(Category . BOOLEANVAR,
							(Integer)leftDV.value()>=(Integer)rightDV.value());
				else 
					return null;
			}
			else
			{
				ErrorMessage . print ("type error");
				return null;
			}
		}
		
		//check for list append operation
		else if (eTree . root() .equals("::"))
		{
			DenotableValue leftDV= (DenotableValue) expression(env, eTree.left());
			DenotableValue rightDV = (DenotableValue) expression(env, eTree.right());
			if(leftDV.category() == Category.INTVAR || rightDV.category() == Category.LISTVAR)
			{
				List<Integer> rightVal = (List<Integer>) rightDV . value();
				rightVal.add(0, (Integer)leftDV.value());
				return new DenotableValue(Category.LISTVAR, rightVal);
			}
			else
			{
				ErrorMessage . print ("type error");
				return null;
			}
		}
		
		//check for head operation
		else if (eTree . root() .equals("head"))
		{
			DenotableValue dv = (DenotableValue) expression(env, eTree.left());
			if(dv.category() == Category.LISTVAR)
			{
				List lst=(List)dv.value();
				if(lst.size()>0)
					return new DenotableValue(Category . INTVAR,
							lst.get(0));
				else
				{
					ErrorMessage . print ("error");
					return null;
				}
					
			}
			else
			{
				ErrorMessage . print ("type error");
				return null;
			}
		}
		
		//check for tail operation
		else if (eTree . root() .equals("tail"))
		{
			DenotableValue dv = (DenotableValue) expression(env, eTree.left());
			if(dv.category() == Category.LISTVAR)
			{
				List lst=(List)dv.value();
				if(!lst.isEmpty())
					if(lst.size()>1)
						return new DenotableValue(Category . LISTVAR, 
								lst.subList(1, lst.size()));
					else
						return new DenotableValue(Category . LISTVAR, 
								new ArrayList());
				else
				{
					ErrorMessage . print ("error");
					return null;
				}
			}
			else
			{
				ErrorMessage . print ("type error");
				return null;
			}
		}
		
		//check for isEmpty operation
		else if (eTree . root() .equals("isEmpty"))
		{
			DenotableValue dv=(DenotableValue)expression(env, eTree.left());
			if(dv.category()==Category.LISTVAR)
				return new DenotableValue(Category . BOOLEANVAR, ((List)dv.value()).isEmpty());
			else
			{
				ErrorMessage . print ("type error");
				return null;
			}
		}
		
		//many more operations to check here
		
		//check for identifier
		else if(eTree . root() .equals("id"))
		{
			String id=eTree . left() . root();
			DenotableValue dv = env.accessEnv(id);
			return new DenotableValue(dv.category(), dv.value());
		}
		
		//check for int value
		else if(eTree . root() .equals("intValue"))
		{
			if (eTree . left() . root().equals("Nil"))
				return new DenotableValue(Category.LISTVAR, new ArrayList());
			else
				return new DenotableValue(Category.INTVAR, Integer.parseInt(eTree . left() . root()));
		}
		//check for functional call
		else if(eTree . root() .startsWith("apply "))
		{
			String funcID= eTree . root().split(" ")[1];
			DenotableValue funDV = env.accessFuncEnv(funcID);
			Procedure p=(Procedure) funDV.value();
			Iterator<Entry<String, DenotableValue>> it=p.env().map().entrySet().iterator();
			//Creating New environment
			//Environment newEnv= p.env().dupEnvironment();
			Environment newEnv= env.dupEnvironment();
			while(it.hasNext())
			{
				Entry<String, DenotableValue> formals=it.next();
				int cat = formals.getValue().category();
				Object value=null;
				if(cat == 0)
					value=0;
				else
					value = new ArrayList();
				newEnv.updateEnv(formals.getKey(), new DenotableValue(cat, value));
			}
			
			List<Object> actuals = new ArrayList<Object>();
			for(SyntaxTree t : eTree . getTrees())
			{
				//actuals.add(expression(env, t));
				DenotableValue dv = (DenotableValue)expression(env, t);
				actuals.add(new DenotableValue(dv.category(), dv.value()));
			}
			//Type and count checking of arguments
			
			//adding return as ID in new env
			newEnv.updateEnv("return",new DenotableValue(p.getReturnType(), null));
			
			it=p.getParameters().entrySet().iterator();
			Iterator<Object> it1 = actuals.iterator();
			if(p.getParameters().size() != actuals.size())
				ErrorMessage . print ("Number of parameters mismatch applying function "+funcID);
			while(it.hasNext() && it1.hasNext())
			{
				Entry<String, DenotableValue> param=it.next();
				DenotableValue argDV = (DenotableValue)it1.next();
				
				if(argDV . category() == param . getValue() . category())
				{
					//param.getValue().setValue(argDV.value());	
					int cat = argDV . category();
					Object val = argDV . value();
					if(cat == 0)
						newEnv.updateEnv(param.getKey(),new DenotableValue(cat, val));
					else
							newEnv.updateEnv(param.getKey(),new DenotableValue(cat, new ArrayList((List)val)));
				}
				else
					ErrorMessage . print ("Parameter type mismatch applying function "+funcID);
			}
			
			statement(newEnv, p.syntaxTree());//Interpret function with new env
			DenotableValue dvNew= new DenotableValue(p.getReturnType(), newEnv.accessEnv("return").value());
			newEnv = null;
			return dvNew;
		}
		else
			return null;
	}
	
}
