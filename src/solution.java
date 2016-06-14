import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;


public class solution {//一個路徑的集合函數，一條路徑配一組需求量一個ＴＡＢＵ順序
	private  HashMap<Integer,HashMap<Integer,Integer>> answer;
	private  HashMap<Integer,HashMap<Integer,Integer>> capcity;
	private  HashMap<Integer,Double> length;
	private  HashMap<Integer,Integer> routeOrder;
	private boolean debug=false;
	private boolean dontWantZero=false;
	
	// TODO 空集合
	
	public solution(int[][] solution,int[][] theCapcity,double[] length,int[] order){
		for(int i=0;i<solution.length;i++){
			for(int j=0;j<solution[i].length;j++){
//				System.out.println(solution[i][j]+" ");
			}
		}
		
		setSolution(solution);
		setCapcity(theCapcity);
		setLength(length);
		setOrder(order);
//		new phase2().showMap(this, "debug");
		try{
			
		}finally{
			
			showRoute("四矩陣架構");
			zeroException();
		}
		
		
	}
	
	public solution(int[][] solution,int[][] theCapcity,double[] length){
		setSolution(solution);
		setCapcity(theCapcity);
		setLength(length);
		int[] order=new int[length.length];
		for(int i=0;i<order.length;i++){
			order[i]=0;
		}
		setOrder(order);
	try{
			
		}finally{
			
			showRoute("三矩陣架構");
			zeroException();
		}
	}
	
	public solution(int[][] solution,int[][] theCapcity){
		setSolution(solution);
		setCapcity(theCapcity);
		setOrder(new int[]{});
		setLength(new double[]{});
	try{
			
		}finally{
			
			showRoute("雙矩陣架構");
			zeroException();
		}
	}

	public solution(int[][] solution,int[][] theCapcity,int[] order){
		setSolution(solution);
		setCapcity(theCapcity);
		routeOrder=iTH(order);
		double[] length=new double[order.length];
		for(int i=0;i<order.length;i++){
			length[i]=0;
		}
		setLength(length);
	try{
			
		}finally{
			
			showRoute("三矩陣架構，不填長度");
			zeroException();
		}
	}
	
	public solution(){
		setSolution(new int[][]{});
		setCapcity(new int[][]{});
		setOrder(new int[]{});
		setLength(new double[]{});
		 answer=new HashMap<Integer,HashMap<Integer,Integer>>();
		 capcity=new HashMap<Integer,HashMap<Integer,Integer>>() ;
		 length=new HashMap<Integer,Double>();
		 routeOrder=new HashMap<Integer,Integer>() ;
			try{
				
			}finally{
				
				showRoute("普通架構");
				zeroException();
			}
	}
	public solution(int forZero){
		setSolution(new int[][]{new int[]{0}});
		setCapcity(new int[][]{new int[]{0}});
		setOrder(new int[]{0});
		setLength(new double[]{0});
	try{
			
		}finally{
			
			showRoute("0架構");
			
		}
	}
	
	public void  removedZero(int route){
			if(this.getSolution().get(route).get(0)==0){
				this.getSolution().remove(route);
				this.getCapcity().remove(route);
				this.getLength().remove(route);
				this.getOrder().remove(route);
			}
		
	}
	
	public void  addZero(int route){
			if(this.getSolution().get(route).size()==0){
				this.addSolution(route,0,new solution(0));
			}
			showRoute("增加0");
			zeroException();
	}
	
