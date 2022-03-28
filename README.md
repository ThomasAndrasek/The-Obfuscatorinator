# The-Obfuscatorinator

## Overview

The Obfuscatorinator is a Java Obfuscator that reads in a Java project, copies it, constructs a relevant graph, and obfuscates it. Ultimately, it will remove any unused code, comments, and whitespace and will add dummy comments and dummy code. All methods, class names, and variables shall be replaced and strings and variables shall be encrypted. The code will be reordered and spaghettified and opaque predicates will be inserted. 

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
First change to the route directory.

    cd the-obfuscatorinator

To clean install:

    mvn clean install

To build

    mvn package

To run

    java -cp "./target/the-obfuscatorinator-1.0.0.jar" com.theobfuscatorinator.App
    
## Code of Conduct

Find our code of conduct at: https://github.com/ThomasAndrasek/The-Obfuscatorinator/blob/main/CODE_OF_CONDUCT.md

## License

MIT License
