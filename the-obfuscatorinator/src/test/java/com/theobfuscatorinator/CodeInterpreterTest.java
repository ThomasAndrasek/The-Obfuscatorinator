package com.theobfuscatorinator;

import com.theobfuscatorinator.codeInterpreter.*;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.*;


public class CodeInterpreterTest {


    @Test
    public void testCodeStructure() {
        File f = new File("./src/test/res/individual-files/TestClassIdentifying.java");
        if(f.exists()){
            CodeStructure t = null;
            try{
                t = new CodeStructure(f);
                assertTrue(true);
            }catch(Exception e){
                e.printStackTrace();
                assertTrue("IOException Thrown in constructor", false);
            }
        }
    }
    
    @Test
    public void testGetOriginalCode() throws IllegalArgumentException, IOException {
        File f = new File("./src/test/res/individual-files/TestClassIdentifying.java");
        CodeStructure t = new CodeStructure(f);
        String oc = new String(Files.readAllBytes(f.toPath()));
        String hold = t.getOriginalCode();
        //checks that the code is original
        assertEquals(oc, hold);
    }
    
    @Test
    public void testRemoveComments() throws IllegalArgumentException, IOException {
        File f = new File("./src/test/res/individual-files/TestClassIdentifying.java");
        CodeStructure t = new CodeStructure(f);
        String hold = t.getUnCommentedCode();
        //checks that there are no comments
        assertFalse(hold.contains("//"));
    }
    
    
    @Test
    public void testRemoveStrings() throws IllegalArgumentException, IOException {
        File f = new File("./src/test/res/individual-files/TestClassIdentifying.java");
        CodeStructure t = new CodeStructure(f);
        String hold = t.getNoStringCode();
        //checks that there String literals
        assertFalse(hold.contains("\""));
    }
    
    
    @Test
    public void testAddComments() throws IllegalArgumentException, IOException {
        File f = new File("./src/test/res/individual-files/TestClassIdentifying.java");
        CodeStructure t = new CodeStructure(f);
        String hold = t.getNewCommentCode();
        //checks that comments are added
        assertTrue(hold.contains("//"));
    }
    

    @Test
    public void testClassIdentifying() {
        File f = new File("./src/test/res/individual-files/TestClassIdentifying.java");
        if(f.exists()){
            CodeStructure t = null;
            try{
                t = new CodeStructure(f);
            }catch(Exception e){
                e.printStackTrace();
                //assertTrue("IOException Thrown in constructor", false);
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