	public  void addPoint(int route,int site,int value,int cap){
		
		int[][] newRoute=new int[answer.size()][];
		int[][] newCapcity=new int[capcity.size()][];
		this.removedZero(route);
		for(int j=0;j<newRoute.length;j++){
			if(j!=route){
				newRoute[j]=hTA(answer.get(j));
				newCapcity[j]=hTA(capcity.get(j));
			}else{
				if(answer.containsKey(j)){
					for(int k=answer.get(j).size();k>=0;k--){
						if(k<site){
							answer.get(j).put(k,answer.get(j).get(k) );
							capcity.get(j).put(k,capcity.get(j).get(k) );
							
						}else if(k==site){
							answer.get(j).put(site,value );
							capcity.get(j).put(site,cap );
						}else{
							answer.get(j).put(k,answer.get(j).get(k-1) );
							capcity.get(j).put(k,capcity.get(j).get(k-1) );
						}
//						System.out.println();
					}
					
				}else{
					answer.put(j,iTH(new int[]{value}));
					capcity.put(j,iTH(new int[]{cap}));
				}
					
				newRoute[j]=hTA(answer.get(j));
				newCapcity[j]=hTA(capcity.get(j));
			}
		}
		
		answer=dATH(newRoute);
		capcity=dATH(newCapcity);
		showRoute("增加點");
		zeroException();
	}
//	
	public  void addRouteToLast(int[] route,int[] capcityD,double length2,int order){
		
		int[][] newRoute=new int[answer.size()+1][];
		int[][] newCapcity=new int[capcity.size()+1][];
		int[] newOrder=new int[routeOrder.size()+1];
		double[] newLength=new double[length.size()+1];
		for(int j=0;j<newRoute.length;j++){
			if(j<answer.size()){
				newRoute[j]=hTA(answer.get(j));
				newCapcity[j]=hTA(capcity.get(j));
				newOrder[j]=routeOrder.get(j);
				newLength[j]=length.get(j);
			}else{
				newRoute[j]=route;
				newCapcity[j]=capcityD;
				newOrder[j]=order;
				newLength[j]=length2;
			}
		}
		
		answer=dATH(newRoute);
		capcity=dATH(newCapcity);
		routeOrder=iTH(newOrder);
		length=dTH(newLength);
		showRoute("增加路徑");
		zeroException();
	}
//	

//	
	public  void removePointFromRoute(int i,int route){
		
		int[][] newRoute=new int[answer.size()][];
		int[][] newCapcity=new int[capcity.size()][];
		for(int j=0;j<newRoute.length;j++){
			if(j!=route){
				newRoute[j]=hTA(answer.get(j));
				newCapcity[j]=hTA(capcity.get(j));
			}else{
				int[] newR=new int[answer.get(j).size()-1];
				int[] newC=new int[capcity.get(j).size()-1];
				boolean hasPoint=false;
				for(int k=0;k<answer.get(j).size();k++){					
					if(i==answer.get(j).get(k)){
						hasPoint=true;
					}else if(!hasPoint&&k!=answer.get(j).size()-1){
						newR[k]=answer.get(j).get(k);
						newC[k]=capcity.get(j).get(k);
					}else if(hasPoint){
						newR[k-1]=answer.get(j).get(k);
						newC[k-1]=capcity.get(j).get(k);
						
					}
				}
				if(hasPoint){
					newRoute[j]=newR;
					newCapcity[j]=newC;
				}else{
					newRoute[j]=hTA(answer.get(j));
					newCapcity[j]=hTA(capcity.get(j));
				}
			}
		}
		
		answer=dATH(newRoute);
		capcity=dATH(newCapcity);
		addZero(route);
		showRoute("移除指定路徑上的點");
		zeroException();
	}
//	
	public  void removeRoute(int route){
		int[][] newRoute=new int[answer.size()-1][];
		int[][] newCapcity=new int[capcity.size()-1][];
		int[] newOrder=new int[routeOrder.size()-1];
		double[] newLength=new double[length.size()-1];
//		phase2 phase2=new phase2();
//		phase2.showMessage(this, "answer");
		for(int j=0;j<newRoute.length;j++){
			if(j<route){
				newRoute[j]=hTA(answer.get(j));
				newCapcity[j]=hTA(capcity.get(j));
				newOrder[j]=routeOrder.get(j);
				newLength[j]=length.get(j);
			}else{
				newRoute[j]=hTA(answer.get(j+1));
				newCapcity[j]=hTA(capcity.get(j+1));
				newOrder[j]=routeOrder.get(j+1);
				newLength[j]=length.get(j+1);
			}
		}
		
		answer=dATH(newRoute);
		capcity=dATH(newCapcity);
		routeOrder=iTH(newOrder);
		length=dTH(newLength);
		showRoute("移除路徑");
		zeroException();
	}
//	
//	public  void rearrangeByCount(){
//		Integer[] rea =new Integer[routeOrder.length];
//		for(int i=0;i<routeOrder.length;i++){
//			rea[i]=routeOrder[i];
//		}
//		Arrays.sort(rea,Collections.reverseOrder());
//		int[][] newRoute=new int[answer.length][];
//		int[][] newCapcity=new int[capcity.length][];
//		int[] newOrder=new int[routeOrder.length];
//		boolean[] removed=new boolean[routeOrder.length];				
//		for(int i=0;i<removed.length;i++){
//			removed[i]=false;
//		}
//		
//		for(int i=0;i<rea.length;i++){
//			for(int j=0;j<routeOrder.length;j++){
//				if(rea[i]==routeOrder[j]&&!removed[i]){
//					newOrder[i]=routeOrder[j];
//					newRoute[i]=answer[j];
//					newCapcity[i]=capcity[j];
//					removed[i]=true;
//				}
//			}
//		}
//
//		answer=newRoute;
//		capcity=newCapcity;
//		routeOrder=newOrder;
//	}
//	
	public  void rearrangeByLength(){
		Double[] rea =new Double[length.size()];
		for(int i=0;i<length.size();i++){
			rea[i]=length.get(i);
		}
		Arrays.sort(rea,Collections.reverseOrder());
		int[][] newRoute=new int[answer.size()][];
		int[][] newCapcity=new int[capcity.size()][];
		double[] newLength=new double[length.size()];
		int[] order=new int[routeOrder.size()];
		boolean[] removed=new boolean[length.size()];				
		for(int i=0;i<removed.length;i++){
			removed[i]=false;
		}
		
		for(int i=0;i<rea.length;i++){
			for(int j=0;j<length.size();j++){
				if(rea[i]==length.get(j)&&!removed[j]){
					newLength[i]=length.get(j);
					order[i]=routeOrder.get(j);
					newRoute[i]=hTA(answer.get(j));
					newCapcity[i]=hTA(capcity.get(j));
					removed[j]=true;
				}
			}
		}

		answer=dATH(newRoute);
		capcity=dATH(newCapcity);
		length=dTH(newLength);
		routeOrder=iTH(order);
		showRoute("照長度重新排列");
		zeroException();
	}
//	
	public  void addSolution(solution solution){
		
		
		solution.addLength(answer.size());
		answer.putAll(solution.getSolution());
		capcity.putAll(solution.getCapcity());
		routeOrder.putAll(solution.getOrder());
		length.putAll(solution.getLength());
for(int k:getSolution().keySet()){
			
			removePointFromRoute(0,k);
		}
		showRoute("加入其他解");
		zeroException();
	}
	
