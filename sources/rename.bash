#!/bin/bash

liste=$(ls *.plic)

for file in $liste
do
#	echo $file
	mv $file "PLIC9n.LOUIS.$2"$file
done

listeOut=$(ls *.out)

for file in $listeOut
do
#       echo $file
        mv $file "PLIC9n.LOUIS.$2"$file".attente"
done
