package com.koreait.pjt.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface JdbcUpdateInterface {
	//자바에서 함수를 넘기는 방법은 인터페이스를 만들어서 객체를 넘기는 방법밖에 없다(콜백함수)
	//인터페이스의 변수앞에는 public과 abstract가 자동으로 들어가있다(컴파일러가 자동으로 넣어줌)
	void update(PreparedStatement ps) throws SQLException;
	//추상 클래스 : 추상메소드를 갖고있는 메소드 (없더라도 abstract를 붙이면 추상클래스로 만들수있음)
	//			갖고있다면 무조건 class에 abstract를 붙여줘야함
	//추상클래스,인터체이스는 스스로 객체화할수 없다 (개발자가 스스로 객체화되는걸 막을때 abstract를써서 막을수도있다)
	
	
}
