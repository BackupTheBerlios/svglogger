===========================================================
README file for the installation of the SVGlogging utility
initial version by whellenthal@lucent.com (08/25/03)
modified by rennoch@fokus.fraunhofer.de (09/14/04)
===========================================================



The SVGlogging utility consists of three parts: 
	
	Tracing server (collects the invocation events from TTthree) 
	Tracing viewer web application
	Browser (must be MS InternetExplorer because of SVG plugin, min. version 5.5 service pack 3)

   ---------------                -----------------                -------------------------
   |   TTthree   | --- CORBA ---- | Tracing Server | ---- FCGI --- | Apache server (+fcgi) |
   ---------------                 -----------------               -------- |---------------
                                                                            |
                                                                           http
                                                                            |
                                                                     -------|---------------
                                                                    | MS InternetExplorer  |
                                                                    ------------------------

1) Install apache_2.0.44-win32-x86-no_ssl.msi (or try a newer Apache2 version from http://archive.apache.org/dist/httpd/binaries/win32/)
2) Unpack the traceserver and -viewer from lucent.zip
3) Copy mod_fastcgi-2.4.0-AP2.so from /lucent/traceviewer/lib to /modules of your Apache-install-dir
4) Add the following 2 lines at the end of /conf/httpd.conf of your Apache-install-dir
   (to inform Apache about the fcgi configuration provided within lucent.zip):
	# include the fcgi configuration for the coach tracing viewer web application
	Include C:/lucent/traceviewer/conf/fcgi.conf
5) Edit /lucent/traceviewer/conf/fcgi.conf to inform about your fcgi_module & html sources (Aliases)

If you call the IExplorer with http://localhost/coach/tracing you'll be asked to install the Adobe SVG viewer.

4 steps to start the Dino example with SVGlogging (Note: scripts may not work under W2K)
========================================================================================
a) launch a CORBA Naming Server e.g. with: "C:\Programme\Testing Tech\TTthree\jre\j2re1.4.2\bin\orbd" -ORBInitialPort 3000
b) launch the Traceing Server using the run_server.bat in lucent.zip (please edit %JAVA_HOME%)
c) launch the run_nt_dino.bat in TTthree\expl\Dino from the TTthree 1.3 Beta release (please edit %JAVA_HOME%, %TT3_DIR%) 
   NOTE: please edit module and adapter file names in dinolistTest.mlf
d) launch InternetExplorer with http://localhost/coach/tracing
