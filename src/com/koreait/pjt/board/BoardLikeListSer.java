package com.koreait.pjt.board;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.koreait.pjt.MyUtils;
import com.koreait.pjt.db.BoardDAO;
import com.koreait.pjt.vo.BoardDomain;

@WebServlet("/board/like")
public class BoardLikeListSer extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	//리스트 가져오기
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	//ㅡㅡㅡㅡㅡㅡㅡㅡㅡ제이슨ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
		int i_board = MyUtils.getIntParameter(request, "i_board");
		System.out.println("i_board : " + i_board);
		
		List<BoardDomain> likeList = BoardDAO.selBoardLikeList(i_board);
		
		Gson gson = new Gson(); //Gson 객체화
		String json = gson.toJson(likeList);
		//likeList를 받아와서 gson을 이용해서 json배열형태로 만든다
		System.out.println("json : " + json);
		
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json"); //제이슨 설정,내가 지금 너한테 보내는 자료는 제이슨이다 라고 알려주는것
		PrintWriter out =response.getWriter();
		out.print(json); //여기가 요청에대해 반환하는 부분
		// JSON 할때는 \"     \"를 절대로 뺴면 안됨
	}

	//좋아요 처리
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

}
