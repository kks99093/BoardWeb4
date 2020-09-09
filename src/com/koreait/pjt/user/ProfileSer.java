package com.koreait.pjt.user;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.koreait.pjt.MyUtils;
import com.koreait.pjt.ViewResolver;
import com.koreait.pjt.db.UserDAO;
import com.koreait.pjt.vo.UserVO;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

@WebServlet("/profile")
public class ProfileSer extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	//프로필 화면 (나의 프로필 이미지, 이미지 변경 가능한 화면)
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UserVO loginUSer = MyUtils.getLoginUser(request);
		
		request.setAttribute("data", UserDAO.selUser(loginUSer.getI_user()));
		
		ViewResolver.forward("user/profile", request, response);
	}
	

	
	
//이미지 변경 처리 파일업로드는 무조건 post방식
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		UserVO loginUser = MyUtils.getLoginUser(request);
// 디렉토리 위치를 어플리케이션을 활용해서 savePath에 저장 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
		String savePath = getServletContext().getRealPath("img")+"/user/" + loginUser.getI_user();
		//(어플리케이션을 이용해야함)톰캣에있는 절대주소값을 리턴해준다, img의 주소값을 준다
		System.out.println("savePath : " + savePath);
//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
		
//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ디렉토리(폴더) 만드는 부분ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
// 절대경로값/img/user/i_user 지금은 i_user폴더가없어서 에러터짐 그래서 폴더를 만들어줘야함↓↓↓↓↓
		File directory = new File(savePath);
		if(!directory.exists()) {
		//만약 폴더(디렉토리)가 없다면 폴더(디렉토리)를 만든다
			directory.mkdirs();
			//mkdirr과 mkdirs의 차이점
			//mkdirs는 img/user/5/300 이렇게 5/300둘다 없을때 둘다 만들어주는데 mkdirr은 한개만 만들어줌
		}
//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
		
//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ크기지정ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
		int maxFileSize = 10_485_760; 
		//1024 * 1024 * 10 (10mb), 최대사이즈 크기, 언더바(_)는 그냥 보기 편하라고 넣어놓음
//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
		
		String fileNm = "";
		String saveFileNm = "";
		try {
			//MultipartRequest객체를 생성할때 지정해놓은 폴더에 파일이 업로드된다
			MultipartRequest mr = new MultipartRequest(request, savePath
					, maxFileSize, "UTF-8", new DefaultFileRenamePolicy());
			//reqeust,경로(위의어플리케이션에서 받아옴),파일최대크기,UTF-8인코딩
			
			Enumeration files = mr.getFileNames();
			//업로드된 파일들의 이름을 담는다? 그리고 밑에서 하나하나 불러오는건가??
			if(files.hasMoreElements()) {
			//Elements가 더있는지 물어보는 부분
			String key = (String)files.nextElement(); //키값이 찍힘
			//파일들중에서 이번엘리먼트의 키값을 가져온다
			fileNm = mr.getFilesystemName(key);
			//키값을 통해 파일명을 바꾼후 fileNm에 저장
			//바뀐파일명을 가져옴, 가져오는 파일의 파일명이 이미있을경우 바꿔줌 없다면 안바꿔줌
			// 완전 편한방법 ★, 여기 null이 넘어왔다면 파일이 없다는것
			
			
//ㅡㅡㅡㅡㅡㅡㅡㅡㅡ확장자명 찾기 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
			String exten = fileNm.substring(fileNm.lastIndexOf(".")+1);
			//lastIndexOf(".") : .의 마지막 인덱스값
			//문자열 짜르기 - substring(n): n에서부터 끝까지 / substring(n1,n2) : n1에서 n2까지 
//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
			
			
//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ UUID로 파일명 변경 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
			//MultipartRequest를 쓴상태에서는 UUID 파일명변경이 불가능하기때문에
			//일단 파일을 만든후 그 파일을 다시 불러와서 이름을 다시 바꾼다↓↓↓↓↓↓
			
			saveFileNm = UUID.randomUUID() + "." + exten;
			//파일명을 DB에 저장할거라서 따로 변수로 빼줬음
			
			File oldFile = new File(savePath + "/" + fileNm);
			//						경로 / 파일이름
			File newFile = new File(savePath + "/" + saveFileNm);
			System.out.println("exten : " + exten );
			// 공파일 만드는부분(비어있는 파일), 실제로 파일만든다기보다는 자바 메모리상에 파일 객체를 만듬
			oldFile.renameTo(newFile);
			//oldFile의 이름을 newFile의 이름으로 바꿈
			//결국  저장된 파일의 이름은 UUID.randomUUID().jpg가 됨
			
			
			//DB에 저장할때는 파일명만 저장한다 [UUID.randomUUID() + "." + exten]<--이부분
			//경로부분,pk값은 다 여기서 불러올수있기때문에 파일명만 DB에 저장한다
			
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(saveFileNm != null) { //DB에 프로필 파일명 저장
			UserVO param = new UserVO();
			param.setProfile_img(saveFileNm);
			param.setI_user(loginUser.getI_user());
			//user_id가 아니라 i_user로 하는이유는 문자열보다는 정수값이 훨씬 빠르기때문이다
			
			UserDAO.updUser(param);
		}
		
		
		
	}


}
