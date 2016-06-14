import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;
public class phase2 {
	//length代表這兩組solution的路徑識別
	private  solution bestSolution;
	private  solution tempSolution;
	private  int theta=0;//代數，移出此程式，每次ＣＯＵＮＴ不德超過此代數
	private  slink slink;
	private  Integer tabuLength=0;// 點+路,TABU的路徑長度
	//length代表這條路徑的S(u) routeOrder對應到tempSolution 的Order
	private  solution orderedRoutes;
	private  int p=0;	
	//Integer當點，solution的length當代數 order當作識別此條路徑的號碼
	//被移出過的
	private  HashMap<Integer,solution> routeU;	
	private  HashMap<Integer,solution> tempU;//暫存值
	//被移入過的
	private  HashMap<Integer,solution> routeR;
	private  HashMap<Integer,solution> tempR;//暫存值
	//審查值 審查後加入暫存
	private  solution R;
	private  solution U;
	//暫存值，直接加入對應temp	
	private  HashMap<Integer,solution> tempRR;
	private  HashMap<Integer,solution> tempUU;
	private  int rstar=0;//被移入的路徑編號
	
	private  int bestRstar=0;//被移入的路徑編號
	private  Integer[] Ustar;//被移出的路徑編號集合
	private  Integer[] bestUstar;//被移出的路徑編號集合
	private  int bestI;
	private  int startPoint=1; //slink中點的起始
	public  HashMap<Integer,solution> getRouteR(){
		return routeR;
	}
	
	public  void setBestI(int bestI){
		this.bestI=bestI;
	}
	
	public  int getBestI(){
		return bestI;
	}
	
	public  solution getBestSolution(){
		return bestSolution;
	}	
	
	//此路徑剩餘需求量
	public  int p(HashMap<Integer,Integer> quality){
		int answer=0;
		for(int i:quality.keySet()){
			answer+=quality.get(i);			
		}
		return slink.getCapcity()-answer;
	}
	
	//單純計算一條路徑的總長度
	public  double length(HashMap<Integer,Integer> route){
		double answer=0;
		if(route==null){
			return 0;
		}
		if(route.size()>0){
			for(int j:route.keySet()){
//				System.out.println(route[j]);
				if(route.get(j)!=0){
					if(j==0){
						answer+=depo(route.get(j));
					}else{
						if(route.get(j-1)!=0){
							answer+=distance(route.get(j-1),route.get(j));
						}else{
							answer+=depo(route.get(j));
						}					
					}				
				}
			}
			if(route.size()>1){
				answer+=depo(route.get(route.size()-1));
			}
		}
		
		
		return answer;
	}
	//單純計算整組路徑的長度
	public  double bestTravelDistance(HashMap<Integer,HashMap<Integer,Integer>>  solution){//F=f(s)=BestValue
		double answer=0;
		for(int i=0;i<solution.size();i++){
			answer+=length(solution.get(i));
		}
		return answer;
	};
	
	public  double depo(int i){//從分群中找出i與原點距離
		return slink.getPoints().get(i)
				.distance(slink.getPoints().get(0));
	}
	public  double distance(int i,int j){//從分群中找出i j兩點的距離
//		System.out.println("點"+i+"與點"+j+"的距離");
		return slink.getPoints().get(i)
				.distance(slink.getPoints().get(j));
	}
		
