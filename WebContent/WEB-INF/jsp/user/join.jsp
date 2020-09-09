<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%-- 리퀘스트.디스패쳐만 접근가능 --%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원가입</title>
</head>
<body>
	<h1>회원가입</h1>
	<div id="container">
		<div>
			<h2><span color="">${msg}</span></h2>
			<form id="frm" action="/join" method="post" onsubmit="return chk()">
				<div><label><input type="text" name="user_id" placeholder="아이디" required" value="${param.user_id}"></label></div>
				<div><label><input type="password" name="user_pw" placeholder="비밀번호" required></label></div>
				<div><label><input type="password" name="user_pwre" placeholder="확인 비밀번호"></label></div>
				<div><input type="text" name="nm" placeholder="이름" required value="${param.nm}"></div>
				<div><input type="email" name="email" placeholder="이메일" value="${param.email}"></div>
				<div><input type="submit" value="회원가입"></div>
				
			</form>
			<script>
			//이름 한글로만 받기(정규식) - 정규식 한글이라고 그냥 검색해서 살짝수정해서 쓰기
			function chk() {
				//form에 id를 줘놓으면 이런식으로 사용하기 편함
				if(frm.user_id.value.length < 5){
					alert('아이디는 5글자 이상 되어야 합니다')
					frm.user_id.focus() //포커스
					return false
				}else if(frm.user_pw.value.length < 5){
					alert('비밀번호는 5글자 이상 되어야 합니다')
					frm.user_pw.focus()
					return false
				}else if(frm.user_pw.value != frm.user_pwre.value){
					alert('비밀번호를 확인해 주세요')
					frm.user_pw.focus()
					return false
				}
				if(frm.nm.value.length > 0){
					//이름 한글인지 체크하는 정규식 (걍 검색해서 쓰기)
					const korean = /[^가-힣]/
					const result = korean.test(frm.nm.value)
					if(result){
						alert('이름은 한글만 입력해 주세요')
						frm.nm.focus()
						return false
					}		
				}
				if(frm.email.value.length > 0){
					//이 정규식도 그냥 검색해서 쓰기 .앞에 전부 \붙여주기
					const email = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;
						//위처럼 result에 넣어써도되고 이렇게 바로 써도됨
						if(!email.test(frm.email.value)){
							alert('이메일을 재대로 입력해주세요')
							console.log(!email.test(frm.email.value))
							frm.eamil.focus()
							return false
						}
				}
				return true
			}
			</script>
		</div>
	</div>
</body>
</html>