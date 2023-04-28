
build:
	@javac ./main/*.java
	@javac ./pieces/*.java

run: build
	@java main/Main.java

play:
	@xboard -fcp "make run" -debug

playbots:
	xboard -fcp "make run" -scp "fairymax" -debug

fullTest: clean build play

clean:
	rm ./main/*.class
	rm ./pieces/*.class