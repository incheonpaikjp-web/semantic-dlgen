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

<!--<jsp:getProperty name="infBean" property="ostr" />-->
<!--<jsp:getProperty name="infBean" property="lresult" /><br>-->
<jsp:getProperty name="infBean" property="acc" /><br>

(Your requested accuracy: 
<jsp:getProperty name="infBean" property="nfp3" />
% or more)<br><br><br>

<br><br>
<a href="output2.jsp">Back to inference page</a><br>
<a href="input.html">back to the top page</a>

</p>




</body>
</html>