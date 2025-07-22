/**
 * @Lines2TFIDF_ESA.java
 * @date Feb. 10, 2020
 * @author Ryo Ataka
 * @brief Example Code for Lines2TFIDF with ESA
 * @details
 *  Deep Learning Generation System, Version 1.0
 *  Copyright (C) 2020 Intelligent Data Analytics Laboratory, All Rights Reserved.
 */

import java.io.*;
import java.lang.*;
import java.util.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Arrays;

public class Lines2TFIDF_ESA {
    private static  String inputPath = "/Users/ataka/Downloads/Lines2TFIDF_ESA/baseball_hockey.txt";
    private static final String outputPath = "/Users/ataka/Downloads/Lines2TFIDF_ESA/TFIDF.csv";
    //private static final String outputPath = "/Users/ataka/Downloads/Lines2TFIDF_ESA/TFIDF.txt";
    private static final String outputPath2 = "/Users/ataka/Downloads/Lines2TFIDF_ESA/TFIDF_ESA.csv";

    private static final String trainPath = "/Users/ataka/Downloads/Lines2TFIDF_ESA/TFIDF_train.csv";
    private static final String testPath = "/Users/ataka/Downloads/Lines2TFIDF_ESA/TFIDF_test.csv";


    private static final String WordListPath = "/Users/ataka/Downloads/Lines2TFIDF_ESA/imp/src/WordList.txt";
    private static final String IDConceptsPath = "/Users/ataka/Downloads/Lines2TFIDF_ESA/imp/src/IDConcepts.txt";
    private static final int NumLine = 10; //文書の行数
    private static final int ConcNum = 20024; //コンセプト数
    private static final int ComWordNum = 21813; //よく使われる単語
    private static final String torc = ",";
    //private static final String torc = " ";
    public static void main(String[] args) throws Exception{

        if(args.length == 1) {
            inputPath = args[0];
        }

        double tfidfTable[][] = CalcTfidf(inputPath);//10行532列のTFIDFテーブル

        WriteTfidf(tfidfTable);

        MakeTT(tfidfTable);



    }

