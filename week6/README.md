## Plugins
### Word Extractors

- **NormalWordExtractor:**
The NormalWordExtractor plugin implements the standard extraction process. It reads a file, splits the text into words by non-word characters, filters out common stop words, and returns the list of extracted words.

- **ZWordExtractor:**
The ZWordExtractor is designed to extract only non-stop words that contain the letter 'z'. It operates similarly to the NormalWordExtractor but filters the result to include only words with 'z'.

### Word Counters
- **NormalWordCounter:**
The NormalWordCounter plugin counts the frequency of each word in the provided list of words, implementing the standard counting process.

- **LetterWordCounter:**
The LetterWordCounter counts words based on their first letters. For each letter of the alphabet, it tallies the number of words that start with that letter.

## Style #20
### Constraints:
- The problem is decomposed using some form of abstraction (procedures, functions, objects, etc.)
- All or some of those abstractions are physically encapsulated into their own, usually pre-compiled, packages. Main program and each of the packages are compiled independently. These packages are loaded dynamically by the main program, usually in the beginning (but not necessarily).
-  Main program uses functions/objects from the dynamically-loaded packages, without knowing which exact implementations will be used. New implementations can be used without having to adapt or recompile the main program.
- External specification of which packages to load. This can be done by a configuration file, path conventions, user input or other mechanisms for external specification of code to be linked at run time.

### Possible names:
- No commitment
- Plugins
- Dependency injection

## Compilation
Navigate to the 'week6' directory in your terminal.
```shell 
javac -d deploy interfaces/*.java
javac -d deploy -cp deploy plugins/wordextractors/*.java plugins/wordcounters/*.java
javac -d deploy -cp deploy main/Twenty.java
```
After compiling, 'week6/deploy' directory will contain all the .class files organized by their packages.

## Set Up Configuration
To configure the application to use plugins, edit the config.properties file. 
Specify the fully qualified class name of the desired plugin for word extraction and counting as shown below:

```shell 
# Example configuration to use the NormalWordExtractor and NormalWordCounter
wordExtractor=plugins.wordextractors.NormalWordExtractor
wordCounter=plugins.wordcounters.NormalWordCounter
```
Replace the values of wordExtractor and wordCounter with the class names of the plugins you wish to use.

## Running the Program
```shell 
java -cp deploy main.Twenty ../pride-and-prejudice.txt
```
