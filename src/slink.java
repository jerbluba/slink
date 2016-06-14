import java.awt.Point;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.LinkedList;




public class slink {
	private  static HashMap<Integer,Point> points=new HashMap<Integer,Point>();
	private  static HashMap<Integer,Integer> quality=new HashMap<Integer,Integer>();
	private  static HashMap<Integer,Integer> fullQuality=new HashMap<Integer,Integer>();
	
	private  static HashMap<Integer,HashMap<Integer,Integer>> answerQuality=new HashMap<Integer,HashMap<Integer,Integer>>();
	private  static HashMap<Integer,Integer> group=new HashMap<Integer,Integer>();
	private  static int capcity=0;
	private static int groupLength=0;
	private static int pointNum=0;
	private static int[][] answer=null;
	private static int startPoint=1;
public static int getCapcity(){
		
		return capcity;
	}
	
public static int getCapcity(int[] route,int[] quality){
	int answer=0;
	for(int i=0;i<route.length;i++){
		answer+=quality[i];		
	}
	
	
	return answer>capcity?capcity:answer;
}

	public static int getGroupLength(){
		
		return groupLength;
	}

	public static int[][] getAnswer(){
		
		return answer;
	}
	public static HashMap<Integer,Point> getPoints(){
		return points;
	}
	public static int getPointNum(){
		return pointNum;
	}
	public static int getQuality(int i){
//		for(int s:fullQuality.keySet()){
//			System.out.println(fullQuality.get(s));
//			
//		}
		return fullQuality.get(i);
		
	}
	public static void readGraph(String path){
        FileReader fr = null;
        BufferedReader buf = null;
		try {
			fr = new FileReader(path);
			buf = new BufferedReader(fr);
	        String line;
	        int i = 0;
	       
	        while ((line = buf.readLine()) != null) {
	        	if(i==0){
	        		capcity=Integer.parseInt(line.split(" ")[2]);
	        		pointNum=Integer.parseInt(line.split(" ")[1]);
	        	}
	            if(i>0){
	            	points.put(i-1, getPoint(line.split(" ")));
	            	if(i>1){
	            		quality.put(i-1, getQuality(line.split(" ")));
	            		fullQuality.put(i-1, getQuality(line.split(" ")));
	            		group.put(i-1, 0);
	            	}
	            }
	            ++i;
	        }
	        
	        for(int j:quality.keySet()){
	        		if(quality.get(j)>capcity){
	        			quality.put(j, quality.get(j)%capcity);
	        			fullQuality.put(j, quality.get(j)%capcity);
	        			
	        		}  	
	        }
	        
	        
	        for(int j=startPoint;j<pointNum+startPoint;j++){
	        	groupLength+=quality.get(j);
	        }
	        
	        
	        groupLength=(groupLength/=capcity)+1;
	        answer=new int[groupLength][];
	        
//	        System.out.println("depo:"+points.get(1));
//	        for(int j=2;j<pointNum+2;j++){
//	        	System.out.println("point1:"+points.get(j)+" quality:"+quality.get(j));
//		        
//	        }
	        
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{			
			close(buf);
			close(fr);       
		}
        
        
    }
	
	public static HashMap<Integer,Integer> quality(int point){
		return answerQuality.get(point);
		
	}
	
	public static int[] makeGroup(int file){
		double max=0;
		int tempCap=capcity;
		int farestPoint=0;
		answerQuality.put(file, new HashMap<Integer,Integer>());
		
		
		for(int j=startPoint;j<pointNum+startPoint;j++){
			if(quality.get(j)>0){
				double tempMax=Math.max(max, points.get(0).distance(points.get(j)));
		        	
//				System.out.println(max+" "+points.get(1).distance(points.get(j)));
				if(tempMax!=max){
		        		farestPoint=j;
		        		max=tempMax;
		        	}				
			}
        	
        }
		if(quality.containsKey(farestPoint)){
			tempCap-=quality.get(farestPoint);
			answerQuality.get(file).put(0, quality.get(farestPoint));
			quality.put(farestPoint,0);		
			
		}
//		double min=points.get(farestPoint).distance(points.get(farestPoint));
		HashMap<Integer,Integer> target=new HashMap<Integer,Integer>();
		target.put(0, farestPoint);
		int count=1;
		while(tempCap>0&&isEnd()){
			int tempMin=getMin(target);
			if(tempCap>=quality.get(tempMin)){
				tempCap-=quality.get(tempMin);
				answerQuality.get(file).put(count, quality.get(tempMin));
				quality.put(tempMin,0);
			}else{
				quality.put(tempMin,quality.get(tempMin)-tempCap);
				answerQuality.get(file).put(count, tempCap);
				tempCap=0;
			}
			
			target.put(count, tempMin);
			
			++count;
		}
		int[] answer=new int[count];
		for(int i=0;i<count;i++){
			answer[i]=target.get(i);			
		}
		
		return answer;
	}
	public static int getMin(HashMap<Integer,Integer> target){
	
		int nearestPoint=0;
		double min=0;
		
		for(Integer i:target.keySet()){
			for(int j=startPoint;j<pointNum+startPoint;j++){
				if(quality.get(j)>0){
					if(min==0){
						min=points.get(target.get(i)).distance(points.get(j));		
						nearestPoint=j;
					}else{
						double tempMin=Math.min(min, points.get(target.get(i)).distance(points.get(j)));
						if(tempMin!=min){
							nearestPoint=j;
				        		min=tempMin;
				        	}
					}
//					System.out.println(
//							"點"+(target.get(i)-1)+
//							"與點"+(j-1)+
//							"的距離為"+points.get(target.get(i)).distance(points.get(j))+
//							"與群組距離最小的點為"+(nearestPoint-1)+
//							"距離為"+min);
				}
				
	        }
		}
		
		
		
		return nearestPoint;
	} 
	
	public static void close(Object closeable) {
	    if (closeable instanceof BufferedReader) {
	        try {
	            ((BufferedReader)closeable).close();
	        } catch (IOException ex) {
	            // ignore
	        }
	    }else if (closeable instanceof FileReader) {
	        try {
	            ((FileReader)closeable).close();
	        } catch (IOException ex) {
	            // ignore
	        }
	    }else if (closeable instanceof FileOutputStream) {
	        try {
	            ((FileOutputStream)closeable).close();
	        } catch (IOException ex) {
	            // ignore
	        }
	    }else if (closeable instanceof BufferedWriter) {
	        try {
	            ((BufferedWriter)closeable).close();
	        } catch (IOException ex) {
	            // ignore
	        }
	    }
	}
	
	public static Point getPoint(String[] point){
		return new Point(Integer.parseInt(point[1]),Integer.parseInt(point[2]));
		
	}
	
	public static Integer getQuality(String[] line){
		return Integer.parseInt(line[3]);
		
	}
	
	public static boolean isEnd(){
		
		for(int s:quality.keySet()){
			if(quality.get(s)>0){
				return true;
				
			}
			
		}
		return false;
	}
	
	
	public static String makeFile(String name,int[] answer){
		String filePath = new File(new File(new File("").getAbsolutePath()).getParent()).getParent()+"/"+name+".txt";
		FileOutputStream fw= null;
		BufferedWriter bw = null ;
		String[] content=new String[answer.length];
	
		System.out.println(filePath);
		File file = new File(filePath);		
	
	    file.getParentFile().mkdirs();
	    try {
	    	if(file.exists()){
	    		file.delete();
	    	}	    	
	        file.createNewFile();
	        fw= new FileOutputStream(file.getAbsoluteFile());
			bw = new BufferedWriter(new OutputStreamWriter(fw, "UTF-8"));
			
			for(int i=0;i<content.length;i++){
				content[i]="";
				for(int j=0;j<content.length;j++){
					content[i]+=points.get(answer[j]).distance(points.get(answer[i]))+" ";
				}			
				bw.write(content[i]);
				bw.newLine();
			}
			
			
	    } catch (IOException e) {
	        e.printStackTrace();
	    }finally{
	    	close(bw);
	    	close(fw);
	    }
	    
	    return filePath;
	}
	
	
	
	
	public static void main(String args[]) throws IOException{
		try {
			slink.readGraph("/Users/jerbluba/Desktop/vrp/vrp/vrpfile/vrpnc1.txt");
			System.out.println("共有"+groupLength+"個路徑");
			for(int i=0;i<1;i++){
				System.out.print("群"+(i+1)+":");
				answer[i]=makeGroup(i);
				makeFile("modtest"+i,answer[i]);
				for(int j=0;j<answer[i].length;j++){
					System.out.print(answer[i][j]+"    ");
					
				}
				System.out.println();
			}
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
