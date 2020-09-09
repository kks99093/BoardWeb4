package com.koreait.pjt.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class JdbcTemplate {

	//INSERT, DELETE, UPDATE
	public static int executeUpdate(String sql, JdbcUpdateInterface jdbc) {
		int result = 0;
		Connection con = null;
		PreparedStatement ps  = null;
		
		try {
			con = DbCon.getCon();
			ps= con.prepareStatement(sql);
			
			jdbc.update(ps);
			
			result = ps.executeUpdate();
			//자바에서 함수를 넘기는 방법은 인터페이스를 만들어서 객체를 넘기는 방법밖에 없다(콜백함수)
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbCon.close(con, ps);
		}
		
		
		return result;
	}
	
	
	//SELECT
	public static int executeQuery(String sql, JdbcSelectInterface jdbc) {
		int result = 0;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			con =DbCon.getCon();
			ps = con.prepareStatement(sql);
			jdbc.prepared(ps); //값넣기
			rs = ps.executeQuery(); //실행
			result = jdbc.executeQuery(rs); //SELECT후에 할것

		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			DbCon.close(con, ps, rs);
		}
		return result;	
	}

	
}
