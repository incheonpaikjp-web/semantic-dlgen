/**
 * @InferenceBean.java
 * @date Feb. 10, 2020
 * @author Ryo Ataka
 * @brief Inference Bean for Ontology and Rule
 * @details
 *  Deep Learning Generation System, Version 1.0
 *  Copyright (C) 2020 Intelligent Data Analytics Laboratory, All Rights Reserved.
 */

package serv;

import java.io.Serializable;

import com.clarkparsia.owlapi.explanation.DefaultExplanationGenerator;
import com.clarkparsia.owlapi.explanation.util.SilentExplanationProgressMonitor;
import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;
import com.google.common.collect.Multimap;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.dlsyntax.renderer.DLSyntaxObjectRenderer;
import org.semanticweb.owlapi.formats.PrefixDocumentFormat;
import org.semanticweb.owlapi.io.OWLObjectRenderer;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.model.parameters.Imports;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;
import uk.ac.manchester.cs.owl.explanation.ordering.ExplanationOrderer;
import uk.ac.manchester.cs.owl.explanation.ordering.ExplanationOrdererImpl;
import uk.ac.manchester.cs.owl.explanation.ordering.ExplanationTree;
import uk.ac.manchester.cs.owl.explanation.ordering.Tree;

import org.semanticweb.owlapi.formats.FunctionalSyntaxDocumentFormat;
import org.semanticweb.owlapi.util.DefaultPrefixManager;


import java.util.*;
import java.io.File;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import serv.InputStreamThread;

import javax.servlet.http.Part;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


public class InferenceBean implements Serializable {
	//private static final long serialVersionUID = 1L;
	
	//private static final String BASE_URL = "http://163.143.92.174/OntKB.owl";
	//private static final String BASE_URL = "http://163.143.92.174/kt1-3.owl";
	//private static final String BASE_URL = "http://localhost:8090/OntDLG/kt1-3.owl";
	private static final String BASE_URL = "http://localhost:8090/OntDLG/OntKB.owl";
    private static final String DOCUMENT_IRI = "http://163.143.92.174/kt2-2.owl";
    private static final String REGISTRY_PATH = "/home/ataka/Documents/reg/";
    
    

    private static OWLObjectRenderer renderer = new DLSyntaxObjectRenderer();
	
	private String fp1, fp2;
	private String fp3, fp3name;
	//private Part fp3;
	private String nfp1, nfp2, nfp3;
	private String targetdl, datagens;
	private String targetdlind, datagensind;
	private String lresult;
	private double acc = 0.0;
	private String istr, ostr;
	private String userInputStr;
	private String owl;
	


  //private ArrayList<StudentRecordBean> studentRecordArray = new ArrayList<StudentRecordBean>();

// Constructor
  public InferenceBean() {
  }

  /*public ArrayList<StudentRecordBean> getStudentRecordArray() {
    return studentRecordArray;
  }

  public void addStudentRecord(StudentRecordBean obj) {
    studentRecordArray.add(obj);
  }

  public int getArraySize() {
    return studentRecordArray.size();
  }*/
  
  public void setFp1(String fp1) {
	  this.fp1 = fp1;
  }
  
  public String getFp1() {
	  return this.fp1;
  }
  
  public void setFp2(String fp2) {
	  this.fp2 = fp2;
  }
  
  public String getFp2() {
	  return this.fp2;
  }
  
  public void setFp3(String fp3) {
	  this.fp3 = fp3;
  }
  
  public String getFp3() {
	  return this.fp3;
  }
  
  /*
  public void setFp3(Part fp3) {
	  this.fp3 = fp3;
  }
  
  public Part getFp3() {
	  return this.fp3;
  }*/
  
  public void setFp3name(String fp3name) {
	  this.fp3name = fp3name;
  }
  
  public String getFp3name() {
	  return this.fp3name;
  }
  
  
  public void setNfp1(String nfp1) {
	  this.nfp1 = nfp1;
  }
  
  public String getNfp1() {
	  return this.nfp1;
  }
  
  public void setNfp2(String nfp2) {
	  this.nfp2 = nfp2;
  }
  
  public String getNfp2() {
	  return this.nfp2;
  }
  
  public void setNfp3(String nfp3) {
	  this.nfp3 = nfp3;
  }
  
  public String getNfp3() {
	  return this.nfp3;
  }
  
  
  
  
  public void setUserInputStr(String userInputStr) {
	  this.userInputStr = userInputStr;
  }
  
  public String getUserInputStr() {
	  return this.userInputStr;
  }

  
  public String getDatagens() {
	  return this.datagens;
  }
  
  public String getTargetdl() {
	  return this.targetdl;
  }
  
  public void setDatagensind(String datagensind) {
	  this.datagensind = datagensind;
  }
  
  public void setTargetdlind(String targetdlind) {
	  this.targetdlind = targetdlind;
  }
  
  public String getDatagensind() {
	  return this.datagensind;
  }
  
  public String getTargetdlind() {
	  return this.targetdlind;
  }
  
  /*
  public String getDatagens() {
	  if(this.datagens.equals("null")) {
		  return "Can not find any services";
	  }else{
		  return this.datagens;
	  }
  }
  
  public String getTargetdl() {
	  if(this.targetdl.equals("null")) {
		  return "Can not find any services";
	  }else{
		  return this.targetdl;
	  }
  }
  */
  
  public String getLresult() {
	  return this.lresult;
  }
  
  public double getAcc() {
	  return this.acc;
  }
  
  public void setIstr(String istr) {
	  this.istr = istr;
  }
  
  public String getIstr() {
	  return this.istr;
  }
  
  public String getOstr() {
	  return this.ostr;
  }
  
