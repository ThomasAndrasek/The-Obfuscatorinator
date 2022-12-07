package com.theobfuscatorinator;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.Before;
import org.junit.Test;

import com.theobfuscatorinator.codeInterpreter.CodeStructure;

public class HelperFunctionTests {
    private File testCode;

    private File answerRemoveInnerCodeOfBraces1;

    private File answerRemoveInnerCode1;

    @Before
    public void setUp() {
        testCode = new File("./src/test/res/individual-files/HelperFunctions.java");

        answerRemoveInnerCodeOfBraces1 = new File("./src/test/res/answers/removeInnerCodeOfBracesAnswer1.txt");

        answerRemoveInnerCode1 = new File("./src/test/res/answers/removeInnerCodeAnswer1.txt");
    }

    /**
     * Tests the removeInnerCodeOfBraces method given a full class.
     */
    @Test
    public void testRemoveInnerCodeOfBraces1() {
        try {
            String answerString = Files.readString(answerRemoveInnerCodeOfBraces1.toPath());

            String code = Files.readString(testCode.toPath());

            String formattedCode = CodeStructure.removeInnerCodeOfBraces(code);

            assertEquals(answerString.length(), formattedCode.length());

            assertEquals(answerString, formattedCode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Tests the removeInnerCodeOfBraces method given a full class.
     */
    @Test
    public void testRemoveInnerCode1() {
        try {
            String answerString = Files.readString(answerRemoveInnerCode1.toPath());

            String code = Files.readString(testCode.toPath());

            String formattedCode = CodeStructure.removeInnerCode(code);

            assertEquals(answerString.length(), formattedCode.length());

            assertEquals(answerString, formattedCode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
