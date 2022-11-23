package com.theobfuscatorinator.codeInterpreter;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Intances of this class will be created by CodeStructure. These instances are used to represent 
 * java classes and contain information about them.
 * @author Carter Del Ciello
 */
public class ClassStructure {

    private String scope;
    private String name;
    private String[] generics;
    private String extended;
    private String[] implemented;

    private String code;

    public ClassStructure(String classHeader, String fullCode){
        this.code = fullCode;

        classHeader = classHeader.trim();

        Pattern classFinder = Pattern.compile("(public|private|protected)?[\\s]*class[\\s]*([^\\s<]+){1}");
        Matcher classMatcher = classFinder.matcher(classHeader);

        this.scope = "";
        this.name = "";

        if (classMatcher.find()) {
            if (classMatcher.group(1) != null) {
                this.scope = classMatcher.group(1);
            }

            this.name = classMatcher.group(2);

            classHeader= classHeader.substring(classMatcher.end());
        }

        classHeader = classHeader.trim();

        this.generics = new String[0];
        if (classHeader.indexOf('<') == 0) {
            String genericList = classHeader.substring(1, classHeader.indexOf('>'));
            ArrayList<String> tempGenericList = new ArrayList<>();
            while (genericList.indexOf(',') != -1 && genericList.indexOf(',') < genericList.indexOf('>')) {
                String generic = genericList.substring(0, genericList.indexOf(',')).trim();
                tempGenericList.add(generic.trim());
                genericList = genericList.substring(genericList.indexOf(',') + 1).trim();
            }

            String generic = genericList.substring(0, genericList.indexOf('>'));
            tempGenericList.add(generic.trim());
            this.generics = (String[]) tempGenericList.toArray();
            classHeader = classHeader.substring(classHeader.indexOf('>') + 1);
            classHeader = classHeader.trim();
        }

        this.extended = "";
        if (classHeader.indexOf("extends") == 0) {
            classHeader = classHeader.substring(7).trim();
            
            Pattern namePattern = Pattern.compile("[^\\s]+");
            Matcher extendsNameMatcher = namePattern.matcher(classHeader);

            if (extendsNameMatcher.find()) {
                this.extended = extendsNameMatcher.group(0);
            }

            classHeader = classHeader.substring(extendsNameMatcher.end()).trim();
        }

        this.implemented = new String[0];
        if (classHeader.indexOf("implements") == 0) {
            classHeader = classHeader.substring(10).trim();
            ArrayList<String> tempImplementList = new ArrayList<>();
            while (classHeader.indexOf(',') != -1) {
                tempImplementList.add(classHeader.substring(0, classHeader.indexOf(',')).trim());
                classHeader = classHeader.substring(classHeader.indexOf(',') + 1).trim();
            }
            tempImplementList.add(classHeader.trim());
            this.implemented = (String[]) tempImplementList.toArray();
        }
    }

    /**
     *
     * @return Name of this class
     */
    public String getName(){
        return name;
    }

    public static ArrayList<ClassStructure> identifyClasses(CodeStructure codeStructure) {
        ArrayList<ClassStructure> classes = new ArrayList<>();

        Pattern classFinder = Pattern.compile("(public|private|protected)?[\\s]*class[\\s]*([^\\s<]+){1}");
        String code = CodeStructure.removeInnerCodeOfBraces(codeStructure.getUnCommentedCode());
        Matcher classMatcher = classFinder.matcher(code);

        while (classMatcher.find()) {
            int start = classMatcher.start(0);
            int end = start;
            while (code.charAt(end) != '{') {
                end++;
            }
            String classHeader = code.substring(start, end).trim();
            
            Pattern actualClassFinder = Pattern.compile(classHeader);
            Matcher actualClassMatcher = actualClassFinder.matcher(codeStructure.getUnCommentedCode());

            while (actualClassMatcher.find()) {
                int actualEnd = actualClassMatcher.end();
                String innerCode = codeStructure.getUnCommentedCode().substring(actualClassMatcher.start(), CodeStructure.getCodeBetweenBrackets(codeStructure.getUnCommentedCode(), actualEnd, '{', '}').second + 1);
                
                classes.add(new ClassStructure(classHeader, code));
            }
        }

        return classes;
    }

