<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Inference results</title>
</head>
<body>
<jsp:useBean id="infBean" class="serv.InferenceBean" scope="session" />


<p>

<font size="4">Plans inferred from requirements</font><br><br>

<form name="form2" action="OntDLGServlet2" method="get">

・Data generation : <br>
<jsp:getProperty name="infBean" property="datagens" /><br><br>

・Deep learning architecture : <br>
<jsp:getProperty name="infBean" property="targetdl" /><br><br>

<!--
・Data generation Service: <br>
<jsp:getProperty name="infBean" property="datagensind" /><br><br>

・Deep learning architecture Service: <br>
<jsp:getProperty name="infBean" property="targetdlind" /><br><br>
-->

<br>
<input type="hidden" name="fp1" value="<jsp:getProperty name="infBean" property="fp1" />">
<input type="hidden" name="fp2" value="<jsp:getProperty name="infBean" property="fp2" />">
<input type="hidden" name="fp3" value="<jsp:getProperty name="infBean" property="fp3" />">
<input type="hidden" name="fp3name" value="<jsp:getProperty name="infBean" property="fp3name" />">
<input type="hidden" name="nfp1" value="<jsp:getProperty name="infBean" property="nfp1" />">
<input type="hidden" name="nfp2" value="<jsp:getProperty name="infBean" property="nfp2" />">
<input type="hidden" name="nfp3" value="<jsp:getProperty name="infBean" property="nfp3" />">
<input type="hidden" name="datagensind" value="<jsp:getProperty name="infBean" property="datagensind" />">
<input type="hidden" name="targetdlind" value="<jsp:getProperty name="infBean" property="targetdlind" />">


<input type="submit" value="Service compose and execute"><br>

<!--<a href="">Show all Candidates</a><br>-->
<a href="input.html">re-input Properties</a>

</form>

</p>




</body>
</html>