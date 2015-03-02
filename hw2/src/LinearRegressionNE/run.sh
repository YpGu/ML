#!/bin/sh

echo "Compiling..."
javac *.java
echo "Finished."

for i in `seq 0 9`; do
	echo $i
#	java LinearRegressionNE ../../data/housing.csv $i
#	java LinearRegressionNE ../../data/yachtData.csv $i
	java LinearRegressionNE ../../data/concreteData.csv $i
done
