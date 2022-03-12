package com.theobfuscatorinator.practice_code;

public class testing1 {
	public static void main(String[] args) {
		//adds a bunch of variables with similar names
		int gross = 1;
		int not_gross = 23;
		int grossest = 15;
		double never = 5;
		//checking that printing still works
		if (gross + not_gross > 15) {
			System.out.println("I am NOT gross");
		}
		//testing if calls still work
		System.out.printf("It is %f", newt(grossest+never, false));
			
    }
	private static double newt(double holding, boolean end) {
		//recursive code to test that the code will still work
		double hold = 0;
		if(holding > 0) {
			hold = holding-holding/2;
		    holding = newt(hold, end);
		}else {
			end = true;
		}
		if (end == true){
			System.out.println("Finished");
		}
		return holding;
	}

}