
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class UseVendingMachine {
  private static final VendingMachine vendingMachine = VendingMachine.create();
  
  //num times a day
  private static final int COOKIEMONSTER = 2;
  private static final int WILLIEWONKA = 1;
  private static final int FATALBERT = new Random().nextInt(2) + 2;//2-4
  
  //number of days in simulation
  private static final int numDays = 15;
  
  public static void main(final String[] args)
    throws InterruptedException, ExecutionException {
	long numCookies = vendingMachine.getCookiesAvailable();
	long numCandy =  vendingMachine.getCandyAvailable();
	
    System.out.println("Vending Machine has " + numCookies + " cookies and "
    		+ numCandy + " candy at first");

    ScheduledThreadPoolExecutor service = scheduleTasks(numDays);
    
    Thread.sleep(numDays + 1);
    numCookies = vendingMachine.getCookiesAvailable();
	numCandy =  vendingMachine.getCandyAvailable();
	
    System.out.println("Vending Machine has " + numCookies + " cookies and "
    		+ numCandy + " candy in the end");
        
    vendingMachine.stopEnergySource();
    service.shutdown();
  }
  
  //schedule tasks for the daily customers
  private static ScheduledThreadPoolExecutor scheduleTasks(int numDays){
	  //total number of tasks
	  int total = COOKIEMONSTER + WILLIEWONKA + FATALBERT;
	  
	  //create scheduler service
	  final ScheduledThreadPoolExecutor service =
				new ScheduledThreadPoolExecutor(total);
	  
	  //schedule tasks
	  for(int j = 0; j < numDays; j++){
		//----create cookie monsters tasks
	    for(int i =0; i < COOKIEMONSTER;i++){
	    	Runnable task = new Runnable(){
	            public void run() {
	            	//shorten printlns
	            	String name = "Cookie Monster";
	            	long state = vendingMachine.getCookiesAvailable();
	            	
	            	//say task
	            	System.out.println("\t" + name + " tries too get a " + 
	            			"cookie. Cookie level:" + state);
	            	
	            	//try task and print result
	            	if(vendingMachine.useCookie(1)){
	            		System.out.println("Me love cookies");
	            	}
	            	else{
	            		System.out.println("Me hungry");
	            	}
	            }
	        };
	        long delay = (long)numDays + (i/COOKIEMONSTER);
	    	service.schedule(task, delay, TimeUnit.SECONDS);
	    }
	    
	    //----create willie wonkas tasks
	    for(int i =0; i < WILLIEWONKA;i++){
	    	Runnable task = new Runnable(){
	            public void run() {
	            	//shorten printlns
	            	String name = "Willie Wonka";
	            	long state = vendingMachine.getCandyAvailable();
	            	
	            	//say task
	            	System.out.println("\t\t" + name + " tries too get a " + 
	            			"candy. Candy level:" + state);
	            	
	            	//try task and print result
	            	if(vendingMachine.useCandy(1)){
	            		System.out.println("The Candy Man Can");
	            	}
	            	else{
	            		System.out.println("Violet - you're turning violet");
	            	}
	            }
	        };
	        
	        long delay = (long)numDays + (i/WILLIEWONKA);
	    	service.schedule(task, delay, TimeUnit.SECONDS);
	    }
	    
	    //----create fat alberts tasks
	    for(int i =0; i < FATALBERT;i++){
	    	Runnable task = new Runnable(){
	            public void run() {
	            	//shorten printlns
	            	String name = "Fat Albert";
	            	long state = vendingMachine.getCandyAvailable();
	            	
	            	//say candy task
	            	System.out.println("\t\t\t" + name + " tries too get a " + 
	            			"candy. Candy level:" + state);
	            	boolean gotCandy = vendingMachine.useCandy(1);
	            	
	            	//say cookie task
	            	System.out.println("\t\t\t" + name + " tries too get a " + 
	            			"cookie. Cookie level:" + state);
	            	boolean gotCookie = vendingMachine.useCookie(1);
	            	
	            	//print results
	            	if(gotCandy && gotCookie){
	            		System.out.println("Hey, hey hey!");
	            	}
	            	else if(gotCandy){
	            		System.out.println("At least I got a Candy");
	            	}
	            	else if(gotCookie){
	            		System.out.println("At least I got a Cookie");
	            	}
	            	else{
	            		System.out.println("No food for me today.");
	            	}
	            }
	        };
	        
	        long delay = (long)numDays + (i/WILLIEWONKA);
	    	service.schedule(task, delay, TimeUnit.SECONDS);
	     }
	  }//end for loops
	  
	  return service;
  }
}