	public  void addSolution(int route,int route2,solution solution){
		
	
		answer.put(route,solution.getSolution().get(route2));
		capcity.put(route,solution.getCapcity().get(route2));
		routeOrder.put(route,solution.getOrder().get(route2));
		length.put(route,solution.getLength().get(route2));
		showRoute("將指定路徑取代為另一路徑");
		zeroException();
	}
	
	private void addLength(int addedLength){
		
		HashMap<Integer,HashMap<Integer,Integer>> newAnswer=answer;
		HashMap<Integer,HashMap<Integer,Integer>> newCapcity=capcity;
		HashMap<Integer,Integer> newOrder=routeOrder;
		HashMap<Integer,Double> newLength=length;
		answer=new HashMap<Integer,HashMap<Integer,Integer>> ();
		capcity=new HashMap<Integer,HashMap<Integer,Integer>>();
		routeOrder=new HashMap<Integer,Integer> ();
		length=new HashMap<Integer,Double>();
		for(int i=newAnswer.size()-1;i>=0;i--){
			answer.put(addedLength+i, newAnswer.get(i));
		}
		for(int i=newCapcity.size()-1;i>=0;i--){
			capcity.put(addedLength+i, newCapcity.get(i));
		}
		for(int i=newLength.size()-1;i>=0;i--){
			length.put(addedLength+i, newLength.get(i));
		}
		for(int i=newOrder.size()-1;i>=0;i--){
			routeOrder.put(addedLength+i, newOrder.get(i));
		}
		showRoute("擴充長度");
		zeroException();
	}
	
	public solution subSolution(int i){
		int[][] newRoute=new int[][]{hTA(answer.get(i))};
		int[][] newCapcity=new int[][]{hTA(capcity.get(i))};
		double[] newLength=new double[]{length.get(i)};
		int[] order=new int[]{routeOrder.get(i)};
		showRoute("子解");
		zeroException();
		return new solution(newRoute,newCapcity,newLength,order);
	}
	
	public  void setRoute(int route,int[] R,int[] C){
		answer.put(route, iTH(R));
		capcity.put(route, iTH(C));
	}
	public  void setOrder(int[] order){
		routeOrder=iTH(order);
	}
	public  void setOrder(int order,int answer){
		routeOrder.put(order, answer);
	}
	public  void setSolution(int[][] solution){
		answer=dATH(solution);
		
	}
	public  void setCapcity(int[][] solution){
		capcity=dATH(solution);
	}
	
	public  void setLength(double[] solution){
		length=dTH(solution);
	}
	public  HashMap<Integer,Double> getLength(){
		return length;
	}
	public  HashMap<Integer,HashMap<Integer,Integer>> getSolution(){
		return answer;
	}
	public  int[][] solution(){
		return hTDA(answer);
	}
	public  int[][] capcity(){
		return hTDA(capcity);
	}
	public  HashMap<Integer,HashMap<Integer,Integer>> getCapcity(){
		return capcity;
	}
	public  HashMap<Integer,Integer> getOrder(){
		return routeOrder;
	}

