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

    private File innerHelperFunctions;

    private File answerRemoveInnerCodeOfBraces1;
    private File answerRemoveInnerCodeOfBraces2;

    private File answerRemoveInnerCode1;
    private File answerRemoveInnerCode2;

    @Before
    public void setUp() {
        testCode = new File("./src/test/res/individual-files/HelperFunctions.java");

        innerHelperFunctions = new File("./src/test/res/testfiles/HelperFunctions.txt");

        answerRemoveInnerCodeOfBraces1 = new File("./src/test/res/answers/removeInnerCodeOfBracesAnswer1.txt");
        answerRemoveInnerCodeOfBraces2 = new File("./src/test/res/answers/removeInnerCodeOfBracesAnswer2.txt");

        answerRemoveInnerCode1 = new File("./src/test/res/answers/removeInnerCodeAnswer1.txt");
        answerRemoveInnerCode2 = new File("./src/test/res/answers/removeInnerCodeAnswer2.txt");
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

            assertEquals("Given: \n" + formattedCode + "\nExpected: \n" + answerString, answerString.length(), formattedCode.length());

            assertEquals(answerString, formattedCode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Tests the removeInnerCodeOfBraces method given contents of a class.
     */
    @Test
    public void testRemoveInnerCodeOfBraces2() {
        try {
            String answerString = Files.readString(answerRemoveInnerCodeOfBraces2.toPath());

            String code = Files.readString(innerHelperFunctions.toPath());

            String formattedCode = CodeStructure.removeInnerCodeOfBraces(code);

            assertEquals("Given: \n" + formattedCode + "\nExpected: \n" + answerString, answerString.length(), formattedCode.length());

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

            assertEquals("Given: \n" + formattedCode + "\nExpected: \n" + answerString, answerString.length(), formattedCode.length());

            assertEquals(answerString, formattedCode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Tests the removeInnerCodeOfBraces method given the contents of a class.
     */
    @Test
    public void testRemoveInnerCode2() {
        try {
            String answerString = Files.readString(answerRemoveInnerCode2.toPath());

            String code = Files.readString(innerHelperFunctions.toPath());

            String formattedCode = CodeStructure.removeInnerCode(code);

            assertEquals("Given: \n" + formattedCode + "\nExpected: \n" + answerString, answerString.length(), formattedCode.length());

            assertEquals(answerString, formattedCode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