	//求最大S(pointI)，如果doOrder，將配置orderRoute
	public  double maxS(solution solution,int pointI,boolean doOrder){
		double max=0;
	//	if(pointI==1)solution.showRoute("原組件", true);
		//要對所有路徑去考慮是否存在pointI，若有，納入考慮  
			for(int k:solution.getSolution().keySet()){					
				for(int j:solution.getSolution().get(k).keySet()){
					//如果為第k條路徑中的第j點，計算S(pointI)
					//distance(pointI,solution.solution()[k][j+1])+distance(pointI,solution.solution()[k][j-1])-distance(solution.solution()[k][j+1],solution.solution()[k][j-1])
//					System.out.println("第"+k+"條,第"+j+"點");
					double length=0;
					if(pointI==solution.getSolution().get(k).get(j)&&solution.getSolution().get(k).size()!=1){
						if(j==0){
							length=distance(pointI,solution.getSolution().get(k).get(1))
									+depo(pointI)-depo(solution.getSolution().get(k).get(1));
							max=Math.max(max,length);
						}else if(j==(solution.solution()[k].length-1)){
							length=distance(pointI,solution.solution()[k][solution.solution()[k].length-2])
									+depo(pointI)-depo(solution.solution()[k][solution.solution()[k].length-2]);
							max=Math.max(max,length);
						}else{
							length=distance(pointI,solution.solution()[k][j-1])+distance(pointI,solution.solution()[k][j+1])
									-distance(solution.solution()[k][j-1],solution.solution()[k][j+1]);
							max=Math.max(max,length);
						}
						
						if(doOrder)orderedRoutes.addRouteToLast(solution.solution()[k],solution.capcity()[k],length,solution.order()[k]);//若有點，加入O(i)
//						orderedRoutes.showRoute("e01", true);
					}else if(pointI==solution.solution()[k][j]&&solution.getSolution().get(k).size()==1){
						if(doOrder)orderedRoutes.addRouteToLast(solution.solution()[k],solution.capcity()[k],length,solution.order()[k]);//若有點，加入O(i)
//						orderedRoutes.showRoute("e02", true);
					}
				}	
			}			
		//配置orderRoute	,如果count>0才有必要製作
		if(doOrder&&orderedRoutes.getSolution().size()>0){
			//將對照值交給O(i)後，照遞減方式排列
			orderedRoutes.rearrangeByLength();
		//	orderedRoutes.showRoute("OO", true);
		}
//		orderedRoutes.showRoute("e03", true);
		if(orderedRoutes.getSolution().size()>solution.getSolution().size()
				){
			System.out.println("O做錯了");
			orderedRoutes.showRoute("e04", true);
			solution.showRoute("ZZ",true);
			System.exit(1);
		}
		
		return max;
	}
	
	//depo(i)*max(S(i))<=sum(1,n,depo(j)*max(S(j)))/n
	//0<=sum(1,n,depo(j)*max(S(j)))/n-depo(i)*max(S(i))
	//0<=sum(1,n,depo(j)*max(S(j))-depo(i)*max(S(i)))/n
	//0<=sum(1,n,depo(j)*max(S(j))-depo(i)*max(S(i)))
	public  double sum(solution solution,int i){
		double answer=0;
			for(int k=startPoint;k<slink.getPointNum()+startPoint;k++){
//				System.out.println(k);
				
				if(k!=i){
					answer+=depo(k)*maxS(solution,k,false);
				}else{
					answer-=(depo(k)*maxS(solution,k,true)*(slink.getPointNum()-1));
				}
			}
			
			System.out.print("點"+i+"值"+(answer>0?"大於零，滿足條件，開始執行phase2":"小於零，跳至下一個點。"));
		return answer;
		
	}
	
	
    // routeR or routeU中有route,就是pointI的tabu
    public  boolean isTabu(int pointI,boolean map,int route,solution solution){//R=true U=false
    		if(map){
			if(routeR.containsKey(pointI)){
				for(int i:routeR.get(pointI).getSolution().keySet()){
					if(routeR.get(pointI).getOrder().get(i)
							!=solution.getOrder().get(route)){
						return true;
					}					
				}    				
			}   
    		}else{
    			if(routeU.containsKey(pointI)){
    				for(int i:routeU.get(pointI).getSolution().keySet()){
    					if(routeU.get(pointI).getOrder().get(i)
    							!=solution.getOrder().get(route)){
    						return true;
    					}					
    				}    				
    			}   
    		}    		
    	//	System.out.print("路徑"+route+"不是"+pointI+"的tabu  ");
    		return false;
    }
    
    public  int d(int i,int r,solution solution){ 
    	//應該是從存在的路徑中，計算它的需求量
    	   int answer=0;
    	   
    	  // solution.showRoute("取第"+r+"條，點"+i, true);
    	//   System.out.println("d的size"+solution.getSolution().size());
    	    if(solution.getSolution().containsKey(r)){
//    	     System.out.println("d的r"+solution.getSolution().get(r));
    	     for(int j:solution.getSolution().get(r).keySet()){
    	      if(solution.getSolution().get(r).get(j)==i){
    	       answer=j;
    	      }
    	     }
    	    }    
    	   
    	   return solution.getCapcity().get(r).get(answer);
    	  }
    
    
    
