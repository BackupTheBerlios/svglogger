set CLASSPATH=.;openorb.jar
"C:\Programme\j2sdk1.4.1_02\bin\java" -Xbootclasspath/p:D:\u\jza\traceserver\openorb.jar -classpath  %CLASSPATH% org.coach.tracing.server.ServerTest corbaloc:iiop:127.0.0.1:3000/NameService %*