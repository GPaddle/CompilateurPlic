#!/bin/bash

input='/mnt/d/Scolarité/2019-2020_DUT_Informatique_2A/S4/Compilation/Eclipse'

tmp1=$input/sources/tmp1.asm
tmp2=$input/sources/tmp2.txt
tmp3=$input/sources/tmp3.txt

echo 'Si il y a des différences, le résultat du haut est celui obtenu, celui du bas, le résultat attendu'

liste="ls $input/sources/valide/*.plic -1"

nbTests=$($liste | wc -l)

echo 'Nombre de tests : ' $nbTests


for nbFichier in $($liste)
do
	printf _
done

echo

for fichier in $($liste)
do

	java -jar $input/plic.jar $fichier > $tmp1
	java -jar $input/Mars4_5.jar $tmp1 > $tmp2

	tail +2 $tmp2 > $tmp3

#	cat $tmp1
#	cat $tmp2
#	echo ----------
#	cat $tmp3
#	echo -------

#	echo $fichier

	if ! diff $tmp3 $fichier.attente >& /dev/null
	then
		printf "."
		echo
		echo ------------------------------------------
		echo 'Problème avec le fichier '$fichier
		diff $tmp3 $fichier.attente
		head -1 $fichier
		echo ------------------------------------------
		echo
	else
		printf \# #$fichier : OK
	fi

done

rm $tmp1
rm $tmp2
rm $tmp3
echo


