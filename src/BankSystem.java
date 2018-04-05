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
		Interact inter = new Interact();
		
		boolean exit = false;
		
		do{
		String comm = inter.getCommand(scan);
		
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
		
		URL url = getClass().getResource("log.html");
		File input = new File(url.getPath());
		
		Document doc = Jsoup.parse(input, "UTF-8");
		doc.select("#transactions tbody").append("<tr><td>" + value + "</td></tr>");
		
		FileWriter fw = new FileWriter(input, false);
		fw.write(doc.toString());
		fw.close();
	}
	
	public void withdraw(Scanner scan) throws IOException{
		
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
		
		value = "-" + value;
		
		URL url = getClass().getResource("log.html");
		File input = new File(url.getPath());
		
		Document doc = Jsoup.parse(input, "UTF-8");
		doc.select("#transactions tbody").append("<tr><td>" + value + "</td></tr>");
		
		FileWriter fw = new FileWriter(input, false);
		fw.write(doc.toString());
		fw.close();
	}
	
	public void balance() throws IOException{
		URL url = getClass().getResource("log.html");
		File input = new File(url.getPath());
		
		double[] amounts;
		
		Document doc = Jsoup.parse(input, "UTF-8");
		Elements table = doc.select("#transactions");
		Elements rows = table.select("tr");
		
		amounts = new double[rows.size()];
		
		double total = 0.0;
		
		 for (int i = 1; i < rows.size(); i++) {
		        Element row = rows.get(i);
		        Elements cols = row.select("td");
		        amounts[i] = Double.parseDouble(cols.text());
		        
		        //System.out.println("Amount " + (i) + ": " + amounts[i]);
		        
		        total += amounts[i];
		 }
		 
		 DecimalFormat df = new DecimalFormat("$0.00");
		 String dfTotal = df.format(total);
		 System.out.println("The current balance is: " + dfTotal);
	}
	
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
