package IO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Database {
	String driver="org.mariadb.jdbc.Driver"; 
	String url = "jdbc:mariadb://localhost:3307/IndianPoker";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null; 

	public void init(){
		try{
			Class.forName(driver);
			con = DriverManager.getConnection(url, "root", "setest");
		}catch(Exception e2) {
			System.out.println(e2.getMessage());
			e2.printStackTrace( );
		}
	}
	public void close(){
		try {rs.close();} catch (Exception ignored) {}
		try {pstmt.close();} catch (Exception ignored) {}
		try {con.close();}catch (Exception ignored) {}
	}

	public String Ranking(){
		init();
		int rowNum;
		String ranking ="";//랭킹출력스트링
		try{
			
			String sql = "select id, score from member order by score desc";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery(sql);    //sql문으로 리턴한 값
			rowNum = pstmt.executeUpdate();  //sql문으로 존재하는 행의 수
			if (rowNum == 0)
				System.out.println("랭킹이 존재하지 않습니다.");
			else{
				while (rs.next()) {
					ranking += ("id:" + rs.getString("id") + "!score:" + Integer.toString(rs.getInt("score"))
					+ "\n");
				}
			}

		}catch(Exception e2) {
			System.out.println(e2.getMessage());
			e2.printStackTrace( );
		}finally{
			close();
		}
		return ranking;
	}

	public void UpdateScore(String id,int score){
		init();
		String sql=null;

		try{
			sql="update member set score="+score+" where id="+"'"+id+"'";
			pstmt = con.prepareStatement(sql);
			pstmt.executeUpdate();
		} catch (Exception e1) {
			System.out.println(e1.getMessage());
		} finally {
			close();
		}

	}

	public String getList() {
		String s = "";//회원출력스트링
		init();
		try {
			String sql = "select * from member";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery(sql);

			while (rs.next()) {
				s += (rs.getString("id") + "@" + rs.getString("pw") + "@" + rs.getInt("score") +"!");
			}
		} catch (Exception e2) {
			System.out.println(e2.getMessage());
			e2.printStackTrace();
		} finally {
			close();
		}
		return s;
	}

	public void newID(String id, String pw) {
		init();

		String sql = null;
		int rowNum; //insert해서 받아온 행의 갯수

		try {
			sql = "insert into member (id, pw, score) values (?,?,?)";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, pw);
			pstmt.setInt(3, 0);

			rowNum = pstmt.executeUpdate();
			if (rowNum == 0)
				System.out.println("회원가입 실패!");
			else {
				System.out.println("회원가입 성공!");
			}
		} catch (Exception e1) {

		} finally {
			close();
		}
	}
}