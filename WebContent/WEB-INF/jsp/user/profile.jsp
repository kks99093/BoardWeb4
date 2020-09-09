<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>프로필</title>
</head>
<body>
<div class="container">
	<h1>프로필</h1>
	<div>
		<div>
			<c:choose>
				<c:when test="${data.profile_img !=null}">
					<img src="img/user/${loginUser.i_user}/${data.profile_img}">
				</c:when>
				<c:otherwise>
					<img src="img/defalut_profile.jpg">
				</c:otherwise>
			</c:choose>
		</div>
		<div>
			<form action="/profile" method="post" enctype="multipart/form-data">
			<%-- 파일업로드할때는 enctype="multipart/form=data"를 꼭! 무조건! 적어줘야한다 --%>
				<div>
					<label>프로필 이미지 : <input type="file" name="profile_img" accept="image/*"></label>
					<%-- 이미지만 넣고싶다면 accept="image/*"을 넣어줘야한다 --%>
					<input type="submit" value="업로드">
				</div>
			</form>
		</div>
		
		<div>ID : ${data.user_id}</div>
		<div>이름 : ${data.nm}</div>		
		<div>이메일 : ${data.email}</div>
		<div>가입일시 : ${data.r_dt}</div>
	</div>
	<div>
	<a href="/changePw"><button>비밀번호 변경</button></a>
	</div>
	<script>
		var proc = '${param.proc}'
		switch(proc){
		case '1' :
			alert('비밀번호를 변경하였습니다')
			break
		}
	</script>
</div>
</body>
</html>