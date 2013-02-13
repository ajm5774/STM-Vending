
public class CookieMonster implements User, Runnable{
	private static VendingMachine vendingMachine;
	private final String name = "Cookie Monster";
	
	public CookieMonster(VendingMachine vendingMachine){
		this.vendingMachine = vendingMachine;
	}
	
	public void run() {
    	//try task and print result
    	if(vendingMachine.useCookie(1, name)){
    		System.out.println("Me love cookies");
    	}
    	else{
    		System.out.println("Me hungry");
    	}
    }
}
