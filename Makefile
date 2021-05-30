
# Equivalent of the eclipse IDE run button

buildrun:
	javac -d ./bin/ ./src/powder/* ./src/color/*
	cd ./bin/ && java powder.Application
