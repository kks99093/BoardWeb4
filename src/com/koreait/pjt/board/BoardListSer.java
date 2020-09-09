package com.koreait.pjt.board;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.koreait.pjt.MyUtils;
import com.koreait.pjt.ViewResolver;
import com.koreait.pjt.db.BoardDAO;
import com.koreait.pjt.vo.BoardDomain;
import com.koreait.pjt.vo.UserVO;

@WebServlet("/board/list")
public class BoardListSer extends HttpServlet {
	private static final long serialVersionUID = 1L;
  
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		//httpSession에 정보가 있는지 확인후 정보가 없다면 login으로 튕기게함
//		HttpSession hs = request.getSession();
//		if(hs.getAttribute(Const.LOGIN_USER) == null) {
//			response.sendRedirect("/login");
//			return;
//			// - Session에다가 로그인정보를 담는 이유 -
//			// Session은 브라우저를 끄면 죽음 - 로그인후 브라우저를 끄지않는다면 list로 감
//			// 브라우저를 껏다가 list로가면 login으로 가짐
//		}		
		if(MyUtils.isLogout(request)) {
		response.sendRedirect("/login");
		return;
		}
		
		String searchType = request.getParameter("searchType");
		searchType = searchType == null ? "a" : searchType;
		
		String searchText = request.getParameter("searchText");
		searchText = searchText == null ? "" : searchText;
		
		int page = MyUtils.getIntParameter(request, "page");
		page = page == 0 ? 1 : page;

		int recordCnt = MyUtils.getIntParameter(request, "recordCnt");
		recordCnt = recordCnt == 0 ? 5 : recordCnt;
		
		UserVO loginUser = MyUtils.getLoginUser(request);

		BoardDomain param = new BoardDomain();
		param.setRecord_cnt(recordCnt); //한 페이지당 10개씩 뿌리겠다
		param.setSearchText("%" + searchText + "%");	
		param.setI_user(loginUser.getI_user());
		param.setSearchType(searchType);
		System.out.println(searchType);
		int pagingCnt = BoardDAO.selPagingCnt(param);

		if(page > pagingCnt) {
			page = pagingCnt;
		}
		request.setAttribute("page", page);

		int eIdx = page * recordCnt;
		int sIdx = eIdx - recordCnt;
		param.seteIdx(eIdx);
		param.setsIdx(sIdx);
		
		request.setAttribute("searchType", searchType);
		request.setAttribute("recordCnt", recordCnt);	
		request.setAttribute("pagingCnt", pagingCnt);
		
		List<BoardDomain> list = BoardDAO.selBoardList(param);
		//하이라이트 처리
		if(("a".equals(searchType) || "c".equals(searchType)) && !"".contentEquals(searchText)) {
			for(BoardDomain item : list) {			
				String title = item.getTitle();
				title = title.replace(searchText
						, "<span class=\"highlight\">"+searchText + "</span>");
				item.setTitle(title);
			}
			
		}
		
		request.setAttribute("data", list);
		
		ViewResolver.forwardLoginChk("board/list", request, response);
		
	}


}
