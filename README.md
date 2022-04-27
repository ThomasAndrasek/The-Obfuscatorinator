# The-Obfuscatorinator

## Overview

The Obfuscatorinator is a Java Obfuscator that reads in a Java project, copies it, constructs a relevant graph, and obfuscates it. Ultimately, it will remove any unused code, comments, and whitespace and will add dummy comments and dummy code. All methods, class names, and variables shall be replaced and strings and variables shall be encrypted. The code will be reordered and spaghettified and opaque predicates will be inserted. 

Learn more at our website: https://theobfuscatorinator.wordpress.com/

## Team
| **Name** | **GitHub Handle** | **Email** |
|:------:|:-------:|:------:|
| Thomas Andrasek | ThomasAndrasek | andrat@rpi.edu | 
| Mindy Yip | mindyyip | mindyyip81@gmail.com | 
| Nathan Whitney | nathanWhitney | nmwhitney.cs@gmail.com | 
| Carter Del Ciello | CDelc | delcic@rpi.edu |

## Tools

This project is written in Java and will use JUnit testing.

## Build and Running

### Minimum Requirements
- Java 11+
- Maven 3.8+

First change to the route directory.

    cd the-obfuscatorinator

To clean install:

    mvn clean install

To build

    mvn package

To run

    java -cp "./target/the-obfuscatorinator-1.0.0.jar" com.theobfuscatorinator.App <inputFile1 inputFile2...> [options]

## Options

- --target [directory] | Specify the name of the directory to place the output. Directory will be created. Default is "output".
- --filelinecount [lines] | Specify how many lines each file should be. Default is 1 if newlines are being removed.
- --unicodeFreq [PercentCharacters] | Specify what percentage of characters should be replaced with their unicode literal.
- --nomethodrenames | Leave all methods with their original names
- --nofakecode | Do not add dummy code to the project
- --nounicode | Do not replace any characters with unicode literals
- --keepSpaces | Do not remove any spaces or tabs from the project
- --keepNewlines | Do not remove any newlines from the file.


## Code of Conduct

Find our code of conduct at: https://github.com/ThomasAndrasek/The-Obfuscatorinator/blob/main/CODE_OF_CONDUCT.md

## License

MIT License
