build:
	javac *.java

run: build
	java Main.java

play:
	xboard -fcp "make run" -debug

clean:
	rm *.class