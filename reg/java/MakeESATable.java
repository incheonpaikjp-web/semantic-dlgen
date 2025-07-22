/**
 * @MakeESATable.java
 * @date Feb. 10, 2020
 * @author Ryo Ataka
 * @brief Example Code for Making ESA Table.
 * @details
 *  Deep Learning Generation System, Version 1.0
 *  Copyright (C) 2020 Intelligent Data Analytics Laboratory, All Rights Reserved.
 */

import java.io.*;
import java.util.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Arrays;


public class MakeESATable {
  //Wikipediaの記事数
  private static final int ArtNum = 3;
  //使いたい単語の数
  private static final int WordNum = 21813;
  //使いたい単語のリスト
  private static final String wordListPass = "/home/ataka/Documents/Impementation/ESA/Demo/A/WordList.txt";
  //Wikipediaの記事があるディレクトリ
  private static final String articlePass = "/home/ataka/Documents/Impementation/ESA/Demo/old_text/";
  //それぞれの記事の文字数
  private static final String wnPass = "/home/ataka/Documents/Impementation/ESA/Demo/A/T3/CWresult.txt";
  //TFの出力先
  private static final String tfPass = "/home/ataka/Documents/Impementation/ESA/Demo/A/T3/CTFresult.txt";
  //DTの出力先
  private static final String dfPass = "/home/ataka/Documents/Impementation/ESA/Demo/A/T3/CDFresult.txt";
  //TF-IDFの出力先
  private static final String tfidfPass = "/home/ataka/Documents/Impementation/ESA/Demo/A/T3/TFIDF.txt";
  private static final String torc = " ";
  //private static final String tfidfPass = "/home/ataka/Documents/Impementation/ESA/Demo/A/T/TFIDF.csv";
  //private static final String torc = ",";

    public static void main(String[] args) throws Exception{
      //TFを数える
      CountTF();
      
      //DFを数える
      CountDF();

      //TF-IDFを計算する
      CalcTfidf();

    }

    public static void CountTF() throws Exception{
      HashMap<String, Integer> wordMap = new LinkedHashMap<String, Integer>();
      File wlfile = new File(wordListPass);
    	BufferedReader br = new BufferedReader(new FileReader(wlfile));

      String patstr[] = new String[WordNum];
    	String strwd;
    	int j = 0;

      while((strwd = br.readLine()) != null){
	  //patstr[j] = strwd.toLowerCase();
	   patstr[j] = strwd;
    	   wordMap.put(patstr[j], 0);
	   j++;
    	}
      //System.out.println(wordMap.size());
      if(j != wordMap.size()) System.out.println("単語リストの中に同じ単語が複数アルヨ");
    	br.close();
	

      File ofile = new File(tfPass);
    	BufferedWriter bw = new BufferedWriter(new FileWriter(ofile));

      for(int i = 0; i < ArtNum; i++) {
    	   if(i % 1000 == 0) System.out.println(i);

    	//wordMap全要素0で初期化
    	for(j = 0; j < WordNum; j++) {
    	   wordMap.put(patstr[j], 0);	   //wordMap 使われる単語, 数字のマップ
    	}
	//System.out.println(wordMap.size());

      File file = new File(articlePass + "old_text" + i + ".txt");
    	//Scanner scan = new Scanner(file);
    	//File file = new File("/home/ataka/Documents/Impementation/ESA/Demo/A/test/old_text0.txt");
    	Scanner scanner = new Scanner(file);
      HashMap<String, Integer> termMap = new HashMap<String, Integer>();

      while(scanner.hasNext()) {
        String term = scanner.next(); //old_textの単語
    	String lterm = term.toLowerCase(); //old_textの単語（小文字）
        // 単語の出現回数を1増やす
        int count = 1;
        if(termMap.containsKey(lterm)){
          count += termMap.get(lterm);
        }
          termMap.put(lterm, count);
        }

        // 単語数の表示 (ソートはせず)
        //int columN = 0;
        Iterator<String> iterator = termMap.keySet().iterator();
    	  while(iterator.hasNext()) {
    	    String key = iterator.next();
    	    Integer value = termMap.get(key);
    	    String lkey = key.toLowerCase();
    	    //System.out.println(lkey + ": " + value);
	    //if(i == 0) System.out.println(columN);columN++;
    	    if(wordMap.containsKey(lkey)){
            wordMap.put(lkey, value);
          }

    	  }



    	  //Object[] mapkey = wordMap.keySet().toArray();
          //Arrays.sort(mapkey);
	  
    	  Iterator<String> iterator1 = wordMap.keySet().iterator();
    	  int f = 0;
	  //int columN = 0;
    	  while(iterator1.hasNext()) {
    	    String key1 = iterator1.next();
    	    Integer value1 = wordMap.get(key1);
    	    String lkey1 = key1.toLowerCase();
    	    //System.out.println(lkey1 + ": " + value1);
    	    //bw.write(value1 + " ");
    	    if(f == 1) bw.write(" ");
    	    //if(f == 1) bw.write(",");
    	    bw.write(String.valueOf(value1));
    	    f = 1;
	    //if(i == 0) System.out.println(columN);columN++;
    	  }
    	    bw.newLine();

        }

    	  bw.close();
	  System.out.println("=====TFの計算終了=====");
    }//End of CountTF



