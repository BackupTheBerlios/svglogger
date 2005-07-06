cls

echo off

set JAVA_HOME="C:\Programme\Testing Tech\TTthree\jre\j2re1.4.2"

set NS_PORT=3000
set NS_HOST=localhost

set CLASSPATH=.;fcgi.jar;openorb.jar;idltree.jar

call %JAVA_HOME%\bin\java -Xbootclasspath/p:"openorb.jar" -classpath %CLASSPATH% -DFCGI_PORT=10000 org.coach.tracing.server.ServerMain corbaloc:iiop:127.0.0.1:3000/NameService