	public double[] length(){
		return hTD(length);
	}
	public int[] order(){
		return hTA(routeOrder);
	}
	public void setCapcity(int k, int i, int o) {
		
		capcity.get(k).put(i, o);
	}
	public HashMap<Integer,HashMap<Integer,Integer>> dATH(int[][] solution){
		HashMap<Integer,HashMap<Integer,Integer>> answer=new HashMap<Integer,HashMap<Integer,Integer>>();
		for(int i=0;i<solution.length;i++){
			answer.put(i, new HashMap<Integer,Integer>());
			for(int j=0;j<solution[i].length;j++){
				answer.get(i).put(j,solution[i][j]);
//				System.out.println(solution[i][j]+" "+answer.get(i).get(j));
			}
		}
		return answer;
	}
	
	public int[][] hTDA(HashMap<Integer,HashMap<Integer,Integer>> solution){
		int[][] da=new int[solution.size()][];
		for(int i=0;i<da.length;i++){
			da[i]=new int[solution.get(i).size()];
			for(int j=0;j<da[i].length;j++){
				da[i][j]=solution.get(i).get(j);
			}
		}
		return da;
	}
	public int[] hTA(HashMap<Integer,Integer> solution){
		int[] a=new int[solution.size()];
		for(int i=0;i<a.length;i++){
				a[i]=solution.get(i);
			
		}
		return a;
	}
	
	public double[] hTD(HashMap<Integer,Double> solution){
		double[] a=new double[solution.size()];
		if(a.length>0){
			for(int i=0;i<a.length;i++){
				a[i]=solution.get(i);
			
			}	
		}
		
		return a;
	}
	
	public HashMap<Integer,Double> dTH(double[] solution){
		HashMap<Integer,Double> answer=new HashMap<Integer,Double>();
		for(int i=0;i<solution.length;i++){
			answer.put(i, solution[i]);
			
		}
		return answer;
	}
	public HashMap<Integer,Integer> iTH(int[] solution){
		HashMap<Integer,Integer> answer=new HashMap<Integer,Integer>();
		for(int i=0;i<solution.length;i++){
			answer.put(i, solution[i]);
			
		}
		return answer;
	}

	public void setLength(int k, double d) {
		length.put(k,d);
		
	}
	public boolean contain(int i,int route ){
		for(int j:this.getSolution().get(route).keySet()){
			if(this.getSolution().get(route).get(j)==i){
				return true;
			}
		}
		return false;
	}
	public boolean contain(int point){
		for(int i:this.getSolution().keySet()){
			for(int j:this.getSolution().get(i).keySet()){
				if(this.getSolution().get(i).get(j)==point){
					return true;
				}
			}
			
		}
		
		return false;
	}
	public  void showRoute(String message){
		if(debug){
		    	System.out.println(message);
		    	for(Integer k:getSolution().keySet()){		
				System.out.println("路徑="+k+"："+getSolution().get(k)+"長度"+k+"："+getLength().get(k));
				System.out.println("需求="+k+"："+getCapcity().get(k)+"順序"+k+"："+getOrder().get(k));	
			}
		}	
	}
	
	public  void showRoute(String message,boolean direct){
		    	System.out.println(message);
		    	for(Integer k:getSolution().keySet()){		
				System.out.println("路徑="+k+"："+getSolution().get(k)+"長度"+k+"："+getLength().get(k));
				System.out.println("需求="+k+"："+getCapcity().get(k)+"順序"+k+"："+getOrder().get(k));	
			}
			
	}
	private void zeroException(){
	//	Exception x= new Exception();
		if(dontWantZero&&getSolution().size()>0){
			for(int l:getSolution().keySet()){
				for(int k:getSolution().get(l).keySet()){
					if(getSolution().get(l).get(k)==0&&getSolution().get(l).size()>1){
						try {
							System.out.println("你做錯事了");
							this.showRoute("這次錯誤的路徑",true);
							System.exit(1);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
			
		}

	}
	
	public void samePointException(){
		
		if(dontWantZero&&getSolution().size()>0){
			for(int l:getSolution().keySet()){
				for(int k:getSolution().get(l).keySet()){
					if(getSolution().get(l).size()>1&getSolution().get(l).get(k+1)==getSolution().get(l).get(k)){
						try {
							System.out.println("你已經死了");
							this.showRoute("這次錯誤的路徑",true);
							System.exit(1);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
			
		}
	}
	
}
