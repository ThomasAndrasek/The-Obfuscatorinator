# User Manual

## Installation Pre-Requirements 

The Obfuscatorinator is written in Java and is strictly for Java projects. Therefore, Java must be installed in order to run The Obfuscatorinator.
To install Java 11, please follow this link https://www.oracle.com/java/technologies/downloads/. 

In addition to Java, this project requires Maven to be installed to build the Java projects.
Please download Maven 3.8 using this link https://maven.apache.org/download.cgi.

This project relies on having a Java file to obfuscate. Please ensure that you have a java file or
project that you would like to obfuscate.

## Installing The Obfuscatorinator

Run the following step in a terminal or command prompt window:

1.  ` git clone https://github.com/ThomasAndrasek/The-Obfuscatorinator `

## Running The Obfuscatorinator

Run the following steps in a terminal or command prompt window:

1. `cd the-obfuscatorinator`
2. `mvn clean install`
3. `mvn package`
4. `java -cp "./target/the-obfuscatorinator-1.0.0.jar" com.theobfuscatorinator.App <inputFile1 inputFile2...> [options]`
5. For optional arguments:
- --target [directory] | Specify the name of the directory to place the output. Directory will be created. Default is "output".
- --filelinecount [lines] | Specify how many lines each file should be. Default is 1 if newlines are being removed.
- --unicodeFreq [PercentCharacters] | Specify what percentage of characters should be replaced with their unicode literal.
- --nomethodrenames | Leave all methods with their original names
- --nofakecode | Do not add dummy code to the project
- --nounicode | Do not replace any characters with unicode literals
- --keepSpaces | Do not remove any spaces or tabs from the project
- --keepNewlines | Do not remove any newlines from the file.
