build:
	javac Pieces/*.java
	javac *.java

run: build
	java Main.java

play:
	xboard -fcp "make run" -debug

clean:
	rm Pieces/*.class
	rm *.class