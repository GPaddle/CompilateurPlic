#!/bin/bash

input='/mnt/d/Scolarité/2019-2020_DUT_Informatique_2A/S4/Compilation/Eclipse/sources/lecture'

tmp1=$input/tmp1.asm
tmp2=$input/tmp2.txt
tmp3=$input/tmp3.txt
tmp4=$input/input.txt

liste="ls $input/*.plic -1"

echo "Tests de lecture : " $($liste | wc -l)

for file in $($liste)
do
	printf "_"
done

echo

for file in $($liste)
do

	java -jar $input/../../plic.jar $file > $tmp1
	java -jar $input/../../Mars4_5.jar $tmp1 < $tmp4 > $tmp2

	tail +2 $tmp2 > $tmp3

	if ! diff $tmp3 $file.attente >& /dev/null
	then
		echo
		echo "-------------------------------------------"
		echo "\nPas encore bon : "$file
		diff $tmp3 $file.attente
		echo "-------------------------------------------"
		echo
	else
		printf "#"
	fi

done
echo 
rm $tmp1
rm $tmp2
rm $tmp3
