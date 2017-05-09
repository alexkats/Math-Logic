if "%1" == "1" (
    mvn exec:java -pl "Task 1" -Dexec.mainClass=com.alex.mathlogic.task1.TaskOneTester
)

if "%1" == "2" (
    mvn exec:java -pl "Task 2" -Dexec.mainClass=com.alex.mathlogic.task2.TaskTwoTester
)

if "%1" == "3" (
    mvn exec:java -pl "Task 3" -Dexec.mainClass=com.alex.mathlogic.task3.TaskThreeTester
)

if "%1" == "4" (
    mvn exec:java -pl "Task 4" -Dexec.mainClass=com.alex.mathlogic.task4.TaskFourTester
)

if "%1" == "8" (
    mvn exec:java -pl "Task 8" -Dexec.mainClass=com.alex.mathlogic.task8.TaskEightTester
)

echo "Wrong argument"
