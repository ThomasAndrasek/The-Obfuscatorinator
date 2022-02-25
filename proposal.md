# Proposal

[The Obfuscatorinator Github](https://github.com/ThomasAndrasek/The-Obfuscatorinator)

## Overview

This project is a Java Obfuscator called The Obfuscatorinator. The main goal of this project is to read in a Java Project, copy it, construct a graph based on the project, and obfuscate it. For the graph aspect of the project, the nodes will be classes, and edges will link one class to another class. The obfuscation part of the project is the most essential. Within the source code, the comments will be removed and dummy comments will be added. Additionally, any whitespace will be removed and code will be condensed. Variables, classes, methods, and interfaces will be renamed with randomized strings and incremental variable names. For example, class A might have method aA, bA, cA, aaA, and so on. Dummy code will be inserted, but this dummy code will not affect any outcome or logic of the original code. Also, strings and other variables will be encrypted at compile time but decrypted for runtime. The Obfuscatorinator will also look for any unused code and the potential to reorder code so that logic will remain unaffected, and there will also be opaque predicate assertion. Possibly, there will be a GUI for this project and it should be runnable by command line and compatible with Maven.

## Semester Plan

The plan is to set up the Java Project using eclipse and get the project to be able to read in. The overall plan is to create a program that can create graphs and obfuscate it. If time permits, then a GUI will be created as well.

## Technology

This project will primarily use Java due to code being obfuscated also being written in **Java**. This will make it easier understand  the obfuscated code beforehand as we would already be working in that language.

## Team
| **Name** | **GitHub Handle** | **Email** |
|:------:|:-------:|:------:|
| Thomas Andrasek | ThomasAndrasek | andrat@rpi.edu | 
| Mindy Yip | mindyyip | mindyyip81@gmail.com | 
| Nathan Whitney | nathanWhitney | nmwhitney.cs@gmail.com | 
| Carter Del Ciello | CDelc | delcic@rpi.edu |


## Milestones

- By Week 1 : Set up the project and make it buildable with maven. Get everyone on the team set up on the working project and decide the weekly organization.
- By Week 2 : Read in Java Projects and copy and duplicate them to the desired directory. Create a "project" that can be used for testing.
- By Week 3 : Build a graph of the project
- By Week 4 : Remove all unused code, comments, and whitespace and add dummy comments.
- By Week 5 : Rename all variables, methods, classes, and etc
- By Week 6: Add the string and variable encryption feature
- By Week 7 : Insert dummy code
- By Week 8 : Reorder and spaghettify the code
- By Week 9: Insert Opaque Predicate
- By Week 10 : 
