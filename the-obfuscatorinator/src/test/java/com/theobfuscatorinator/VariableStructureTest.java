package com.theobfuscatorinator;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.theobfuscatorinator.codeInterpreter.ClassStructure;
import com.theobfuscatorinator.codeInterpreter.InterfaceStructure;
import com.theobfuscatorinator.codeInterpreter.MethodStructure;
import com.theobfuscatorinator.codeInterpreter.VariableStructure;

public class VariableStructureTest {
    private File methodBody1;

    private File class1;

    private File interface1;

    @Before
    public void setUp() {
        methodBody1 = new File("./src/test/res/testfiles/MethodTests/MethodBody1.txt");

        class1 = new File("./src/test/res/testfiles/ClassTests/ClassTest1.txt");

        interface1 = new File("./src/test/res/testfiles/InterfaceTests/InterfaceTest1.txt");
    }

    /**
     * Tests how the program handles finding zero parameters for a method.
     * 
     * @expected Finds zero parameters.
     */
    @Test
    public void zeroParametersMethodTest() {
        ArrayList<String> zeroParams = new ArrayList<String>();

        ArrayList<VariableStructure> parametersFound = VariableStructure.identifyParameters(zeroParams);

        assertEquals(0, parametersFound.size());
    }

    /**
     * Tests how the program handles finding a single parameter.
     * 
     * @expected Finds the single parameter 'int foo'
     */
    @Test
    public void oneParametersMethodTest() {
        ArrayList<String> oneParams = new ArrayList<String>();
        oneParams.add("int foo");

        ArrayList<VariableStructure> parametersFound = VariableStructure.identifyParameters(oneParams);

        ArrayList<VariableStructure> actualParameters = new ArrayList<>();
        actualParameters.add(new VariableStructure("", false, false, "int", "foo", false, true));

        assertEquals(1, parametersFound.size());

        for (int i = 0; i < parametersFound.size(); i++) {
            assertEquals(actualParameters.get(i), parametersFound.get(i));
        }
    }

    /**
     * Tests the program to see how it handles finding two object variables.
     * 
     * @epected Finds the two object variables 'private int testVal; private String testString'
     */
    @Test
    public void identifyClassVariableTest1() {
        try {
            String classBody = Files.readString(class1.toPath());

            ClassStructure classStructure = new ClassStructure("public class HelperFunctions", classBody);

            ArrayList<VariableStructure> variables = VariableStructure.identifyClassVariables(classStructure);

            ArrayList<VariableStructure> actualVariables = new ArrayList<>();
            actualVariables.add(new VariableStructure("private", false, false, "int", "testVal", false, false));
            actualVariables.add(new VariableStructure("private", false, false, "String", "testString", false, false));

            assertEquals(actualVariables.size(), variables.size());

            int found = 0;
            for (int i = 0; i < variables.size(); i++) {
                for (int j = 0; j < actualVariables.size(); j++) {
                    if (variables.get(i).equals(actualVariables.get(j))) {
                        found++;
                        break;
                    }
                }
            }
            assertEquals(found, actualVariables.size());
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test the program to find variables in an interface.
     * 
     * @expected Finds the variable 'public static final int TEST_VAR'
     */
    @Test
    public void identifyInterfaceVariableTest1() {
        try {
            String interfaceBody = Files.readString(interface1.toPath());

            InterfaceStructure interfaceStructure = new InterfaceStructure("TestFace", interfaceBody);
            
            ArrayList<VariableStructure> variables = VariableStructure.identifyInterfaceVariables(interfaceStructure);

            ArrayList<VariableStructure> actualVariables = new ArrayList<>();
            actualVariables.add(new VariableStructure("public", true, true, "int", "TEST_VAR", false, false));

            assertEquals(actualVariables.size(), variables.size());

            int found = 0;
            for (int i = 0; i < variables.size(); i++) {
                for (int j = 0; j < actualVariables.size(); j++) {
                    if (variables.get(i).equals(actualVariables.get(j))) {
                        found++;
                        break;
                    }
                }
            }
            assertEquals(actualVariables.size(), found);
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Tests the program to find variables in a method.
     * 
     * @expected Finds the variables 'int a; int b'
     */
    @Test
    public void identifyMethodVariableTest1() {
        try {
            String methodBody = Files.readString(methodBody1.toPath());

            MethodStructure methodStructure = new MethodStructure("main", "public", true, "", "String[] args", "void", methodBody);
            
            ArrayList<VariableStructure> variables = VariableStructure.identifyMethodVariables(methodStructure);

            ArrayList<VariableStructure> actualVariables = new ArrayList<>();
            actualVariables.add(new VariableStructure("", false, false, "int", "a", false, false));
            actualVariables.add(new VariableStructure("", false, false, "int", "b", false, false));

            assertEquals(actualVariables.size(), variables.size());

            int found = 0;
            for (int i = 0; i < variables.size(); i++) {
                for (int j = 0; j < actualVariables.size(); j++) {
                    if (variables.get(i).equals(actualVariables.get(j))) {
                        found++;
                        break;
                    }
                }
            }
            assertEquals(actualVariables.size(), found);
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