    // 把U中所有的i點插入某solution的route 此方法將回傳solution s'  
  /* rule1:選最小的S(i)進入
   * rule2:若此路徑編號已存在routeR第i項中，則不可使用
   * rule3:U(i)照順序拔除,順序參考O(i)
   * */
    public  solution insert(int i, int route,solution solution,boolean uorr){
    	solution returnAnswer=new solution();
    	returnAnswer.addSolution(solution);
    	//solution中的路徑必含i，
    	//route為R中之路徑
    	System.out.println("將點"+i+"插入路徑"+(uorr?"R":"U")+"("+route+")中");
    		if(p<slink.getCapcity()){
    			solution subU=new solution();//U的暫存值，被移出點的才會加入此集合
    			solution subR=new solution();//R的暫存值，被移入點的才會加入此集合
    			
    			int capR=0;
    			int answer4=-1;
    			int tempCount=0;
    			ArrayList<Integer> tempUstar=new ArrayList<Integer>();
    			//處理被移除的U
//    			System.out.println("處理U");
        		for(int l=0;l<U.getSolution().size();l++){
        			answer4=-1;
        			for(int k=0;k<U.getSolution().get(l).size();k++){
        				if(U.getSolution().get(l).get(k)==i){
        					answer4=k;
        				}
        			}
        			if(answer4>-1){
        			//	subU=new solution();
//        				System.out.println("進行拔點");
        				tempUstar.add(tempCount, l);
        				if(p-U.capcity()[l][answer4]
    							>=0){
            				capR+=U.capcity()[l][answer4];
            				U.removePointFromRoute(i, l);
        					returnAnswer.setRoute(U.getOrder().get(l), U.solution()[l],U.capcity()[l] );
    					subU.addSolution(U.subSolution(l));
	    				}else{
	    					int o=U.capcity()[l][answer4]-p; 
	    				     capR+=(U.capcity()[l][answer4]-o);
	    				     U.setCapcity(l,answer4,o);
     					 returnAnswer.setRoute(U.getOrder().get(l),U.solution()[l] ,U.capcity()[l] );
	    				     subU.addSolution(U.subSolution(l));
	    				}
                		++tempCount;
        			}
        		}
        		int keyK=0;int keyJ=0;boolean destory=false;
        		Ustar=tempUstar.toArray(new Integer[]{});
        		//System.out.println("Ustar長度:"+Ustar.length);
        		//將路徑加入tabuListU
//        		System.out.println("加入tabuU");
        		for(int l=0;l<Ustar.length;l++){
        		//	System.out.println("Ustar["+l+"]:"+Ustar[l]);
        			if(getLength(false)>=tabuLength){
            			for(int j:tempUU.keySet()){
        					for(int k:tempUU.get(j).getLength().keySet()){
        				//		tempUU.get(j).showRoute("又是你，幹！點"+j+"的tabuList,第"+k+"條路徑", true);
    							if(tempUU.get(j).getLength().get(k)==0){	
    								keyK=k;
    								keyJ=j;
    								destory=true;		
    							}else{
    								tempUU.get(j).setLength(k, tempUU.get(j).getLength().get(k)-1);
    							}
    						}
            			}
            		}
        			if(destory){
            			//System.out.println(tempUU.containsKey(keyJ));;
//        				tempUU.get(keyJ).showRoute("好恐怖：", true);
            			tempUU.get(keyJ)
            			.removeRoute(keyK);
    					if(tempUU.get(keyJ).getLength().size()==0){
    						tempUU.remove(keyJ);		
    					}
        			}

            		subU.getLength().put(l,(double) getLength(false));
            		//.setLength(new double[]{getLength(false)});
            		
            		if(tempUU.containsKey(i)){
                		tempUU.get(i).addSolution(subU.subSolution(l));
                		
            		}else{
            			tempUU.put(i,subU.subSolution(l));
            		}
        			
        		}
        		

    			//處理Ｒ,確定此點存在於Ｕ才可進行搬移
        		//if(answer4>-1){
        			double answer = 0;//S(i)
            		int answer3=-1;//被選擇的點前方
//            		System.out.println("處理R");
            		//取最小的S(i)，j為終點,上一個點為起點
            		if(!R.getSolution().get(route).containsValue(0)){
            			if(R.contain(i, route)){
            				for(int key:R.getSolution().get(route).keySet()){
            					if(R.getSolution().get(route).get(key)==i){
                   				answer3=key;
            					}
            				}
            			}else{
            				for(int j=0;j<R.solution()[route].length+1;j++){
                    			double tempAnswer=answer;//S(i)暫存值
                    			if(j==0){//answer初始值
                    				answer=depo(i)+
                    						distance(i,R.solution()[route][0])-
                    						depo(R.solution()[route][0]);
                    			}else if(j==(R.solution()[route].length)){
                    				answer=Math.min(answer,
                    						depo(i)+
                    						distance(i,R.solution()[route][R.solution()[route].length-1])-
                    						depo(R.solution()[route][R.solution()[route].length-1])
                    						);
                    			}else{
                    				answer=Math.min(answer,
                    						distance(i,R.solution()[route][j])+
                    						distance(i,R.solution()[route][j-1])-
                    						distance(R.solution()[route][j],R.solution()[route][j-1])	
                    						);
                    			}
                    			//暫存值與原值不同，改變選定的位置
                    			if(tempAnswer!=answer){
                					answer3=j;
                    			}	
                    		}
            				
            			}
            			
            		}
           // 		System.out.println("將插入位置"+answer3);
//            		System.out.println("插入點");
//            		R.showRoute("R", true);
            		//插點換值
            		if((!R.contain(i, route))&&answer3>-1&&R.getSolution().get(0).get(0)!=0){
            			
//            			System.out.println((!R.contain(i, route))+" "+(answer3>-1)+" "+(R.getSolution().get(0).get(0)!=0));
//            			System.out.println(route+"中無此點"+i);
            			R.addPoint(route, answer3,i, capR);
            		}else if(R.contain(i, route)&&R.getSolution().get(0).get(0)!=0&&answer3>-1){//已存在點i
//            			System.out.println(route+"中有此點"+i);	
            			R.setCapcity(route, answer3, 
            					capR+R.getCapcity().get(route).get(answer3));
            		}else if(R.getSolution().get(0).get(0)==0){//空路徑        	
//            			System.out.println("空der");
            			R.setCapcity(route, 0, capR);
            			R.getSolution().get(route).put(0, i);
            		}
            		
            		R.samePointException();
            		returnAnswer.setRoute(rstar,
            				R.solution()[route] ,
            				R.capcity()[route]);
            		
            		subR.addSolution(R.subSolution(route));
//            		if(subR.getSolution().size()!=1){
//            			for(int l:subR.getSolution().keySet()){
//            				subR.removePointFromRoute(0, l);
//            			}
//            			
//            		}
            		keyK=0;keyJ=0;destory=false;
//            		System.out.println("加入tabuR");
            		//將點加入tabuList
            		if(getLength(true)>=tabuLength){
            			for(int j:tempRR.keySet()){
        					for(int k:tempRR.get(j).getLength().keySet()){
        					//	tempRR.get(j).showRoute("老哥耶", true);
        					//	System.out.println("第"+j+" "+k);
    						if(tempRR.get(j).getLength().get(k)==0){
    							keyK=k;
    							keyJ=j;
    							destory=true;
    						}else{
    							tempRR.get(j).setLength(k, tempRR.get(j).getLength().get(k)-1);
    						}
    					}
    				}
            		}
            		if(destory){
            			tempRR.get(keyJ)
            			.removeRoute(keyK);
    					if(tempRR.get(keyJ).getSolution().size()==0){
    						tempRR.remove(keyJ);		
    					}
        			}
            		subR.setLength(new double[]{getLength(true)});
            		if(tempRR.containsKey(i)){
                		tempRR.get(i).addSolution(subR);
            		}else{
            			tempRR.put(i,subR);
            		}
        			
        //		}
        		
        		
    			
    		}
    		System.out.print("將點"+i+"插入R("+route+")做出："+bestTravelDistance(returnAnswer.getSolution())+" ");
		
    	return returnAnswer;    		
    }
    
