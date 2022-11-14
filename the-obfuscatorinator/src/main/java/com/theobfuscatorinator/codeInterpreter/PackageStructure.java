package com.theobfuscatorinator.codeInterpreter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PackageStructure {

    private String packageId;

    public PackageStructure(String packageId) {
        this.packageId = packageId;
    }
    
    public static PackageStructure identifyPackage(CodeStructure codeStructure) {
        Pattern findPackage = Pattern.compile("package[\\s]+([^;\\s]+)");
        Matcher packageMatcher = findPackage.matcher(codeStructure.getUnCommentedCode());
        
        while (packageMatcher.find()) {
            if (packageMatcher.group(1) != null) {
                return new PackageStructure(packageMatcher.group(1));
            }
        }

        return null;
    }

    public String getPackageId() {
        return this.packageId.substring(0);
    }
}
