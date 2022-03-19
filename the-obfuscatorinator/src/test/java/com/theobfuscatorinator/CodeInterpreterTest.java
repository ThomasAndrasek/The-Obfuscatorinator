package com.theobfuscatorinator;

import com.theobfuscatorinator.codeInterpreter.*;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class CodeInterpreterTest {


    @Test
    public void testCodeStructure() {
        File f = new File("test.java");
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
