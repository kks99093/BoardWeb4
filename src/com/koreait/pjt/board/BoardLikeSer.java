package com.koreait.pjt.board;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.koreait.pjt.MyUtils;
import com.koreait.pjt.db.BoardDAO;
import com.koreait.pjt.vo.BoardDomain;
import com.koreait.pjt.vo.UserVO;

@WebServlet("/like")
public class BoardLikeSer extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String str_i_board = request.getParameter("i_board");
		String str_t_like = request.getParameter("t_like");
		String searchText = request.getParameter("searchText");
		int page = MyUtils.getIntParameter(request, "page");
		int recordCnt = MyUtils.getIntParameter(request, "recordCnt");
		
		searchText = URLEncoder.encode(searchText, "UTF-8"); //한글 인코딩부분
		//한글검색을 유지하려면 적어줘야함
		UserVO vo = MyUtils.getLoginUser(request);
		
		int i_board = MyUtils.paraseStrToInt(str_i_board, 0);
		int t_like = MyUtils.paraseStrToInt(str_t_like, 5);
		BoardDomain param = new BoardDomain();
		//지금은 이렇게해도 되지만 큰프로그램할때는 likeVO를 만들어서 확실하게 구분해주는게 좋다
		param.setI_board(i_board);
		param.setI_user(vo.getI_user());
		
		if(t_like == 0) {
			BoardDAO.likeIns(param);
		}else if(t_like == 1){
			BoardDAO.likeDel(param);
		}else {
			System.out.println("ㅠㅠ");
		}
		response.sendRedirect("board/detail?i_board="+i_board+"&page="+page+"&recordCnt="+recordCnt+"&searchText="+searchText);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

}
