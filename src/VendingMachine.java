/***
 * Excerpted from "Programming Concurrency on the JVM",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/vspcon for more book information.
***/

import akka.stm.Ref;
import akka.stm.Atomic;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class VendingMachine {
  private final long MAXCOOKIES = 6;
  private final long MAXCANDY = 6;
  final Ref<Long> curCookies = new Ref<Long>(MAXCOOKIES);
  final Ref<Long> curCandy = new Ref<Long>(MAXCANDY);
  final Ref<Boolean> keepRunning = new Ref<Boolean>(true);
  private static final ScheduledExecutorService replenishTimer =
    Executors.newScheduledThreadPool(10);


  private VendingMachine() {}
  
  private void init() {   
    replenishTimer.schedule(new Runnable() {
      public void run() { 
        replenish();
        if (keepRunning.get()) replenishTimer.schedule(
          this, 3, TimeUnit.SECONDS);
      }
    }, 1, TimeUnit.SECONDS);
  }
  
  public static VendingMachine create() {
    final VendingMachine energySource = new VendingMachine();
    energySource.init();
    return energySource;
  }

  public void stopEnergySource() { keepRunning.swap(false); }

  public long getCookiesAvailable() { return curCookies.get(); }
  public long getCandyAvailable() { return curCandy.get(); }

  public boolean useCookie(final long units, final String name) {
    return  new Atomic<Boolean>() {
      public Boolean atomically() {
        long currentLevel = curCookies.get();
        System.out.println(name + " tries too get a " + 
      			"Cookie. Cookie level:" + currentLevel);
        if(units > 0 && currentLevel >= units) {
          curCookies.swap(currentLevel - units);
          return true;          
        } else {
          return false;
        }
      }  
    }.execute();
  }
  
  public boolean useCandy(final long units, final String name) {
	    return  new Atomic<Boolean>() {
	      public Boolean atomically() {
	        long currentLevel = curCandy.get();
	        System.out.println(name + " tries too get a " + 
	      			"candy. Candy level:" + currentLevel);
	        if(units > 0 && currentLevel >= units) {
	          curCandy.swap(currentLevel - units);
	          return true;          
	        } else {
	          return false;
	        }
	      }  
	    }.execute();
	  }

  private void replenish() {
    new Atomic() {
      public Object atomically() {
        if (curCookies.get() < MAXCOOKIES) 
        	curCookies.swap(MAXCOOKIES);
        if (curCandy.get() < MAXCANDY) 
        	curCandy.swap(MAXCANDY);
        return null;
      }
    }.execute();
  }
}
