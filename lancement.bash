#!/bin/bash

if [ $# -ne 1 ]
then
	echo usage $0 '<file.jar>'
	exit 1
fi

echo valide :

for file in ./sources/valide/*; do
	echo "$file"
	java -jar "$1" "$file"
	echo ----------------------
	echo
	echo
done

echo 
echo invalides :
echo

for file in ./sources/invalide/*; do
	echo "$file"
	java -jar "$1" "$file"
	echo ----------------------
done
