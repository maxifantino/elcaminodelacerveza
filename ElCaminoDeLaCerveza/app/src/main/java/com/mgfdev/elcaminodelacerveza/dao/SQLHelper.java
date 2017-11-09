package com.mgfdev.elcaminodelacerveza.dao;

import java.util.List;

public class SQLHelper {

	public static String createSingleCondition (String field, String operand, String value)
	{
		String whereValue = field + " " + operand + "'" + value + "'";
		
		return whereValue;
	}
	
	public static String colapseInString(String field, Boolean not, List<String> stringValues)
	{
		String strNot="";
		if (not) strNot = " NOT ";
		
		StringBuilder values = new StringBuilder();
		if (stringValues == null || stringValues.size() == 0)
		{
			return null;
		}
		
		for(int index=0; index < stringValues.size(); index++)
		{
			values.append("'" + stringValues.get(index) + "'");
			if (index != stringValues.size() -1 )
			{
				values.append(",");
			}
		}
		return field + strNot+ " in( " + values.toString() + ")";
		
	}
}
