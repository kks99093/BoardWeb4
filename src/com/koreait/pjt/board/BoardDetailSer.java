package com.koreait.pjt.board;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.koreait.pjt.MyUtils;
import com.koreait.pjt.ViewResolver;
import com.koreait.pjt.db.BoardCmtDAO;
import com.koreait.pjt.db.BoardDAO;
import com.koreait.pjt.vo.BoardCmtVO;
import com.koreait.pjt.vo.BoardDomain;
import com.koreait.pjt.vo.UserVO;

@WebServlet("/board/detail")
public class BoardDetailSer extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UserVO loginUser = MyUtils.getLoginUser(request);
		if(loginUser == null) {
			response.sendRedirect("/login");
			return;
		}
		String strI_board = request.getParameter("i_board");
		String searchType = request.getParameter("searchType");
		String searchText = request.getParameter("searchText");
		

		//어플리케이션 내장객체 가져오는방법(이건 외우던가 필요할때 검색해서 쓰던가해야함, 이방법밖에 없음)
		ServletContext application = getServletContext(); //HttpServlet을 상속받았기 떄문에 사용가능
		Integer readI_user = (Integer)application.getAttribute("read_" + strI_board);
		//int랑 Integer둘다 같지만 다른점은 Integer는 객체이기 떄문에 null을 담을수있다
		//여기서 처음엔 null값이 들어가기 떄문에 Integer를 썻다
		// int형과 Integer의 비교는 값비교로 할수있다(주소값비교 x)
		int i_board = Integer.parseInt(strI_board);
		if(readI_user == null || readI_user != loginUser.getI_user()) {
			//조회수 올리기
			BoardDAO.hitsUpdate(i_board);
			
			application.setAttribute("read_" + strI_board, loginUser.getI_user());
		}
		
		BoardDomain param = new BoardDomain();
		param.setI_board(i_board);
		param.setI_user(loginUser.getI_user());
		param.setSearchType(searchType);
		param.setSearchText(searchText);
		
		BoardDomain data = BoardDAO.selBoard(param);
		


//		if(("a".equals(searchType) || "c".equals(searchType)) && !"".contentEquals(searchText)) {	
//				String title = data.getTitle();
//				title = title.replace(searchText
//						, "<span class=\"highlight\">"+searchText + "</span>");
//				data.setTitle(title);
//		}
//		if("b".equals(searchType) || "c".equals(searchType) && !"".contentEquals(searchText)) {
//			String ctnt = data.getCtnt();
//			ctnt = ctnt.replace(searchText
//					, "<span class=\"highlight\">"+searchText + "</span>");
//			data.setCtnt(ctnt);
//		}
//		
		request.setAttribute("data", data);
		request.setAttribute("search", param);
		List<BoardCmtVO> cmtData = BoardCmtDAO.selCmt(param);
		request.setAttribute("cmtData", cmtData); //댓글
		request.setAttribute("like_user", BoardDAO.selBoardLikeList(i_board));//좋아요 한사람
		
		
		
		
		ViewResolver.forwardLoginChk("/board/detail", request, response);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

}