    public static void CountDF() throws Exception{
      File wlfile = new File(wordListPass);
      BufferedReader br = new BufferedReader(new FileReader(wlfile));

      String patstr[] = new String[WordNum];
    	String strwd;
    	int j = 0;

    	while((strwd = br.readLine()) != null){
    	    patstr[j] = strwd.toLowerCase();
    	    j++;
    	}
    	br.close();

      int[] wl = new int[WordNum];
    	Arrays.fill(wl, 0);

      File ofile = new File(dfPass);
    	BufferedWriter bw = new BufferedWriter(new FileWriter(ofile));

      for(int i=0; i < ArtNum; ++i) {
        File file = new File(articlePass + "old_text" + i + ".txt");
  	    Scanner scan = new Scanner(file);

  	    String strall = scan.useDelimiter("\\r\\n").next().toLowerCase();
	    //String strall = scan.useDelimiter("\\z").next();
  	    if(i%100 == 0) System.out.println(strall);
  	    scan.close();

  	     for(int k = 0; k < WordNum; ++k) {

  		     Pattern pattern = Pattern.compile(Pattern.quote(patstr[k]));

  		     Matcher matcher = pattern.matcher(strall);

  		     if(matcher.find()) {
		       if(i%100 == 0 && k%1000 == 0) System.out.println("-------match!-------");
  		       ++wl[k];
  		     }else{
		       if(i%100 == 0 && k%1000 == 0) System.out.println("-------NOT match!-------");
  		     }
  	    }

  	    if(i % 10 == 0) System.out.println(i);
  	}

    for(int k = 0; k < WordNum; ++k) {
  	bw.write(k + " " + wl[k]);
  	bw.newLine();
  	}

  	bw.close();
	System.out.println("=====DFの計算終了=====");
  }//Enf of CountDF


  public static void CalcTfidf() throws Exception{
    File dffile = new File(dfPass);
  	BufferedReader br0 = new BufferedReader(new FileReader(dffile));

    String strid0;
  	String dfstr;
  	int dfdata[] = new int[WordNum];
  	int j = 0;
  	while((strid0 = br0.readLine()) != null){
  	    String[] worddf = strid0.split(" ", 2);
  	    dfstr = worddf[1];
  	    dfdata[j] = Integer.parseInt(dfstr); //61 23 791 ...(WordNum)
  	    ++j;
  	}
  	br0.close();

    File tf1file = new File(wnPass);
    BufferedReader br1 = new BufferedReader(new FileReader(tf1file));

    String tf1str;
  	int tf1data[] = new int[ArtNum];
  	j = 0;
  	while((strid0 = br1.readLine()) != null){
  	    String[] concepttf = strid0.split(" ", 2);
  	    tf1str = concepttf[1];
  	    tf1data[j] = Integer.parseInt(tf1str); //17845 8943 3363 ...(N)
  	    ++j;
  	}
  	br1.close();

    File tfidffile = new File(tfidfPass);
  	BufferedWriter bw2 = new BufferedWriter(new FileWriter(tfidffile));

  	File tffile = new File(tfPass);
  	BufferedReader br = new BufferedReader(new FileReader(tffile));

    j = 0;
  	float tf;
  	float idf;
  	float tfidf;

    String strid;
      while((strid = br.readLine()) != null){
      if(j % 1000 == 0) System.out.println(j);
      int tfdata[] = new int[WordNum];

      String[] idconcept = strid.split(" ", WordNum);

      for(int i = 0; i < WordNum; ++i) {
	  if(i == 0) System.out.println(idconcept.length);
        tf1str = idconcept[i];
    	tfdata[i] = Integer.parseInt(tf1str);//0 0 0 1 2 0 ...(WN)
      }

      int f = 0;
	    for(int i = 0; i < WordNum; ++i) {
        //TFIDFを求める
  	    tf = (float)(1.0 + Math.log(tfdata[i]));
  	    //tf = tfdata[i]/tf1data[j];
  	    if(tfdata[i] == 0) tf = 0;
  	    idf = (float)(Math.log(ArtNum/(double)(dfdata[i])));
  	    //idf = N/(Math.log(dfdata[i]));
  	    tfidf = (float)(tf * idf);
  	    if(Float.isNaN( tfidf )) tfidf = 0;
        //書き込む
  	    if(f == 1) bw2.write(torc);
  	    bw2.write(String.valueOf(tfidf));
  	    f = 1;
  	    }
  	    bw2.newLine();
        ++j;
      }
      br.close();
      bw2.close();
      System.out.println("=====TF-IDFの計算終了=====");
  }//End of CalcTfidf



}//End of Class
