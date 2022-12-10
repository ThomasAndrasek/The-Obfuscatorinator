package com.theobfuscatorinator;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.theobfuscatorinator.codeInterpreter.CodeStructure;
import com.theobfuscatorinator.codeInterpreter.PackageStructure;

public class PackageStructureTests {
    
    private File package1;

    @Before
    public void setUp() {
        package1 = new File("./src/test/res/testfiles/PackageTests/PackageTest1.txt");
    }

    /**
     * Test to see if the valid package is identified in file package1.
     */
    @Test
    public void packageIdentificationTest1() {
        try {
            CodeStructure codeStructure = new CodeStructure(package1);

            PackageStructure packageStructure = PackageStructure.identifyPackage(codeStructure);

            PackageStructure actual = new PackageStructure("com.theobfuscatorinator");

            assertEquals(actual, packageStructure);
        } 
        catch (IllegalArgumentException e) {
            e.printStackTrace();
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
        
    }
}
