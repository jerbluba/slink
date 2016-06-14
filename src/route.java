
public class route {
	private static int[] answer;
	private static int[] capcity;
	
	public route(int[] Route,int[] theCapcity){
		setRoute(Route);
		setCapcity(theCapcity);
	}

	public static void setRoute(int[] Route){
		answer=Route;
	}
	public static void setCapcity(int[] Route){
		capcity=Route;
	}
	public static int[] getRoute(){
		return answer;
	}
	public static int[] getCapcity(){
		return capcity;
	}
}
