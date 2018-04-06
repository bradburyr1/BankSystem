For this problem, I wanted to keep the code for the various functionalities separate. I did this 
by making a class separate from the main class (the Interact class) which contained the 
methods the main method would need. First, the getCommand gets the command. Then, based on the 
user's input, one of three other classes are called (deposit, withdraw, or balance). formatCheck 
is used to make sure numbers are inputted correctly by the user. 

This program makes use of the library "jsoup" (https://jsoup.org/), which offers 
functionality for java to interact with HTML. Jsoup allowed this program to parse the HTML table 
"transactions", as well as append information to it. 

Intructions: 
1) Download all files 
2) Open command prompt
3) Navigate to the project's bin folder
4) Enter this command "java -cp jsoup-1.11.2.jar;. BankSystem" (without quotes)

The project should then run. Note: The Java JDK must be installed for this to work. 