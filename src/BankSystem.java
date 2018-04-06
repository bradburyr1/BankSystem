import java.util.Scanner;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.Jsoup;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.InputMismatchException;

public class BankSystem {

	public static void main(String[] args) throws IOException{
		
		Scanner scan = new Scanner(System.in);//Instantiate Scanner class 
		Interact inter = new Interact();//Instantiate the interact class, which contains the 
										//methods that are used 
		
		boolean exit = false;//To exit the program
		
		do{
		String comm = inter.getCommand(scan);//Get the command, then go through if statements 
											//to check which it was and act accordingly
		
		if(comm.equals("deposit")){
			inter.deposit(scan);
		}
		else if(comm.equals("balance")){
			inter.balance();
		}
		else if(comm.equals("withdraw")){
			inter.withdraw(scan);
		}
		else if(comm.equals("exit")){
			exit = true;
		}
		}while(!exit);
		
		scan.close();
	}
}

class Interact{
		
	/**
	   * This gets the user's input for the command, being deposit, balance, withdraw, or exit
	   * @param scan Accepting scanner as a parameter allows scanner to be opened and closed only once
	   * @return Returns a string value of the user's input, which is their command
	   */
	public String getCommand(Scanner scan){
		
		String command;//What the user enters
		boolean good = false;//Check whether the input is good 
		
		do{
			//Show prompt and get string input
			System.out.print("Please enter in a command (Deposit, Withdraw, Balance, Exit) :");
			command = scan.nextLine();
			
			command = command.toLowerCase();//Change to lower case to make input case insensitive 
			
			//Check for one of the four commands we ask for
			if(command.equals("deposit") || command.equals("withdraw") ||
				command.equals("balance") || command.equals("exit")){
				
			good = true;//Input is good
			
			}
		}while(!good);//repeat until input is good
		
		return command;
		}
	
	/**
	   * The deposit method is what appends a positive value to the HTML file
	   * @param scan Accepting scanner as a parameter allows scanner to be opened and closed only once
	   * @exception IOException Exception for input error
	   */
	public void deposit(Scanner scan) throws IOException{
		
		String value = "";//What the user enters
		boolean good = false;//Check whether the input is good
		
		do{
			//Show prompt and get string input
			System.out.print("Please enter an amount to deposit:");
			try{
				value = scan.nextLine();//Try to get input
			}
			catch(InputMismatchException e){
				scan.nextLine();//Goes to next line in case of an error
			}
			
			good = formatCheck(value);
		}while(!good);
		
		//Open the log.html file
		URL url = getClass().getResource("log.html");
		File input = new File(url.getPath());
		
		//Use Jsoup to parse the file
		Document doc = Jsoup.parse(input, "UTF-8");
		
		//Append the value to the proper place
		doc.select("#transactions tbody").append("<tr><td>" + value + "</td></tr>");
		
		//Re-write the document with the new value appended
		FileWriter fw = new FileWriter(input, false);
		fw.write(doc.toString());
		fw.close();
	}
	
	/**
	   * The withdraw method is what appends a negative value to the HTML file
	   * @param scan Accepting scanner as a parameter allows scanner to be opened and closed only once
	   * @exception IOException Exception for input error
	   */
	public void withdraw(Scanner scan) throws IOException{
		
		String value = "";//What the user enters
		boolean good = false;//Check whether the input is good
		
		do{
			//Show prompt and get string input
			System.out.print("Please enter an amount to withdraw:");
			try{
				value = scan.nextLine();//Try to get input
			}
			catch(InputMismatchException e){
				scan.nextLine();//Goes to next line in case of an error
			}
			good = formatCheck(value);
		}while(!good);
		
		value = "-" + value;
		
		//Open the log.html file
		URL url = getClass().getResource("log.html");
		File input = new File(url.getPath());
		
		//Use Jsoup to parse the file
		Document doc = Jsoup.parse(input, "UTF-8");
		
		//Append the value to the proper place
		doc.select("#transactions tbody").append("<tr><td>" + value + "</td></tr>");
		
		//Re-write the document with the new value appended
		FileWriter fw = new FileWriter(input, false);
		fw.write(doc.toString());
		fw.close();
	}
	
	/**
	   * The balance method reads the HTML file and totals the values for the transactions table
	   * @exception IOException Exception for input error
	   */
	public void balance() throws IOException{
		
		//Open the log.html file
		URL url = getClass().getResource("log.html");
		File input = new File(url.getPath());
		
		//Parse and select rows from transactions table
		Document doc = Jsoup.parse(input, "UTF-8");
		Elements table = doc.select("#transactions");
		Elements rows = table.select("tr");
		
		double total = 0.0;
		
		//Fill out the "amounts" array and total the results
		 for (int i = 1; i < rows.size(); i++) {
		        Element row = rows.get(i);
		        Elements cols = row.select("td");
		        
		        //Total the values found in the table
		        total += Double.parseDouble(cols.text());
		 }
		 
		 DecimalFormat df = new DecimalFormat("$0.00");
		 String dfTotal = df.format(total);
		 System.out.println("The current balance is: " + dfTotal);
	}
	
	/**
	   * This is the method that checks whether the number is formatted correct
	   * @param num The number that needs to be checked
	   * @return Returns whether or not the number the user inputed is good
	   */
	public boolean formatCheck(String num){
		boolean good = false;
		DecimalFormat df = new DecimalFormat("0.00");
		
		String[] numSplit = num.split("\\.");
		
		double dubNum = 0.0;
		
		try{
		dubNum = Double.parseDouble(num);
		}catch(NumberFormatException e){
			
		}
		String formatted = "";
		
		formatted = df.format(dubNum);
		
		 if(formatted.equals(num)){
			if(Double.parseDouble(num) >= 0){//Make sure it is positive
			good = true;
			}
		}
		 else if(numSplit.length != 2 || !numSplit[1].equals("00")){
			 good = false;
		 }
		return good;
	}
}
