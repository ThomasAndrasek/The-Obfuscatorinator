package com.theobfuscatorinator.codeInterpreter;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents an import statement in a Java file.
 * 
 * @author Thomas Andrasek
 */
public class ImportStructure {
    private boolean isStatic;
    private String[] packagePath;
    private boolean hasStar;

    /**
     * Constructs a structure to represent a Java import statement.
     * 
     * @param isStatic Whether the import is static.
     * @param packagePath The path of the import statement.
     */
    public ImportStructure(boolean isStatic, String packagePath) {
        this.isStatic = isStatic;
        this.hasStar = false;

        this.packagePath = packagePath.split("\\.");

        // Check if import utilizes the '*'
        if (this.packagePath[this.packagePath.length-1].equals("*")) {
            this.hasStar = true;
        }
    }

    /**
     * Identifies all imports in a Java file.
     * 
     * @param codeStructure The code structure that represents a Java file.
     * @return All of the identified import statements as structures.
     */
    public static ArrayList<ImportStructure> identifyImports(CodeStructure codeStructure) {
        ArrayList<ImportStructure> importStructures = new ArrayList<>();

        Pattern findImport = Pattern.compile("import[\\s]+(static)?[\\s]*([^\\s;]+){1}");
        Matcher importMatcher = findImport.matcher(codeStructure.getUnCommentedCode());

        while (importMatcher.find()) {
            boolean isStatic = false;
            String packagePath = importMatcher.group(2);;

            // Check if the import is static.
            if (importMatcher.group(1) != null) {
                isStatic = true;
            }

            importStructures.add(new ImportStructure(isStatic, packagePath));
        }

        return importStructures;
    }


    /**
     * Checks if the import is static. 
     * 
     * @return If the import is static. 
     */
    public boolean isStatic() {
        return this.isStatic;
    }

    /**
     * Get the package path of the import statement.
     * 
     * @return The package path of the import statement.
     */
    public String[] getPackagePath() {
        String[] path = new String[this.packagePath.length];
        for (int i = 0; i < path.length; i++) {
            path[i] = this.packagePath[i];
        }

        return path;
    }

    /**
     * Checks if the import statement utilizes the '*'
     * 
     * @return If the import statement utilizes the '*'
     */
    public boolean hasStar() {
        return this.hasStar;
    }

    @Override
    public String toString() {
        String ret = "import ";

        if (this.isStatic) {
            ret += "static ";
        }

        for (int i = 0; i < this.packagePath.length - 1; i++) {
            ret += this.packagePath[i] + ".";
        }

        ret += this.packagePath[this.packagePath.length-1];

        ret += ";";

        return ret;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof ImportStructure)) {
            return false;
        }

        ImportStructure other = (ImportStructure) o;

        if (this.isStatic != other.isStatic) {
            return false;
        }

        if (this.hasStar != other.hasStar) {
            return false;
        }

        if (this.packagePath.length != other.packagePath.length) {
            return false;
        }

        // Check if the package paths are equal.
        for (int i = 0; i < this.packagePath.length; i++) {
            // If one part of the path is not equal then they are not equal.
            if (!(this.packagePath[i].equals(other.packagePath[i]))) {
                return false;
            }
        }

        return true;
    }
}
