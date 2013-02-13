
public class FatAlbert  implements Runnable{
	private static VendingMachine vendingMachine;
	private final String name = "            Fat Albert";
	
	public FatAlbert(VendingMachine vendingMachine){
		this.vendingMachine = vendingMachine;
	}
	
	public void run() {
    	//get candy
    	boolean gotCandy = vendingMachine.useCandy(1, name);
    	
    	//get cookie
    	boolean gotCookie = vendingMachine.useCookie(1, name);
    	
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
}
