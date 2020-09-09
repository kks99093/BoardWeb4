package com.koreait.pjt.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.koreait.pjt.vo.UserLoginHistoryVO;
import com.koreait.pjt.vo.UserVO;

public class UserDAO {
	
	public static int insUserLoginHistory(UserLoginHistoryVO param) {
		String sql = " INSERT INTO t_user_loginhistory  "
				+ " (i_history, i_user, ip_addr, os, browser) "
				+ " VALUES "
				+ " ( seq_userloginhistory.nextval, ?, ?, ?, ?) ";
		return JdbcTemplate.executeUpdate(sql, new JdbcUpdateInterface() {
			
			@Override
			public void update(PreparedStatement ps) throws SQLException {
				ps.setInt(1, param.getI_user());
				ps.setNString(2, param.getIp_addr());
				ps.setNString(3, param.getOs());
				ps.setNString(4, param.getBrowser());;
				
			}
		});
	}
	
	
	//int update, delete쓸 친구
	public static int insUser(UserVO param) {
			
			String sql = " INSERT INTO t_user "
					+ " (i_user, user_id, user_pw, nm, email) "
					+ " VALUES "
					+ " (seq_user.nextval, ?, ?, ?, ?) ";
	
			
			// AUP 간접지향 프로그래밍
			return JdbcTemplate.executeUpdate(sql, new JdbcUpdateInterface(){
				@Override						//여기서 얘는 객체화라기보다는 implement라고 보는게 이해하기 쉽다
				public void update(PreparedStatement ps) throws SQLException{
					ps.setNString(1, param.getUser_id());
					ps.setNString(2, param.getUser_pw());
					ps.setNString(3, param.getNm());
					ps.setNString(4, param.getEmail());
				
				//new JdbcUpdateInterface() : 익명클래스, 익명클래스를 객체화하는 기법중의 하나
				//원래는 class 를만들어서 인터페이스 구현후 여기서 객체생성한후 쓴다거나 하지만 
				//이런식으로 익명클래스로 만들어서 이 메소드에서 받는 param변수까지 쓰면서 사용할수있다
				}
			});
		}
	
	
	//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ셀렉트
	public static int login(UserVO param) {
		String sql = " SELECT i_user, user_pw, nm "
				+ " FROM t_user "
				+ " WHERE user_id = ?";
		
		return JdbcTemplate.executeQuery(sql, new JdbcSelectInterface(){

			@Override
			public void  prepared(PreparedStatement ps) throws SQLException {
				ps.setNString(1, param.getUser_id());
			}

			@Override
			public int executeQuery(ResultSet rs) throws SQLException {
				if(rs.next()) {
					String dbPw = rs.getNString("user_pw");
					if(dbPw.equals(param.getUser_pw())) {
						int i_user = rs.getInt("i_user"); //pk값
						String nm = rs.getNString("nm");
						
						param.setUser_pw(null);
						param.setI_user(i_user);
						param.setNm(nm);
						//로그인성공후 로그인한 계정에대한 정보를 param에다가 넣어주고 비밀번호는 null로 바꿈
						//그래서 로그인후에는 param에 user_id, i_user, nm값이 들어있다
						//근데 왜 비밀번호를 null로 바꾼거지? 정보수정같은거할때도 쓰지않나?
						return 1; //로그인 성공
					}else {
						return 2; //비번 틀림
					}
				}
				else{
					return 3; //아이디없음
				}
			}
			//0이면 에러
		});
	}
	
	// 프로필 입력 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
	 public static UserVO selUser(int i_user) {
		 String sql = " SELECT user_id, nm, profile_img, email, r_dt "
		 		+ " FROM t_user WHERE i_user = ? ";
		 
		 UserVO result = new UserVO();
		 
		 JdbcTemplate.executeQuery(sql, new JdbcSelectInterface() {
			
			@Override
			public void prepared(PreparedStatement ps) throws SQLException {
				ps.setInt(1, i_user);
			}
			
			@Override
			public int executeQuery(ResultSet rs) throws SQLException {
				if(rs.next()) {
					result.setUser_id(rs.getNString("user_id"));
					result.setNm(rs.getNString("nm"));
					result.setProfile_img(rs.getNString("profile_img"));
					result.setEmail(rs.getNString("email"));
					result.setR_dt(rs.getNString("r_dt"));
				}
				return 1;
			}
		});
		 
		 return result;
	 }
	 
	 // 1. 셀렉트후 다 가져와서 거기서 내가 봐꾸고싶은것만 값을 바꾼후 전부 업데이트
	 // 2. 내가 바꾸고자하는 값만 넣어놓고 바꾸고싶은것만 쿼리문 만들어서 수정
	 
	 public static int updUser (UserVO param) {
		 StringBuilder sb = new StringBuilder(" UPDATE t_user SET m_dt = sysdate ");
		 //STringBuilder 쓰는이유 : 문자열 합치기할때 많이씀, 퍼포먼스가 좋음 , for문쓸떄 꼭 써야함
		 if(param.getUser_pw() != null) {
			sb.append(" , user_pw = '");
			sb.append(param.getUser_pw());
			sb.append("' "); //홀따움표 필수
			// sql += " , user_pw = '" + param.getUser_pw()+"' "; //'홀따움표 넣어줘야함!
		 }
		 if(param.getNm() != null) {
			 sb.append(" , nm = '");
			 sb.append(param.getNm());
			 sb.append("' ");
		 }
		 if(param.getEmail() != null) {
			 sb.append(" , email = '");
			 sb.append(param.getEmail());
			 sb.append("' ");
		 }
		 if(param.getProfile_img() != null) {
			 sb.append(" , profile_img = '");
			 sb.append(param.getProfile_img());
			 sb.append("' ");
		 }
		 sb.append(" WHERE i_user = ");
		 sb.append(param.getI_user()); //정수값이하 홀따움표 필요없음
		//이렇게 해놓으면 param을 통해서 보내준값만 변경이 된다
		 System.out.println("sb : " + sb.toString());
		 
		 return JdbcTemplate.executeUpdate(sb.toString(), new JdbcUpdateInterface() {
			@Override
			public void update(PreparedStatement ps) throws SQLException {
			}
		});
		 
	 }
	 

	
}
