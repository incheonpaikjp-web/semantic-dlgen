/**
 * @OntDLGServlet.java
 * @date Feb. 10, 2020
 * @author Ryo Ataka
 * @brief Servlet for DL Generation System
 * @details
 *  Deep Learning Generation System, Version 1.0
 *  Copyright (C) 2020 Intelligent Data Analytics Laboratory, All Rights Reserved.
 */



package serv;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.semanticweb.owlapi.model.*;

import javax.servlet.annotation.MultipartConfig;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;




/**
import com.clarkparsia.owlapi.explanation.DefaultExplanationGenerator;
import com.clarkparsia.owlapi.explanation.util.SilentExplanationProgressMonitor;
import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;
import com.google.common.collect.Multimap;

import java.InferenceBean;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.dlsyntax.renderer.DLSyntaxObjectRenderer;
import org.semanticweb.owlapi.formats.PrefixDocumentFormat;
import org.semanticweb.owlapi.io.OWLObjectRenderer;

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

import java.util.Scanner;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
**/


@WebServlet("/OntDLGServlet")
//@MultipartConfig(location="/tmp", maxFileSize=1048576)
//@MultipartConfig(location="/tmp")
//@MultipartConfig
@MultipartConfig(location="/home/ataka/Documents/reg/data/uploaded")
public class OntDLGServlet extends HttpServlet {
	//private static final long serialVersionUID = 1L;
	//InferenceBean infBean = new InferenceBean();
	final File uploadDir = new File("upload");
	public void init() throws ServletException {
	    uploadDir.mkdir();
	}
	
	@Override
	  protected void doGet(HttpServletRequest request,
	      HttpServletResponse response)
	      throws ServletException, IOException {

	    String fp1, fp2;
	    String fp3name;
	    Part fp3 = request.getPart("file");
		String nfp1, nfp2, nfp3;

	    try {
	      //studentNo = Integer.parseInt(request.getParameter("studentno"));
	      fp1 = request.getParameter("fp1");
	      fp2 = request.getParameter("fp2");
	      //fp3 = request.getParameter("fp3");
	      //fp3 = request.getParameter("inputFiles[]");
	      nfp1 = request.getParameter("nfp1");
	      nfp2 = request.getParameter("nfp2");
	      nfp3 = request.getParameter("nfp3");
	      
	      //Part part = request.getPart("inputFiles[]");
	      fp3name = this.getFileName(fp3);
	      fp3.write(getServletContext().getRealPath("/WEB-INF/uploaded") + "/" + fp3name);
	      
	      
	      

	    } catch (NumberFormatException e) {
	      getServletContext().getRequestDispatcher("/errorinput.html")
	          .forward(request, response);
	      return;
	    }

	    
	    InferenceBean infBean = new InferenceBean();
	    infBean.setFp1(fp1);
	    infBean.setFp2(fp2);
	    //infBean.setFp3(fp3);
	    //infBean.setFp3(fp3);
	    infBean.setFp3name(fp3name);
	    infBean.setNfp1(nfp1);
	    infBean.setNfp2(nfp2);
	    infBean.setNfp3(nfp3);
	    try {
	    	//infBean.executeInf();
	    	infBean.passTest();
	    }catch(OWLOntologyCreationException e) {
	    }catch(OWLOntologyStorageException e) {
	    }
	    
	    
	    HttpSession session = request.getSession();
	    session.setAttribute("infBean", infBean);
	    //getServletContext().getRequestDispatcher("/output.jsp").forward(request, response);
	    getServletContext().getRequestDispatcher("/output2.jsp").forward(request, response);
	    
	    /*
	    request.setAttribute("infBean", infBean);
	    getServletContext().getRequestDispatcher("/output.jsp").forward(request, response);
	  */
	  }//End of doGet
	
	
	
	
	@Override
	  protected void doPost(HttpServletRequest request,
	      HttpServletResponse response)
	      throws ServletException, IOException {

	    String fp1, fp2;
	    String fp3name;
	    Part fp3;
		String nfp1, nfp2, nfp3;

	    try {
	      //studentNo = Integer.parseInt(request.getParameter("studentno"));
	      fp1 = request.getParameter("fp1");
	      fp2 = request.getParameter("fp2");
	      fp3 = request.getPart("fp3");
	      //fp3 = request.getParameter("inputFiles[]");
	      nfp1 = request.getParameter("nfp1");
	      nfp2 = request.getParameter("nfp2");
	      nfp3 = request.getParameter("nfp3");
	      
	      //Part part = request.getPart("inputFiles[]");
	      fp3name = this.getFileName(fp3);
	      System.out.println(fp3);
	      System.out.println(fp3name);
	      //fp3.write(getServletContext().getRealPath("/WEB-INF/uploaded") + "/" + fp3name);
	      //fp3.write("/WEB-INF/uploaded" + "/" + fp3name);
	      fp3.write("/home/ataka/Documents/reg/data/uploaded" + "/" + fp3name);
	      
	      //save(fp3, new File(uploadDir, fp3name));
	      

	    } catch (NumberFormatException e) {
	      getServletContext().getRequestDispatcher("/errorinput.html")
	          .forward(request, response);
	      return;
	    }

	    
	    InferenceBean infBean = new InferenceBean();
	    infBean.setFp1(fp1);
	    infBean.setFp2(fp2);
	    //infBean.setFp3(fp3);
	    //infBean.setFp3(fp3);
	    infBean.setFp3name(fp3name);
	    infBean.setNfp1(nfp1);
	    infBean.setNfp2(nfp2);
	    infBean.setNfp3(nfp3);
	    try {
	    	//infBean.executeInf();
	    	infBean.passTest();
	    }catch(OWLOntologyCreationException e) {
	    }catch(OWLOntologyStorageException e) {
	    }
	    
	    
	    HttpSession session = request.getSession();
	    session.setAttribute("infBean", infBean);
	    //getServletContext().getRequestDispatcher("/output.jsp").forward(request, response);
	    getServletContext().getRequestDispatcher("/output2.jsp").forward(request, response);
	    
	    /*
	    request.setAttribute("infBean", infBean);
	    getServletContext().getRequestDispatcher("/output.jsp").forward(request, response);
	  */
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
    }
	
	
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
	}
	    
	    

}