    /**
     *
     * @return ArrayList of ClassStructures that represents classes found in the code.
     */
    // private ArrayList<ClassStructure> identifyClasses() {
    //     String code = sourceCode;
    //     ArrayList<ClassStructure> classes = new ArrayList<>();
    //     Pattern classFinder = Pattern.compile("\\s+class\\s+");
    //     Matcher classMatcher = classFinder.matcher(code);
    //     int index = 0;
    //     while(classMatcher.find(index)){
    //         int classStart = classMatcher.end();
    //         int i = classStart;
    //         while (i < code.length() && code.charAt(i) != '{') {
    //             i++;
    //         }
    //         String className = code.substring(classStart, i);

    //         String full = className.substring(0);

    //         ArrayList<String> templates = new ArrayList<String>();
    //         //Handle template arguments
    //         if(className.contains("<")){
    //             className = code.substring(classStart, code
    //                     .indexOf("<", classStart)).trim();
    //             CodeStructure.Pair<String, Integer> templateContents =
    //                      CodeStructure.getCodeBetweenBrackets(code, classStart, '<', '>');
    //             String arguments = templateContents.first;
    //             templates = CodeStructure.getCommaSeparatedValues(arguments);
    //         }

    //         CodeStructure.Pair<String, Integer> currentClass =
    //              CodeStructure.getCodeBetweenBrackets(code, classStart, '{', '}');
    //         ArrayList<ClassStructure> newContainerList = new ArrayList<ClassStructure>(containers);
    //         newContainerList.add(this);
    //         int classEnd = className.indexOf(" ");
            
    //         if (classEnd == -1) {
    //             classEnd = className.length();
    //         }
    //         className = className.substring(0, classEnd);
    //         className = className.replaceAll("\\s+", "");
    //         boolean implement = full.indexOf("implements") != -1;
    //         String[] implementedClassesArray = new String[0];
    //         if (implement) {
    //             String implementedClasses = full.substring(full.indexOf("implements") + 10);
    //             implementedClasses.trim();
    //             implementedClassesArray = implementedClasses.split(",");
    //         }
    //         classes.add(new ClassStructure(currentClass.first, className, sourceFile, 
    //                                        newContainerList, templates, implement, implementedClassesArray));
    //         index = currentClass.second;
    //     }

    //     return classes;
    // }

    /**
     * Finds all methods defined in this class. Does not include methods defined in other classes 
     * nested within this class. This will not include constructors.
     * @return An arraylist of MethodStructures that represents every method that was found.
     */
    // private ArrayList<MethodStructure> identifyMethods(){
    //     String code = ignoreNestedClasses();
    //     ArrayList<MethodStructure> output = new ArrayList<MethodStructure>();
    //     // Regex adopted from: https://stackoverflow.com/a/16118844/5956948
    //     Pattern methodFinder = Pattern.compile("(?:(?:public|private|protected|static|final|native|synchronized|abstract|transient)+\\s+)+[$_\\w<>\\[\\]\\s]*\\s+[\\$_\\w]+\\([^\\)]*\\)?\\s*\\{?[^\\}]*\\}?");
    //     Matcher methodMatcher = methodFinder.matcher(code);
    //     int index = 0;
    //     while(methodMatcher.find(index)){
    //         String method = "";
    //         int i = 0;
    //         while (i <= methodMatcher.groupCount() && methodMatcher.group(i) != null){
    //             method += methodMatcher.group(i);
    //             i++;
    //         }

    //         String fullMethod = method.substring(0);

    //         method = method.replaceAll("\\s+", " ");

    //         if (method == "") {
    //             break;
    //         }

    //         int indexB = code.indexOf(fullMethod) - 1;

