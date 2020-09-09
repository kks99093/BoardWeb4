package com.koreait.pjt.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.koreait.pjt.vo.BoardCmtVO;
import com.koreait.pjt.vo.BoardDomain;

public class BoardCmtDAO {
	//셀렉트 부분
	public static List<BoardCmtVO> selCmt(BoardDomain param) {
		List<BoardCmtVO> result = new ArrayList(); 
		String sql = " SELECT i_cmt, A.i_user, cmt, B.nm as nm, A.r_dt, A.m_dt, B.profile_img  "
				+ " FROM T_BOARD4_CMT A"
				+ " INNER JOIN t_user B"
				+ " on A.i_user = B.i_user "
				+ " WHERE i_board = ? "
				+ " ORDER BY A.i_cmt DESC ";
		JdbcTemplate.executeQuery(sql, new JdbcSelectInterface() {
			
			@Override
			public void prepared(PreparedStatement ps) throws SQLException {
				ps.setInt(1, param.getI_board());
				
			}
			
			@Override
			public int executeQuery(ResultSet rs) throws SQLException {
				while(rs.next()) {
					BoardCmtVO vo = new BoardCmtVO();
					vo.setI_cmt(rs.getInt("i_cmt"));
					vo.setI_user(rs.getInt("i_user"));
					vo.setCmt(rs.getNString("cmt"));
					vo.setR_dt(rs.getNString("r_dt"));
					vo.setM_dt(rs.getNString("m_dt"));
					vo.setNm(rs.getNString("nm"));
					vo.setProfile_img(rs.getNString("profile_img"));
					result.add(vo);
				}
				return 1;
			}
		});
		return result;
		
	}
	
	
	public static int insCmt(BoardCmtVO param) {
		String sql = " INSERT INTO T_BOARD4_CMT"
				+ " (i_cmt, i_board, i_user, cmt) "
				+ " VALUES"
				+ " (seq_board4_cmt.nextval, ?, ?, ?) ";
		
		return JdbcTemplate.executeUpdate(sql, new JdbcUpdateInterface() {
			
			@Override
			public void update(PreparedStatement ps) throws SQLException {
				ps.setInt(1, param.getI_board());
				ps.setInt(2, param.getI_user());
				ps.setNString(3, param.getCmt());
				
			}
		});
		
	}
	
	
	public static int updCmt(BoardCmtVO param) {
		String sql = " UPDATE t_board4_cmt "
				+ " SET cmt = ?, m_dt = sysdate where i_cmt = ? AND i_user = ? ";
		return JdbcTemplate.executeUpdate(sql, new JdbcUpdateInterface() {
			
			@Override
			public void update(PreparedStatement ps) throws SQLException {
				ps.setNString(1, param.getCmt());
				ps.setInt(2, param.getI_cmt());		
				ps.setInt(3, param.getI_user());
			}
		});
	}
	
	public static int delCmt(BoardCmtVO param) {
		String sql = " DELETE FROM t_board4_cmt "
				+ " WHERE i_cmt = ? AND i_user = ?";
		
		return JdbcTemplate.executeUpdate(sql, new JdbcUpdateInterface() {
			
			@Override
			public void update(PreparedStatement ps) throws SQLException {
				ps.setInt(1, param.getI_cmt());
				ps.setInt(2, param.getI_user());
			}
		});
	}

}
