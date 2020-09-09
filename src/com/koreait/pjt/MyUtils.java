package com.koreait.pjt;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.koreait.pjt.vo.UserVO;

public class MyUtils {
	
	
	
	public static int getIntParameter(HttpServletRequest request, String keyNm) {
		return paraseStrToInt(request.getParameter(keyNm));
	}
	
	
	
	public static String encryptString(String str){

	       String sha = "";

	       try{
	          MessageDigest sh = MessageDigest.getInstance("SHA-256");
	          sh.update(str.getBytes());
	          //내가보낸 문자열을 byte로 바꿔서 sh에 추가한다
	          byte byteData[] = sh.digest();
	          StringBuffer sb = new StringBuffer();
	          //StringBuffer : 문자열 합치기할때 좋음 (문자열계의 List), 요즘은 "" + ""하더라도 자동으로 StringBuffer를씀
	          //단, +를 for문에 쓰면 자동으로 StringBuffer이 안써지기때문에 객체생성을 해줘야 한다
	          for(int i = 0 ; i < byteData.length ; i++){
	              sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
	          }

	          sha = sb.toString();

	      }catch(NoSuchAlgorithmException e){
	          //e.printStackTrace();
	          System.out.println("Encrypt Error - NoSuchAlgorithmException");
	          sha = null;
	      }

	      return sha;
	    } 

	//return true : 로그인 안됨, flase : 로그인 된 상태
	public static boolean isLogout(HttpServletRequest request) throws IOException {
		if(getLoginUser(request) == null) {
			return true;
		}
		return false;
	}
	
	public static UserVO getLoginUser(HttpServletRequest request) {
		HttpSession hs= request.getSession();
		return (UserVO)hs.getAttribute(Const.LOGIN_USER);
	}
	
	public static int paraseStrToInt(String str) {
		return paraseStrToInt(str, 0);
		//값한개만 넣어도 0리턴하게 (대부분 오버로딩은 이런식으로 되어있다)
}


	public static int paraseStrToInt(String str, int n1) {
	try {
		return Integer.parseInt(str);
	}catch(Exception e){
		return n1;
	}
	}

}
