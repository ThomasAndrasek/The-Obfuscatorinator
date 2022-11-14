package com.theobfuscatorinator.codeInterpreter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A structure class to represent a package in Java.
 * 
 * @author Thomas Andrasek
 * @version 1.0.0
 */
public class PackageStructure {

    private String packageId;

    /**
     * Constructs a PackageStructure
     * 
     * @param packageId The id of the package.
     */
    public PackageStructure(String packageId) {
        this.packageId = packageId;
    }
    
    /**
     * Identifies the package inside of a Java file.
     * 
     * Given a CodeStructure that represents a Java file this method will find the package declared
     * in the Java file if it exists.
     * 
     * @param codeStructure The CodeStructure that represents a Java file.
     * @return If the CodeStructure declares a package a PackageStructure will be returned, null
     *         otherwise.
     */
    public static PackageStructure identifyPackage(CodeStructure codeStructure) {
        // Construct regex to find the package.
        Pattern findPackage = Pattern.compile("package[\\s]+([^;\\s]+)");
        Matcher packageMatcher = findPackage.matcher(codeStructure.getUnCommentedCode());
        
        // Find the first package declaration in the Java file.
        while (packageMatcher.find()) {
            if (packageMatcher.group(1) != null) {
                return new PackageStructure(packageMatcher.group(1));
            }
        }

        return null;
    }

    /**
     * Get the Id of the package.
     * 
     * @return Id of the package.
     */
    public String getPackageId() {
        return this.packageId;
    }
}
