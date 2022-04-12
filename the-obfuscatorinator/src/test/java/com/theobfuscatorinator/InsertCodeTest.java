package com.theobfuscatorinator;

import com.theobfuscatorinator.codeInterpreter.CodeStructure;
import com.theobfuscatorinator.insertcode.InsertCode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

public class InsertCodeTest {
    
    @Test
    public void testCodeStructure()
    {
    	File f = new File("test.java");
    	if (f.exists()) {
    		CodeStructure t = null;
    		try{
                t = new CodeStructure(f);
            }catch(IOException e){
                assertTrue("IOException Thrown in constructor", false);
            }
    	}
    }

    


}
