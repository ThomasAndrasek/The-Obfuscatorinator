import java.util.Scanner;

public class TestScan {

     public static void main(String []args){
         Scanner scan = new Scanner(System.in);
         
         System.out.print("Enter text >> ");
         String input = scan.next();
         System.out.println(input);
         scan.close();

     }
}