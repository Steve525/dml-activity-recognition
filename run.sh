#!/bin/bash
OUTPUT_DIR="bin"
LIB="lib"
CLASS_PATH="$OUTPUT_DIR:$LIB"
SOURCE="src"
EXEC_SOURCE="src/feature_extractor/Main.java"
EXEC="feature_extractor.Main"
echo "Compiling..."
javac -verbose -sourcepath "$SOURCE" -classpath "$CLASS_PATH" "$EXEC_SOURCE" -d "$OUTPUT_DIR"
echo "Running $EXEC ..."
java -classpath "$CLASS_PATH" "$EXEC"