    public static double[][] CalcTfidf(String input) throws Exception{

        String str = "";
        String lines[] = new String[NumLine];
        int linesWc[] = new int[NumLine];

        BufferedReader br = new BufferedReader( new FileReader(input) );

        int line = 0;
        while( br.ready() ){
            String tmp = br.readLine();
            tmp = tmp.replace("0,","");
            tmp = tmp.replace("1,","");
            lines[line++] = tmp;
            //br.deleteCharAt(0);
            //br.delete(0,1);
            str += tmp + " ";
            System.out.println(tmp);
        }
        br.close();


        String [] word = str.toLowerCase().split("[\\p{Blank}|\\.|,]++");


        for(int i=0; i < word.length; i++){
            word[i] = removeNoise(word[i]);
        }

        System.out.println("単語数は：" + word.length );

        HashMap<String, Integer> wordMap = new LinkedHashMap<String, Integer>();

        for(int i=0; i < word.length; i++){
            wordMap.put(word[i], 0);
        }
        wordMap.remove(null);
        wordMap.remove("");


        for (String key : wordMap.keySet()) {
            System.out.println(key + " => " + wordMap.get(key));
        }

        System.out.println("単語数は：" + wordMap.size());

        int numWord = wordMap.size();
        double tfidfTable[][] = new double[NumLine][numWord];//10行531列

        for(int i = 0; i < NumLine; i++){
            for(int j = 0; j < numWord; j++){
                tfidfTable[i][j] = 0.0;
            }
        }

        //System.out.println(lines[1]);

        //int[] wl = new int[numWord];
        line = 0;
        String[] wordlist = new String[numWord];
        for (String key : wordMap.keySet()) {
            //System.out.println(key + " => " + wordMap.get(key));
            wordlist[line++] = key;
        }
        //System.out.println(wordlist[0]);
        //System.out.println(wordlist[numWord-1]);



        //DFを計算する 531要素のint型配列 df[]
        int df[] = new int[numWord];//531列

        /*for(int i = 0; i < NumLine; i++){
            for(int j = 0; j < numWord; j++){
                wordMap.containsKey()
            }
        }*/

        df = CountDF(lines, wordlist);

        //idfを計算する 531要素のdouble型配列 df[]
        double idf[] = new double[numWord];
        for(int i=0; i<numWord; i++){
            idf[i] = (double)(Math.log(NumLine/(double)(df[i]+1)));
            //System.out.println("######"+i+"######");
            //System.out.println(df[i]);
            //System.out.println(idf[i]);
        }


        //TFを計算する 10行531列のdouble型配列 ctf[][]
        int tf[][] = new int[NumLine][numWord];//10行531列
        for(int i = 0; i < NumLine; i++){
            for(int j = 0; j < numWord; j++){
                tf[i][j] = 0;
            }
        }

        for(int i = 0; i < NumLine; i++){
            linesWc[i] = wordCount(lines[i]); //それぞれの文書の単語数を数える
        }

        for(int i = 0; i < NumLine; i++){
            tf[i] = CountTF(lines[i], wordlist);
        }

        double ctf[][] = new double[NumLine][numWord];//10行531列
        for(int i = 0; i < NumLine; i++){
            for(int j = 0; j < numWord; j++){
                ctf[i][j] = (double)tf[i][j]/linesWc[i];
            }
        }

        /*
        System.out.println(tf[0][0]);
        System.out.println(tf[0][530]);
        System.out.println(tf[1][0]);
        System.out.println(tf[1][530]);
        System.out.println();

        System.out.println(linesWc[0]);
        System.out.println(linesWc[1]);
        System.out.println();


        System.out.println(String.format("%.10f",ctf[0][0]));
        System.out.println(String.format("%.10f",ctf[0][530]));
        System.out.println(String.format("%.10f",ctf[1][0]));
        System.out.println(String.format("%.10f",ctf[1][530]));
        System.out.println();

        System.out.println(tf[0][0]/linesWc[0]);
        System.out.println(1/26);
        System.out.println((double)1/26);
        */




        //TFIDFを求める
        //tf = (float)(1.0 + Math.log(tfdata[i]));
        //tf = tfdata[i]/tf1data[j];
        //if(tfdata[i] == 0) tf = 0;
        //idf = (float)(Math.log(ArtNum/(double)(dfdata[i])));
        //idf = N/(Math.log(dfdata[i]));
        //tfidf = (float)(tf * idf);
        //if(Float.isNaN( tfidf )) tfidf = 0;
        ////書き込む
        //if(f == 1) bw2.write(torc);
        //bw2.write(String.valueOf(tfidf));
        //f = 1;
        for(int i = 0; i<NumLine; i++){
            for(int j=0; j<numWord; j++){
                tfidfTable[i][j] = ctf[i][j]*idf[j];
                //if(i==0 || i==9)System.out.println(tfidfTable[i][j]);
            }
        }

        return tfidfTable;
    }

    public static String removeNoise(String str) throws Exception{
        String DoubleQuotation = "\"";
        str = str.replace("(", "");
        str = str.replace(")", "");
        str = str.replace("#", "");
        str = str.replace("'", "");
        str = str.replace(DoubleQuotation, "");
        return str;
    }

    public static int wordCount(String str) throws Exception{
        String [] word = str.toLowerCase().split("[\\p{Blank}|\\.|,]++");

        for(int i=0; i < word.length; i++){
            word[i] = removeNoise(word[i]);
        }

        HashMap<String, Integer> wordMap = new LinkedHashMap<String, Integer>();
        for(int i=0; i < word.length; i++){
            wordMap.put(word[i], 0);
        }
        wordMap.remove(null);
        wordMap.remove("");

        System.out.println("単語数は：" + wordMap.size());

        return wordMap.size();
    }


