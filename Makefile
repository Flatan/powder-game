
# Equivalent of the eclipse IDE run button

buildrun:
	javac -d ./bin/ ./src/powder/* ./src/color/* ./src/ui/* ./src/core/*
	java core.Application
