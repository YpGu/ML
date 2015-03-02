#!/bin/bash

echo 'Compiling...'
javac *.java
echo 'Compile done'

for i in `seq 1 15`; do
	echo $i
	java PolynomialRegression ../../data/sinData_Train.csv ../../data/sinData_Validation.csv $i
done

