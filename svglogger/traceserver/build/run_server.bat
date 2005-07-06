set CLASSPATH=.;fcgi.jar;openorb.jar;idltree.jar
"C:\Programme\j2sdk1.4.1_02\bin\java" -Xbootclasspath/p:D:\u\jza\traceserver\openorb.jar -classpath %CLASSPATH% -DFCGI_PORT=10000 org.coach.tracing.server.ServerMain corbaloc:iiop:127.0.0.1:3000/NameService 
