<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link href="https://fonts.googleapis.com/icon?family=Material+Icons"
      rel="stylesheet">
	<style>
		a { float: left; margin-right: 10px;}
		.cmtInput { width: 300px; height: 30px; margin: 10px 0px 10px 0px}
		table {border-collapse: collapse; width: 800px;}
		table th{border : 1px solid black; width: 70px;}
		table td{border : 1px solid black; width: 30px;}
		#elTitle {width: 120px;}
		#textbox { width: 540px;}
		.cmt { width: 200px;}
		#btn { width: 50px; border: 2px solid black;}
		.uni_td{width: 150px;  height: 200px; vertical-align: top;}
		.point_cursor:hover{cursor: pointer;}
		.containerPImg {
			display: inline-block;	
			width: 30px;
			height: 30px;
		    border-radius: 50%;
		    overflow: hidden;
		}
		.pImg {
			object-fit: cover;
			height: 25px;
			width: 25px;
		}
		.highlight{
			font-weight: bold;
			font-style: italic;
			color: fuchsia;
		
		}
		#likeListContainer {			
			padding: 10px;			
			opacity: 0;
			border: 1px solid #bdc3c7;
			position: absolute;
			left: 0px;
			top: 30px;
			width: 130px;
			height: 0;
			overflow-y: auto;
			background-color: white !important;
			transition-duration : 500ms;
		}				
		#id_like { 
			position:relative;
			font-size: 1em;
		 }		
		
		#id_like:hover #likeListContainer {
			height: 130px;						
			opacity: 1;
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
		}
		.likeItemContainer .nm {
			background-color: white !important;
			margin-left: 7px;
			font-size: 0.7em;
			display: flex;
			align-items: center;
		}
	</style>

