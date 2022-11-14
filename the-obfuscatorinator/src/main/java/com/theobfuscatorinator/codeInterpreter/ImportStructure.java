package com.theobfuscatorinator.codeInterpreter;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImportStructure {
    private boolean isStatic;
    private String[] packagePath;
    private boolean hasStar;

    public ImportStructure(boolean isStatic, String packagePath) {
        this.isStatic = isStatic;
        this.hasStar = false;

        this.packagePath = packagePath.split("\\.");

        if (this.packagePath[this.packagePath.length-1].equals("*")) {
            this.hasStar = true;
        }
    }

    public static ArrayList<ImportStructure> identifyImports(CodeStructure codeStructure) {
        ArrayList<ImportStructure> importStructures = new ArrayList<>();

        Pattern findImport = Pattern.compile("import[\\s]+(static)?[\\s]*([^\\s;]+){1}");
        Matcher importMatcher = findImport.matcher(codeStructure.getUnCommentedCode());

        while (importMatcher.find()) {
            boolean isStatic = false;
            String packagePath = importMatcher.group(2);;

            if (importMatcher.group(1) != null) {
                isStatic = true;
            }

            importStructures.add(new ImportStructure(isStatic, packagePath));
        }

        return importStructures;
    }

    public boolean isStatic() {
        return this.isStatic;
    }

    public String[] getPackagePath() {
        String[] path = new String[this.packagePath.length];
        for (int i = 0; i < path.length; i++) {
            path[i] = this.packagePath[i];
        }

        return path;
    }

    public boolean hasStar() {
        return this.hasStar;
    }

    @Override
    public String toString() {
        String ret = "";

        if (this.isStatic) {
            ret += "static ";
        }

        for (int i = 0; i < this.packagePath.length - 1; i++) {
            ret += this.packagePath[i] + ".";
        }

        ret += this.packagePath[this.packagePath.length-1];

        return ret;
    }
}
