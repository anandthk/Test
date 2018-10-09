package com.calculator;



import java.text.DecimalFormat;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringExpCalculator {

	
	private static String format(String inputStr) {	 
		inputStr = inputStr.replaceAll("(?<=[0-9()])[\\/]", " / ");
		inputStr = inputStr.replaceAll("(?<=[0-9()])[\\^]", " ^ ");
		inputStr = inputStr.replaceAll("(?<=[0-9()])[\\*]", " * ");
		inputStr = inputStr.replaceAll("(?<=[0-9()])[+]", " + ");
		inputStr = inputStr.replaceAll("(?<=[0-9()])[-]", " - ");
		return inputStr;
	}

	public static Double calculate(String expr) throws Exception{
		DecimalFormat df = new DecimalFormat("#.##");		 
		String expression = format(expr);
		try {
			int iClose = expression.indexOf(")");
			int iOpen = -1;
			if (iClose != -1) {
				String substring = expression.substring(0, iClose);
				iOpen = substring.lastIndexOf("(");
				substring = substring.substring(iOpen + 1).trim();
				if (iOpen != -1 && iClose != -1) {
					Double result = calculate(substring);
					expression = expression.substring(0, iOpen).trim() + " " + result + " "
							+ expression.substring(iClose + 1).trim();
					return calculate(expression.trim());
				}
			}
			String operation = "";
			if (expression.indexOf(" / ") != -1) {
				operation = "/";
			} else if (expression.indexOf(" ^ ") != -1) {
				operation = "^";
			} else if (expression.indexOf(" * ") != -1) {
				operation = "*";
			} else if (expression.indexOf(" + ") != -1) {
				operation = "+";
			} else if (expression.indexOf(" - ") != -1) { 
				operation = "-";
			} else {
				return Double.parseDouble(expression);
			}

			int index = expression.indexOf(operation);
			if (index != -1) {
				iOpen = expression.lastIndexOf(" ", index - 2);
				iOpen = (iOpen == -1) ? 0 : iOpen;
				iClose = expression.indexOf(" ", index + 2);
				iClose = (iClose == -1) ? expression.length() : iClose;
				if (iOpen != -1 && iClose != -1) {
					Double lhs = Double.parseDouble(expression.substring(iOpen, index));
					Double rhs = Double.parseDouble(expression.substring(index + 2, iClose));
					Double result = null;
					switch (operation) {
					case "/":
						if (rhs == 0) {
							return null;
						}
						result = lhs / rhs;
						break;
					case "^":
						result = Math.pow(lhs, rhs);
						break;
					case "*":
						result = lhs * rhs;
						break;
					case "-":
						result = lhs - rhs;
						break;
					case "+":
						result = lhs + rhs;
						break;
					default:
						break;
					}
					if (iClose == expression.length()) {
						expression = expression.substring(0, iOpen) + " " + result + " "
								+ expression.substring(iClose);
					} else {
						expression = expression.substring(0, iOpen) + " " + result + " "
								+ expression.substring(iClose + 1);
					}
					return Double.valueOf(df.format(calculate(expression.trim())));
				}
			}
		} catch (Exception exp) {
			exp.printStackTrace();
		}
		return 0.0;
	}

	private static int counter = 0;
	
	public static void main(String args[]) {
		Scanner scanner = new Scanner(System.in);		
		String input = "";
		do {		    
			System.out.print("Enter String: ");
		    input = scanner.nextLine();
		    if(!input.equalsIgnoreCase("done")) {
		    	try {
		    		counter =counter+1;
			        Double val = +calculate(input);
			        System.out.println("Case #"+counter+" : "+val);				 
			    }catch(Exception ex) {
			    	 System.out.println("Case #"+counter+" : Invalid Expression");
			    	 input = "done";
			    }
			  
		    }
		} while (!input.equalsIgnoreCase("done"));
		scanner.close();
	}
	
	private static boolean validateInput(String input) {
		Pattern pattern = Pattern.compile("([\\+\\-\\*\\/])\\1\\1", Pattern.CASE_INSENSITIVE);
		final Matcher m = pattern.matcher(input);
		boolean bl 		= m.matches();
		System.out.println("---"+bl);

		return bl;
	}
}

