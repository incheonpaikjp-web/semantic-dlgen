/**
 * @RegistrationServlet.java
 * @date Feb. 10, 2020
 * @author Ryo Ataka
 * @brief Servlet for Registration
 * @details
 *  Deep Learning Generation System, Version 1.0
 *  Copyright (C) 2020 Intelligent Data Analytics Laboratory, All Rights Reserved.
 */

package serv;

import java.io.*;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;



@WebServlet("/RegistrationServlet")
@MultipartConfig(location="/home/ataka/Documents/reg/data/uploaded")
public class RegistrationServlet extends HttpServlet {
	//private static final long serialVersionUID = 1L;
	//InferenceBean infBean = new InferenceBean();
	final File uploadDir = new File("upload");
	public void init() throws ServletException {
	    uploadDir.mkdir();
	}
	
	@Override
	  protected void doPost(HttpServletRequest request,
	      HttpServletResponse response)
	      throws ServletException, IOException {

	    String owl;
	    String serviceName;
	    Part service;
		

	    try {
	      //studentNo = Integer.parseInt(request.getParameter("studentno"));
	    	owl = request.getParameter("owl");
	    	service = request.getPart("service");
	      
	    	serviceName = this.getFileName(service);
	      System.out.println(service);
	      System.out.println(serviceName);
	      //fp3.write(getServletContext().getRealPath("/WEB-INF/uploaded") + "/" + fp3name);
	      //service.write("/WEB-INF/uploaded/" + serviceName);
	      service.write("/home/ataka/Documents/reg/New/" + serviceName);
	      
	      //save(fp3, new File(uploadDir, fp3name));
	      addOwl(owl);

	    } catch (NumberFormatException e) {
	      getServletContext().getRequestDispatcher("/errorinput.html")
	          .forward(request, response);
	      return;
	    }

	    
	    
	    getServletContext().getRequestDispatcher("/output.jsp").forward(request, response);
	    
	  }//End of doPost
	
	
	private String getFileName(Part part) {
        String name = null;
        for (String dispotion : part.getHeader("Content-Disposition").split(";")) {
            if (dispotion.trim().startsWith("filename")) {
                name = dispotion.substring(dispotion.indexOf("=") + 1).replace("\"", "").trim();
                name = name.substring(name.lastIndexOf("\\") + 1);
                break;
            }
        }
        return name;
    }//End of getFileName
	
	
	public void save(Part in, File out) throws IOException {
	    BufferedInputStream br
	      = new BufferedInputStream(in.getInputStream());
	    try (BufferedOutputStream bw =
	      new BufferedOutputStream(new FileOutputStream(out))
	    ) {
	      int len = 0;
	      byte[] buff = new byte[1024];
	      while ((len = br.read(buff)) != -1) {
	        bw.write(buff, 0, len);
	      }
	    }
	}//End of save
	
	
	public void addOwl(String owl) {
		FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        
        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        BufferedWriter bw = null;
        
        try{
            
            // ファイル入力（MS932）
            fis = new FileInputStream("/home/ataka/Documents/OntDLG/WebContent/OntKB2.owl");
            isr = new InputStreamReader(fis, "MS932");
            br = new BufferedReader(isr);
            
            // ファイル出力（UTF8）
            fos = new FileOutputStream("/home/ataka/Documents/OntDLG/WebContent/OntKB3.owl");
            osw = new OutputStreamWriter(fos, "UTF8");
            bw = new BufferedWriter(osw);
            
            String line;
            while((line = br.readLine()) != null){
            	
            	if(line.equals(")#End of Ontology")) {
                	line = line.replace(")#End of Ontology", "");
                	
                	bw.write(moldOwl(owl));
                	
                	bw.newLine();
                	bw.newLine();
                	
                	bw.write(")#End of Ontology");
                	bw.newLine();
                }else {
                
                // 置換処理
                line = line.replace("OntKB2", "OntKB3");
                
                // ファイルへ書き込み
                bw.write(line);
                bw.newLine();
                }
            }
            
        } catch(IOException e) {
            e.printStackTrace();
        }finally{
            if(br != null){
                try{
                    br.close();
                }catch(IOException ie){
                }
            }
            if(isr != null){
                try{
                    isr.close();
                }catch(IOException ie){
                }
            }
            if(fis != null){
                try{
                    fis.close();
                }catch(IOException ie){
                }
            }
            
            if(bw != null){
                try{
                    bw.close();
                }catch(IOException ie){
                }
            }
            if(osw != null){
                try{
                    osw.close();
                }catch(IOException ie){
                }
            }
            if(fos != null){
                try{
                    fos.close();
                }catch(IOException ie){
                }
            }
        }
		
	}//End of addOwl
	
	public String moldOwl(String owl) {
		return owl = owl.replace("^M", "");
	}//End of moldOwl

}


