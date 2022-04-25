package com.theobfuscatorinator.codeInterpreter;

import java.util.*;

public class WhitespaceHandler {

    public static void newlineClearAndInsert(ArrayList<CodeStructure> codeStructures,
                                             int numNewlines){
        clearNewlines(codeStructures);
        insertNewlines(codeStructures, numNewlines);
    }

    private static void clearNewlines(ArrayList<CodeStructure> codeStructures){
        for(CodeStructure struct : codeStructures){
            String code = struct.getUnCommentedCode();
            String output = "";
            for(String segment : code.split("[\\n\\r]+")) {
                output += segment;
            }
            struct.setUnCommentedCode(output);
        }
    }

    private static void insertNewlines(ArrayList<CodeStructure> codeStructures, int count){
        if(count < 0) count *= -1;
        count--;
        char[] safechars = {';', '(', ')', '{', '}', ',',
                ' ', '\t', '[', ']', '=', '+', '-', '*',
                '/', '%', '^', '&', '&', '|', '!', '.'};
        HashSet<Character> safeCharacters = new HashSet<Character>();
        ArrayList<Integer> indices = new ArrayList<Integer>();
        for(char c : safechars) safeCharacters.add(c);
        Random rand = new Random();

        for(CodeStructure struct : codeStructures){
            indices.clear();
            String code = struct.getUnCommentedCode();
            //Find potential places to insert a newline
            for(int i = 0; i < code.length(); i++){
                char c = code.charAt(i);
                if(safeCharacters.contains(c)){
                    if(c == '=' || c == '+' || c == '-' || c == '*' || c == '/'
                            || c == '&' || c == '|' || c == '%'){
                        if(i > 0 && safeCharacters.contains(code.charAt(i - 1))){
                            indices.remove((Integer) i);
                        }
                        indices.add(i + 1);
                    }
                    else{
                        indices.add(i);
                        indices.add(i + 1);
                    }
                }
            }
            //Insert the newlines at random locations
            TreeSet<Integer> randomSelect = new TreeSet<Integer>();
            for(int i = 0; i < count; i++){
                int tmp = indices.get(rand.nextInt(indices.size()));
                randomSelect.add(tmp);
            }
            //Do this to ensure no indices are offset while we enter the newlines
            Iterator<Integer> itr = randomSelect.descendingIterator();
            while(itr.hasNext()){
                code = insertChar(code, '\n', itr.next());
            }
            struct.setUnCommentedCode(code);
        }
    }

    public static void removeSpaces(ArrayList<CodeStructure> codeStructures){
        char[] separators = {'=', '+', '-', '*', '/', '%', '!', '|', '&',
                '(', ')', '{', '}', '[', ']', ';','\n', '\r', ' ', '\t'};
        HashSet<Character> separatorSet= new HashSet<Character>();
        for(char c : separators) separatorSet.add(c);

        for(CodeStructure struct : codeStructures){
            String code = struct.getUnCommentedCode();
            for(int i = 0; i < code.length(); i++){
                char c = code.charAt(i);
                while((c == ' ' || c == '\t') &&
                        (separatorSet.contains(code.charAt(i + 1)) ||
                        (i > 0 && separatorSet.contains(code.charAt(i - 1))))){
                    code = removeChar(code, i);
                    c = code.charAt(i);
                }
            }
            struct.setUnCommentedCode(code);
        }
    }


    private static String insertChar(String s, char c, int index){
        String tmp = s.substring(0, index);
        tmp += c;
        tmp += s.substring(index);
        return tmp;
    }

    private static String removeChar(String s, int index){
        return s.substring(0, index).concat(s.substring(index + 1));
    }
}