  public void setOwl(String owl) {
	  this.owl = owl;
  }
  
  public String getOwl() {
	  return this.owl;
  }
  
  
  public void passTest() throws OWLOntologyCreationException, OWLOntologyStorageException, IOException {
	  /*System.out.println(this.fp1);
	  System.out.println(this.fp2);
	  System.out.println(this.nfp1);
	  System.out.println(this.nfp2);
	  System.out.println(this.nfp3);
superL
Classification
near
2
50
*/
	  
	  String fp1, fp2, fp3, nfp1, nfp2, nfp3;
	  /*String fp1, fp2, nfp1, nfp2, nfp3;
	  Part fp3;*/
	  
	  fp1 = this.fp1; //教師の有無
	  fp2 = this.fp2; //タスクの種類
	  fp3 = this.fp3; //データ
	  nfp1 = this.nfp1; //ドメイン距離 or 入力言語
	  nfp2 = this.nfp2; //クラス数 or 出力言語
	  nfp3 = this.nfp3; //精度 or 実行時間
	  System.out.println(fp1);
	  System.out.println(fp2);
	  //System.out.println(fp3);
	  System.out.println(nfp1);
	  System.out.println(nfp2);
	  System.out.println(nfp3);
	  
	  
	  
	  
	  OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
      OWLOntology ontology = manager.loadOntologyFromOntologyDocument(IRI.create(BASE_URL));
      OWLReasonerFactory reasonerFactory = PelletReasonerFactory.getInstance();
      OWLReasoner reasoner = reasonerFactory.createReasoner(ontology, new SimpleConfiguration());
      OWLDataFactory factory = manager.getOWLDataFactory();
      PrefixDocumentFormat pm = manager.getOntologyFormat(ontology).asPrefixOWLOntologyFormat();
      pm.setDefaultPrefix(BASE_URL + "#");
      
      String fileType = "Text";
      
      OWLOntologyManager managerL;
      OWLOntology ontologyL;
      PrefixDocumentFormat pmL;
      OWLReasoner reasonerL;
      
      managerL = OWLManager.createOWLOntologyManager();
      ontologyL = managerL.loadOntologyFromOntologyDocument(IRI.create(BASE_URL));
      pmL = managerL.getOntologyFormat(ontologyL).asPrefixOWLOntologyFormat();

      OWLNamedIndividual task = factory.getOWLNamedIndividual(":Task", pmL);
      OWLNamedIndividual nfp = factory.getOWLNamedIndividual(":NFP", pmL);
      OWLObjectProperty IsRequiredProperty = factory.getOWLObjectProperty(":IsRequired", pmL);
      
      OWLNamedIndividual[] IndL = new OWLNamedIndividual[10];
      OWLObjectProperty[] OPL = new OWLObjectProperty[10];
      
      
      
      
      
      
      
      if(fp2.equals("Classification")) {
      
      IndL[6] = factory.getOWLNamedIndividual(":"+fp1, pmL); //教師の有無
      managerL.addAxiom(ontologyL, factory.getOWLObjectPropertyAssertionAxiom(IsRequiredProperty, task, IndL[6]));
      
      IndL[0] = factory.getOWLNamedIndividual(":"+fileType+fp2, pmL); //タスクの種類
      managerL.addAxiom(ontologyL, factory.getOWLObjectPropertyAssertionAxiom(IsRequiredProperty, task, IndL[0]));
      
      
      
      IndL[1] = factory.getOWLNamedIndividual(":"+nfp1, pmL);//ドメイン距離
      managerL.addAxiom(ontologyL, factory.getOWLObjectPropertyAssertionAxiom(IsRequiredProperty, nfp, IndL[1]));
      IndL[2] = factory.getOWLNamedIndividual(":"+nfp2, pmL);//クラス数
      IndL[3] = factory.getOWLNamedIndividual(":ClassNo", pmL);
      managerL.addAxiom(ontologyL, factory.getOWLObjectPropertyAssertionAxiom(IsRequiredProperty, nfp, IndL[3]));
      managerL.addAxiom(ontologyL, factory.getOWLObjectPropertyAssertionAxiom(IsRequiredProperty, IndL[3], IndL[2]));
      int acc = Integer.parseInt(nfp3);
      if(acc <= 50) {
    	  IndL[4] = factory.getOWLNamedIndividual(":Low", pmL);//精度
      }else if(50 < acc && acc < 80) {
    	  IndL[4] = factory.getOWLNamedIndividual(":Medium", pmL);//精度
      }else if(80 <= acc) {
    	  IndL[4] = factory.getOWLNamedIndividual(":High", pmL);//精度
      }
      //IndL[4] = factory.getOWLNamedIndividual(":"+nfp3, pmL);//精度
      IndL[5] = factory.getOWLNamedIndividual(":Accuracy", pmL);
      managerL.addAxiom(ontologyL, factory.getOWLObjectPropertyAssertionAxiom(IsRequiredProperty, nfp, IndL[5]));
      managerL.addAxiom(ontologyL, factory.getOWLObjectPropertyAssertionAxiom(IsRequiredProperty, IndL[5], IndL[4]));
      
      IndL[7] = factory.getOWLNamedIndividual(":RawDataProperty", pmL);
      IndL[8] = factory.getOWLNamedIndividual(":Line", pmL);
      OWLObjectProperty IsComposedProperty = factory.getOWLObjectProperty(":IsComposed", pmL);
      managerL.addAxiom(ontologyL, factory.getOWLObjectPropertyAssertionAxiom(IsComposedProperty, IndL[7], IndL[8]));

      
      
      }else if(fp2.equals("Translation")){
    	  
    	  IndL[0] = factory.getOWLNamedIndividual(":"+fp1, pmL); //タスクの種類
          managerL.addAxiom(ontologyL, factory.getOWLObjectPropertyAssertionAxiom(IsRequiredProperty, task, IndL[0]));
          
          IndL[1] = factory.getOWLNamedIndividual(":"+fp2, pmL);
          managerL.addAxiom(ontologyL, factory.getOWLObjectPropertyAssertionAxiom(IsRequiredProperty, task, IndL[1]));
    			  
          IndL[2] = factory.getOWLNamedIndividual(":InputLang", pmL);
          IndL[3] = factory.getOWLNamedIndividual(":"+nfp1, pmL);//入力言語
          managerL.addAxiom(ontologyL, factory.getOWLObjectPropertyAssertionAxiom(IsRequiredProperty, nfp, IndL[2]));
          managerL.addAxiom(ontologyL, factory.getOWLObjectPropertyAssertionAxiom(IsRequiredProperty, IndL[2], IndL[3]));
          
          IndL[4] = factory.getOWLNamedIndividual(":OutputLang", pmL);
          IndL[5] = factory.getOWLNamedIndividual(":"+nfp2, pmL);//出力言語
          managerL.addAxiom(ontologyL, factory.getOWLObjectPropertyAssertionAxiom(IsRequiredProperty, nfp, IndL[4]));
          managerL.addAxiom(ontologyL, factory.getOWLObjectPropertyAssertionAxiom(IsRequiredProperty, IndL[4], IndL[5]));
          
          IndL[6] = factory.getOWLNamedIndividual(":LearningTime", pmL);
          //実行時間
          if(nfp3.equals("hurry")) {
        	  IndL[7] = factory.getOWLNamedIndividual(":Short", pmL);
        	  managerL.addAxiom(ontologyL, factory.getOWLObjectPropertyAssertionAxiom(IsRequiredProperty, nfp, IndL[6]));
              managerL.addAxiom(ontologyL, factory.getOWLObjectPropertyAssertionAxiom(IsRequiredProperty, IndL[6], IndL[7]));
          }
          
      }//
      
      reasonerL = reasonerFactory.createReasoner(ontologyL, new SimpleConfiguration());
      printResult(reasonerL, factory, pmL);
      saveResult(reasonerL, factory, pmL);
      
      
      
      //System.out.println("targetdl : " + this.targetdl);
      //System.out.println("datagens : " + this.datagens);
      
	  
  }
  
  
  
