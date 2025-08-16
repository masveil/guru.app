#!/usr/bin/env sh
APP_BASE_NAME=`basename "$0"`
DIRNAME=`dirname "$0"`
DEFAULT_JVM_OPTS=""
CLASSPATH=$DIRNAME/gradle/wrapper/gradle-wrapper.jar
if [ -n "$JAVA_HOME" ] ; then
    if [ -x "$JAVA_HOME/jre/sh/java" ] ; then
        JAVACMD="$JAVA_HOME/jre/sh/java"
    else
        JAVACMD="$JAVA_HOME/bin/java"
    fi
    if [ ! -x "$JAVACMD" ] ; then
        echo "ERROR: JAVA_HOME is set to an invalid directory: $JAVA_HOME" 1>&2
        exit 1
    fi
else
    JAVACMD="java"
fi
exec "$JAVACMD" $DEFAULT_JVM_OPTS -classpath "$CLASSPATH" org.gradle.wrapper.GradleWrapperMain "$@"
