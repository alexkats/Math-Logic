#!/bin/sh

mvn exec:java -pl "Task $1" -Dexec.mainClass=com.alex.mathlogic.task"$1".Runner
