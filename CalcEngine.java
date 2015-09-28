import java.util.ArrayList;

public final class CalcEngine {
	
	private static String currentInput= "";
	private static boolean hasDecimal = false;
	private static ArrayList <String> calcStack = new ArrayList <String>();
	private static boolean carryOver = false;
	private static boolean allowOperator = false;
	private static boolean allowEquals = false;
	private static boolean allowDigits = true;

	final private static char BACKSPACE ='\u2190';
	final private static char SQR_ROOT ='\u221A';
	final private static char INVERSE = '\u215f';
	final private static char SQUARE = 'X';
	public CalcEngine () {
	}
		
	public static String inputChar (String input) {
		String retString ="";
	if (input.charAt(0) >= '0' && input.charAt(0) <= '9'&& allowDigits) {
		if (carryOver) {													//if we still have a number as a result of the last calculation in the display clear it and its flag
			currentInput = "";
			carryOver = false;
			allowOperator = true;	
			allowEquals = true;
			currentInput += input;
			return currentInput;	
		} else {	
			allowOperator = true;
			allowEquals = true;
			currentInput += input;
			return currentInput;					
		}
	}		
		switch (input.charAt(0)) {		
			case '.' :
			if (carryOver) {													//if we still have a number as a result of the last calculation in the display clear it and its flag
				currentInput = "";
				carryOver = false;	
			}	else if (! hasDecimal) {													//if we enter a decimal point and we have not entered one before add it and set flag to stop us entering .'s
			currentInput += input;
			hasDecimal = true;
			retString = currentInput;			
			}
			retString = currentInput;
			break;
			case SQR_ROOT:														//if we have entered an operator save the operator and then the number on the display
			case INVERSE:
			writeTerm(input.substring(0,1));
			writeTerm(currentInput);
			currentInput = "";													//clear number we were inputing
			carryOver = false;
			hasDecimal= false;													//unset the decimal point flag to allow us to enter .'s again		
			allowDigits = false;
			allowEquals = true;
			allowOperator = true;
			break;
		
			case SQUARE:
			if (allowOperator) {
				writeTerm(currentInput);
				writeTerm (input.substring(1));
				currentInput = "";													//clear number we were inputing
				carryOver = false;
				hasDecimal= false;													//unset the decimal point flag to allow us to enter .'s again	
				allowDigits = false;
				allowEquals = true;
				allowOperator = true;
			}
			break;
			case '+':
			case '-':
			case '/':
			case '*':
				if (allowOperator && currentInput.length() > 0) {
					carryOver = false;
					writeTerm(currentInput);
					writeTerm(input);
					currentInput = "";													//clear number we were inputing
					carryOver = false;
					allowDigits = true;
					hasDecimal= false;													//unset the decimal point flag to allow us to enter .'s again	
					allowOperator = false;
					allowEquals = false;
				}
			break;
			case '=':															//if  we entered an equals																						
			if (currentInput != "" && allowEquals) {  
				writeTerm(currentInput);											//save the number we were inputing
				allowEquals = false;	
				currentInput = calculate();											//call the calculate method
				hasDecimal= false;													//unset flag so we can input .'s again
				carryOver = true;													//set the carryOver flag so that we can clear the answer from the display if we start by entering a new number	
				calcStack.clear();													//clear the stored calculation 
				allowEquals = false;
				allowOperator = true;
				allowDigits = true;
				retString = currentInput;
			} else if (allowEquals) {
				allowEquals = false;	
				currentInput = calculate();											//call the calculate method
				hasDecimal= false;													//unset flag so we can input .'s again
				carryOver = true;													//set the carryOver flag so that we can clear the answer from the display if we start by entering a new number	
				calcStack.clear();													//clear the stored calculation 
				allowEquals = false;
				allowOperator = true;
				allowDigits = true;
				retString = currentInput;												//return answer to display			
			}		
			break;
			case 'C':
				if (input == "CE") {												
					currentInput = "";													//delete the digits currently on display
					hasDecimal = false;													//unset the .'s flag 
					return "";															//return 0 for display
				} else  {
				currentInput = "";													//clear stored calculation and input in display
				calcStack.clear();
				hasDecimal = false;
			} 
			
			case BACKSPACE:														//trims last character from display 	
			if (currentInput.length() > 0) {									//check if we are deleting a decimal point and unset .'s flag if true
				if (currentInput.charAt(currentInput.length() -1) == '.') {
					hasDecimal = false;
				}
				currentInput = currentInput.substring(0, currentInput.length() -1);					
			}
			retString = currentInput;
			break;
			
			default:
			return currentInput;
			}	
		return retString;
		}
	
		
	

