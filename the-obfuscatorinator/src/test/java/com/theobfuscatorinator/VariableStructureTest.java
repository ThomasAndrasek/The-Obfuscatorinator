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
}
