package com.theobfuscatorinator;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.theobfuscatorinator.codeInterpreter.CodeStructure;
import com.theobfuscatorinator.codeInterpreter.ImportStructure;

public class ImportStructureTest {
 
    private File import1;

    @Before
    public void setUp() {
        import1 = new File("./src/test/res/testfiles/ImportTests/ImportTest1.txt");
    }

    /**
     * Test to find if the proper imports are identified from the import1 file.
     */
    @Test
    public void importIdentificationTest1() {
        try {
            CodeStructure codeStructure = new CodeStructure(import1);

            ArrayList<ImportStructure> imports = ImportStructure.identifyImports(codeStructure);

            ArrayList<ImportStructure> actualImports = new ArrayList<>();
            actualImports.add(new ImportStructure(false, "java.util.ArrayList"));

            assertEquals(actualImports.size(), imports.size());

            int found = 0;

            for (int i = 0; i < actualImports.size(); i++) {
                for (int j = 0; j < imports.size(); j++) {
                    if (actualImports.get(i).equals(imports.get(j))) {
                        found++;
                        break;
                    }
                }
            }

            assertEquals(actualImports.size(), found);
        } 
        catch (IllegalArgumentException e) {
            e.printStackTrace();
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
