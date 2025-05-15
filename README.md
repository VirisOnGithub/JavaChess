# JavaChess

As the name suggests, this is a chess game written in Java.

## Download

### From source

Simply clone the repo and run:

```bash
# Windows
.\gradlew.bat run
# Linux
./gradlew run
```

### From release

You can also only download the release (`.jar` file):

```bash
# Windows & Linux
java -jar app-all.jar
```

## Documentation

### From source

All the documentation can be generated from Gradle:

```bash
# Windows
.\gradlew.bat javadoc
# Linux
./gradlew javadoc
```

Then open the `build/docs/javadoc/index.html` file in your browser.

### Already generated

The latest version of this javadoc is also online [here](https://javachess.clement-reniers.fr).

## Sample problems

- Mate in 1
  - FEN : `r1b5/3n3R/2p2p2/p4P2/qp1P4/1k2P3/5PQ1/1NK5 w - - 0 33`
  - Solution : 1. Qg8# 1-0

- Mate in 1
  - FEN : `8/pN4bP/B3Nr1P/3rkq1R/1n6/Pp1nKQ1p/1Pp1RBb1/8 w - - 0 1`
  - Solution : 1. Kg2# 1-0

- Mate in 2
  - FEN : `5r1k/6pp/7N/3Q4/8/8/6PP/6K1 w - - 0 1`
  - Solution : 1. Qg8+ Rxg8 2. Nf7# 1-0

- Mate in 1 by promotion
  - FEN : `7k/Q3P3/8/8/8/8/8/7K w - - 0 1`
  - Solution : 1. e8=R# 1-0

- Mate in 1 by Knight promotion
  - FEN : `6b1/5P1k/8/4B1K1/8/8/8/8 w - - 0 1`
  - Solution : 1. f8=N# 1-0