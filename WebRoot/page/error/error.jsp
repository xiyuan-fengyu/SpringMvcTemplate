<%@ page language="java" import="java.util.*,java.io.ByteArrayOutputStream,java.io.PrintStream" pageEncoding="UTF-8" isErrorPage="true"%>
<!DOCTYPE html>
<html>
  <head>
    
    <title>报错了！</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">

	<link rel="icon" href="<%=request.getContextPath()%>/image/favicon/favicon32.ico" type="image/x-icon"/> 
	<link rel="shortcut icon" href="<%=request.getContextPath()%>/image/favicon/favicon16.ico" type="image/x-icon"/>
	
  </head>
  
  <body>
    <%=exception.getClass()%>:<%=exception.getMessage()%> 
    <br>
    <% 	
    	Enumeration<String> e = request.getHeaderNames(); 
    	String key; 
    	while(e.hasMoreElements())
    	{ 
    		key = e.nextElement(); 
    	} 
    	e = request.getAttributeNames(); 
    	while(e.hasMoreElements())
    	{ 
    		key = e.nextElement(); 
    	} 
    	e = request.getParameterNames(); 
    	while(e.hasMoreElements())
    	{ 
    		key = e.nextElement(); 
    	} 
    %> 
    <%=request.getAttribute("javax.servlet.forward.request_uri")%>
    <br> 
    <%=request.getAttribute("javax.servlet.forward.servlet_path")%>
    <p>With the following stack trace:</p> 
    <pre> 
    	<% 
	    	exception.printStackTrace(); 
	    	ByteArrayOutputStream ostr = new ByteArrayOutputStream(); 
	    	exception.printStackTrace(new PrintStream(ostr)); 
	    	out.print(ostr); 
    	%>
    </pre>
  </body>
</html>
