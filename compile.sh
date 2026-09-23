#!/bin/bash
mkdir -p out
javac -cp "lib/*" -d out $(find src -name "*.java")
echo "Compilation complete."