  public void executeInf() throws OWLOntologyCreationException, OWLOntologyStorageException, IOException {
	  
	//prepare ontology and reasoner
      OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
      OWLOntology ontology = manager.loadOntologyFromOntologyDocument(IRI.create(BASE_URL));
      OWLReasonerFactory reasonerFactory = PelletReasonerFactory.getInstance();
      OWLReasoner reasoner = reasonerFactory.createReasoner(ontology, new SimpleConfiguration());
      OWLDataFactory factory = manager.getOWLDataFactory();
      PrefixDocumentFormat pm = manager.getOntologyFormat(ontology).asPrefixOWLOntologyFormat();
      pm.setDefaultPrefix(BASE_URL + "#");

      //User request1
      String Task = "TextClassification";
      String No_Class = "2";
      String DomainDistance = "NEAR";


      /**

TextClassification
4
Near
Accuracy
- ClassNo 2

Translation
6
Short
BLEU
- InputLang English
- OutputLang Japanese

手書き文字認識(テストの為)
TestCase
- Accuracy 9.0




      **/


      //OWLOntologyManager managerL[] = OWLManager.createOWLOntologyManager();
      //OWLOntologyManager managerL[10];
      OWLOntologyManager managerL;
      //OWLOntology ontologyL = managerL[i].loadOntologyFromOntologyDocument(IRI.create(BASE_URL));
      //PrefixDocumentFormat pmL = manager[i].getOntologyFormat(ontologyL).asPrefixOWLOntologyFormat();
      OWLOntology ontologyL;
      PrefixDocumentFormat pmL;

      //OWLNamedIndividual NIndL[] = factory.getOWLNamedIndividual(":ClassNo", pm);
      //OWLObjectProperty OPL[] = factory.getOWLObjectProperty(":IsRequired", pm);
      //OWLNamedIndividual[] NIndL;
      //OWLObjectProperty[] OPL;
      OWLNamedIndividual[] IndL = new OWLNamedIndividual[10];
      OWLObjectProperty[] OPL = new OWLObjectProperty[10];

      //OWLReasoner reasoner3 = reasonerFactory.createReasoner(ontology2, new SimpleConfiguration());
      OWLReasoner reasonerL;

      Scanner scan = new Scanner(System.in);
      String str;
      int num;
      String[] strs;
      int j = 0;
      String tasktemp;


      Integer exitFlag = 0;
      for(int i = 0; i < 10; i++){
        managerL = OWLManager.createOWLOntologyManager();
        ontologyL = managerL.loadOntologyFromOntologyDocument(IRI.create(BASE_URL));
        pmL = managerL.getOntologyFormat(ontologyL).asPrefixOWLOntologyFormat();

        OWLNamedIndividual task = factory.getOWLNamedIndividual(":Task", pmL);
        OWLNamedIndividual nfp = factory.getOWLNamedIndividual(":NFP", pmL);
        OWLObjectProperty IsRequiredProperty = factory.getOWLObjectProperty(":IsRequired", pmL);

        System.out.println("Task : ");
        str = scan.nextLine();
        tasktemp = str;
        if(str.equals("exit")) exitFlag = 1;
        if(exitFlag == 1) break;
        IndL[0] = factory.getOWLNamedIndividual(":"+str, pmL);

        managerL.addAxiom(ontologyL, factory.getOWLObjectPropertyAssertionAxiom(IsRequiredProperty, task, IndL[0]));

        System.out.println("Number of NFP : ");
        num = scan.nextInt();

        System.out.println("NFP : ");

        //for(int j = 0; j < num+1; j++){
        j = 0;
        while(j < num+1){
          /**if(j == 0) {
            j++;
            continue;
          }**/
          if(j != 0) System.out.println(j);
          //if(j <= -1) continue;
          str = scan.nextLine();
          if(j == 0) str = tasktemp;
          strs = str.split(" ");
          if(j != 0) System.out.println(strs[0]);
          //System.out.println(strs[1]);
          //System.out.println(strs[2]);
          if(strs[0].equals("-")){

            IndL[j] = factory.getOWLNamedIndividual(":"+strs[1], pm);
            IndL[++j] = factory.getOWLNamedIndividual(":"+strs[2], pm);
            managerL.addAxiom(ontologyL, factory.getOWLObjectPropertyAssertionAxiom(IsRequiredProperty, IndL[j-1], IndL[j]));
            j++;

          }else{

            //System.out.println(str);
            IndL[j] = factory.getOWLNamedIndividual(":"+str, pm);
            managerL.addAxiom(ontologyL, factory.getOWLObjectPropertyAssertionAxiom(IsRequiredProperty, nfp, IndL[j]));
            j++;
        }

      }





        reasonerL = reasonerFactory.createReasoner(ontologyL, new SimpleConfiguration());
        printResult(reasonerL, factory, pmL);

        //System.out.println("\n*\n*\n*\n");

        System.out.println("Do you want to execution? (y or n)");
        str = scan.nextLine();
        tasktemp = str;
        if(str.equals("y")) exitFlag = 1;
        if(str.equals("n")) {
          System.out.println("\n*\n*\n*\n");
          continue;
        }
        if(exitFlag == 1) break;

        System.out.println("\n*\n*\n*\n");


        Runtime runtime = Runtime.getRuntime();
        //String analysisFilePath = "C:/Users/suzuki_y/Documents/analysisObject.txt"; // mecabで形態素解析したいtxtファイルを指定


        //String[] Command = { "cmd", "/c", "mecab.exe " + analysisFilePath }; // cmd.exeでmecab.exeを起動し指定したファイル(filePath)を形態素解析する
        String[] Command = {"python", "neuralnet_mnist2.py"}; // cmd.exeでmecab.exeを起動し指定したファイル(filePath)を形態素解析する

        Process p = null;
        //File dir = new File("C:/Program Files (x86)/MeCab/bin");// 実行ディレクトリの指定
        File dir = new File("/Users/ataka/Documents/python/0ml1/deep-learning-from-scratch-master/ch03/");
        try {
            p = runtime.exec(Command, null, dir); // 実行ディレクトリ(dir)でCommand(mecab.exe)を実行する
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            p.waitFor(); // プロセスの正常終了まで待機させる
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        InputStream is = p.getInputStream(); // プロセスの結果を変数に格納する
        BufferedReader br = new BufferedReader(new InputStreamReader(is)); // テキスト読み込みを行えるようにする

        String Acc = "";
        while (true) {
            String line = br.readLine();
            if (line == null) {
                break; // 全ての行を読み切ったら抜ける
            } else {
                System.out.println(line); // 実行結果を表示
                Acc = line;
            }
        }


        double Accuracy = Double.parseDouble(Acc);






        System.out.println("\n*\n*\n*\n");
        //exitFlag = 1;
        //if(exitFlag == 1) break;
      }//End of for
      scan.close();

	  
  }//End of executeInf
  
  
  private void saveResult(OWLReasoner reasoner, OWLDataFactory factory, PrefixDocumentFormat pm) {
	  OWLObjectProperty haveIndProperty = factory.getOWLObjectProperty(":haveInd", pm);
      /**System.out.println("");
      System.out.println("Result");
      System.out.println("");

      System.out.println("");
      System.out.println("・GU");
      System.out.println("");
      **/
	  /**
      OWLClass CAClass = factory.getOWLClass(":CandidateArch", pm);
      for(OWLNamedIndividual CA : reasoner.getInstances(CAClass, false).getFlattened()){
        System.out.println("(Candidate Architecture : " + renderer.render(CA) + ")");
      }

      OWLClass TAClass = factory.getOWLClass(":TA", pm);
      for(OWLNamedIndividual TA : reasoner.getInstances(TAClass, false).getFlattened()){
        System.out.println("TA : " + renderer.render(TA));
      }

      OWLClass TAInfoClass = factory.getOWLClass(":TAInfo", pm);
      for(OWLNamedIndividual TAInfo : reasoner.getInstances(TAInfoClass, false).getFlattened()){
        System.out.println("TAInfo : " + renderer.render(TAInfo));
      }**/

      /**System.out.println("");
      System.out.println("・DP");
      System.out.println("");**/

      int i = 0;
      OWLClass SelectedServiceClass = factory.getOWLClass(":SelectedService", pm);
      for(OWLNamedIndividual SelectedService : reasoner.getInstances(SelectedServiceClass, false).getFlattened()){
        System.out.println("SelectedService : " + renderer.render(SelectedService));
        //this.selectedservice[i] = renderer.render(SelectedService).toString();
        this.datagens = renderer.render(SelectedService).toString();
        
        for (OWLNamedIndividual ind : reasoner.getObjectPropertyValues(SelectedService, haveIndProperty).getFlattened()) { 
            System.out.println("DGS: " + renderer.render(ind)); 
            this.datagensind = renderer.render(ind).toString();
        }
        i++;
      break;
      }
      
      /*
      int i = 0;
      OWLClass SelectedServiceClass = factory.getOWLClass(":SelectedService", pm);
      for(OWLNamedIndividual SelectedService : reasoner.getInstances(SelectedServiceClass, false).getFlattened()){
        System.out.println("SelectedService : " + renderer.render(SelectedService));
        //this.selectedservice[i] = renderer.render(SelectedService).toString();
        this.datagens = renderer.render(SelectedService).toString();
        i++;
      break;
      }
      */
      
      /**
      OWLClass DLInfoClass = factory.getOWLClass(":DLInfo", pm);
      for(OWLNamedIndividual DLInfo : reasoner.getInstances(DLInfoClass, false).getFlattened()){
        System.out.println("DLInfo : " + renderer.render(DLInfo));
      }**/

      /**System.out.println("");
      System.out.println("・DLG");
      System.out.println("");**/
      
      i = 0;
      OWLClass TargetDLClass = factory.getOWLClass(":TargetDL", pm);
      for(OWLNamedIndividual TargetDL : reasoner.getInstances(TargetDLClass, false).getFlattened()){
        System.out.println("TargetDL : " + renderer.render(TargetDL));
        this.targetdl = renderer.render(TargetDL);
        
        for (OWLNamedIndividual ind : reasoner.getObjectPropertyValues(TargetDL, haveIndProperty).getFlattened()) { 
            System.out.println("DLGS: " + renderer.render(ind)); 
            this.targetdlind = renderer.render(ind).toString();
        }
        i++;
      break;
      }
      /*
      i = 0;
      OWLClass TargetDLClass = factory.getOWLClass(":TargetDL", pm);
      for(OWLNamedIndividual TargetDL : reasoner.getInstances(TargetDLClass, false).getFlattened()){
        System.out.println("TargetDL : " + renderer.render(TargetDL));
        //this.targetdl[i] = renderer.render(TargetDL);
        this.targetdl = renderer.render(TargetDL);
        i++;
      break;
      }
      */
      /**
      OWLClass DLComponentsClass = factory.getOWLClass(":DLComponents", pm);
      for(OWLNamedIndividual DLComponents : reasoner.getInstances(DLComponentsClass, false).getFlattened()){
        System.out.println("DLComponents : " + renderer.render(DLComponents));
      }

      OWLClass DLPropertyClass = factory.getOWLClass(":DLProperty", pm);
      for(OWLNamedIndividual DLProperty : reasoner.getInstances(DLPropertyClass, false).getFlattened()){
        System.out.println("DLProperty : " + renderer.render(DLProperty));
      }**/
      //System.out.println("");
    }//End saveResult
  
  
  private static void printResult(OWLReasoner reasoner, OWLDataFactory factory, PrefixDocumentFormat pm) {
      System.out.println("");
      System.out.println("Result");
      System.out.println("");

      System.out.println("");
      System.out.println("・GU");
      System.out.println("");

      OWLClass CAClass = factory.getOWLClass(":CandidateArch", pm);
      for(OWLNamedIndividual CA : reasoner.getInstances(CAClass, false).getFlattened()){
        System.out.println("(Candidate Architecture : " + renderer.render(CA) + ")");
      }

      OWLClass TAClass = factory.getOWLClass(":TA", pm);
      for(OWLNamedIndividual TA : reasoner.getInstances(TAClass, false).getFlattened()){
        System.out.println("TA : " + renderer.render(TA));
      }

      OWLClass TAInfoClass = factory.getOWLClass(":TAInfo", pm);
      for(OWLNamedIndividual TAInfo : reasoner.getInstances(TAInfoClass, false).getFlattened()){
        System.out.println("TAInfo : " + renderer.render(TAInfo));
      }

      System.out.println("");
      System.out.println("・DP");
      System.out.println("");

      OWLClass SelectedServiceClass = factory.getOWLClass(":SelectedService", pm);
      for(OWLNamedIndividual SelectedService : reasoner.getInstances(SelectedServiceClass, false).getFlattened()){
        System.out.println("SelectedService : " + renderer.render(SelectedService));
      }

      OWLClass DLInfoClass = factory.getOWLClass(":DLInfo", pm);
      for(OWLNamedIndividual DLInfo : reasoner.getInstances(DLInfoClass, false).getFlattened()){
        System.out.println("DLInfo : " + renderer.render(DLInfo));
      }

      System.out.println("");
      System.out.println("・DLG");
      System.out.println("");

      OWLClass TargetDLClass = factory.getOWLClass(":TargetDL", pm);
      for(OWLNamedIndividual TargetDL : reasoner.getInstances(TargetDLClass, false).getFlattened()){
        System.out.println("TargetDL : " + renderer.render(TargetDL));
      }

      OWLClass DLComponentsClass = factory.getOWLClass(":DLComponents", pm);
      for(OWLNamedIndividual DLComponents : reasoner.getInstances(DLComponentsClass, false).getFlattened()){
        System.out.println("DLComponents : " + renderer.render(DLComponents));
      }

      OWLClass DLPropertyClass = factory.getOWLClass(":DLProperty", pm);
      for(OWLNamedIndividual DLProperty : reasoner.getInstances(DLPropertyClass, false).getFlattened()){
        System.out.println("DLProperty : " + renderer.render(DLProperty));
      }
      System.out.println("");
    }//End printResult
  
  
  private static OWLReasoner makeGoalClass(OWLOntologyManager manager, OWLDataFactory factory, OWLOntology ontology, DefaultPrefixManager pm, OWLAnnotationProperty annotationProperty, OWLReasonerFactory reasonerFactory, String ID){

      OWLAnnotationValue value = factory.getOWLLiteral("");
      OWLAnnotation annotation = factory.getOWLAnnotation(annotationProperty, value);

      OWLClass goalClass = factory.getOWLClass(":Goal"+ID, pm);
      manager.addAxiom(ontology, factory.getOWLDeclarationAxiom(goalClass));
      OWLNamedIndividual goal = createIndividual(ontology, pm, manager, ":Goal"+ID);
      manager.addAxiom(ontology, factory.getOWLClassAssertionAxiom(goalClass, goal));

      OWLClass guClass = factory.getOWLClass(":GU"+ID, pm);
      manager.addAxiom(ontology, factory.getOWLDeclarationAxiom(guClass));
      //OWLNamedIndividual gu2 = createIndividual(ontology3, pm3, manager, ":GU2");
      //manager.addAxiom(ontology3, factory.getOWLClassAssertionAxiom(gu2Class, gu2));

      OWLClass taClass = factory.getOWLClass(":TA"+ID, pm);
      manager.addAxiom(ontology, factory.getOWLDeclarationAxiom(taClass));
      //OWLNamedIndividual ta2 = createIndividual(ontology3, pm3, manager, ":TA2");
      OWLSubClassOfAxiom subClassOfAxiom = factory.getOWLSubClassOfAxiom(taClass, guClass, Collections.singleton(annotation));
      manager.addAxiom(ontology, subClassOfAxiom);

      //reasonerは最後に定義する（それまでのontologyを反映させる為）
      OWLReasoner reasoner = reasonerFactory.createReasoner(ontology, new SimpleConfiguration());

      for(OWLNamedIndividual cont : reasoner.getInstances(goalClass, false).getFlattened()){
        System.out.println("Goal : " + renderer.render(cont));
      }

      return reasoner;

    }//End of makeGoalClass



    private static void printIndented(Tree<OWLAxiom> node, String indent) {
        OWLAxiom axiom = node.getUserObject();
        System.out.println(indent + renderer.render(axiom));
        if (!node.isLeaf()) {
            for (Tree<OWLAxiom> child : node.getChildren()) {
                printIndented(child, indent + "    ");
            }
        }
    }

    /**
     * Helper class for extracting labels, comments and other anotations in preffered languages.
     * Selects the first literal annotation matching the given languages in the given order.
     */
    @SuppressWarnings("WeakerAccess")
    public static class LocalizedAnnotationSelector {
        private final List<String> langs;
        private final OWLOntology ontology;
        private final OWLDataFactory factory;

        /**
         * Constructor.
         *
         * @param ontology ontology
         * @param factory  data factory
         * @param langs    list of prefered languages; if none is provided the Locale.getDefault() is used
         */
        public LocalizedAnnotationSelector(OWLOntology ontology, OWLDataFactory factory, String... langs) {
            this.langs = (langs == null) ? Collections.singletonList(Locale.getDefault().toString()) : Arrays.asList(langs);
            this.ontology = ontology;
            this.factory = factory;
        }

        /**
         * Provides the first label in the first matching language.
         *
         * @param ind individual
         * @return label in one of preferred languages or null if not available
         */
        public String getLabel(OWLNamedIndividual ind) {
            return getAnnotationString(ind, OWLRDFVocabulary.RDFS_LABEL.getIRI());
        }

        @SuppressWarnings("UnusedDeclaration")
        public String getComment(OWLNamedIndividual ind) {
            return getAnnotationString(ind, OWLRDFVocabulary.RDFS_COMMENT.getIRI());
        }

        public String getAnnotationString(OWLNamedIndividual ind, IRI annotationIRI) {
            return getLocalizedString(EntitySearcher.getAnnotations(ind, ontology, factory.getOWLAnnotationProperty(annotationIRI)));
        }

        private String getLocalizedString(Collection<OWLAnnotation> annotations) {
            List<OWLLiteral> literalLabels = new ArrayList<>(annotations.size());
            for (OWLAnnotation label : annotations) {
                if (label.getValue() instanceof OWLLiteral) {
                    literalLabels.add((OWLLiteral) label.getValue());
                }
            }
            for (String lang : langs) {
                for (OWLLiteral literal : literalLabels) {
                    if (literal.hasLang(lang)) return literal.getLiteral();
                }
            }
            for (OWLLiteral literal : literalLabels) {
                if (!literal.hasLang()) return literal.getLiteral();
            }
            return null;
        }
    }//End of LocalizedAnnotationSelector

    private static OWLNamedIndividual createIndividual(OWLOntology ontology, DefaultPrefixManager pm, OWLOntologyManager manager, String name) {
        OWLDataFactory factory = manager.getOWLDataFactory();
        OWLNamedIndividual individual = factory.getOWLNamedIndividual(name, pm);
        manager.addAxiom(ontology, factory.getOWLDeclarationAxiom(individual));
        return individual;
    }//End of createIndividual

    private static OWLObjectProperty createObjectProperty(OWLOntology ontology, DefaultPrefixManager pm, OWLOntologyManager manager, String name) {
        OWLDataFactory factory = manager.getOWLDataFactory();
        OWLObjectProperty objectProperty = factory.getOWLObjectProperty(name, pm);
        manager.addAxiom(ontology, factory.getOWLDeclarationAxiom(objectProperty));
        return objectProperty;
    }//End of createObjectProperty
    
    
    public void learningExecution() throws IOException, InterruptedException{
    	//ProcessBuilder pb = new ProcessBuilder("pyhton", "/home/ataka/Downloads/Get_registration_status-master/Get_registration_status.py");
    	/**
    	Properties prop = new Properties();
    	prop.put("python.console.encoding", "UTF-8");
    	PythonInterpreter.initialize(System.getProperties(), prop, new String[]{});

    	try(PythonInterpreter py = new PythonInterpreter()){
    	     py.execfile("/home/ataka/Downloads/Get_registration_status-master/Get_registration_status.py");
    	}**/
    	
    	/**ProcessBuilder pb = new ProcessBuilder("sh", "/home/ataka/Documents/test.sh");
    	Process p = pb.start();

    	InputStream stdErr = p.getErrorStream();
    	int c;
    	while ((c = stdErr.read()) != -1) {
    	    System.out.print((char)c);
    	}
    	stdErr.close();

    	p.waitFor();**/
    	
    	/**try{
    		//String command = "ls -l";
    		//String command = "pwd";
    		//String command = "";
    		String command = "pyhton /home/ataka/Downloads/Get_registration_status-master/Get_registration_status.py";
    		Process proc = Runtime.getRuntime().exec(command);
    		InputStream is = proc.getInputStream();
    		BufferedReader br = new BufferedReader(new InputStreamReader(is));
    		String line = null;
    		while ((line = br.readLine()) != null) {
    			System.out.println(line);
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    	}**/
    	
    	/**String result;
    	  try {
    	    Runtime rt = Runtime.getRuntime();
    	    Process p = rt.exec("/usr/bin/pyhton /home/ataka/Downloads/Get_registration_status-master/Get_registration_status.py");
    	    InputStream is = p.getInputStream();
    	    InputStreamReader isr = new InputStreamReader(is);
    	    BufferedReader br = new BufferedReader(isr);
    	      while ((result = br.readLine()) != null) {
    	      System.out.println(result);
    	      }
    	  } catch (IOException ex) {
    	    ex.printStackTrace();
    	  }**/
    	
    	/*ProcessBuilder pb = new ProcessBuilder("sh", "/home/ataka/Documents/test.sh");
    	Process p = pb.start();

    	InputStream stdErr = p.getErrorStream();
    	int c;
    	while ((c = stdErr.read()) != -1) {
    	    System.out.print((char)c);
    	}
    	stdErr.close();
    	p.waitFor();*/
    	
    	//ProcessBuilder pb = new ProcessBuilder("sh", "/home/ataka/Documents/test.sh");
    	ProcessBuilder pb = new ProcessBuilder("sh", REGISTRY_PATH + "python/Seq2seq/test2.sh");
    	Process p = pb.start();
    	
    	/*p.waitFor();
    	
    	InputStream is = p.getInputStream();	//標準出力
    	printInputStream(is);
    	InputStream es = p.getErrorStream();	//標準エラー
    	printInputStream(es);
    	
    	p.waitFor();*/
    	
    	
    	
    	//
    	
    	InputStreamThread it = new InputStreamThread(p.getInputStream());
    	InputStreamThread et = new InputStreamThread(p.getErrorStream());
    	it.start();
    	et.start();

    	//プロセスの終了待ち
    	p.waitFor();

    	//InputStreamのスレッド終了待ち
    	it.join();
    	et.join();

    	//System.out.println("戻り値：" + p.exitValue());

    	//標準出力の内容を出力
    	for (String s : it.getStringList()) {
    		System.out.println(s);
    	}
    	//標準エラーの内容を出力
    	for (String s : et.getStringList()) {
    		System.err.println(s);
    	}
    	
    	/*
    	String LResult = it.getLearningResult();    	
    	String[] LResults = LResult.split(" ",0);
    	this.lresult = "Optimization Finished! " + LResult;    	
    	this.acc = Double.parseDouble(LResults[3].replace(")",""));
    	*/
    	//プロセスの終了待ち
    	//p.waitFor();
    	//this.lresult = "Optimization Finished! " + "(kari)";    	
    	
    	this.ostr = it.getLearningResult2();
    	this.acc = 632.38;
    	
    }//End of learningExecution
    
    
    public void learningExecution1() throws IOException, InterruptedException{

    	try {
        	FileWriter file = new FileWriter(REGISTRY_PATH + "python/CNN/test1.sh");
        	PrintWriter pw = new PrintWriter(new BufferedWriter(file));
        	//CNNtest
        	pw.println("#!/bin/sh");
            pw.println();
            pw.println(". /home/ataka/tensorflow/bin/activate");
            
            //pw.println("python " + REGISTRY_PATH + "python/Seq2seq/" + this.targetdlind);
            pw.println("python /home/ataka/Documents/Impementation/ESA/Demo/exp/test3/IS_OS/data/result/Add/OUTPUT/CNN-TFIDF-se-con.py");
            
            pw.close();
        	}catch(IOException e) {
        		e.printStackTrace();
        }
    	
    	/*
    	 try {
        	FileWriter file = new FileWriter(REGISTRY_PATH + "java/test.sh");
        	PrintWriter pw = new PrintWriter(new BufferedWriter(file));
        	
        	pw.println("#!/bin/sh");
            pw.println();
            System.out.println("\"" + REGISTRY_PATH + "data/uploaded/" + this.fp3name + "\"");
            //pw.println("python " + REGISTRY_PATH + "python/Seq2seq/makeData.py" + " " + "\"" + REGISTRY_PATH + "data/uploaded/" + this.fp3name + "\"");
            System.out.println("python " + REGISTRY_PATH + "java/" + this.datagensind + " " + "\"" + REGISTRY_PATH + "data/uploaded/" + this.fp3name + "\"");
            pw.println("python " + REGISTRY_PATH + "java/" + this.datagensind + " " + "\"" + REGISTRY_PATH + "data/uploaded/" + this.fp3name + "\"");
            pw.println();
            //pw.println("python " + REGISTRY_PATH + "python/Seq2seq/makeModel.py");
            System.out.println("python " + REGISTRY_PATH + "python/Seq2seq/" + this.targetdlind);
            pw.println("python " + REGISTRY_PATH + "python/CNN/" + this.targetdlind);
            
            pw.close();
        	}catch(IOException e) {
        		e.printStackTrace();
        }*/
    	
    	ProcessBuilder pb = new ProcessBuilder("sh", REGISTRY_PATH + "python/CNN/test1.sh");
    	Process p = pb.start();
    	
    	InputStreamThread it = new InputStreamThread(p.getInputStream());
    	InputStreamThread et = new InputStreamThread(p.getErrorStream());
    	it.start();
    	et.start();

    	//プロセスの終了待ち
    	p.waitFor();

    	//InputStreamのスレッド終了待ち
    	it.join();
    	et.join();

    	//System.out.println("戻り値：" + p.exitValue());

    	//標準出力の内容を出力
    	for (String s : it.getStringList()) {
    		System.out.println(s);
    	}
    	//標準エラーの内容を出力
    	for (String s : et.getStringList()) {
    		System.err.println(s);
    	}
    	
    	String LResult = it.getLearningResult();    	
    	String[] LResults = LResult.split(" ",0);
    	this.lresult = "Optimization Finished! " + LResult;    	
    	this.acc = Double.parseDouble(LResults[3].replace(")",""));
    	
    }//End of learningExecution1
    
    
    
    
    public void learningExecution2() throws IOException, InterruptedException{
    	
    	try {
        	FileWriter file = new FileWriter(REGISTRY_PATH + "python/Seq2seq/test2.sh");
        	PrintWriter pw = new PrintWriter(new BufferedWriter(file));
        	
        	pw.println("#!/bin/sh");
            pw.println();
            System.out.println("\"" + REGISTRY_PATH + "data/uploaded/" + this.fp3name + "\"");
            //pw.println("python " + REGISTRY_PATH + "python/Seq2seq/makeData.py" + " " + "\"" + REGISTRY_PATH + "data/uploaded/" + this.fp3name + "\"");
            System.out.println("python " + REGISTRY_PATH + "python/Seq2seq/" + this.datagensind + " " + "\"" + REGISTRY_PATH + "data/uploaded/" + this.fp3name + "\"");
            pw.println("python " + REGISTRY_PATH + "python/Seq2seq/" + this.datagensind + " " + "\"" + REGISTRY_PATH + "data/uploaded/" + this.fp3name + "\"");
            pw.println();
            //pw.println("python " + REGISTRY_PATH + "python/Seq2seq/makeModel.py");
            System.out.println("python " + REGISTRY_PATH + "python/Seq2seq/" + this.targetdlind);
            pw.println("python " + REGISTRY_PATH + "python/Seq2seq/" + this.targetdlind);
            
            pw.close();
        	}catch(IOException e) {
        		e.printStackTrace();
        }
    	
    	ProcessBuilder pb = new ProcessBuilder("sh", REGISTRY_PATH + "python/Seq2seq/test2.sh");
    	Process p = pb.start();
    	
    	InputStreamThread it = new InputStreamThread(p.getInputStream());
    	InputStreamThread et = new InputStreamThread(p.getErrorStream());
    	it.start();
    	et.start();

    	//プロセスの終了待ち
    	p.waitFor();

    	//InputStreamのスレッド終了待ち
    	it.join();
    	et.join();

    	//System.out.println("戻り値：" + p.exitValue());

    	//標準出力の内容を出力
    	for (String s : it.getStringList()) {
    		System.out.println(s);
    	}
    	//標準エラーの内容を出力
    	for (String s : et.getStringList()) {
    		System.err.println(s);
    	}
    	
    	this.ostr = it.getLearningResult2();
    	this.acc = 632.38;
    	
    }//End of learningExecution2
    
    
    public void learningExecution3() throws IOException, InterruptedException{
    	try {
    	FileWriter file = new FileWriter(REGISTRY_PATH + "python/Seq2seq/test3.sh");
    	PrintWriter pw = new PrintWriter(new BufferedWriter(file));
    	
    	pw.println("#!/bin/sh");
        pw.println();
        pw.println("python " + REGISTRY_PATH + "python/Seq2seq/excute.py" + " " + "\"" + this.userInputStr + "\"" + " " + REGISTRY_PATH + "python/Seq2seq/");
        
        pw.close();
    	}catch(IOException e) {
    		e.printStackTrace();
    	}
    	
    	
    	ProcessBuilder pb = new ProcessBuilder("sh", REGISTRY_PATH + "python/Seq2seq/test3.sh");
    	Process p = pb.start();
    	
    	InputStreamThread it = new InputStreamThread(p.getInputStream());
    	InputStreamThread et = new InputStreamThread(p.getErrorStream());
    	it.start();
    	et.start();

    	//プロセスの終了待ち
    	p.waitFor();

    	//InputStreamのスレッド終了待ち
    	it.join();
    	et.join();

    	//System.out.println("戻り値：" + p.exitValue());

    	//標準出力の内容を出力
    	for (String s : it.getStringList()) {
    		System.out.println(s);
    	}
    	//標準エラーの内容を出力
    	for (String s : et.getStringList()) {
    		System.err.println(s);
    	}
    	
    	this.istr = this.userInputStr;
    	this.ostr = it.getLearningResult2();
    	this.acc = 632.38;
    	
    }//End of learningExecution3
    
    
    
    public static void printInputStream(InputStream is) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		try {
			for (;;) {
				String line = br.readLine();
				if (line == null) break;
				System.out.println(line);
			}
		} finally {
			br.close();
		}
	}//End of printInputStream
  
  

}
