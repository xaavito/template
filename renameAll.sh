#!/bin/bash

rm -rf .git

sed -i "s/template/$1/g" pom.xml

find . -name "*.java" -exec sed -i "s/template/$1/g" {} +

find . -name "*.md" -exec sed -i "s.xaavito/template.xaavito/$1.g" {} +

mv ../template "../$1"	

cd ..
cd "$1"
git init
