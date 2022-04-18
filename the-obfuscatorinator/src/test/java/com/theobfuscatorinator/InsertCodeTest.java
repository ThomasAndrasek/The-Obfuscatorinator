package com.theobfuscatorinator;

import com.theobfuscatorinator.codeInterpreter.CodeStructure;
import com.theobfuscatorinator.insertcode.InsertCode;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class InsertCodeTest {
    File f = new File("./src/test/res/individual-files/TestClassIdentifying.java");
    @Test
    public void testInsert() throws IllegalArgumentException, IOException {
    	if (f.exists()) {
    		CodeStructure t = null;
    		try{
                t = new CodeStructure(f);
                InsertCode.insertCode(f, t);
            }catch(IOException e){
                assertTrue("IOException Thrown in constructor", false);
            }
    	}
    }

    @Test
    public void testCompiles() {
        //4680 size of class
    }


}
