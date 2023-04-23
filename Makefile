CLASSPATH := -classpath '.:./lib/*'

all:
	javac $(CLASSPATH) Main.java

.PHONY: compile
compile:
	mvn compile

.PHONY: run
run: compile
	mvn exec:java -Dexec.mainClass='com.teddywing.pdf_form_replace_font2.App'
