#!/bin/bash

echo 'Compiling...'
javac *.java
echo 'Compile done'

java PolynomialRegression ../../data/yachtData.csv 7