    public  int getLength(boolean map){//true=R false=U
    		int answer=0;
    		if(map){
    			for(int i:tempRR.keySet()){    		
        			answer+=tempRR.get(i).getSolution().size();
        		}
    		}else{
    			for(int i:tempUU.keySet()){    			
        			answer+=tempUU.get(i).getSolution().size();
        		}
    		}System.out.println("每一次的長度"+answer);
    		return answer;
    }
    public boolean hasUTI(int i,int j){
		if(routeU.containsKey(i)){
			for(int k:routeU.get(i).getCapcity().keySet()){
				if(this.d(i, k, routeU.get(i))<=this.p(R.getCapcity().get(j))){
					return true;		
				}
			}			
		}    	
    		return false;
    }
    
    //代做到Ｆ（Ｓ）小於最小的才可為最佳解    
    //tempSolution
    public  solution bestNeighbor(solution tempSolution){
    		solution answer=null;//回傳值,回傳這代最佳的tempAnswer
    		solution tempAnswer=tempSolution;//比較值,比較出最好的F
    		bestI=1;
    		System.out.println("迴圈開始,從點"+startPoint+"~點"+slink.getPointNum()+"選出最佳解");
    		//迴圈開始1~n(slink的點1開始)
		for(int i=startPoint;i<slink.getPointNum()+startPoint;i++){			
			//建立新的O(i)
			orderedRoutes=new solution();
			
			//篩選條件
			if(0<=sum(tempSolution,i)){

				boolean insertFunction=false;
				boolean mustInsert=false;
				System.out.print("建立新的O("+i+")...");
				R=new solution();
				
				if(!tempSolution.contain(0)){
					System.out.println("路徑不含空集合，加入");
					int nullOrder=tempSolution.getSolution().size();
					solution nullSolution=new solution(0);
					nullSolution.setLength(0, nullOrder);
					nullSolution.setOrder(0, nullOrder);
					tempSolution.addSolution(nullSolution);	
				}

				//提取暫存路徑中的所有路徑
				for(int s=0;s<tempSolution.getSolution().size();s++){
					//找出未滿足的路徑
					if(slink.getCapcity(tempSolution.solution()[s],tempSolution.capcity()[s])!=slink.getCapcity()){//先尋找未滿的路徑
							R.addSolution(tempSolution.subSolution(s));							
					}
				}
				System.out.print("建立新的R...");
			//	R.showRoute("路徑R("+i+")",true);
				//對R中的每一條路徑j去找最佳鄰居 
    				for(int j=0;j<R.getSolution().size();j++){
    					System.out.print("R("+j+"),");
    					rstar=R.getOrder().get(j);
    					System.out.print("為s中第"+rstar+"條路徑");
    					//tempR tempU的審查值，挑出最後的給予tempR tempU    		
	    		    		tempRR=(HashMap<Integer, solution>) routeR.clone();	
	    		    		tempUU=(HashMap<Integer, solution>) routeU.clone();		
    		    			boolean hasUTI=hasUTI(i,j);
	    					
	    					
	    					if(isTabu(i,true,j,R)||hasUTI){//2.2從r插回u
	    						System.out.println("執行2.2");
	    						//(1)
	    						System.out.println("步驟1:");
							if(isTabu(i,true,j,R)){
								System.out.println("若R("+j+")是點"+i+"的tabu");
								U=new solution();
	    						}else if(!isTabu(i,true,j,R)&&routeU.containsKey(i)){
								System.out.println("若R("+j+")非點"+i+"的tabu");
								System.out.println("但存在UT("+i+")且d("+i+",U)<=p(R)");
								boolean hasUT=false;
								U=new solution();
								for(int k=0;k<orderedRoutes.solution().length;k++){
		    							for(int l:routeU.get(i).getSolution().keySet()){
		    								if(!hasUT&&orderedRoutes.getOrder().get(k)==routeU.get(i).getOrder().get(l)){	
			    								U.addRouteToLast(orderedRoutes.solution()[k],orderedRoutes.capcity()[k],
		    									orderedRoutes.length()[k],orderedRoutes.order()[k]);
			    								hasUT=true;
		    								}
									}
		    						}	
							}
							//(2)
							System.out.println("步驟2:");
						solution 	subSolution=new solution();
	    						for(int k=0;k<orderedRoutes.getSolution().size();k++){
//	    							System.out.println("無限迴圈的O長度"+k);
	    							
	    							if(orderedRoutes.getOrder().get(k)!=R.getOrder().get(j)){
	    								for(int l=0;l<U.getSolution().size();l++){
//	    									System.out.println("無限迴圈的U長度"+l);
	    	    							
	    									if(p>=d(i,k,orderedRoutes)
	    											&&orderedRoutes.getOrder().get(k)!=U.getOrder().get(l)){
	    										subSolution.addRouteToLast(orderedRoutes.solution()[k],orderedRoutes.capcity()[k],orderedRoutes.length()[k],orderedRoutes.order()[k]);
//	    										System.out.println("d("+i+","+k+")小於p,加入U");
	    									}
	    								}
	    							}
	    						}
	    						U.addSolution(subSolution);
	    						//(3) same as 2.1(3)
	    						System.out.println("步驟3:");
	    						if(U.getSolution().size()!=0){
		    						System.out.println("U不等於空集合，執行插點");
		    						mustInsert=true;
							}else{
								System.out.println("點"+i+"對R("+j+")做出無限大的距離");
							}
						
    					}else if(!isTabu(i,true,j,R)){//2.1
            				insertFunction=true;
    						System.out.println("執行2.1");
    						//(1)
    						System.out.println("步驟1:");
    						p=p(R.getCapcity().get(j));
    						System.out.println("令p=p(r),p="+p);
    						U=new solution();
    						System.out.println("建立新的U");
    						//(2)
    						System.out.println("步驟2:");
    						for(int k=0;k<orderedRoutes.solution().length;k++){
    							//排除r中的路徑
    							if(orderedRoutes.getOrder().get(k)!=R.getOrder().get(j)&&
								!isTabu(i,false,k,orderedRoutes)&&p>=d(i,k,orderedRoutes)){
//    								System.out.print("對於O("+i+")."+k+"非"+i+"的tabu路徑，且p>=d("+i+","+k+")");
									U.addRouteToLast(orderedRoutes.solution()[k],orderedRoutes.capcity()[k],
											orderedRoutes.length()[k],orderedRoutes.order()[k]);
//									System.out.print("則U加入O("+i+")."+k);
									//p-=d(i,k,orderedRoutes);
//									System.out.print("p-=d("+i+","+k+ ") p="+p);
    							}
    						}
    						
    						if(U.getSolution().size()==0){//(4)以(2)法U硬找一條路切點
    							System.out.println("步驟4:");
    							System.out.println("找出第一個u在O("+i+")-R("+j+")");
    							boolean hasU=false;
    							for(int k=0;k<orderedRoutes.getSolution().size();k++){
        							if(!hasU&&orderedRoutes.getOrder().get(k)!=R.getOrder().get(j)){
        								hasU=true;
        								U.addRouteToLast(orderedRoutes.solution()[k],orderedRoutes.capcity()[k],
        										orderedRoutes.length()[k],orderedRoutes.order()[k]);
        							}
        						}
    							
    						}else{//(3)
//    							Ustar=new int[U.getSolution().size()];
//	    						for(int k=0;k<Ustar.length;k++){
//	    							Ustar[k]=U.getOrder().get(k);
//	    						}	
    							System.out.println("步驟3:");
    							System.out.println("執行插點");
    						}
    						if(U.getSolution().size()==0){
    							System.out.println("U為空集合，F為無限大");
    						}else{
	    						System.out.println("U不等於空集合，執行換點");
	    						mustInsert=true;
    						}
    					}
    					//2.2(4)&2.1(5)
	    					if(mustInsert){
	    						tempAnswer=insert(i,j,tempSolution,insertFunction);
    							
	    						if(answer==null){
	    							System.out.println("第一值找出,進行回傳值初始化");
		    						answer=new solution();
		    						answer.addSolution(tempAnswer);
		    					}else if(bestTravelDistance(tempAnswer.getSolution())<
		    								bestTravelDistance(answer.getSolution())){
		    							System.out.println("將點"+i+"插入R("+j+"),此解較佳,值為："+bestTravelDistance(answer.getSolution()));
		    							
		    							//把關後再改值
		    							answer=new solution();
			    						answer.addSolution(tempAnswer);
		        						tempR=new HashMap<Integer,solution>();
		        						tempR.putAll(tempRR);		
		        						tempU=new HashMap<Integer,solution>();
		        						tempU.putAll(tempUU);
		        						bestI=i;
		        						bestUstar=Ustar;
		        						bestRstar=rstar;
		    					}
	    						
	    					}
	    					if(answer!=null)
	    					System.out.println("做完點"+i+"的解:"+this.bestTravelDistance(answer.getSolution()));
    				}   		
    			}
			
		}
		
		return answer;//回傳local最佳解
    }
    
