
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

public class Main {
  private static final VendingMachine vendingMachine = VendingMachine.create();
  
  //num times a day
  private static final int COOKIEMONSTER = 2;
  private static final int WILLIEWONKA = 1;
  private static final int FATALBERTLOW = 2;
  private static final int FATALBERTHIGH = 4;
  
  //number of days in simulation (half a month)
  private static final int numDays = 15;
  
  
  /*
   * main starts the program, schedules tasks, and shuts down
   */
  public static void main(final String[] args)
    throws InterruptedException, ExecutionException {
	long numCookies = vendingMachine.getCookiesAvailable();
	long numCandy =  vendingMachine.getCandyAvailable();
	
	//print starting state of the vending machine for convenience
    System.out.println("Vending Machine has " + numCookies + " cookies and "
    		+ numCandy + " candy at first");
    
    //schedule tasks with users
    ScheduledThreadPoolExecutor service = scheduleTasks(numDays);
    
    //sleep for the number of days before shutting down
    Thread.sleep((numDays)*1000);
        
    vendingMachine.stopEnergySource();
    service.shutdown();
  }
  
  
  /*
   * schedule tasks for the daily customers
   */
  private static ScheduledThreadPoolExecutor scheduleTasks(int numDays){
	  //total number of tasks
	  int total = COOKIEMONSTER + WILLIEWONKA + FATALBERTHIGH;
	  
	  //create scheduler service with a maximum pool of the maximum # tasks
	  final ScheduledThreadPoolExecutor service =
				new ScheduledThreadPoolExecutor(total * numDays);
	  
	  //schedule tasks
	  for(int j = 0; j < numDays; j++){
		//----create cookie monsters tasks
	    for(int i = 0; i < COOKIEMONSTER;i++){
	    	Runnable task = new CookieMonster(vendingMachine);
	        long delay = (long)(j*1000 + ((i*1000.0)/COOKIEMONSTER));
	    	service.schedule(task, delay, TimeUnit.MILLISECONDS);
	    }
	    
	    //----create willie wonkas tasks
	    for(int i =0; i < WILLIEWONKA;i++){
	    	Runnable task = new WillieWonka(vendingMachine);
	        
	        long delay = (long)(j*1000 + ((i*1000.0)/WILLIEWONKA));
	    	service.schedule(task, delay, TimeUnit.MILLISECONDS);
	    }
	    
	    //----create fat alberts tasks
	    	//high number is exclusive, must add 1
	    int FATALBERT = new Random().nextInt(FATALBERTHIGH - FATALBERTLOW + 1)
	    		+ FATALBERTLOW;//FATALBERTLOW - FATALBERTHIGH
	    for(int i = 0; i < FATALBERT;i++){
	    	Runnable task = new FatAlbert(vendingMachine);
	    	
	        long delay = (long)(j*1000 + ((i*1000.0)/FATALBERT));
	    	service.schedule(task, delay, TimeUnit.MILLISECONDS);
	     }
	  }//end for loops
	  
	  return service;
  }
}