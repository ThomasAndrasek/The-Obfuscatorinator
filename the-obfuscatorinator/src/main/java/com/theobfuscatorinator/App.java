package com.theobfuscatorinator;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        try{
            if(args.length == 0) throw new IllegalArgumentException("Not enough arguments");
        }
        catch(Exception e){
            System.err.print("Exception thrown: " + e.getMessage() + "\n" + e.toString() + "\n");
            System.exit(1);
        }
    }
}
