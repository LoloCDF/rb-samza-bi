#!/bin/bash

dir1=$(mktemp -d)
pushd $dir1 &>/dev/null
wget https://github.com/redBorder/tranquility/archive/rb.tar.gz
tar xzf rb.tar.gz
pushd tranquility-rb &>/dev/null
sbt publishM2
popd &>/dev/null
popd &>/dev/null

mvn clean package

rm -rf $dir1
