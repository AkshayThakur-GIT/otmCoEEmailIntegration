@echo off
setlocal

set PROJECT=EmailTrackingApplication-v1.0
set JAVA_VERSION="1.7"

set ETRACK_HOME=%~dp0..\
cd %ETRACK_HOME%

:validate
for /F %%v in ('echo %1 "^start$ ^stop$ ^restart$ ^status"') do set COMMAND=%%v
if "%COMMAND%" == "" (
    echo Usage: %0 { start : stop : restart : status }
    set COMMAND=start
)
call :%COMMAND%
goto :eof

:start
call :status
if not "%pid%" == "" (
    echo Start was canceled
    goto :eof
)
goto :findJava
goto :eof

:run
echo ETRACK_HOME=%cd%

echo Execution environment passed and proceed with starting the application
choice /d y /t 5 > nul
			
start "%PROJECT%" "%JAVA_CMD%" -Dspring.config.location=config/Mail.properties -Dlogging.config=config/logback.xml -jar lib/EmailTrackingApplication-v1.0.jar
echo %PROJECT% successfully started...
goto :eof

:stop
call :status
if not "%pid%" == "" (
choice /d y /t 3 > nul
echo Stopping %PROJECT% Application...
choice /d y /t 3 > nul
    taskkill /pid %pid%
echo %PROJECT% successfully stopped...

)

goto :eof

:status
set pid=
for /F "tokens=2 skip=2" %%i in ('tasklist /fi "windowtitle eq %PROJECT%"') do set pid=%%i
if "%pid%" == "" (
    echo %PROJECT% is NOT running
	
) else (
    echo %PROJECT% (pid %pid%^) is running...
	
)
goto :eof

:restart
call :stop
call :start
goto :eof

:findJava

echo Checking execution environment before starting the application
choice /d y /t 5 > nul
set JAVA_CMD=
if not "%JAVA_HOME%" == "" if exist "%JAVA_HOME%\bin\java.exe" (
    echo Found java executable in JAVA_HOME
	choice /d y /t 3 > nul
    set JAVA_CMD=%JAVA_HOME%\bin\java
    call :checkJava
)
if "%JAVA_CMD%" == "" if not "%JAVA%" == "" if exist "%JAVA%" (
    echo Found java executable by JAVA
	choice /d y /t 3 > nul
    set JAVA_CMD=%JAVA%
    call :checkJava
)
if "%JAVA_CMD%" == "" (
    java -version >nul 2>&1 && (
        echo Found java executable in PATH
		choice /d y /t 3 > nul
        set JAVA_CMD=java
        call :checkJava
    )
)
if "%JAVA_CMD%" == "" (
    echo Cannot find Java %JAVA_VERSION:"=%. Please set JAVA_HOME, JAVA executable or put java in your PATH.
    exit /b 1
)

goto :eof

:checkJava
if not "%JAVA_CMD%" == "" (
    echo JAVA_CMD=%JAVA_CMD%
	choice /d y /t 3 > nul
    for /f "tokens=4" %%v in ('"%JAVA_CMD%" -fullversion 2^>^&1') do (
        if "%%v" GTR "%JAVA_VERSION%" (
            echo Java version %%v
			choice /d y /t 3 > nul
            goto :run
        ) else (
            set JAVA_CMD=
            echo Java version %%v is less than required %JAVA_VERSION%
        )
    )
)
goto :eof