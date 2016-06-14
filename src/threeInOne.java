import java.util.HashMap;


public class threeInOne {
	
	private  slink slink=new slink();	
	private  HashMap<Integer,AntTsp> antSolution=new HashMap<Integer,AntTsp>();
	private  phase2 phase2=new phase2();
	private  int[][] allSolution;
	private  String[] firstPath;
	private  int[][] answerQuality;
	private  int[][] arrayIndex;
	private  solution bestSolution;
    private  double[] number;
	private  int[] order;
    public  int[][] quality(int point){
		return answerQuality;
		
	}
	
    public static void main(String args[]){
//    	while(true)
    		new threeInOne().makeTSP("C:/Users/jerjerjer/Desktop/個專案們!!/working象山陳TABU專案/vrp/vrp/vrp檔案/","vrpnc1.txt");
 	       
        		
    	}
    	
	
	
	public  void makeTSP(String filePath,String fileName){
		try {
			// TODO 群為組
			slink.readGraph(filePath+fileName);
			firstPath=new String[slink.getGroupLength()];
			
			System.out.println("共有"+slink.getGroupLength()+"個路徑");
			for(int i=0;i<slink.getGroupLength();i++){
				//分群完成
				slink.getAnswer()[i]=slink.makeGroup(i);
				firstPath[i]=slink.makeFile(fileName+i,slink.getAnswer()[i]);				
				
				System.out.print("分群結果"+(i+1)+":");
				for(int j=0;j<slink.getAnswer()[i].length;j++){
					System.out.print(slink.getAnswer()[i][j]+"    ");
					
				}
				System.out.println();
				
				//傳資料給ＡＮＴ
				antSolution.put(i, new AntTsp());
				if(!(slink.getAnswer()[i].length<2)){
					antSolution.get(i).readGraph(firstPath[i]);
				}
			}
			//解ＴＳＰ
			allSolution=new int[slink.getGroupLength()][];
			answerQuality=new int[slink.getGroupLength()][];
			arrayIndex=new int[slink.getGroupLength()][];
			
			number=new double[slink.getGroupLength()];
			order=new int[slink.getGroupLength()];
			
			System.out.print("ANT結果:");
			for(int i=0;i<allSolution.length;i++){
				
						
					if(!(slink.getAnswer()[i].length<2)){
						arrayIndex[i]=antSolution.get(i).solve();	
						
					}else{
						arrayIndex[i]=new int[]{0};
					}
					order[i]=i;
					number[i]=i;
					System.out.print("Ant Tour:");
//					System.out.print("allsolution:");
					
					
					answerQuality[i]=new int[arrayIndex[i].length];
					allSolution[i]=new int[arrayIndex[i].length];
					
					for(int j=0;j<arrayIndex[i].length;j++){
						answerQuality[i][j]=slink.quality(i).get(arrayIndex[i][j]);
						allSolution[i][j]=slink.getAnswer()[i][arrayIndex[i][j]];						
						System.out.print((allSolution[i][j])+" ");
					}
					System.out.println();
			}
//			for(int i=0;i<allSolution.length;i++){
//				for(int j=0;j<allSolution[i].length;j++){
//					System.out.println(allSolution[i][j]+" ");
//				}
//			}
			bestSolution=new solution(allSolution,answerQuality,number,order);
			
//			new phase2().showMap(bestSolution, "bestSolution");
			
			
				phase2.doBestNeigbhor(slink,bestSolution,100 );//100代
				
			
			//直接把解轉換成對應點
			
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
