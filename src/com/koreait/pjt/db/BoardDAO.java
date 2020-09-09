package com.koreait.pjt.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.koreait.pjt.vo.BoardDomain;
import com.koreait.pjt.vo.BoardVO;

public class BoardDAO {
	//ㅡㅡㅡㅡㅡㅡㅡㅡ리스트
	public static List<BoardDomain> selBoardList(BoardDomain param){
		final List<BoardDomain> list = new ArrayList();
		//래퍼런스 변수의 fianl은 주소값변경이 안된다는뜻(객체를 못바꿀뿐이지 객체에 추가 삭제는 가능)
		String sql = " SELECT * FROM ( "  
				+ " SELECT ROWNUM as RNUM,A.* FROM ( "  
				+ " 	SELECT A.i_board,B.i_user, A.title, A.hits, A.r_dt, B.nm"
				+ "			, B.profile_img, nvl(C.cnt,0) as like_cnt, nvl(D.cnt, 0) as cmt_cnt"
				+ "			, DECODE(E.i_board, null, 0, 1) as yn_like "  
				+ " 	FROM t_board4 A "
				+ " 	INNER JOIN t_user B "
				+ "			ON A.i_user = B.i_user"
				+ " 	LEFT JOIN ("
				+ "			SELECT i_board, count(i_board) as cnt FROM t_board4_like GROUP BY i_board"
				+ "			) C "
				+ " 	ON A.i_board = C.i_board "
				+ " 	LEFT JOIN ( "
				+ "			SELECT i_board, count(i_board) as cnt FROM t_board4_cmt GROUP BY i_board"
				+ "			) D "
				+ " 	ON A.i_board = D.i_board "
				+ " 	LEFT JOIN ( "
				+ " 		SELECT i_board FROM t_board4_like WHERE i_user = ?"
				+ "			) E "
				+ " 	ON A.i_board = E.i_board "
				+ " 	WHERE ";
				switch(param.getSearchType()) {
				case "a" :
					sql +=" A.title like ? ";
					break;
				case "b" :
					sql += " A.ctnt like ? ";
					break;
				case "c" :
					sql += " (A.ctnt like ? or A.title like ? )";
					break;
				}				
				
				sql += " 	ORDER BY i_board DESC) A "  
				+ " WHERE ROWNUM <= ?) A "  
				+ " WHERE A.RNUM > ? ";
		
		//int형으로 리턴을 안받을거기떄문에 return을 안적어줌
		int result = JdbcTemplate.executeQuery(sql, new JdbcSelectInterface(){
		
			@Override
			public void prepared(PreparedStatement ps) throws SQLException {
				int seq = 1;
				ps.setInt(seq, param.getI_user());
				ps.setNString(++seq, param.getSearchText());
				if(param.getSearchType().equals("c")) {
					ps.setNString(++seq, param.getSearchText());	
				}
				
				
				ps.setInt(++seq, param.geteIdx());
				ps.setInt(++seq, param.getsIdx());
				ps.executeQuery();
			}

			@Override
			public int executeQuery(ResultSet rs) throws SQLException {
				while(rs.next()) {
					BoardDomain vo = new BoardDomain();
					vo.setI_board(rs.getInt("i_board"));
					vo.setTitle(rs.getNString("title"));
					vo.setNm(rs.getNString("nm"));
					vo.setHits(rs.getInt("hits"));
					vo.setR_dt(rs.getNString("r_dt"));
					vo.setProfile_img(rs.getNString("profile_img"));
					vo.setI_user(rs.getInt("i_user"));
					vo.setLike_cnt(rs.getInt("like_cnt"));
					vo.setCmt_cnt(rs.getInt("cmt_cnt"));
					vo.setYn_like(rs.getInt("yn_like"));
					list.add(vo);
				}
				return 1;
			}
			
		});
		
		return list;
					
	}
	//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ페이징ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
	public static int selPagingCnt(final BoardDomain param) {
		String sql = " SELECT CEIL(COUNT(i_board) / ?) "
				+ " FROM t_board4"
				+ " WHERE ";
		switch(param.getSearchType()) {
		case "a" :
			sql +=" title like ? ";
			break;
		case "b" :
			sql += " ctnt like ? ";
			break;
		case "c" :
			sql += " (ctnt like ? or title like ? )";
			break;
		}	
		
		return JdbcTemplate.executeQuery(sql, new JdbcSelectInterface() {
			
			@Override
			public void prepared(PreparedStatement ps) throws SQLException {
				ps.setInt(1, param.getRecord_cnt()); //int는 ''를 자동으로 안넣어줌
				ps.setNString(2, param.getSearchText()); //String은 ''를 자동으로 넣어줌
				if(param.getSearchType().equals("c")) {
					ps.setNString(3, param.getSearchText());
				}
				
			}
			
			@Override
			public int executeQuery(ResultSet rs) throws SQLException {
				if(rs.next()) {
					return rs.getInt(1); //레코드의 첫번째 컬럼값
				}
				return 0;
			}
		});
		
		
	}
	
	
	//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ도메인을 추가해서 하는 디테일
	public static BoardDomain selBoard(BoardDomain param) {
		final BoardDomain result = new BoardDomain();
		result.setI_board(param.getI_board());
		
		String sql = " SELECT A.i_board, A.i_user, title, B.nm as nm, B.profile_img, ctnt, hits "
				+ " , to_char(A.r_dt,'yyyy-mm-dd hh24:mi') as r_dt"
				+ " , DECODE(C.i_user,null,0,1) as t_like , nvl(D.cnt, 0) as like_cnt"
				+ " FROM t_board4 A " 
				+ " LEFT JOIN t_user B "  
				+ " on A.i_user = B.i_user "
				+ " LEFT JOIN t_board4_like C "
				+ " on A.i_board = C.i_board "
				+ " AND C.i_user = ? "
				+ " LEFT JOIN( "
				+ " SELECT i_board, count(i_board) as cnt FROM t_board4_like "
				+ "	WHERE i_board = ? "
				+ "	GROUP BY i_board "
				+ " ) D"
				+ "	ON A.i_board = D.i_board" //얘는 로그인한유저 i_user
				+ " WHERE A.i_board = ? ";
				
		//유저번호 ==유저명 (조인필요)     댓글내용
		JdbcTemplate.executeQuery(sql, new JdbcSelectInterface(){

			@Override
			public void prepared(PreparedStatement ps) throws SQLException {
				ps.setInt(1, param.getI_user() ); //얘는 로그인한유저 i_user
				ps.setInt(2, param.getI_board());
				ps.setInt(3, param.getI_board());
			}

			@Override
			public int executeQuery(ResultSet rs) throws SQLException {
				if(rs.next()) {
				result.setTitle(rs.getNString("title"));
				result.setCtnt(rs.getNString("ctnt"));
				result.setNm(rs.getNString("nm"));
				result.setI_user(rs.getInt("i_user")); //작성자 i_user
				result.setR_dt(rs.getNString("r_dt"));
				result.setHits(rs.getInt("hits"));
				result.setT_like(rs.getInt("t_like"));
				result.setProfile_img(rs.getNString("profile_img"));
				result.setLike_cnt(rs.getInt("like_cnt"));
				}
				return 1;
			}
		});
		
		return result;
	}
	
