set CLASSPATH=fcgi.jar;openorb.jar;coach_tracing_api.jar;coach_utils_idltree.jar;.

javac -d . -classpath %CLASSPATH% myServerTest.java
