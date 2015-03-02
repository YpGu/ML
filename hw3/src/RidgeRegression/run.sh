#!/bin/bash

echo 'Compiling...'
javac *.java
echo 'Compile done'

for i in `seq 0 50`;
do
#	java RidgeRegression ../../data/sinData_Train.csv $i 5
	java RidgeRegression ../../data/sinData_Train.csv $i 9
done