</head>
<body>
	<div>
		<a href="/board/list?page=${param.page}&recordCnt=${param.recordCnt}&searchText=${search.searchText}&searchType=${search.searchType}">리스트</a></div>
		<c:if test = "${loginUser.i_user == data.i_user }">
			<%--?뒤는 쿼리스트링 : 서버에게 값(아규먼트)을 보내는 용도 --%>
			<a href="/regmod?i_board=${data.i_board}">수정</a>
			<form id="frm" action="/board/del" method="post">
				<input type="hidden" name="i_board" value="${data.i_board}">
				<a href="#" onclick="boardDel()">삭제</a>
			</form>
		</c:if>
	</div>
	<br>
	<div>
		<table>
				<tr>
					<td>No.${data.i_board}</td>
					<td colspan="2" id="elTitle">${data.title}</td>
				</tr>
				<tr>
					<th>작성자</th>
					<td colspan="2">
						<c:choose>
							<c:when test="${data.profile_img != null}">
								<img class="pImg" src="/img/user/${data.i_user}/${data.profile_img}">
							</c:when>
							<c:otherwise>
								<img class="pImg" src="/img/defalut_profile.jpg">
							</c:otherwise>
						</c:choose>
						${data.nm}
					</td>
					
				</tr>
				<tr>
					<th>작성일시</th>
					<td colspan="2">${data.r_dt}</td>
				</tr>
				<tr>
					<th>조회수</th>
					<td>${data.hits}</td>
					<td>
					<c:if test= "${data.t_like == 0}">
						<span onclick="change(${data.t_like})" class="material-icons point_cursor">favorite_border</span>
					</c:if>
					<c:if test= "${data.t_like == 1}">
						<span style="color:red" onclick="change(${data.t_like})" class="material-icons point_cursor">favorite</span>
					</c:if>
					
				</tr>
				<c:if test="${data.like_cnt > 0 }">
					<tr>
						<td colspan="3">
							<span id="id_like" class="point_cursor">좋아요 ${data.like_cnt }개
								<div id="likeListContainer">
									<c:forEach items="${like_user}" var="item">
										<div class="likeitemcontainer">
											<div class="profilecontainer">
												<div class="profile">
													<c:choose>											
														<c:when test="${item.profile_img != null}">
															<img class="pImg" src="/img/user/${item.i_user}/${item.profile_img}">
														</c:when>
														<c:otherwise>
															<img class="pImg" src="/img/defalut_profile.jpg">
														</c:otherwise>
													</c:choose>
												</div>
											</div>											
										</div>		
										<div class="nm">${item.nm}</div>																				
									</c:forEach>
								</div>
							</span>
						</td>												
					</tr>
				</c:if>
				<tr>
					<td class="uni_td"colspan="3" id="elCtnt">${data.ctnt}</td>
				</tr>
			</table>
		</div>
		<div>
			<form id="cmtFrm" action="/board/cmt" method= "post">
				<input type="hidden" name ="i_cmt" value="0">
				<input type="hidden" name ="i_board" value="${data.i_board}">
				<input type="hidden" name="page" value="${param.page}">
				<input type="hidden" name="searchText" value="${param.searchText}">
				<input type="hidden" name="recordCnt" value="${param.recordCnt}">
				<div>
					<input type="text" name="cmt" id="textbox" class="cmtInput" placeholder="댓글내용">
					<input type="submit" value="전송" name="cmt_sub" id="btn" class="cmtInput">
					<input type="button" value="취소" onclick="clkCmtCancel()">
				</div>
			</form>
		</div>
		<div>
			<table>
				<tr>
					<th>작성자</th>
					<th class="cmt">내용</th>
					<th>등록시간</th>
					<th>수정시간</th>
				</tr>
				<c:forEach items="${cmtData}" var="cmts">
					<tr>	
						<td>
							<c:choose>
								<c:when test="${cmts.profile_img != null}">
									<img class="pImg" src="/img/user/${cmts.i_user}/${cmts.profile_img}">
								</c:when>
								<c:otherwise>
									<img class="pImg" src="/img/defalut_profile.jpg">
								</c:otherwise>
							</c:choose>
							${cmts.nm}
						</td>
						<td>${cmts.cmt}</td>
						<td>${cmts.r_dt}</td>
						<td>${cmts.m_dt}</td>
						<c:if test="${loginUser.i_user == cmts.i_user }">
						<td>
							<button onclick="clkCmtDel(${cmts.i_cmt})">삭제</button>>
							<button onclick="cmtUp('${cmts.cmt}', '${cmts.i_cmt}')">수정</button>
						</td>
						</c:if>
					</tr>
				</c:forEach>
			</table>		
		</div>
	<script>
	
		function boardDel(){
			if(confirm('삭제하시겠습니까')){
				frm.submit()
			}
		}

		function doUpdate(i_board){
			location.href = '/regmod?i_board='+i_board
		}
		
		function change(t_like){
			location.href = '/like?i_board=${data.i_board}&searchText=${param.searchText}&page=${param.page}&recordCnt=${param.recordCnt}&t_like='+t_like			
		}
		
		//수정
		function cmtUp(cmt, i_cmt){
			cmtFrm.cmt.value = cmt
			cmtFrm.cmt_sub.value = '수정'
			cmtFrm.i_cmt.value = i_cmt
		}
		
		// 수정 취소
		function clkCmtCancel(){
			cmtFrm.i_cmt.value = 0
			cmtFrm.cmt.value = ''
			cmtFrm.cmt_sub.value = '전송'
		}
		
		function clkCmtDel(i_cmt){
			if(confirm('삭제 하시겠습니까?'))
				location.href = '/board/cmt?i_board=${data.i_board}&searchText=${param.searchText}&page=${param.page}&recordCnt=${param.recordCnt}&i_cmt='+i_cmt
		}
		
		
	//스크립트로 하이라이트 주는방법

		function doHighlight(){
			var searchText = '${param.searchText}'
			var searchType = '${param.searchType}'
			
			
			
			switch(searchType){
			case 'a':
				var txt = elTitle.innerText
				txt = txt.replace(new RegExp('${searchText}'), '<span class="highlight">' + searchText + '</span>')
				elTitle.innerHTML = txt
				break
			case 'b':
				var txt = elCtnt.innerText
				txt = txt.replace(new RegExp('${searchText}'), '<span class="highlight">' + searchText + '</span>')
				elCtnt.innerHTML = txt
				break
			case 'c':
				var txt = elTitle.innerText
				txt = txt.replace(new RegExp('${searchText}'), '<span class="highlight">' + searchText + '</span>')
				elTitle.innerHTML = txt
				
				var txt = elCtnt.innerText
				txt = txt.replace(new RegExp('${searchText}'), '<span class="highlight">' + searchText + '</span>')
				elCtnt.innerHTML = txt
				break
			}
			
		}
		doHighlight()

	</script>
</body>
</html>