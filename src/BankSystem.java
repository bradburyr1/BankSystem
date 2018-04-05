import java.util.Scanner;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.Jsoup;

import java.io.File;
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
		
		double value = 0.0;//What the user enters
		
		boolean good = false;//Check whether the input is good 
		do{
			//Show prompt and get string input
			System.out.print("Please enter an amount to deposit:");
			try{
				value = scan.nextDouble();//Try to get input
			}
			catch(InputMismatchException e){
				scan.nextLine();//Goes to next line in case of an error
			}
			
			good = formatCheck(value);
		}while(!good);
		
		URL url = getClass().getResource("log.html");
		File input = new File(url.getPath());
		
		Document doc = Jsoup.parse(input, "UTF-8");
		//System.out.println(doc.select("#transactions").select("tr").select("td").get(1));
		Elements table = doc.select("#transactions");
		Elements rows = table.select("tr");
		
		 for (int i = 0; i < rows.size(); i++) {
		        Element row = rows.get(i);
		        Elements cols = row.select("td");
		        System.out.println(cols.text());
		 }
		
	}
	
	public void balance() throws IOException{
		URL url = getClass().getResource("log.html");
		File input = new File(url.getPath());
		
		double[] amounts;
		
		Document doc = Jsoup.parse(input, "UTF-8");
		//System.out.println(doc.select("#transactions").select("tr").select("td").get(1));
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
		 
		 System.out.println("The current balance is: " + total);
	}
	
	public boolean formatCheck(double num){
		boolean good = false;
		DecimalFormat df = new DecimalFormat("0.00");
		String formatted = df.format(num);
		String numString = Double.toString(num);
		
		if(formatted.equals(numString)){
			if(num >= 0){//Make sure it is positive
			good = true;
			}
		}
		return good;
	}
}
