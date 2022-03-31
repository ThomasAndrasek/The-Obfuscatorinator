package com.theobfuscatorinator;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.theobfuscatorinator.codeInterpreter.CodeStructure;

import org.junit.Test;

public class StringEncryptionTest {
    
    @Test
    public void testStringIdentifying() {
        File f = new File("./src/test/res/individual-files/TestClassIdentifying.java");
        if(f.exists()){
            CodeStructure t = null;
            try{
                t = new CodeStructure(f);
            }catch(IOException e){
                assertTrue("IOException Thrown in constructor", false);
            }
        }
    }

}