    public static int[] CountTF(String str, String[] wordlist) throws Exception{
        //531個の単語がそれぞれの文書で何回登場するか。2次元配列 登場回数
        //文書(文字列)と全単語を受け取り、531個の単語がそれぞれ何回登場するかを返す。
        int numWord = wordlist.length; //531
        int tf[] = new int[numWord];

        String [] word = str.toLowerCase().split("[\\p{Blank}|\\.|,]++");

        for(int i=0; i < word.length; i++){ //文書の文字数
            word[i] = removeNoise(word[i]);
        }


        for(int i=0; i < numWord; i++){ //531
            for(int j=0; j < word.length; j++){ //文書の文字数
                if(wordlist[i].equals(word[j])){
                    tf[i]++;
                }
            }

        }

        return tf;
    }

    public static int[] CountDF(String lines[], String[] wordlist) throws Exception{
        //531個の単語が10個中何個の文書で登場するか、つまりそれぞれの文書に登場するか否かを調べたい。2次元配列 0 or 1
        //文書(文字列)とハッシュマップを受け取り、531個の単語がそれぞれ何個の文書で登場するかを返す。
        int df[] = new int[wordlist.length];
        System.out.println(df[0]);
        for(int i=0; i<NumLine; i++){
            String [] word = lines[i].toLowerCase().split("[\\p{Blank}|\\.|,]++");
            HashMap<String, Integer> wordMap = new LinkedHashMap<String, Integer>();
            for(int j=0; j < word.length; j++){
                word[j] = removeNoise(word[j]);
                wordMap.put(word[j], 0);
            }

            wordMap.remove(null);
            wordMap.remove("");

            for(int j=0; j<wordlist.length; j++){
                if(wordMap.containsKey(wordlist[j])){
                    df[j]++;
                }
            }


        }
        //for(int j=0; j<wordlist.length; j++) {
            //System.out.println("df"+j+":"+df[j]);
        //}
        return df;
    }


    public static void WriteTfidf(double[][] tfidfTable) throws Exception{
        File tfidffile = new File(outputPath);
        BufferedWriter bw = new BufferedWriter(new FileWriter(tfidffile));

        //System.out.println(tfidfTable.length);//NumLine
        //System.out.println(tfidfTable[0].length);//numWord
        int numWord = tfidfTable[0].length;

        for(int i = 0; i<NumLine; i++){
            for(int j=0; j<numWord; j++){

                if(j!=0) bw.write(torc);
                bw.write(String.valueOf(tfidfTable[i][j]));

            }
            bw.newLine();
        }


        bw.close();

    }


    public static void MakeTT(double[][] tfidfTable) throws Exception{

        File trainfile = new File(trainPath);
        BufferedWriter trainb = new BufferedWriter(new FileWriter(trainfile));

        File testfile = new File(testPath);
        BufferedWriter testb = new BufferedWriter(new FileWriter(testfile));

        //System.out.println(tfidfTable.length);//NumLine
        //System.out.println(tfidfTable.length);//10
        //System.out.println(tfidfTable.length/2);//5
        //System.out.println(2/tfidfTable.length);//0
        //System.out.println(tfidfTable[0].length);//numWord
        int halfLine = tfidfTable.length/2;
        double preTrainLine = ((double)halfLine/10)*6;
        int trainLine = (int)preTrainLine;
        System.out.println(preTrainLine);
        System.out.println(trainLine);
        int numWord = tfidfTable[0].length;


        for(int i = 0; i<NumLine; i++){
            if(i<trainLine || (halfLine<=i && i<(halfLine+trainLine))) {
                for (int j = 0; j < numWord; j++) {
                    if (j == 0) trainb.write("#########"+i+"#########");
                    if (j != 0) trainb.write(torc);
                    trainb.write(String.valueOf(tfidfTable[i][j]));

                }
                trainb.newLine();
            }else{
                for (int j = 0; j < numWord; j++) {
                    if (j == 0) testb.write("#########"+i+"#########");
                    if (j != 0) testb.write(torc);
                    testb.write(String.valueOf(tfidfTable[i][j]));

                }
                testb.newLine();
            }
        }


        trainb.close();
        testb.close();

    }





}

//文字数
//文書1 26
//文書2 106
//文書3 60
//文書4 59
//文書5 90
//文書6 103
//文書7 197
//文書8 40
//文書9 59
//文書10 83