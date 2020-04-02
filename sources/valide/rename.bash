#!/bin/bash

for file in $(ls *out*)
do
	echo $file
	sed s :out:plic $file | echo
done
