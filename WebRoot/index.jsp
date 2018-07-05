<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title></title>
	<style type="text/css">
		img{width:50px;}
		td{text-align: center;}
		a:LINK {text-decoration: none;}
	</style>
	<script type="text/javascript">
		var strColor='';
		
		//鼠标移动上来
		function rowin(rowobj){
			//记录原始颜色
			strColor=rowobj.style.backgroundColor;
			
			//变成高亮颜色
			rowobj.style.backgroundColor='#FC3';
		}
		
		//鼠标移动出去
		function rowout(rowobj){
			rowobj.style.backgroundColor=strColor;
		}
		
		//奇偶行变色
		function rowColorInit(){
			var oTab=document.getElementsByTagName('table')[0];			
			var arrayRows=oTab.getElementsByTagName('tr');
			var sColor='';
			
			for(var i=0;i<arrayRows.length;i++){
				
				if(i==0){
					sColor='#06C';
				}else if(i==arrayRows.length-1){
					sColor='#FFF';
				}else if(i%2==0){
					sColor='#999';
				}else{
					sColor='#CCC';
				}
				
				arrayRows[i].style.backgroundColor=sColor;
			}
		}
				
		//删除提示
		function delmessage(){
			return window.confirm('是否确定删除');
		}
		
	</script>
  </head>
  
  <body onload="rowColorInit()">
	<table id="tab1" border="1" align="center" width="50%" cellpadding="0" cellspacing="1">
		<caption>
			<a href="add.jsp">新增</a>
			<a href="myusers/findall.do">加载</a>
		</caption>
		<thead>
			<tr>
				<th>主键</th>
				<th>姓名</th>
				<th>年龄</th>
				<th>头像</th>
				<th>部门</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>			
			<c:if test="${!empty mudata.list }">
				<c:forEach items="${mudata.list}" var="mu">
					<tr onmouseover="rowin(this)" onmouseout="rowout(this)">
						<td>${mu.id }</td>
						<td>${mu.name }</td>
						<td>${mu.age }</td>
						<td>
							<c:if test="${mu.headpath !=null}">
								<img src="upload/${mu.headpath }"/>
							</c:if>
							<c:if test="${mu.headpath ==null}">
								<img src="img/default_head.png"/>
							</c:if>							
						</td>
						<td>${mu.dept.name }</td>
						<td>
							<a href="">编辑</a>
							<a href="">删除</a>
						</td>
					</tr>				
				</c:forEach>
			</c:if>
		</tbody>
		<tfoot>
			<tr>
				<td colspan="6" style="text-align:right;">
					<!-- 首页 -->
	    			<c:url var="begin" value="myusers/findall.do">
	    				<c:param name="pageIndex">1</c:param>
	    			</c:url>
	    			
	    			<a href="${begin }">首页</a>
	    			
	    			<!-- 上一页 -->
	    			<c:if test="${ mudata.page.pageIndex-1>=1}">
	    				<c:url var="up" value="myusers/findall.do">
		    				<c:param name="pageIndex">
								${mudata.page.pageIndex-1 }
							</c:param>
	    				</c:url>
	    			
	    				<a href="${up }">上一页</a>
	    			</c:if>
	    			<c:if test="${mudata.page.pageIndex-1<=0 }">
	    				上一页
	    			</c:if>
	    			
	    			
	    			<!-- 下一页 -->
	    			<c:if test="${ mudata.page.pageIndex+1<=mudata.page.pageCount}">
	    				<c:url var="dwon" value="myusers/findall.do">
		    				<c:param name="pageIndex">
								${mudata.page.pageIndex+1 }
							</c:param>
	    				</c:url>
	    			
	    				<a href="${dwon }">下一页</a>
	    			</c:if>
	    			<c:if test="${mudata.page.pageIndex+1>mudata.page.pageCount }">
	    				下一页
	    			</c:if>
	    			
	    			<!-- 末页 -->
	    			<c:url var="end" value="myusers/findall.do">
	    				<c:param name="pageIndex">${mudata.page.pageCount }</c:param>
	    			</c:url>
	    			
	    			<a href="${end }">末页</a>
				
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    				【当前第${ mudata.page.pageIndex}页/总共${mudata.page.pageCount }页】
				</td>
			</tr>
		</tfoot>
	</table>
  </body>
</html>
