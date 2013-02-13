
public class WillieWonka implements Runnable{
	private static VendingMachine vendingMachine;
	private final String name = "        Willie Wonka";
	
	public WillieWonka(VendingMachine vendingMachine){
		this.vendingMachine = vendingMachine;
	}
	
	public void run() {
    	//try task and print result
    	if(vendingMachine.useCandy(1, name)){
    		System.out.println("The Candy Man Can");
    	}
    	else{
    		System.out.println("Violet - you're turning violet");
    	}
    }
}