    //         String inbetween = "";
    //         while (indexB >= 0) {
    //             char c = code.charAt(indexB);
    //             if (c == '{' || c == '}' || c == ';') {
    //                 break;
    //             }
    //             inbetween = c + inbetween;
    //             indexB--;
    //         }

    //         boolean valid = true;
    //         if (inbetween.toString().contains("@Override")) {
    //             valid = false;
    //         }

    //         String scope = "";
    //         if (method.startsWith("public")) {
    //             scope = "public";
    //             method = method.substring(7);
    //         } else if (method.startsWith("private")) {
    //             scope = "private";
    //             method = method.substring(8);
    //         } else if (method.startsWith("protected")) {
    //             scope = "protected";
    //             method = method.substring(9);
    //         }

    //         String staticStatus = "";
    //         if (method.startsWith("static")) {
    //             staticStatus = "static";
    //             method = method.substring(7);
    //         }

    //         String name = "";
    //         if (method.contains("(")) {
    //             int indexC = method.indexOf("(");
    //             int extraSpace = 0;
    //             if (method.charAt(indexC - 1) == ' ') {
    //                 extraSpace = 1;
    //                 indexC -= 2;
    //             }
    //             while (indexC != -1 && method.charAt(indexC) != ' ') {
    //                 indexC--;
    //             }
    //             name = method.substring(indexC+1, method.indexOf("(") - extraSpace);
    //             method = method.substring(0, indexC+1) + method.substring(method.indexOf("("));
    //         }

    //         String arguments = "";
    //         if (method.contains("(")) {
    //             arguments = method.substring(method.indexOf("(") + 1, method.indexOf(")"));
    //             method = method.substring(0, method.indexOf("("));
    //         }

    //         String template = "";
    //         if (method.startsWith("<")) {
    //             template = method.substring(0, method.indexOf(">") + 1);
    //             method = method.substring(method.indexOf(">") + 2);
    //         }

    //         String returnType = method;

    //         CodeStructure.Pair<String, Integer> detectBody =
    //              CodeStructure.getCodeBetweenBrackets(code, methodMatcher.start(), '{','}');
    //         index = detectBody.second;

    //         if (!returnType.equals("") && valid) {
    //             output.add(
    //                 new MethodStructure(name, scope, staticStatus, template, arguments, 
    //                                     returnType, detectBody.first, containers, sourceFile));
    //         } 
    //     }

    //     return output;
    // }

    /**
     *
     * @return Returns a version of this classes source code with all bodies of nested classes 
     * removed. This is useful for making sure methods and variables that are added to this 
     * ClassStructure belong to this class scope and not that of a nested class.
     */
    // private String ignoreNestedClasses(){
    //     String out = "";
    //     Pattern classFinder = Pattern.compile("\\s+class\\s+");
    //     Matcher classMatcher = classFinder.matcher(sourceCode);
    //     int index = 0;
    //     while(classMatcher.find(index)){
    //         int bodyStart = sourceCode.indexOf('{', classMatcher.end());
    //         out += sourceCode.substring(index, bodyStart);
    //         CodeStructure.Pair<String, Integer> currentClass =
    //              CodeStructure.getCodeBetweenBrackets(sourceCode, classMatcher.end(), '{', '}');
    //         index = currentClass.second;
    //         if(index + 1 < sourceCode.length()) index++;
    //     }
    //     out += sourceCode.substring(index);

    //     return out;
    // }

    /**
     * Checks if this class, or any nested classes, contains a main method.
     * @return True if this class contains the main method or contains another class for which this
     *  function returns true.
     */
    // protected boolean containsMainMethod(){
    //     for(ClassStructure c : classes){
    //         if(c.containsMainMethod()) return true;
    //     }
    //     for(MethodStructure m : methods){
    //         if(m.methodName.equals("main")) return true;
    //     }
    //     return false;
    // }

    public void setName(String newName) {
        name = newName;
    }

    public ArrayList<MethodStructure> getMethods() {
        return null;
    }

    public ArrayList<ClassStructure> getClasses() {
        return null;
    }

    public String getCode() {
        return "";
    }
}
