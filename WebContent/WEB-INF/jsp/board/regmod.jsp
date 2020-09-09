<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>${rdate == null ? '등록' : '수정' }</title>
<style>
	.container{width: 1000px; margin: 10px auto;}
	#title{width:700px;}
	#ctnt{width: 700px; height: 130px;}
</style>
</head>
<body>
<div class="container">
	<div><span color="red">${msg}</span></div>
	<div>
		<form action="regmod" id="frm" method="post" onsubmit="retrun chk()">
			<table>
				<tr>
					<th>제목</th>
					<td><input type="text" name="title" value="${rdate.title}" id="title"></div></td>
				</tr>
				<tr>
					<th>내용</th>
					<td><div><textarea name="ctnt" id="ctnt">${rdate.ctnt}</textarea></div></td>
				</tr>
				
			</table>	
 			<input type="hidden" name="i_board" value="${rdate.i_board}">
			<div><input type="submit" value="${rdate == null ? '등록' : '수정' }"></div>
		</form>
	</div>
</div>
	<script>
		function chk() {
			
		}
	</script>

</body>
</html>