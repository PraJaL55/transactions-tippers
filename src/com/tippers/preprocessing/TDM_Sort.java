package com.tippers.preprocessing;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class TDM_Sort {
	
	public void sort(File f) throws IOException{
		Map<String, List<String>> content = new HashMap<String, List<String>>();
		
		BufferedReader br = new BufferedReader(new FileReader(f));
		String line = null;
		boolean isSelect = false;
		int count = 0;
		while ((line = br.readLine()) != null) {
			if(line.charAt(0) == 'S') {
				isSelect = true;
			} else {
//				System.out.println(count);
//				System.out.println("Not select");
//				for(int i = 0; i < 10; i++){
//					System.out.println("First letter: " + line.charAt(i));
//			}
				isSelect = false;
			}
			int i = 0, j = 0;
			if(isSelect == true){
				i = line.indexOf("--2017-11");
				j = i;
				while(line.charAt(j) != ','){
					j++;
				}
				i += 2;
			} else {
				i = line.indexOf("2017-11");
				j = i;
				while(line.charAt(j) != '\''){
					j++;
				}
			}
//			System.out.println(line);
			
			String date = line.substring(i, j);
			if(!content.containsKey(date)){
				List<String> newList = new ArrayList<String>();
				newList.add(line);
				content.put(date, newList);
			} else {
				List<String> newList = content.get(date);
				newList.add(line);
				content.put(date, newList);
			}
			count++;
		}
        BufferedWriter bw = new BufferedWriter(new FileWriter("27-Nov-2017-hc.sql"));

		TreeMap<String, List<String>> sorted = new TreeMap<>(content);
		for (Entry<String, List<String>> entry : sorted.entrySet()) {
//			System.out.println("Key = " + entry.getKey() + 
//                    ", Value = " + entry.getValue());
			for(String query : entry.getValue()) {
				bw.write(query);
				bw.write("\n");
			}
		}
            
		br.close();
		bw.close();
	}
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		TDM_Sort t = new TDM_Sort();
		File f = new File("20-Nov.txt");
		t.sort(f);
	}

}
