package com.theobfuscatorinator;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.theobfuscatorinator.codeInterpreter.VariableStructure;

public class VariableStructureTest {
    private File zeroParametersMethod;

    @Before
    public void setUp() {
        zeroParametersMethod = new File("./src/test/res/testfiles/ZeroParameters.txt");
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
}
