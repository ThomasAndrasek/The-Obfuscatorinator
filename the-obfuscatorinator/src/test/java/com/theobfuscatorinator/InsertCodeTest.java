package com.theobfuscatorinator;

import com.theobfuscatorinator.codeInterpreter.CodeStructure;
import com.theobfuscatorinator.insertcode.InsertCode;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class InsertCodeTest {

    @Test
    public void testInsertStrings() throws IllegalArgumentException, IOException {
        File f = new File("./src/test/res/individual-files/TestClassIdentifying.java");
    	if (f.exists()) {
    		CodeStructure t = null;
    		try{
                Path filePath = Paths.get("./src/test/res/individual-files/TestClassIdentifying.java");
                FileChannel fileSize = FileChannel.open(filePath);
                Long originalSize = fileSize.size();
                t = new CodeStructure(f);
                String code = InsertCode.insertStrings(t);
                InsertCode.modifyFile(f, code);
                Long newSize = fileSize.size();
                assertTrue(null, newSize > originalSize);
            }catch(IOException e){
                assertTrue("IOException Thrown in constructor", false);
            }
    	}
    }

    @Test
    public void testInsertClass() throws IllegalArgumentException, IOException {
        File f = new File("./src/test/res/individual-files/TestClassIdentifying.java");
    	if (f.exists()) {
    		CodeStructure t = null;
    		try{
                Path filePath = Paths.get("./src/test/res/individual-files/TestClassIdentifying.java");
                FileChannel fileSize = FileChannel.open(filePath);
                Long originalSize = fileSize.size();
                t = new CodeStructure(f);
                String code = InsertCode.insertClass(t);
                InsertCode.modifyFile(f, code);
                Long newSize = fileSize.size();
                assertTrue(null, newSize > originalSize);
            }catch(IOException e){
                assertTrue("IOException Thrown in constructor", false);
            }
    	}
    }

    @Test
    public void testBothInserts() throws IllegalArgumentException, IOException {
        File f = new File("./src/test/res/individual-files/TestClassIdentifying.java");
    	if (f.exists()) {
    		CodeStructure t = null;
    		try{
                t = new CodeStructure(f);
                InsertCode.insertStrings( t);
                InsertCode.insertClass( t);
            }catch(IOException e){
                assertTrue("IOException Thrown in constructor", false);
            }
    	}
    }
}
