package com.theobfuscatorinator;

import com.theobfuscatorinator.codeInterpreter.CodeStructure;
import com.theobfuscatorinator.insertcode.InsertCode;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class InsertCodeTest {


    @Test
    public void testInsertClass() throws IllegalArgumentException, IOException {
        File f = new File("./src/test/res/individual-files/TestClassIdentifying.java");
    	if (f.exists()) {
    		CodeStructure t = null;
    		try{
                t = new CodeStructure(f);
                InsertCode.insertClass(t);
            }catch(IOException e){
                assertTrue("IOException Thrown in constructor", false);
            }
    	}
    }

    @Test
    public void testInsertStrings() throws IllegalArgumentException, IOException {
        File f = new File("./src/test/res/individual-files/TestClassIdentifying.java");
    	if (f.exists()) {
    		CodeStructure t = null;
    		try{
                t = new CodeStructure(f);
                InsertCode.insertStrings(t);
            }catch(IOException e){
                assertTrue("IOException Thrown in constructor", false);
            }
    	}
    }


}
