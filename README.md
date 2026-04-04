# Java Quiz Application

Interactive desktop quiz system built with Java Swing.

This application includes:
- A modern user interface
- 3 quiz stages (Math, Logical Reasoning, Abstract Thinking)
- Timed questions
- Score tracking with per-stage breakdown

## Who This Is For

This guide is written for clients and end users who want to run the app locally.

## Requirements

- Java JDK 8 or newer installed
- Windows, macOS, or Linux

To check Java installation:

```bash
java -version
javac -version
```

## Project Location

Main source file:

- PS1-IE-QUIZSYSTEM/quix/src/QuizApp.java

## How To Run

1. Open a terminal in the repository root.
2. Move to the source folder.
3. Compile the application.
4. Run the application.

```bash
cd PS1-IE-QUIZSYSTEM/quix/src
javac QuizApp.java
java QuizApp
```

## How To Use The App

1. Click Start on the welcome screen.
2. Review the rules and click Begin Test.
3. Answer each question before the timer expires.
4. Click Next (or Finish on the last question).
5. View final score and stage-by-stage results.

## Scoring

- Stage 1: 5 questions, 1 point each (max 5)
- Stage 2: 5 questions, 2 points each (max 10)
- Stage 3: 5 questions, 3 points each (max 15)
- Total maximum score: 30

## Timer Behavior

- Each question has a countdown timer.
- If time runs out, the app shows the correct answer and moves to the next question.

## Troubleshooting

If `java` or `javac` is not recognized:
- Install a Java JDK.
- Reopen terminal after installation.
- Ensure Java is added to PATH.

If the app does not open after running `java QuizApp`:
- Recompile with `javac QuizApp.java`.
- Make sure you are in PS1-IE-QUIZSYSTEM/quix/src.

## Notes

- Questions and choices are shuffled each run.
- Internet connection is not required.
- This is a local desktop app (no server setup needed).

