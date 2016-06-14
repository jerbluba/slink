//import java.awt.Point;
//import java.io.BufferedReader;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.LinkedList;
//import java.util.Random;
//public class CopyOfphase2 {
//	
//	private static solution bestSolution;
//	private static solution tempSolution;
//	private static int theta=0;//代數，移出此程式，每次ＣＯＵＮＴ不德超過此代數
//	private static slink slink;
//	private static Integer tabuLength=0;// 點+路,TABU的路徑長度
//	//HashMap<i,HashMap<j,k>>i點,第j個,k值
//	private static solution orderedRoutes;
//	private static solution R;
//	private static solution U;
//	private static int[] subRouteU;		
//	private static int p=0;	
//	//Integer個點對應 路徑Integer[]Integer  替物件命名就可抓到
//	//被移出過的
//	private static HashMap<Integer,HashMap<Integer,Integer[]>> routeU;
//	//被移入過的
//	private static HashMap<Integer,HashMap<Integer,Integer[]>> routeR;
//	//要被移入的
////	private static ArrayList<Integer[]> R ;
////	private static ArrayList<Integer[]> dR ;
////	//要被移出的
////	private static HashMap<Integer,Integer[]> U;
////	private static ArrayList<Integer[]> dU ;
//	private static int[] rstar;//被插入的路徑
//	
//	
//	private static int bestI;
//	private static int[] bestR;
//	
//	public static int[] getSubRouteU(){
//		return subRouteU;
//	}	
//	
//	public static void setSubRouteU(int[] subRouteU){
//		CopyOfphase2.subRouteU=subRouteU;
//	}
//	
//	public static HashMap<Integer,HashMap<Integer,Integer[]>>getRouteR(){
//		return routeR;
//	}
//	
//	public static void setBestI(int bestI){
//		CopyOfphase2.bestI=bestI;
//	}
//	
//	public static int getBestI(){
//		return bestI;
//	}
//	
//	
//	
//	public static solution getBestSolution(){
//		return bestSolution;
//	}	
//	
//	
//	public static int p(int[] quality){
//		int answer=0;
//		for(int i=0;i<quality.length;i++){
//			answer+=quality[i];			
//		}
//		return slink.getCapcity()-answer;
//	}
//	
//	//單純計算一條路徑的總長度
//	public static double length(int[] route){
//		double answer=0;
//		for(int j=0;j<route.length;j++){
//			if(j==0){
//				answer+=slink.getPoints().get(1).distance(slink.getPoints().get(route[j]));
//			}else{
//				answer+=slink.getPoints().get(route[j-1]).distance(slink.getPoints().get(route[j]));
//			}
//		}
//		answer+=slink.getPoints().get(1).distance(slink.getPoints().get(route[route.length-1]));
//		return answer;
//	}
//	
//	public static double bestTravelDistance(int[][] solution){//F=f(s)=BestValue
//		double answer=0;
//		for(int i=0;i<solution.length;i++){
//			answer+=length(solution[i]);
//		}
//		return answer;
//	};
//	
//	public static double depo(int i){//與原點距離
//		return slink.getPoints().get(i)
//				.distance(slink.getPoints().get(1));
//	}
//	public static double distance(int i,int j){//與原點距離
//		return slink.getPoints().get(i)
//				.distance(slink.getPoints().get(j));
//	}
//		
//	//求最大，配order route
//	public static double maxS(int[][] solution,int pointI){
//		//要對所有路徑去考慮是否存在pointI，若有，納入考慮
//		double max=0;
//		int count=0;
//		//順便做出ＯＲＤＥＲＲＯＴＵＥ（Ｉ）
//		ArrayList<Double> temp=new ArrayList<Double>();
//		HashMap<Integer,int[]> tempRoutes=new HashMap<Integer,int[]>();
//		
//			for(int k=0;k<solution.length;k++){				
//				for(int j=0;j<solution[k].length;j++){
//					if(pointI==solution[k][j]){
//						if(j==0){
//							max=Math.max(max,
//									distance(pointI,solution[k][1])
//									+depo(pointI)
//									-depo(solution[k][1])
//									
//									);
//							tempRoutes.put(count,new int[]{1,pointI,solution[k][1]});
//						}else if(j==(solution[k].length-1)){
//							max=Math.max(max,
//									distance(pointI,solution[k][solution[k].length-2])
//									+depo(pointI)
//									-depo(solution[k][solution[k].length-2])
//									
//									);
//							tempRoutes.put(count,new int[]{solution[k][solution[k].length-2],pointI,1});	
//						}else{
//							max=Math.max(max,
//									distance(pointI,solution[k][j-1])
//									+distance(pointI,solution[k][j+1])
//									-distance(solution[k][j-1],solution[k][j+1])
//									);
//							tempRoutes.put(count,new int[]{solution[k][j-1],pointI,solution[k][j+1]});
//						}	
//						temp.add(count,max);
//						++count;
//					}
//				}	
//			}				
//		
//		if(temp.size()>1){
//			temp.toArray(new Double[]{});
//		}
//		
//		Double[] routeLength= temp.toArray(new Double[]{});
//		Arrays.sort(routeLength,Collections.reverseOrder());
//		
//		int[][] route=new int[count][];
//		boolean[] removed=new boolean[count];
//		
//		for(int i=0;i<removed.length;i++){
//			removed[i]=false;
//		}
//		
//		for(int i=0;i<route.length;i++){
//			for(int j=0;j<routeLength.length;j++){
//				if(!removed[j]){
//					if(temp.get(j)==routeLength[i]){
//						route[i]=tempRoutes.get(i);
//						temp.remove(j);
//						removed[j]=true;
//					}
//				}
//			}
//		}
//		int[][] answer=new int[route.length][];
//		for(int i=0;i<route.length;i++){
//			answer[i]=new int[]{0,slink.getQuality(route[i][1]),0};
//			
//		}
//		
//		orderedRoutes.setSolution(route);		
//		orderedRoutes.setCapcity(answer);		
//		return max;
//	}
//	
//	public static double sum(int[][] solution){
//		double answer=0;
//			for(int k=0;k<slink.getPointNum();k++){
//				answer+=depo(k+2)*maxS(solution,k+2);
//			}
//		
//		return answer/slink.getPointNum();
//		
//	}
//	
//	
//    // R or U剛移除/加入過i 就是ＴＡＢＵ
//    public static boolean isTabu(int pointI,boolean map,int[] route){//R=true U=false
//    		if(map){
//			if(routeR.containsKey(pointI)){
//				for(int i=0;i<routeR.get(pointI).size();i++){
//					boolean isSame=true;
//					if(route.length==routeR.get(pointI).get(i).length){
//						for(int j=0;j<route.length;j++){
//							if(route[j]!=routeR.get(pointI).get(i)[j]){
//								isSame=false;
//							}
//						}
//					}
//					if(isSame){
//						return true;
//					}					
//				}    				
//			}   
//    		}else{
//    			if(routeU.containsKey(pointI)){
//    				for(int i=0;i<routeU.get(pointI).size();i++){
//    					boolean isSame=true;
//    					if(route.length==routeU.get(pointI).get(i).length){
//    						for(int j=0;j<route.length;j++){
//    							if(route[j]!=routeU.get(pointI).get(i)[j]){
//    								isSame=false;
//    							}
//    						}
//    					}
//    					if(isSame){
//    						return true;
//    					}					
//    				}    				
//    			}   
//    		}    		
//    		return false;
//    }
//    
//    public static boolean isSameRoute(int[] integers,int[] routeR2){
//    		if(integers.length!=routeR2.length){
//    			return false;
//    			
//    		}else{
//    			boolean answer=true;
//				
//    			for(int i=0;i<integers.length;i++){
//    				if(integers[i]!=routeR2[i]){
//    					answer=false;
//    				}
//    				
//    				
//    			}
//    			return answer;
//    		}
//    	
//    }
//    
//  public static int d(int i,int r,solution solution){
//	  int answer=0;
//	  for(int j=0;j<solution.getSolution()[r].length;j++){
//		  if(solution.getSolution()[r][j]==i){
//			  answer=j;
//		  }		  
//	  }
//	  return 	  solution.getCapcity()[r][answer];
//  }
//    
//    
//    
//    // TODO 在solution 中把i點插入某route的solution 也就是s'
//    public static solution solution(int i, int route,int[][] solution){
//    		double answer = 0;
//    		int[] answer2=new int[solution[route].length+1];
//    		int answer3=0;
//    		for(int j=0;j<solution[route].length;j++){
//    			double tempAnswer=0;
//    			if(j==0){
//    				answer=slink.getPoints().get(i).distance(slink.getPoints().get(1))+
//    						slink.getPoints().get(i).distance(slink.getPoints().get(solution[route][j]))+
//    						slink.getPoints().get(solution[route][j]).distance(slink.getPoints().get(1));
//    				
//    			}else if(j==(solution[route].length-1)){
//    				tempAnswer=Math.min(answer,
//    						slink.getPoints().get(i).distance(slink.getPoints().get(1))+
//    						slink.getPoints().get(i).distance(slink.getPoints().get(solution[route][solution[route].length-1]))+
//    						slink.getPoints().get(solution[route][solution[route].length-1]).distance(slink.getPoints().get(1))
//    						
//    						);
//    				
//    			}else{
//    				tempAnswer=Math.min(answer,
//    						slink.getPoints().get(i).distance(slink.getPoints().get(1))+
//    						slink.getPoints().get(i).distance(slink.getPoints().get(solution[route][j]))+
//    						slink.getPoints().get(solution[route][j]).distance(slink.getPoints().get(1))
//    						
//    						);
//    			}
//    			if(tempAnswer!=answer){
//					answer3=j;
//					
//				}
//    		}
//    		for(int j=0;j<answer2.length;j++){
//    			int answer4=0;
//    			if(j<answer3){
//    				answer4=solution[route][j];
//    			}else if(j==answer3){
//    				answer4=i;
//    				
//    			}else{
//    				answer4=solution[route][j-1];
//    				
//    			}
//    			answer2[j]=answer4;
//    			
//    		}
//    	
//    		int[][] answer5=new int[solution.length][];
//    		for(int j=0;j<answer5.length;j++){
//    			
//    			answer5[j]=solution[j];
//    			if(j==route){
//    				answer5[j]=answer2;
//    			}
//    		}
//    		// TODO
//    		for(Integer u:U.keySet()){
//    			HashMap<Integer,Integer[]> answer6=new HashMap<Integer,Integer[]>();
//    			routeU.put(i, answer6);
//    			
//    		}
//    	return answer5;
//    		
//    }
//    
//    public static void setRouteR(Integer[] route,Integer pointI){
//		int routeRSize=0;
//		HashMap<Integer,Integer[]> answer=new HashMap<Integer,Integer[]>();
//		if(routeR.size()<tabuLength){
//			routeR.put(routeR.size(), answer);			
//		}else{
//			routeR.remove(0);
//			for(int i=1;i<routeR.size();i++){
//				routeR.put(i-1, routeR.get(i));
//				
//			}
//			routeR.put(routeR.size(), answer);			
//		}
//		
//	}
//    
//    // TODO 被插入的路徑
//    public static int[] rstar(int i, HashMap<Integer,Integer[]> U){
//		return new int[]{};
//		
//    }
//    //代做到Ｆ（Ｓ）小於最小的才可為最佳解     最衰n(n-1)次
//    //tempSolution
//    public static solution bestNeighbor(solution tempSolution){
//    		solution answer;
//		orderedRoutes=new solution(new int[][]{},new int[][]{});
//		
//		for(int i=2;i<slink.getPointNum()+2;i++){
//		
//			if(depo(i)*maxS(tempSolution.getSolution(),i)
//				<=sum(tempSolution.getSolution())){//篩選條件
//				//建立R
//				
//				Integer count=0;
//				HashMap<Integer,int[]> solutionA=new HashMap<Integer,int[]>();
//				HashMap<Integer,int[]> solutionB=new HashMap<Integer,int[]>();
//				
//				R=new solution();
//				for(int s=0;s<tempSolution.getSolution().length;s++){
//					if(slink.getCapcity(tempSolution.getSolution()[s])!=slink.getCapcity()){//先尋找未滿的路徑
//						int[] temp=new int[tempSolution.getSolution()[s].length];
//						for(int j=0;j<temp.length;j++){
//							temp[j]=tempSolution.getSolution()[s][j];
//						}
//						int[] temp2=new int[tempSolution.getCapcity()[s].length];
//						for(int j=0;j<temp2.length;j++){
//							temp2[j]=tempSolution.getCapcity()[s][j];
//						}
//						
//						solutionA.put(count, temp);
//						solutionB.put(count, temp2);
//						++count;
//					}
//				}
//				int[][] getA=new int[solutionA.size()][];
//				int[][] getB=new int[solutionB.size()][];
//				for(int k=0;k<getA.length;k++){
//					getA[k]=solutionA.get(k);
//					getB[k]=solutionB.get(k);
//				}
//				
//				if(solutionA.size()==0){
//					getA=new int[][]{new int[]{}};
//					getB=new int[][]{new int[]{}};
//				}
//				
//				R=new solution(getA,getB);
//			
//    				for(int[] j:R.getSolution()){
//    					int tempCount=0;
//    					if(isTabu(i,true,j)){//2.2
//    						// TODO or Uti contains a route u with diu<=pr
//    						p=slink.getCapcity();//p=pr
//    						U=new HashMap<Integer,Integer[]> ();//set U=空集合
//    						// TODO diu<=pr U={u} p=pr-diu
//    						for(int k=0;k<orderedRoutes.get(i).length;k++){
//    							if(!isSameRoute(orderedRoutes.get(i)[k],routeR[j])){
//    								if(U.size()>0){
//    									for(int u:U.keySet()){
//        									if(!isSameRoute(U.get(u),routeR[j])&&p>=d(i,orderedRoutes.get(i)[k])){
//        										U.put(tempCount, orderedRoutes.get(i)[k]);
//            									p-=d(i,orderedRoutes.get(i)[k]);
//            									++tempCount;
//        									}   									
//        								}
//    									
//    								}else if(p>=d(i,orderedRoutes.get(i)[k])){
//    									U.put(tempCount, orderedRoutes.get(i)[k]);
//    									p-=d(i,orderedRoutes.get(i)[k]);
//    									++tempCount;
//    									
//    								}
//    								
//    								if(bestTravelDistance>bestTravelDistance(solution(i,U,solution))){
//        								bestTravelDistance=Math.min(bestTravelDistance, bestTravelDistance(solution(i,U,solution)));
//            							bestSolution=solution(i,U,solution);
//        								Ustar=U;
//        								rstar=rstar(i, U);
//        							}
//    							}
//    						}
//    					}else{//2.1
//    						//(1)
//    						p=p(j);
//    						U=new solution();
//    						//(2)
//
//    						for(int k=0;k<orderedRoutes.getSolution().length;k++){
//    							if(!isSameRoute(orderedRoutes.getSolution()[k],j)){
//    								if(!isTabu(i,false,orderedRoutes.getSolution()[k])&&p>=d(i,k,orderedRoutes)){
//    									U.addRouteToLast(orderedRoutes.getSolution()[k], orderedRoutes.getCapcity()[k]);
//    									p-=d(i,k,orderedRoutes);
//    									++tempCount;
//    								}
//    							}
//    						}
//    						
//    						if(U.size()==0){
//    							boolean temp=true;
//    							for(int k=0;k<orderedRoutes.getSolution()[i].length;k++){
//        							if(!isSameRoute(orderedRoutes.getSolution()[i][k],routeR[j])){
//        								if(!isTabu(i,orderedRoutes.getSolution()[i][k])&&temp){
//        									U.put(0, orderedRoutes.getSolution()[i][k]);
//        									
//        									// TODO diu dir
//        									
//        									if(bestTravelDistance>bestTravelDistance(solution(i,U,solution))){
//	        	    								bestTravelDistance=Math.min(bestTravelDistance, bestTravelDistance(solution(i,U,solution)));
//	        	        							bestSolution=solution(i,U,solution);
//	        	    								Ustar=U;
//	        	    								rstar=rstar(i, U);
//        	    								}
//        									temp=false;
//        								}
//        							}
//        						}
//    							
//    						}else{//(3)
//    							Integer routeJ=0;
//    							boolean searchJ=true;
//    							for(int k=0;k<tempSolution.getSolution().length;k++){
//    								if(j.length==tempSolution.getSolution()[k].length){
//    									for(int l=0;l<j.length;l++){
//    										if(j[l]!=tempSolution.getSolution()[k][l]){
//    											searchJ=false;
//    										}
//    									}
//    									if(searchJ){
//										routeJ=k;
//									}
//    								}
//    							}
//    							solution answerJ=solution(i,routeJ,tempSolution.getSolution());
//							if(bestTravelDistance(tempSolution.getSolution())>bestTravelDistance(answerJ.getSolution())){
//								answer=answerJ;
//							}
//    						}
//    						
//    					}	
//    				}
//    				    				
//    			}    
//			
//			
//		}
//		return answer;
//    }
//    
//    public static void doBestNeigbhor(slink mySlink,solution solution,int myTheta){
//    		int count=0;
//    		theta=myTheta;
//    		boolean hasBest=false;
//    		slink=mySlink;
//    		bestSolution=solution;
//		tempSolution=bestSolution;
//    		routeR=new HashMap<Integer,HashMap<Integer,Integer[]>>();
//    		routeU=new HashMap<Integer,HashMap<Integer,Integer[]>>();
//    		tabuLength=slink.getPoints().size()-1+slink.getAnswer().length;
//    		
//		for(int i=0;i<theta;i++){  
//			count=0;
//			while(!hasBest&&count<theta){    				
//				tempSolution=bestNeighbor(tempSolution);    				
//    				if(bestTravelDistance(tempSolution.getSolution())<bestTravelDistance(bestSolution.getSolution())){
//    					bestSolution=tempSolution;
//    					count=theta;
//    				}else if(count!=(theta-1)){
//    					++count;
//    				}else{
//    					hasBest=true;
//    				}
//    			}    			
//    		}
//    		System.out.println("最佳路徑長度為"+CopyOfphase2.bestTravelDistance(bestSolution.getSolution()));
//    		for(int i=0;i<CopyOfphase2.getBestSolution().getSolution().length;i++){
//    			System.out.println("最佳路徑為"+CopyOfphase2.getBestSolution().getSolution()[i]);    			
//    		}
//    		System.out.println();
//    }
//    
//    
//    
//    
//    public static void main(String[] args){
//    		
//    	
//    }
//}
