#!/bin/sh

if [[ $1 == "1" ]]
then
    mvn exec:java -pl "Task 1" -Dexec.mainClass=com.alex.mathlogic.task1.TaskOneTester
    exit
fi

if [[ $1 == "2" ]]
then
    mvn exec:java -pl "Task 2" -Dexec.mainClass=com.alex.mathlogic.task2.TaskTwoTester
    exit
fi

if [[ $1 == "3" ]]
then
    mvn exec:java -pl "Task 3" -Dexec.mainClass=com.alex.mathlogic.task3.TaskThreeTester
    exit
fi

if [[ $1 == "4" ]]
then
    mvn exec:java -pl "Task 4" -Dexec.mainClass=com.alex.mathlogic.task4.TaskFourTester
    exit
fi

if [[ $1 == "8" ]]
then
    mvn exec:java -pl "Task 8" -Dexec.mainClass=com.alex.mathlogic.task8.TaskEightTester
    exit
fi

echo "Wrong argument"
