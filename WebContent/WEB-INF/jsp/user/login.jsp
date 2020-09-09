<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>로 그 인</title>
<style>
	.container{width:400px; margin:20px auto; text-align: center;}
</style>
</head>
<body>
	<div class="container">
		<div><span color="red">${msg}</span></div>
		<div><h1>로그인</h1></div>
		<form id="frm" action="/login" method="post" onsubmit="return chk()">
			<div><input type="text" name="user_id" placeholder="아이디" value="${user_id}"></div>
			<div><input type="password" name="user_pw" placeholder="비밀번호"></div>
			<div><input type="submit" value="로그인"></div>
		</form>
		<a href="/join">회원가입</a>
	</div>
	<script>
		function chk() {
			if(frm.user_id.value.length == 0){
				alert("아이디를 입력해 주세요")
				return false
			}else if(frm.user_pw.value.length == 0){
				alert("비밀번호를 입력해 주세요")
				return false
			}
			return true
		}
	</script>
</body>
</html>