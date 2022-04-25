# User Manual

## What Exactly is an Obfuscator?

An obfuscator is a tool that developers use to complicate their code while maintaining functionality.
Developers use an obfuscator because they want to protect their code from getting maliciously hacked or hijacked, which is done so by deeply burying the important code into a lot of scrambled code.
It should convert very straightforward source code into another file that still works the same way, but looks different.
In the end, the obfuscated code should be extremely hard to comprehend and figure out its purpose.

## Overview of The Obfuscatorinator

This project is an obfuscator specifically for java projects. It reads in a Java project, copies it, constructs a relevant graph, and obfuscates it.
The Obfuscatorinator goes through the following steps to obfuscate:
- Renames classes
- Renames methods
- Inserts dummy code
- Adds decryption methods 
- Replace characters with unicode literals
- Remove whitespace
- Remove newlines

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
