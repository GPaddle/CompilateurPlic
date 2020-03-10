#!/bin/bash

for file in ./src/sources/*; do
	java -jar "$1" "$file"
done