    public  void doBestNeigbhor(slink mySlink,solution solution,int myTheta){
    	System.out.println("程式開始執行");
    	//建立物件	
    		int count=0;
    		theta=myTheta;//代數
    		System.out.print("將執行"+theta+"代，");
    		boolean hasBest=false;//控制迴圈
    		slink=mySlink;//分群資料，提取距離跟需求
    		bestSolution=new solution();//最佳解		
    		bestSolution.addSolution(solution);
    		tempSolution=new solution();//暫存解
    		tempSolution.addSolution(solution);
		//tabu路徑，不超過tabuLength
    		routeR=new HashMap<Integer,solution>();//tabu for Integer移入路徑的集合
    		routeU=new HashMap<Integer,solution>();//tabu for Integer移出路徑的集合
    		tempR=new HashMap<Integer,solution>();//tabu for Integer移入路徑暫存的集合
    		tempU=new HashMap<Integer,solution>();//tabu for Integer移出路徑暫存的集合
    		tabuLength=slink.getPoints().size()+slink.getAnswer().length;//ＴＡＢＵ總數
    		System.out.print("一個tabuList長度為"+tabuLength+"。");
    		//tempSolution.showRoute("初始路徑", true);
    		System.out.println("初始最佳解為："+bestTravelDistance(bestSolution.getSolution()));
    		//theta代
		for(int i=0;i<theta;i++){  
			count=0;
			
			//當沒找到最佳解，count<theta，此迴圈不會結束
			while(!hasBest&&count<theta){    
				//暫存尋找最佳鄰居
				System.out.println("開始執行第"+i+"代第"+count+"次：");
				tempSolution=bestNeighbor(tempSolution); 
				//每處理一次,更新routeR routeU
				routeR=new HashMap<Integer,solution>();
	    			routeR.putAll(tempR);		
    			
				routeU=new HashMap<Integer,solution>();
	    			routeU.putAll(tempU);		
    			
				System.out.println("更新tabuList");
				//如果暫存解比最佳解的路徑小，則置換bestSolution routeR routeU count為theta改執行下一個迴圈
				if(bestTravelDistance(tempSolution.getSolution())
						<bestTravelDistance(bestSolution.getSolution())){
					System.out.println("第"+i+"代第"+count+"次解為"+bestTravelDistance(tempSolution.getSolution())+"，更優於原始解"+bestTravelDistance(bestSolution.getSolution()));
    					bestSolution=new solution();
    					bestSolution.addSolution(tempSolution);
    					count=theta;
    				}else if(count!=(theta-1)){
    					++count;
    				}else{//若無法出此迴圈，結束後面所有迴圈
    					System.out.println("找不到更好的解了。");    					
    					hasBest=true;
    				}	
				System.out.println("第"+i+"代第"+count+"次最佳解為："+bestTravelDistance(tempSolution.getSolution()));
				System.out.print("BestI="+bestI+" R*="+bestRstar+" U*=");
				for(int l=0;l<bestUstar.length;l++){
					System.out.print(bestUstar[l]+" ");
				}
    				tempSolution.showRoute("BestS=", true);
			}
			System.out.println("第"+i+"代最佳解為："+bestTravelDistance(tempSolution.getSolution()));
		}	
		System.out.println("最佳解為："+bestTravelDistance(bestSolution.getSolution()));
		
    }
    
    
}
