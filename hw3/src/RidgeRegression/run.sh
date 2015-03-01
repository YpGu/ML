#!/bin/bash

echo 'Compiling...'
javac *.java
echo 'Compile done'

for i in `seq 0 50`;
do
#	let j=`expr $i+10`
#	echo $j
	java RidgeRegression ../../data/sinData_Train.csv $i 5
done
