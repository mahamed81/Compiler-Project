The commits of the scanner part of the compiler is in another project called AhmedCompilerOld in Bitbucket. I 
switched IDEs from Netbeans to Eclips because it was easier for testing. The commits for the Recognizer class and the test should be in a repository called AhmedCompiler.


Scanner package should contain the following files:

scan.jflex 
MyScanner.java
Token.java 
TokenType.java
LookupTable.java 
MyScannerTest.java   


Parser package should contain the following files:

Recognizer.java 
RecognizerTest.java
RecognizerSymbolTableTest.java


Symbol Table package should contain the following files:

SymbolTable.java
SymbolTableTest.java
DataType.java
Identi.java
Kind.java


Compiler Table package should contain the following files:

CompilerMain.java