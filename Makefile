CLASSPATH := -classpath '.:./lib/*'

all:
	javac $(CLASSPATH) Main.java

.PHONY: run
run:
	java $(CLASSPATH) Main
