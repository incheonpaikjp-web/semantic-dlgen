<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Learning Result Page</title>
</head>
<body>
<jsp:useBean id="infBean" class="serv.InferenceBean" scope="session" />


<p>

<font size="5">Learning Result : </font><br><br><br>

<form name="form3" action="OntDLGServlet3" method="get">

(Your Input sentence: 
<jsp:getProperty name="infBean" property="istr" />
)<br><br><br>

<jsp:getProperty name="infBean" property="ostr" />
<!--<jsp:getProperty name="infBean" property="lresult" /><br>-->
<!--<jsp:getProperty name="infBean" property="acc" /><br>-->

<br><br>
Please inputs 
<jsp:getProperty name="infBean" property="nfp1" />
 sentence :<br>
 <input type="text" name="userInputStr">


<input type="submit" value="Test translation"><br>
<br><br>
<a href="output2.jsp">Back to inference page</a><br>
<a href="input.html">back to the top page</a>
</form>
</p>




</body>
</html>