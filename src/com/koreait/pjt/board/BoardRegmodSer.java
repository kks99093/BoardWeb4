package com.koreait.pjt.board;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.koreait.pjt.Const;
import com.koreait.pjt.ViewResolver;
import com.koreait.pjt.db.BoardDAO;
import com.koreait.pjt.vo.BoardDomain;
import com.koreait.pjt.vo.BoardVO;
import com.koreait.pjt.vo.UserVO;
@WebServlet("/regmod")
public class BoardRegmodSer extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	//화면띄우기용 (등록창/수정창)
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String strI_board = request.getParameter("i_board");
		if(strI_board != null) {
		int i_board = Integer.parseInt(strI_board);
		BoardDomain param = new BoardDomain();
		param.setI_board(i_board);
		BoardDomain vo = BoardDAO.selBoard(param);
		request.setAttribute("rdate", vo);
		}
		
		ViewResolver.forwardLoginChk("board/regmod", request, response);
	}
	
	//처리용(DB에등록/수정)실시
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String title = request.getParameter("title");
		String ctnt = request.getParameter("ctnt");
		String strI_board = request.getParameter("i_board");
//		System.out.println("asd" + strI_board);
		HttpSession hs = request.getSession();
		UserVO loginUser = (UserVO) hs.getAttribute(Const.LOGIN_USER);
		
		String filter1 = scriptFilter(ctnt);
		String filter2 = swearWordFilter(filter1);
		
		BoardVO param = new BoardVO();
		param.setTitle(title);
		param.setCtnt(filter2);
		param.setI_user(loginUser.getI_user());
		int result = 0;
		
		if(strI_board.isEmpty()) { //"".equals(strI_board))   등록
			result = BoardDAO.insBoard(param);
			response.sendRedirect("/board/list");
		//	return; //여기에 리턴을 주고 else를 뺴고 쓸수도있음
		}else { // 수정
			int i_board = Integer.parseInt(strI_board);
			param.setI_board(i_board);
			result = BoardDAO.updateBoard(param);
			response.sendRedirect("/board/detail?i_board="+strI_board);
		}
		
	}
	// 스크립트 필터
	private String scriptFilter(final String ctnt) {
		String[] filters = {"<",">"};
		//모든 태그를 막으려면 <랑 >를 &lt; &gt;로 바꿔주면 되나?
		String[] fiterReplaces = {"&lt;", "&gt;"};
		
		String result = ctnt;
		for(int i=0; i<filters.length; i++) {
			result = result.replace(filters[i], fiterReplaces[i]);
		//replace : 바꾸다
		//ctnt의 문자열중 filter[i]부분을 fiterReplaces[i]로 바꾼다
		}
		return result;
	}
	
	//욕 필터
	private String swearWordFilter(final String ctnt) {
		String[] filters = {"개새끼","미친년","ㄱㅐㅅㅐㄲㅣ"};
		String result = ctnt;		
		for(int i=0; i<filters.length; i++) {
			result = result.replace(filters[i], "***");
		//replace : 바꾸다
		//ctnt의 문자열중 filter[i]부분을 fiterReplaces[i]로 바꾼다
		}
		return result;
	}

}
