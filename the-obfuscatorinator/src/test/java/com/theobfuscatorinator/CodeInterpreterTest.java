package com.theobfuscatorinator;

import com.theobfuscatorinator.codeInterpreter.*;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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

    @Test
    public void testClassIdentifying() {
        File f = new File("./src/test/res/individual-files/TestClassIdentifying.java");
        if(f.exists()){
            CodeStructure t = null;
            try{
                t = new CodeStructure(f);
            }catch(IOException e){
                assertTrue("IOException Thrown in constructor", false);
            }

            ArrayList<ClassStructure> classes = t.getClasses();
            assertTrue("Classes should not be empty", classes.size() > 0);

            ArrayList<String> expected = new ArrayList<>();
            expected.add("TestClassIdentifying");
//            expected.add("InnerClass");
//            expected.add("DeepInnerClass");
//            expected.add("SecondInnerClass");
//            expected.add("TestFormat");
//            expected.add("TestFormat2");

            int counter = 0;
            for (ClassStructure classobj : classes) {
                String classStr = classobj.getClassName();
                assertTrue("Classes should not contain " + classStr, expected.contains(classStr));
                counter++;
            }

            assertTrue("Classes should contain " + expected.size() + " classes, got: " + counter, counter == expected.size());
        }
    }
}
