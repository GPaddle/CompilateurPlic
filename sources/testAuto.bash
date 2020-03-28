#!/bin/bash

input='/mnt/d/Scolarité/2019-2020_DUT_Informatique_2A/S4/Compilation/Eclipse'

tmp1=$input/sources/tmp1.asm
tmp2=$input/sources/tmp2.txt
tmp3=$input/sources/tmp3.txt

echo 'Si il y a des différences, le résultat du haut est celui obtenu, celui du bas, le résultat attendu'

liste="ls $input/sources/valide/*.plic -1"
listeInvalide="ls $input/sources/invalide/*.plic -1"

nbTests=$($liste | wc -l)
nbTestsInvalides=$($listeInvalide | wc -l)

echo 'Nombre de tests valides: ' $nbTests


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

echo
echo

echo "Nombre de tests invalides: " $nbTestsInvalides

for nbfichier in $($listeInvalide)
do
	printf _
done

echo

for fichier in $($listeInvalide)
do
	java -jar $input/plic.jar $fichier > $tmp1
	if ! grep "ERREUR:" $tmp1 >& /dev/null
	then
		java -jar $input/Mars4.5.jar $tmp1 > $tmp2
		if ! grep "ERREUR:" $tmp2 >& /dev/null
		then
			printf "."
			echo
                        echo------------------------------------
                        echo 'Problème avec le fichier' $fichier
			head -1 $fichier
                        echo------------------------------------
		fi
	else
		printf \#
	fi
done


rm $tmp1
rm $tmp2
rm $tmp3
echo
