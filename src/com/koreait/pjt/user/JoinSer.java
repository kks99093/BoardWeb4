package com.koreait.pjt.user;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.koreait.pjt.MyUtils;
import com.koreait.pjt.ViewResolver;
import com.koreait.pjt.db.UserDAO;
import com.koreait.pjt.vo.UserVO;

@WebServlet("/join")
public class JoinSer extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	//주로 화면띄울떄 (jsp파일 띄울때)
		// String jsp = "/WEB-INF/jsp/join.jsp"; 
		// request.getRequestDispatcher(jsp).forward(request, response);
		// 사실 여기서 바뀌는건 join밖에없음 그렇기떄문에 메소드를 만들어서 주소값을 바꿔주는 메소드를 만들어서 주소를 적을때 jsp명만 적을거임
		// pjt패키지에 ViewResolver클래스에 forward라는 매소드로 만들어놓음
		ViewResolver.forward("user/join", request, response);
		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	//주로 업무 처리할때 씀
		String user_id = request.getParameter("user_id");
		String user_pw = request.getParameter("user_pw");
		String encrypt_pw = MyUtils.encryptString(user_pw);
		String user_nm = request.getParameter("nm");
		String user_eamil = request.getParameter("email");
//		System.out.println(user_id);
//		System.out.println(user_pw);
//		System.out.println(user_nm);
//		System.out.println(user_eamil);
		
		UserVO param = new UserVO();
		param.setUser_id(user_id);
		param.setUser_pw(encrypt_pw);
		param.setNm(user_nm);
		param.setEmail(user_eamil);		
		int result = UserDAO.insUser(param);
		if(result != 1) {
			request.setAttribute("date", param);
			request.setAttribute("msg", "에러가 발생하였습니다");
			doGet(request, response);
			return;
		}
		response.sendRedirect("/login");
		
		
		
	}

}