	//method to return the long version of the calculation for display 
	public static String getCalculation() {
		String tempString = "";	
		for (int i = 0; i < calcStack.size(); i++) {
		tempString += readTerm(i);
		}		
		return tempString;
	}				
	//main perform calculations	method
		private static String calculate() {
			double firstNum, secondNum;
			while  (calcStack.contains("["+ "\u00b2" +"]")) {  // X to the power of 2
				for (int i = 1; i < calcStack.size(); i++) {
					if (readTerm(i).equals("\u00b2")) {
						firstNum = Double.parseDouble(readTerm(i-1));
						calcStack.remove(i);
						calcStack.remove(i - 1);
						writeTerm(i-1,String.valueOf(Math.pow(firstNum, 2)));	
					}
				}
			}
			while  (calcStack.contains("["+ "\u221A" +"]")) {   //Square root
				for (int i = 0; i < calcStack.size() -1; i++) {
					if (readTerm(i).equals("\u221A")) {
						firstNum = Double.parseDouble(readTerm(i+1));
						calcStack.remove(i+1);
						calcStack.remove(i);
						writeTerm(i,String.valueOf(Math.sqrt(firstNum)));	
					} 
				}
			}
			
			while  (calcStack.contains("["+ "\u215f" +"]")) {  //inverse
				for (int i = 0; i < calcStack.size() -1; i++) {
					if (readTerm(i).equals("\u215f")) {
						firstNum = Double.parseDouble(readTerm(i+1));
						calcStack.remove(i+1);
						calcStack.remove(i);
						writeTerm(i,String.valueOf(1.0/firstNum));	
					} 
				}
			}
			
			while  (calcStack.contains("[/]")) {
				for (int i = 1; i < calcStack.size() -1; i++) {
					if (readTerm(i).equals("/")) {
						firstNum = Double.parseDouble(readTerm(i-1));
						secondNum = Double.parseDouble(readTerm(i +1));
						calcStack.remove(i);
						calcStack.remove(i);
						calcStack.remove(i -1);
						writeTerm(i-1, String.valueOf(firstNum / secondNum));
					} 
				} 
			}
			while  (calcStack.contains("[*]")) {
				for (int i = 1; i < calcStack.size() -1; i++) {
					if (readTerm(i).equals("*")) {
						firstNum = Double.parseDouble(readTerm(i-1));
						secondNum = Double.parseDouble(readTerm(i +1));
						calcStack.remove(i);
						calcStack.remove(i);
						calcStack.remove(i -1);
						writeTerm(i-1, String.valueOf(firstNum * secondNum));
					} 
				}
			}
			while  (calcStack.contains("[+]")) {
				for (int i = 1; i < calcStack.size() -1; i++) {
					if (readTerm(i).equals("+")) {
						firstNum = Double.parseDouble(readTerm(i-1));
						secondNum = Double.parseDouble(readTerm(i +1));
						calcStack.remove(i);
						calcStack.remove(i);
						calcStack.remove(i -1);
						writeTerm(i-1, String.valueOf((firstNum + secondNum)));
					} 
				}
			}
			while  (calcStack.contains("[-]")) {
				for (int i = 1; i < calcStack.size() -1; i++) {
					if (readTerm(i).equals("-")) {
						firstNum = Double.parseDouble(readTerm(i-1));
						secondNum = Double.parseDouble(readTerm(i +1));
						calcStack.remove(i);
						calcStack.remove(i);
						calcStack.remove(i -1);
						writeTerm(i-1, String.valueOf((firstNum - secondNum)));
					} 
				}
			}		
			return readTerm(0);
		}															
		//save and read the 'words' of our calculation to/from the calcStack arrayList adding and removing [ ] so that we can tell negative numbers from the '-' operator
		private static void writeTerm( int index ,String term) {		
			term = "[" + term + "]";
			calcStack.add(index,term);
		}		
		private static void writeTerm(String term) {			
			term = "[" + term + "]";
			calcStack.add(term);
		}		
		private static String readTerm (int index) {
			String term = calcStack.get(index);
			return term.substring(1, term.length() -1);
		}	
}