	//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ글등록
	public static int insBoard(BoardVO param) {
		
		String sql = " INSERT into t_board4 (i_board, title, ctnt, i_user)"
				+ " VALUES((SELECT nvl(max(i_board)+1,0) from t_board4), ?, ?, ?) ";
		//String sql = " INSERT into t_board4 (i_board, title, ctnt, i_user) "
		//			+" VALUES "
		//			+" (seq_board4.nextval,?,?,?) ";
		
		return JdbcTemplate.executeUpdate(sql,new JdbcUpdateInterface() {
			
			@Override
			public void update(PreparedStatement ps) throws SQLException {
				ps.setNString(1, param.getTitle());
				ps.setNString(2, param.getCtnt());
				ps.setInt(3, param.getI_user());
			}
		});
	}
	//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ좋아요한사람 목록 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
	public static List<BoardDomain> selBoardLikeList(int i_board){
		List<BoardDomain> list = new ArrayList();
		String sql = " SELECT B.i_user, B.nm, b.profile_img FROM t_board4_like A " 
				+ " INNER JOIN t_user B " 
				+ " on A.i_user = B.i_user " 
				+ " WHERE A.i_board = ? "
				+ " ORDER BY A.R_DT ASC ";
		
		JdbcTemplate.executeQuery(sql, new JdbcSelectInterface() {
			
			@Override
			public void prepared(PreparedStatement ps) throws SQLException {
				ps.setInt(1, i_board);
			}
			
			@Override
			public int executeQuery(ResultSet rs) throws SQLException {
				while(rs.next()) {
				BoardDomain vo = new BoardDomain();
				vo.setI_user(rs.getInt("i_user"));
				vo.setNm(rs.getNString("nm"));
				vo.setProfile_img(rs.getNString("profile_img"));
				list.add(vo);				
				}
				return 0;
			}
		} );
		
		return list;
	}
	
	// ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ글수정
	public static int updateBoard(final BoardVO param) {
		String sql = " UPDATE t_board4 SET title = ?, ctnt = ?, m_dt = sysdate "
				+ " WHERE i_board = ? AND i_user = ? ";
		
		return JdbcTemplate.executeUpdate(sql, new JdbcUpdateInterface() {
			
			@Override
			public void update(PreparedStatement ps) throws SQLException {
				ps.setNString(1, param.getTitle());
				ps.setNString(2, param.getCtnt());
				ps.setInt(3, param.getI_board());
				ps.setInt(4, param.getI_user());
			}
		});
	}
	
	// ㅡㅡㅡㅡㅡㅡㅡㅡ글삭제
	public static int delBoard(BoardVO param) {
		String sql = " DELETE FROM t_board4 WHERE i_board = ? AND i_user = ? ";
		return JdbcTemplate.executeUpdate(sql, new JdbcUpdateInterface() {
			
			@Override
			public void update(PreparedStatement ps) throws SQLException {
				ps.setInt(1, param.getI_board());;
				ps.setInt(2, param.getI_user());
			}
		});
	}

	// ㅡㅡㅡㅡ 조회수
	public static void hitsUpdate(final int i_board) {
		String sql = " UPDATE t_board4 SET hits = hits+1 where i_board = ? ";
		
		JdbcTemplate.executeUpdate(sql, new JdbcUpdateInterface() {
			
			@Override
			public void update(PreparedStatement ps) throws SQLException {
				ps.setInt(1, i_board);				
			}
		});
		
	}

	
	
	//좋아요  on
	public static void likeIns(BoardDomain param) {
		String sql = " INSERT INTO t_board4_like (i_user, i_board) "
				+ " VALUES (?, ?) ";
		
		JdbcTemplate.executeUpdate(sql, new JdbcUpdateInterface() {
			
			@Override
			public void update(PreparedStatement ps) throws SQLException {
				ps.setInt(1, param.getI_user());
				ps.setInt(2, param.getI_board());
			}
		});
	}
	
	//좋아요 off
	public static void likeDel(BoardDomain param) {
		String sql = " DELETE FROM t_board4_like WHERE i_board = ? AND i_user = ? ";
		
		JdbcTemplate.executeUpdate(sql, new JdbcUpdateInterface() {
			
			@Override
			public void update(PreparedStatement ps) throws SQLException {
				ps.setInt(1, param.getI_board());;
				ps.setInt(2, param.getI_user());
			}
		});
	}
}
