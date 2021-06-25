# SPAN Digital: ranking-challenge

Submission for a SPAN Digital challenge.

## Overview

* [Introduction](#introduction)
    * [Input/output](#input/output)
    * [The rules](#the-rules)
    * [Implementation](#implementation)
    * [Platform support](#platform-support)
    * [Sample Input](#sample-input)
    * [Expected Output](#expected-output)
* [Running Application](#running-application)
    * [From IDE (Eclipse)]
    * [From Commandline](#from-commandline)

## Introduction

This a command-line application that will calculate the ranking table for a league.

### Input/output

The input and output will be text files. We will pass an input file name on the command line, and the league table will
be saved to a new file named `league-table.txt`. The input contains results of games, one per line.
See [Sample Input](#sample-input) for details. The output is ordered from most to the least points, following the format
specified in [Expected Output](#expected-output). There is no special handling for malformed input files.

### The rules

In this league, a draw (tie) is worth 1 point, and a win is worth 3 points. A loss is worth 0 points. If two or more
teams have the same number of points, they should have the same rank and be printed in alphabetical order (as in the tie
for 3rd place in the sample data).

### Implementation

This is implemented in Java 8 and dependency management done by Maven. During development, JUnit tests were used to
validate the code.

### Platform support

Platform-agnostic constructs have been used where possible (line-endings and file-path-separators are two problematic
areas).

### Sample Input

```
Lions 3, Snakes 3
Tarantulas 1, FC Awesome 0
Lions 1, FC Awesome 1
Tarantulas 3, Snakes 1
Lions 4, Grouches 0
```

### Expected Output

```
1. Tarantulas, 6 pts
2. Lions, 5 pts
3. FC Awesome, 1 pt
3. Snakes, 1 pt
5. Grouches, 0 pts
```

## Running Application

- Download the zip or clone the Git repository.
- Unzip the zip file (if you downloaded one)
- Open Command Prompt and Change directory (cd) to folder containing `pom.xml`
- Run `mvn` with the goals `clean install`

### From IDE (Eclipse)

- Open Eclipse
    - File -> Import -> Existing Maven Project -> Navigate to the folder where you unzipped the zip
    - Select the right project
- Search for `com.spandigital.codechallenge.ranking.App` (which is the entry point class) and right-click on the file to
  Run as Java Application
    - Make sure you add the arguments `scores.txt` otherwise the app will throw an error.

### From Commandline

If you run Maven builds from your IDE or Command Prompt, an executable jar is created in the target folder and will be
named something like (ranking-challenge-0.0.1-SNAPSHOT-jar-with-dependencies.jar)

- Copy the jar to the folder with your scores.txt file.
    - Use `java -jar ranking-challenge-0.0.1-SNAPSHOT-jar-with-dependencies.jar scores.txt` to calculate the rankings
      from your input file.
