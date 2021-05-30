buildrun:
	javac -d ./bin/ ./src/powder/* ./src/color/*
	cd ./bin/ && java powder.Application
