package com.theobfuscatorinator;

import com.theobfuscatorinator.codeInterpreter.CodeStructure;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import java.io.IOException;
import java.util.Arrays;

import javax.tools. *;

import org.junit.Test;


//obsolete shellscript work will handle changes
public class CodeStructureTest {
	
	public static void main(String args[]) throws IOException {
		
	}
    @Test
    public void testRemoveComments() {
        //assertTrue();
    }
    @Test
    public void testRemoveStrings() {
        //assertTrue();
    }
    @Test
    public void testRemoveSpaces() {
        //assertTrue();
    }
    @Test
    public void testChangeVar() {
        //assertTrue();
    }
    @Test
    public void testGetCodeBetweenBrackets() {
        //assertTrue();
    }
    @Test
    public void testGetCommaSeparatedValues() {
        //assertTrue();
    }
    
    private static void checker() throws IOException {
    	//Check stack overflow for more information
    	//https://stackoverflow.com/questions/8363904/how-to-compile-java-file-from-within-java-program
    	//still working on understanding this
    	JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
		StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);
		Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromStrings(Arrays.asList("testing1.java"));
		JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, null,
		        null, compilationUnits);
		boolean success = task.call();
		fileManager.close();
		System.out.println("Success: " + success);
    	
    }
    

}