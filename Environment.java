//Environment.java

//Environment is a class to represent the environment for PL/0 programs.

import java.util.*;

public class Environment {

	// private Environment parent;
	private Environment parent;
	private TreeMap map;
	private int maxIdLength = 2; // for heading "Id"

	public Environment() {
		map = new TreeMap();
	}

	public Environment(Environment staticParent) {
		map = new TreeMap();
		parent = staticParent;
	}

	public TreeMap map() {
		return map;
	}

	public DenotableValue accessEnv(String id) {
		DenotableValue denotVal = (DenotableValue) map.get(id);
		if (denotVal == null)
			if (parent == null) {
				ErrorMessage.print("Identifier " + id + " undeclared");
				return null; // will not happen since ErrorMessage . print exits
			} else
				return parent.accessEnv(id);
		else
			return denotVal;
	}

	public DenotableValue accessFuncEnv(String id) {
		DenotableValue denotVal = (DenotableValue) parent.map.get(id);
		if (denotVal == null || denotVal.category() != 2) {
			ErrorMessage.print("Function " + id + " undeclared");
			return null;
		} else
			return denotVal;
	}

	public void updateEnvIntVar(String id, Integer intValue) {
		updateEnv(id, new DenotableValue(Category.INTVAR, intValue));
	}

	public void updateEnvListVar(String id) {
		updateEnv(id, new DenotableValue(Category.LISTVAR, new ArrayList()));
	}

	public void updateEnvFunc(String id) { // denotable value filled in later
		updateEnv(id, new DenotableValue(Category.FUNCTIONID, null));
	}

	public Environment newFunction() {
		return new Environment(this);
	}

	public Environment dupEnvironment() {
		return new Environment(this.parent);
	}

	public void updateEnvFunc(String id, SyntaxTree syntaxTree, Environment env,
			LinkedHashMap<String, DenotableValue> params, int returnType) {
		updateEnv(id, new DenotableValue(Category.FUNCTIONID, new Procedure(env, params, syntaxTree, returnType)));
	}

	public void updateEnv(String id, DenotableValue denotableValue) {
		DenotableValue denotVal = (DenotableValue) map.get(id);
		if (denotVal != null && denotVal.value() != null)
			ErrorMessage.print("Identifier " + id + " previously declared");
		if (id.length() > maxIdLength)
			maxIdLength = id.length();
		map.put(id, denotableValue);
	}

	public void updateEnvVal(String id, DenotableValue denotableValue) {
		if (denotableValue.category() != Category.FUNCTIONID) {
			DenotableValue denotVal = (DenotableValue) map.get(id);
			if (denotVal == null)
				if (parent == null)
					ErrorMessage.print("Identifier " + id + " undeclared");
				else
					parent.updateEnvVal(id, denotableValue);
			else
				map.put(id, denotableValue);
		} else
			ErrorMessage.print("Function Identifier " + id + " value not updatable");
	}

	public void print(String blockName) {
		System.out.println("");
		System.out.println("Identifier Table for " + blockName);
		System.out.print("---------------------");
		for (int i = 0; i < blockName.length(); i++)
			System.out.print("-");
		System.out.println("");
		System.out.println("");
		System.out.print("Id");
		for (int i = 0; i <= maxIdLength - 2; i++)
			System.out.print(" ");
		System.out.println("Category");
		System.out.print("--");
		for (int i = 0; i <= maxIdLength - 2; i++)
			System.out.print(" ");
		System.out.println("--------");
		Iterator envIterator = map.entrySet().iterator();
		TreeMap procedureList = new TreeMap();
		while (envIterator.hasNext()) {
			Map.Entry envEntry = (Map.Entry) envIterator.next();
			String entryId = (String) envEntry.getKey();
			System.out.print(entryId);
			for (int i = 0; i <= maxIdLength - entryId.length(); i++)
				System.out.print(" ");
			DenotableValue entryDenotVal = (DenotableValue) envEntry.getValue();
			System.out.println(entryDenotVal);
			if (entryDenotVal.category() == Category.FUNCTIONID)
				procedureList.put(entryId, entryDenotVal.value());
		}
		Iterator procIterator = procedureList.entrySet().iterator();
		while (procIterator.hasNext()) {
			Map.Entry procEntry = (Map.Entry) procIterator.next();
			String procId = (String) procEntry.getKey();
			((Procedure) procEntry.getValue()).print(procId);
		}
	}

}