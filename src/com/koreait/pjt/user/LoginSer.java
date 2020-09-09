package com.koreait.pjt.user;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.koreait.pjt.Const;
import com.koreait.pjt.MyUtils;
import com.koreait.pjt.ViewResolver;
import com.koreait.pjt.db.UserDAO;
import com.koreait.pjt.vo.UserLoginHistoryVO;
import com.koreait.pjt.vo.UserVO;

@WebServlet("/login")
public class LoginSer extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		if(!MyUtils.isLogout(request)) {
			response.sendRedirect("/board/list");
			return;
		}
		
		ViewResolver.forward("/user/login", request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String user_id = request.getParameter("user_id");
		String user_pw = request.getParameter("user_pw");
		String encrypt_pw = MyUtils.encryptString(user_pw);
		
		UserVO param = new UserVO();
		param.setUser_id(user_id);
		param.setUser_pw(encrypt_pw);
		
		int result = UserDAO.login(param);
		//이때 param에는 user_id, i_user, nm값이 들어있다
		
		if(result != 1) {// 에러처리
			String msg = null;
			switch(result) {
			case 2: msg = "비밀번호를 확인해 주세요.";break;
			case 3: msg = "아이디를 확인해 주세요.";break;
			default:msg = "에러가 발생했습니다.";
			}
			request.setAttribute("user_id",user_id);
			request.setAttribute("msg", msg);
			doGet(request,response);
			return;
		}
		
		String agent = request.getHeader("User-Agent") ;
		// User-Agent라는 키값으로 값을 받아오며 저안에 cpu,브라우저,os 등 사용자정보가 들어가있다
		System.out.println("agent : " + agent);
		String os = getOs(agent); // 위의 User-Agent를 활용하는 메소드를 만듬
		String browser = getBrowse(agent); // 위의 User-Agent를 활용하는 메소드를 만듬
		String ip_addr = request.getRemoteAddr(); //ip주소 가져오기
		
		UserLoginHistoryVO ulhVO = new UserLoginHistoryVO();
		ulhVO.setI_user(param.getI_user());
		ulhVO.setOs(os);
		ulhVO.setBrowser(browser);
		ulhVO.setIp_addr(ip_addr);
		//내가 들어가면 ip가 0:0:0:0:0:1이 찍힘 다른사람은 재대로 찍힘
		System.out.println("os: " + os + ", browser : " + browser + ", ip : " + ip_addr);
		UserDAO.insUserLoginHistory(ulhVO);
		
		
		
		HttpSession hs = request.getSession();
		hs.setAttribute(Const.LOGIN_USER, param);
		//Const.LOGIN_USER으로 하는이유는 내가 혹시나 소문자 대문자 잘못쓰는 실수를 방지하기위해서임
		//http의 Session에다가 param을 넣어줌
		//Session에다가 로그인정보를 담아놓는 이유 : Session은 브라우저가 종료되는순간 죽음
		response.sendRedirect("/board/list");
	}
	
	private String getBrowse(String agent) {
		if(agent.toLowerCase().contains("msie")) { //agent문자열안에  msie라는 문자열이 있는지 확인
			return "ie";
		} else if(agent.toLowerCase().contains("chrome")) {			
			return "chrome";
		} else if(agent.toLowerCase().contains("safari") && agent.contains("version")) {
			//사파리의 Webkit이란걸 사용하고있어서 safari는 무조건 뜸 그렇기때문에 version도 같이 있나 확인해서 체크함
			return "safari";
		}
			
		return "";
	}
	
	private String getOs(String agent) {
		if(agent.toLowerCase().contains("mac")) {
			return "mac";
		} else if(agent.toLowerCase().contains("windows")) {
			return "win";
		} else if(agent.toLowerCase().contains("x11")) {
			return "unix";
		} else if(agent.toLowerCase().contains("android")) {
			return "android";
		} else if(agent.toLowerCase().contains("iphone")) {
			return "iOS";
		} else if(agent.toLowerCase().contains("linux")) {
			return "linux";
		}
		return "";
	}

}
