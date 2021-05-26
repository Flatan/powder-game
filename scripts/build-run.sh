#!/bin/sh

x=$(realpath $0)
SCRIPTS=$(dirname $x)

cd $SCRIPTS/.. && javac -d ./bin/ ./src/powder/*
cd ./bin && java powder.Application


