package com.koreait.pjt.board;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.koreait.pjt.MyUtils;
import com.koreait.pjt.db.BoardCmtDAO;
import com.koreait.pjt.vo.BoardCmtVO;
import com.koreait.pjt.vo.UserVO;

@WebServlet("/board/cmt")
public class BoardCmtSer extends HttpServlet {
	private static final long serialVersionUID = 1L;

	//댓글삭제
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String strI_cmt = request.getParameter("i_cmt");
		String strI_board = request.getParameter("i_board");		
		int i_cmt = MyUtils.paraseStrToInt(strI_cmt, 0);
		String searchText = request.getParameter("searchText");
		int page = MyUtils.getIntParameter(request, "page");
		int recordCnt = MyUtils.getIntParameter(request, "recordCnt");
		
		UserVO loginUser = MyUtils.getLoginUser(request);
		if(loginUser == null) {
			response.sendRedirect("/login");
			return;
		}
		
		BoardCmtVO param = new BoardCmtVO();
		param.setI_cmt(i_cmt);
		param.setI_user(loginUser.getI_user());
		BoardCmtDAO.delCmt(param);
		response.sendRedirect("/board/detail?i_board="+strI_board+"&page="+page+"&recordCnt="+recordCnt+"&searchText="+searchText);
		
	}

	//댓글 등록과 수정
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String strI_board = request.getParameter("i_board");
		String strI_cmt = request.getParameter("i_cmt");
		String cmt = request.getParameter("cmt");
		int i_board = MyUtils.paraseStrToInt(strI_board, 0);
		String searchText = request.getParameter("searchText");
		int page = MyUtils.getIntParameter(request, "page");
		int recordCnt = MyUtils.getIntParameter(request, "recordCnt");
		
		UserVO vo = MyUtils.getLoginUser(request);
		BoardCmtVO param = new BoardCmtVO();
		param.setI_user(vo.getI_user());
		param.setCmt(cmt);
		
		switch(strI_cmt) {
		case "0": //등록
			param.setI_board(i_board);
			BoardCmtDAO.insCmt(param);
			break;
		default: //수정
			int i_cmt = MyUtils.paraseStrToInt(strI_cmt);
			param.setI_cmt(i_cmt);
			BoardCmtDAO.updCmt(param);
			break;
		}
		response.sendRedirect("/board/detail?i_board="+i_board+"&page="+page+"&recordCnt="+recordCnt+"&searchText="+searchText);
	}

}
