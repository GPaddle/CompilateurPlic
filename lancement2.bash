#!/bin/bash

echo valide :

for file in ./src/sources/valide/*; do
	echo "$file"
	java -jar "$1" "$file"
	echo
done

echo 
echo invalides :
echo

for file in ./src/sources/invalide/*; do
	echo "$file"
	java -jar "$1" "$file"
	echo
done
