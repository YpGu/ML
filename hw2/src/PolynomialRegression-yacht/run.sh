#!/bin/bash

echo 'Compiling...'
javac *.java
echo 'Compile done'

for i in `seq 1 7`; do
	echo $i
	java PolynomialRegression ../../data/yachtData.csv $i
done

