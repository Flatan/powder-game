
# Equivalent of the eclipse IDE run button
# https://makefiletutorial.com/


buildrun: sources.txt
	javac -d ./bin/ @sources.txt 
	cd bin/ && java core.Application
	rm ./sources.txt

sources.txt:
	find -name "*.java" > sources.txt

docs: sources.txt
	javadoc -quiet -d doc/ @sources.txt
	rm ./sources.txt

