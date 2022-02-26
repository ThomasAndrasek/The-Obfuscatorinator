# Proposal

[The Obfuscatorinator Github](https://github.com/ThomasAndrasek/The-Obfuscatorinator)

## Overview

This project is a Java Obfuscator called The Obfuscatorinator. The main goal of this project is to read in a Java project, copy it, construct a graph based on the project, and obfuscate it. For the graph aspect of the project, the nodes will be classes, and edges will link one class to another class. An edge will be created when one class references another class in some way. The obfuscation part of the project is the most essential and is thus the primary objective of this project. Within the source code, the comments will be removed and dummy comments will be added. Additionally, any whitespace will be removed and code will be condensed. Variables, classes, methods, and interfaces will be renamed with randomized strings and incremental variable names. For example, class A might have method aA, bA, cA, aaA, and so on. Dummy code will be inserted, but this dummy code will not affect the outcome or logic of the original code. Also, hardcoded strings and other variables will be encrypted at compile time but decrypted for runtime. The Obfuscatorinator will also look for any unused code and the potential to reorder code so that logic will remain unaffected, and there will also be opaque predicate assertion. Depending on whether time allows, there will be a GUI for the obfuscator and it should be runnable by command line and compatible with Maven.

## Semester Plan

With the amount of obfuscation being variable the "end point" of the project doesn't need to be set in stone. The plan for this semester is to finish the first 7 or so levels of obfuscation and have them be able to be applied to any Java program. Another goal is for the program to be able to create graphs of the Java projects that are imported into the program. We want the first usage of the program to be through command line or terminal commands. If time permits, then a GUI will be created as another option to run the program.

## Technology

This project will primarily use Java due to code being obfuscated also being written in **Java**. This will make it easier understand  the obfuscated code beforehand as we would already be working in that language. Overall the plan is to set up the Java project using eclipse.

## Graph Construction of Java Projects

A graph of a Java project will represent the dependence of a Class on another Class. Whether that be methods, constructors, variables, interfaces, etc. A node or vertex in the graph will be a Class. Edges in the graph will be the dependence of a Class on another Class. Edges will be directional, going from one Class to another but not necessarily vice versa. The edge will also contain the data pertaining to the dependence. So if Class A uses method getID() from class B, then in the graph there will be an edge going from Class A to Class B with the data "Method, getID()."

## Team
| **Name** | **GitHub Handle** | **Email** |
|:------:|:-------:|:------:|
| Thomas Andrasek | ThomasAndrasek | andrat@rpi.edu | 
| Mindy Yip | mindyyip | mindyyip81@gmail.com | 
| Nathan Whitney | nathanWhitney | nmwhitney.cs@gmail.com | 
| Carter Del Ciello | CDelc | delcic@rpi.edu |


## Milestones

To be done by the week number.

- By Week 1 : Set up the project and make it buildable with maven. Get everyone on the team set up on the working project and decide the weekly organization.
- By Week 2 : Read in Java Projects and copy and duplicate them to the desired directory. Create a "project" that can be used for testing.
- By Week 3 : Be able to construct a graph of an inputted Java project from a user. 
- By Week 4 : Remove all unused code, comments, and whitespace and add dummy comments.
- By Week 5 : Rename all variables, methods, classes, and etc.
- By Week 6: Add the string and variable encryption feature.
- By Week 7 : Insert dummy code.
- By Week 8 : Reorder and spaghettify the code.
- By Week 9: Insert Opaque Predicate.
- By Week 10 : Add finishing touches / finish any unfinished objectives.
