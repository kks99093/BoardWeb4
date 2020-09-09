package com.koreait.pjt;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ViewResolver {
	//로그인체크가 필요없다면 이 메소드를 사용
	public static void forward(String fileNm, HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		String jsp = String.format("/WEB-INF/jsp/%s.jsp",fileNm);
		request.getRequestDispatcher(jsp).forward(request, response);
	}
	
	//로그인체크가 필요하다면 이 메소드를 사용
	public static void forwardLoginChk(String fileNm, HttpServletRequest request
			, HttpServletResponse response) throws IOException, ServletException {
			if(MyUtils.isLogout(request)) {
			response.sendRedirect("/login");
			return;
		}
		forward(fileNm, request, response);
	}

}
