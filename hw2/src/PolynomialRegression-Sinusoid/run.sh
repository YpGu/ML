#!/bin/bash

echo 'Compiling...'
javac *.java
echo 'Compile done'

java PolynomialRegression ../../data/sinData_Train.csv ../../data/sinData_Validation.csv 15

