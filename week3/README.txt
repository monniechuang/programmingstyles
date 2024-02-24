## 1. Compilation
javac week3/*.java

## 2. Running the Program
- Run `seven.java` using the command `java week3.seven pride-and-prejudice.txt`.
- For `eight.java`, use `java -Xss256m week3.eight pride-and-prejudice.txt` due to its stack requirements.

## 3. Troubleshooting
- Stack Overflow Error: If you encounter a stack overflow error in `eight.java` due to deep recursion, consider increasing the stack size using the -Xss flag