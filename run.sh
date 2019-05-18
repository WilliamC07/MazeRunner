#!/bin/bash

javac -cp "core.jar" *.java
java -cp "core.jar:." Main
