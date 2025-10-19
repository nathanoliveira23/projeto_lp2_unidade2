all:
	javac src/*.java src/**/*.java -d bin
	java -cp bin Main
