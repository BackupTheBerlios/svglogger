set CLASSPATH=fcgi.jar;openorb.jar;openorb_tools.jar;idltree.jar;.

"C:\Programme\j2sdk1.4.1_02\bin\java"  -classpath %CLASSPATH% org.openorb.compiler.IdlCompiler -d . coach_tracing.idl
"C:\Programme\j2sdk1.4.1_02\bin\javac" -d . -classpath %CLASSPATH% *.java
