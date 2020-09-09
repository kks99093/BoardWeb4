<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="com.koreait.pjt.vo.BoardVO" %>

<%--jstl 사용(실무에서는 다 이거 쓴다고함) --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>리스트</title>
<link href="https://fonts.googleapis.com/icon?family=Material+Icons"
      rel="stylesheet">
<style>
	.container{width: 1000px; margin: 10px auto;}
	.s_th {width: 65px;}
	.l_th {width: 450px;}
	table {border-collapse: collapse; text-align: center; width: 900px}
	table th{border: 1px solid black; }
	table td{border: 1px solid black; }
	a {text-decoration: none; color:black;}
	a:hover { font-weight: bold: color: #212121}
	.uni_tr:hover{ background-color: #cce1ff; cursor: pointer;}
	.fontCenter{text-align: center;}
	.pageSelected { color:red; font-weight: bold; }
	.pagingFont {
		font-size: 1.3em;
	}
	.highlight{
		font-weight: bold;
		font-style: italic;
		color: fuchsia;
	}
	.pagingFont:not(:first-child) {
		margin-left: 13px;
	}
	.containerPimg{
	display: inline-block;
	width:30px;
	height:30px;
	border-radius: 50%;
	overflow: hidden;
	

	}
	.pImg{
		object-fit: cover;
		width:100%;
		height:100%;
	}
	#likeListContainer {
		display: none;			
		padding: 10px;			
		border: 1px solid #bdc3c7;
		position: absolute;
		left: 0px;
		top: 30px;
		width: 130px;
		height: 200px;
		overflow-y: auto;
		background-color: white !important;
		transition-duration : 500ms;
	}				
	#id_like { 
		position:relative;
		font-size: 1em;
	 }
	 .profile {
		background-color: white !important;
		display: inline-block;	
		width: 25px;
		height: 25px;
	    border-radius: 50%;
	    overflow: hidden;
	}
	.likeItemContainer {
		display: flex;
		width: 100%;
	}
	.likeItemContainer .nm {
		background-color: white !important;
		margin-left: 7px;
		font-size: 0.7em;
		display: flex;
		align-items: center;
	}	
	#page
