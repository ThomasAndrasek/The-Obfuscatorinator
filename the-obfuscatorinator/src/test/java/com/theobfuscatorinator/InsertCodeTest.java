package com.theobfuscatorinator;

import com.theobfuscatorinator.codeInterpreter.CodeStructure;
import com.theobfuscatorinator.insertcode.InsertCode;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class InsertCodeTest {
    
    @Test
    public void testPrint() throws IllegalArgumentException, IOException {
        File f = new File("./src/test/res/individual-files/TestClassIdentifying.java");
    	if (f.exists()) {
    		CodeStructure t = null;
    		try{
                t = new CodeStructure(f);
            }catch(IOException e){
                assertTrue("IOException Thrown in constructor", false);
            }
    	}
        CodeStructure t = new CodeStructure(f);
        InsertCode.insertCode(f, t);
    }

    


}
