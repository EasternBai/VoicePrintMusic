<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title></title>
    

  </head>
  
  <body>
    <form action="<%=basePath%>myusers/add.do" enctype="multipart/form-data" method="post">
    	姓名：<input type="text" name="name"/><br/>
    	年龄：<input type="text" name="age"/><br/>
    	部门：<select name="dept.id">
    			<option value="1">市场部</option>
    			<option value="2">开发部</option>
    			<option value="3">维护部</option>
    		</select></br>
    	头像：<input type="file" name="headfile" /></br>
    	<input type="submit" value="新增测试"/>
    </form>
  </body>
</html>
