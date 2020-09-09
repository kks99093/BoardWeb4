<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>비밀번호 변경</title>
</head>
<body>
	<c:if test="${isAuth == false || isAuth == null}"> <!-- 현재 비밀번호 확인 -->
		<div><a href="/profile">돌아가기</a></div>
		<div>${msg }</div>
		<div>
			<form action="/changePw" method="post">
				<%-- 1을 보내면 현재비밀번호 확인 --%>
				<input type="hidden" name="type" value="1">
				<div>
					<label><input type="password" placeholder="현재 비밀번호" name="pw"></label>
				</div>
				<div>
					<input type="submit" value="확인">
				</div>
			</form>
		</div>
	</c:if>
	<c:if test="${isAuth == true}"> <!-- 비밀번호 변경 -->
		<form id="changefrm" action="/changePw" method="post" onsubmit="return chkChangePW()">
			<input type="hidden" name="type" value="2">
		<div>
			<label><input type="password" placeholder="변경 비밀번호" name="pw"></label>
		</div>
		<div>
			<label><input type="password" placeholder="변경 비밀번호 확인" name="rwpw"></label>
		</div>
		<div>
			<input type="submit" value="확인">
		</div>
		</form>
		
	</c:if>
	<script>
		function chkChangePW(){
			if(changefrm.pw.value.length < 5){
				alert("비빌번호를 5글자이상 적어주세요")
			}else if(changefrm.pw.value != changefrm.rwpw.value){
				alert("비빌번호를 확인해주세요")
			} else{
				return true
			}
			return false
		}
	</script>
</body>
</html>