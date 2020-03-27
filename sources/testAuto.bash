#!/bin/bash

input='/mnt/d/Scolarité/2019-2020_DUT_Informatique_2A/S4/Compilation/Eclipse'

tmp1=$input/sources/tmp1.asm
tmp2=$input/sources/tmp2.txt
tmp3=$input/sources/tmp3.txt

echo 'Si il y a des différences, le résultat du haut est celui obtenu, celui du bas, le résultat attendu'


for fichier in $(ls $input/sources/valide/)
do

	java -jar $input/plic.jar $input/sources/valide/$fichier > $tmp1
	java -jar $input/Mars4_5.jar $tmp1 > $tmp2

	tail +2 $tmp2 > $tmp3

	if ! diff $tmp3 $input/sources/attendusValide/$fichier >& /dev/null
	then
		echo
		echo ------------------------------------------
		echo problème avec le fichier $fichier
		diff $tmp3 $input/sources/attendusValide/$fichier
		head -1 $input/sources/valide/$fichier
		echo ------------------------------------------
		echo 
	else
		echo $fichier : OK
	fi

	rm $tmp1
	rm $tmp2
	rm $tmp3

done


