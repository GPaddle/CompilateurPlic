#!/bin/bash

for file in ./scr/sources/*; do
	java -jar "$1" "$file"
done