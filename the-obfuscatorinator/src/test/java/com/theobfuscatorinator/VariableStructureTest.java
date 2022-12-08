package com.theobfuscatorinator;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.theobfuscatorinator.codeInterpreter.MethodStructure;
import com.theobfuscatorinator.codeInterpreter.VariableStructure;

public class VariableStructureTest {
    private File methodBody1;

    @Before
    public void setUp() {
        methodBody1 = new File("./src/test/res/testfiles/MethodTests/MethodBody1.txt");
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
    public void identifyMethodVariableTest1() {
        try {
            String methodBody = Files.readString(methodBody1.toPath());

            MethodStructure method = new MethodStructure("main", "public", true, "", "String[] args", "void", methodBody);

            ArrayList<VariableStructure> variables = VariableStructure.identifyMethodVariables(method);

            ArrayList<VariableStructure> actualVariables = new ArrayList<>();
            actualVariables.add(new VariableStructure("", false, false, "int", "a", false, false));
            actualVariables.add(new VariableStructure("", false, false, "int", "b", false, false));

            assertEquals(actualVariables.size(), variables.size());

            for (int i = 0; i < variables.size(); i++) {
                assertEquals(actualVariables.get(i), variables.get(i));
            }
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
