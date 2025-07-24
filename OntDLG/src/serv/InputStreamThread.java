/**
 * @InputStreamThread.java
 * @date Feb. 10, 2020
 * @author Ryo Ataka
 * @brief Input Stream and Thread Process for it
 * @details
 *  Deep Learning Generation System, Version 1.0
 *  Copyright (C) 2020 Intelligent Data Analytics Laboratory, All Rights Reserved.
 */

package serv;

import java.io.*;
import java.util.*;

public class InputStreamThread extends Thread {

	private BufferedReader br;

	private List<String> list = new ArrayList<String>();

	/** コンストラクター */
	public InputStreamThread(InputStream is) {
		br = new BufferedReader(new InputStreamReader(is));
	}

	/** コンストラクター */
	public InputStreamThread(InputStream is, String charset) {
		try {
			br = new BufferedReader(new InputStreamReader(is, charset));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void run() {
		try {
			for (;;) {
				String line = br.readLine();
				if (line == null) 	break;
				list.add(line);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/** 文字列取得 */
	public List<String> getStringList() {
		return list;
	}
	
	public String getLearningResult() {
		int flag = 0;
		for (String s : list) {
    		//System.out.println(s);
    		if(s.equals("Optimization Finished!")) {
    			flag = 1;
    			continue;
    		}
    		if(flag == 1) return s;
    	}
		return "No Result";
	}//End of getStringList()
	
	public String getLearningResult2() {
		int flag = 0;
		for (String s : list) {
    		//System.out.println(s);
    		if(s.equals("---")) {
    			flag = 1;
    			continue;
    		}
    		if(flag == 1) {
    			flag = 2;
    			continue;
    		}if(flag == 2) {
    			return s;
    		}
    	}
		return "No Result";
	}//End of getLearningResult2()
	
	public void makeSh() {
		
	}//End of makeSh()
	
	
	
}