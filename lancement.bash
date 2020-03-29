#!/bin/bash

input="/mnt/d/Scolarité/2019-2020_DUT_Informatique_2A/S4/Compilation/Eclipse"

if [ $# -ne 1 ]
then
	echo usage $0 '<file.jar>'
	exit 1
fi

echo valide :

for file in $input/sources/valide/*.plic; do
	echo "$file"
	java -jar "$1" "$file"
	echo ----------------------
	echo
	echo
done

echo 
echo invalides :
echo

for file in $input/sources/invalide/*.plic; do
	echo "$file"
	java -jar "$1" "$file"
	echo ----------------------
done