</style>
</head>
<body>
	
	<div class="container">
		<h2>${loginUser.nm}님 환영합니다</h2> 
		<a href="/profile">프로필</a>
		<a href="/logout">로그아웃</a>
		<%-- set으로 만들어놓은건 el식으로 다 쓸수있다 --%>
		<div>
			<a href="/regmod">글쓰기</a>
			<%--'/주소/' 쓰는것과 '주소/' 쓰는것 차이 
				앞에 /를 붙이면 8089/뒤에 바로붙고, 안붙이면 제일뒤에있는것만 바뀐다(/주소/주소/ 쓸때만)
				ex) 붙이면 8089/board/reg , 안붙이면 /board/board/reg--%>
		</div>
		<h1>리스트</h1>
		<div>
			<form id="selFrm" action="/board/list">
			<%--param.page는 request.getParameter("page")랑 같다, 여기서 param이 키값이아니라 파라미터를 받아오는거 --%>
			<input type="hidden" name="page" value="${page}">
			<input type="hidden" name="searchText" value="${param.searchText}">
				<select name="recordCnt" onchange="changeRecord()">
				<%-- jstl을 c로 적는이유 : 위에 import할때 prefix="c" 해놨기때문(c로쓰는게 약속같은거) --%>
					<c:forEach begin="5" end="20" step="5" var="item">
						<c:choose>
						<%-- jstl의 if는 else가 없다 하지만 choose는 if(when)-else(otherwise)처럼 쓸수있다 --%>
							<c:when test="${param.recordCnt == item}">
								<option value="${item}" selected>${item}개</option>
							</c:when>
							<c:otherwise>
								<option value="${item}">${item}개</option>
							</c:otherwise>
						</c:choose>									
					</c:forEach>
				</select>
			</form>
		</div>
		<table>
			<tr>
				<th class="s_th">No</th>
				<th class="l_th">제목</th>
				<th>작성자</th>
				<th>작성일</th>
				<th class="s_th">조회수</th>
				<th class="s_th">좋아요수</th>
			</tr>
			<c:forEach items="${data}" var ="item"> 
			<%-- items="${?}" ?자리에 setAttribute의 키값을 넣어줌 --%>
			<%-- var="item"의 item은 변수명처럼 바꿀수있다 바꾸면 밑에도 바꿔줘야됨 --%>
			<tr class="uni_tr">		
				<td onclick = "moveDetail(${item.i_board})">
					<c:if test="${item.yn_like == 1}">
						<span style="color:red;" class="material-icons">favorite</span>
					</c:if>
					${item.i_board}
				</td>
				<td onclick = "moveDetail(${item.i_board})">${item.title} 
					<span style="font-size:small; font-weight: bold;">[${item.cmt_cnt}]
					</span>
				</td>
				<td onclick = "moveDetail(${item.i_board})">
					<div class="containerPimg">
						<c:choose>
							<c:when test="${item.profile_img != null}">
								<img class="pImg" src="/img/user/${item.i_user}/${item.profile_img}">
							</c:when>
							<c:otherwise>
								<img class="pImg" src="/img/defalut_profile.jpg">
							</c:otherwise>
						</c:choose>
					</div>
					${item.nm}
				</td>
				<td onclick = "moveDetail(${item.i_board})">${item.r_dt}</td>
				<td onclick = "moveDetail(${item.i_board})">${item.hits}</td>
				<td><span onclick="getLikeList(${item.i_board}, ${item.like_cnt },this)">${item.like_cnt}</span></td>
			</tr>
			</c:forEach>
		</table>
		<div class="fontCenter">
			<c:forEach var="item" begin="1" end="${pagingCnt}">
				<c:choose>
					<c:when test="${page == item}">
					<%--param.page는 request.getParameter("page")랑 같다, 여기서 param이 키값이아니라 파라미터를 받아오는거 --%>
						<span class="pagingFont pageSelected">${item}</span>
					</c:when>
					<c:otherwise>
						<span class="pagingFont"><a href="/board/list?page=${item}&recordCnt=${param.recordCnt}&searchText=${param.searchText}&searchType=${searchType}">${item}</a></span>
					</c:otherwise>
				</c:choose>
			</c:forEach>
		</div>
		<div>
			<form action="/board/list">
				<select name="searchType">
					<option value="a" ${searchType == 'a' ? 'selected' : ''}>제목</option>
					<option value="b" ${searchType == 'b' ? 'selected' : ''}>내용</option>
					<option value="c" ${searchType == 'c' ? 'selected' : ''}>제목+내용</option>
				</select>
				<input type="hidden" name="page" value="${param.page}">
				<input type="hidden" name="recordCnt" value="${param.recordCnt}">
				<input type="search" name="searchText">
				<input type="submit" value="검색">
			</form>
		</div>
	</div>
	<div id="likeListContainer"></div>
	
	<script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
	<script>
		function changeRecord(){
			
			selFrm.submit();
		}
	
		function moveDetail(i_board){
			location.href="/board/detail?page=${page}&recordCnt=${recordCnt}&searchText=${param.searchText}&searchType=${searchType}&i_board="+i_board
		}
		
		
		
		
		
		
		//AZAX 통신 (Json 사용, axios 라이브러리 사용)
		let beforeI_board = 0
		function getLikeList(i_board, cnt, span) {
			console.log("i_board : " + i_board)
			if(cnt == 0) { return }
			if(beforeI_board == i_board) {
				likeListContainer.style.display = 'none'
				return
			} else if(beforeI_board != i_board) {
				beforeI_board = i_board
				likeListContainer.style.display = 'unset'
				//여기서 숨겨져있는 display가 들어나게함
			}			
			
			//여기서부터 위치정하기
			const locationX = window.scrollX + span.getBoundingClientRect().left //span공간의 left값(위치)
							//span.get.....만 적으면 화면에 보이는곳기준으로 위치값이 적혀서 앞에 window.scrollX를 줘서 항상 같은위치에 뜨게함
							//그냥 detail처럼 공간마다 <div></div>를 다 박아놓고 눌럿을때 나타나게하는방식이 더 완벽함
			const locationY = window.scrollY + span.getBoundingClientRect().top + 30 //span공간의 top+30의값(위치)
			
			likeListContainer.style.left = `\${locationX}px` //위에서 저장해놓은 위치값을 likeListContainer에 넣음
			likeListContainer.style.top = `\${locationY}px`
			
			likeListContainer.innerHTML = "" //그전의 값이 들어가있을수도있으니 내용을 비움
			
			//이제 azax통신을 함, 이제 생각해보니 이게 좀더 위에있는게 좋았을거임
			//파라미터 보내는법 1. 쿼리스트링(?키값=벨류값)↓↓↓↓
			//axios.get('/board/like?i_board='+i_board).then(function(res){})
			axios.get('/board/like', { //여기가 요청하는부분?
			//파라미터 보내는법2.↓↓↓↓
				params: {
					i_board//key, 변수명이 같을때는 이렇게 사용, 원래는 i_board: i_board 이렇게 해야 함
							//키값과 벨류값이 같다면 그냥 i_board만 적어놔도됨
				}	
			}).then(function(res) {		//요청후 응답을받고 그후에 하고싶은 작업을 여기에 적어줌 내가 받은정보는 res에 담겨있음 res.data를 하면 내가 원하는값을 받을수있음	
				if(res.data.length > 0) {					
					for(let i=0; i<res.data.length; i++) {
						const result = makeLikeUser(res.data[i])
						likeListContainer.innerHTML += result
					}
				}
			})
		}
		function makeLikeUser(one){
			const img = one.profile_img == null ?
					'<img class="pImg" src="/img/defalut_profile.jpg">'
					:
					`<img class="pImg" src="/img/user/\${one.i_user}/\${one.profile_img}" >`	
					
					
			const ele = `<div class="likeItemContainer">
				<div class="profileContainer">
					<div class="profile">
						\${img}
					</div>
				</div>
				<div class="nm">\${one.nm}</div>
			</div>`
			
			return ele
		}
		
	</script>
	

</body>
</html>