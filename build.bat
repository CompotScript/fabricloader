@echo off
setlocal

REM Компиляция
if not exist out mkdir out
javac -d out src\main\java\com\soez\fake\FakeFabricLoader.java

REM Создание манифеста
echo Main-Class: com.soez.fake.FakeFabricLoader > manifest.txt

REM Сборка jar
jar cfm fabricloader.jar manifest.txt -C out .

REM Упаковка в exe через jpackage (встроен в JDK 21)
jpackage ^
  --type app-image ^
  --name "Fabric Loader" ^
  --input . ^
  --main-jar fabricloader.jar ^
  --main-class com.soez.fake.FakeFabricLoader ^
  --icon fabric.ico ^
  --dest dist

echo.
echo Готово: dist\Fabric Loader\Fabric Loader.exe
pause
