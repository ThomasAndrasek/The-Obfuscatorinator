package com.theobfuscatorinator;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.theobfuscatorinator.codeInterpreter.ClassStructure;
import com.theobfuscatorinator.codeInterpreter.MethodStructure;
import com.theobfuscatorinator.codeInterpreter.VariableStructure;

public class VariableStructureTest {
    private File methodBody1;

    private File class1;

    @Before
    public void setUp() {
        methodBody1 = new File("./src/test/res/testfiles/MethodTests/MethodBody1.txt");

        class1 = new File("./src/test/res/testfiles/ClassTests/ClassTest1.txt");
    }

    @Test
    public void zeroParametersMethodTest() {
        ArrayList<String> zeroParams = new ArrayList<String>();

        ArrayList<VariableStructure> parametersFound = VariableStructure.identifyParameters(zeroParams);

        assertEquals(0, parametersFound.size());
    }

    @Test
    public void oneParametersMethodTest() {
        ArrayList<String> zeroParams = new ArrayList<String>();
        zeroParams.add("int foo");

        ArrayList<VariableStructure> parametersFound = VariableStructure.identifyParameters(zeroParams);

        ArrayList<VariableStructure> actualParameters = new ArrayList<>();
        actualParameters.add(new VariableStructure("", false, false, "int", "foo", false, true));

        assertEquals(1, parametersFound.size());

        for (int i = 0; i < parametersFound.size(); i++) {
            assertEquals(actualParameters.get(i), parametersFound.get(i));
        }
    }

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
}
