#!/bin/bash
#
# Email Tracking Application startup script
#

PROJECT=EmailTrackingApplication-v1.0
JAVA_VERSION="1.7"

SCRIPT_PATH=$(dirname $(readlink -f $0))
ETRACK_HOME=$SCRIPT_PATH/../
export ETRACK_HOME

getpid() {
    pid=`pgrep -f "java.*$PROJECT"`
}

start() {
    getpid
    if [ -n "$pid" ]; then
        echo "$PROJECT (pid $pid) is already running"
        exit 1
    fi

    if [[ -n "$JAVA_HOME" ]] && [[ -x "$JAVA_HOME/bin/java" ]];  then
        echo Found java executable in JAVA_HOME
        JAVA_CMD="$JAVA_HOME/bin/java"
    elif [[ -n "$JAVA" ]] && [[ -x "$JAVA" ]];  then
        echo Found java executable by JAVA
        JAVA_CMD="$JAVA"
    elif type -p java; then
        echo Found java executable in PATH
        JAVA_CMD=java
    else
        echo "Cannot find a Java JDK. Please set JAVA_HOME, JAVA executable or put java (>=1.8) in your PATH."
        exit 1
    fi

    if [[ "$JAVA_CMD" ]]; then
        version=$("$JAVA_CMD" -version 2>&1 | awk -F '"' '/version/ {print $2}')
        if [[ "$version" > $JAVA_VERSION ]]; then
        echo Java version "$version"
        else
        echo JAVA_CMD=$JAVA_CMD
        echo Java version "$version" is less than required $JAVA_VERSION
        exit 1
        fi
    fi

    cd "$ETRACK_HOME"
    echo -n "ETRACK_HOME="
    pwd

    nohup "$JAVA_CMD" -Dspring.config.location=config/Mail.properties -Dlogging.config=config/logback.xml -jar lib/EmailTrackingApplication-v1.0.jar > /dev/null 2>&1 &

    echo -ne "Starting process"
    for i in {1..10}; do
        if ! [ -n "$pid" ]; then
            echo -ne "."
            sleep 1
            getpid
        fi
    done
    echo

    if [ -n "$pid" ]
        then status
        else echo "Error during $PROJECT starting, see log for details."
    fi
}

stop() {
    status
    if [ -n "$pid" ]
    then
        echo -ne "Stopping process"
        kill $pid
        res=$?
        for i in {1..10}; do
            if [ -n "$pid" ]; then
                echo -ne "."
                sleep 1
                getpid
            fi
        done
        echo
        if ! [ -n "$pid" ]
            then echo "$PROJECT has been successfully stopped."
            else echo "Error during $PROJECT stopping... $res"
        fi
    fi
}

status() {
    getpid
    if [ -n "$pid" ]
        then echo "$PROJECT (pid $pid) is running..."
        else echo "$PROJECT is NOT running"
    fi
}

case "$1" in
    start)
        start
        ;;
    stop)
        stop
        ;;
    status)
        status
        ;;
    restart)
        stop
        start
        ;;
    *)
        echo $"Usage: $0 {start|stop|restart|status}"
        exit 1
esac

exit 0
