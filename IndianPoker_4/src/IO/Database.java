package IO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Database {
	String s = "";//회원출력스트링
	String ranking ="";//랭킹출력스트링
	String driver="org.mariadb.jdbc.Driver"; 
	String url = "jdbc:mariadb://localhost:3307/IndianPoker";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null; 

	public void init(){
		try{
			Class.forName(driver);
			con = DriverManager.getConnection(url, "root","setest");
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
		try{
			String sql = "select * from ranking order by score desc";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery(sql);    //sql문으로 리턴한 값
			rowNum = pstmt.executeUpdate();  //sql문으로 존재하는 행의 수
			if (rowNum == 0)
				System.out.println("랭킹이 존재하지 않습니다.");
			else{
				while (rs.next()) {
					ranking += ("id:" + rs.getString("id") + " score:" + Integer.toString(rs.getInt("score"))
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
		String sql2=null;

		try{
			sql2="select id from ranking where id="+"'"+id+"'";//로그인한 회원의 아이디비교
			pstmt = con.prepareStatement(sql2);
			rs = pstmt.executeQuery(sql2);
			rs.next();
			if(id.equals(rs.getString("id"))){//로그인한회원이면
				sql2="select score from ranking where id="+"'"+id+"'"; //그 회원의 score출력
				pstmt = con.prepareStatement(sql2);
				rs = pstmt.executeQuery(sql2); 
				rs.next();
				if(score>=rs.getInt("score")){
					System.out.println("회원 랭킹갱신");
				}
			}

			sql="update ranking set score="+score+" where id="+"'"+id+"'";
			pstmt = con.prepareStatement(sql);


		} catch (Exception e1) {
			System.out.println(e1.getMessage());
		} finally {
			close();
		}

	}
	public void insert(String id,int score){
		init();
		int rowNum;
		String sql=null;
		
		try{
			sql = "insert into ranking (id, score) values (?,?)";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id); 
			pstmt.setInt(2, score);
			
			rowNum = pstmt.executeUpdate();

			if (rowNum == 0)
				System.out.println("데이터 추가 실패!");
			else{
				System.out.println("데이터 추가 성공!");

			}

		}
		catch(Exception e2) {
			System.out.println(e2.getMessage());
			e2.printStackTrace( );
		}finally{
			close();
		}
	}

	public String getList() {
		init();
		try {
			String sql = "select id, pw from login";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery(sql);

			while (rs.next()) {
				s += (rs.getString("id") + "@" + rs.getString("pw") + "!");
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
			sql = "insert into login (id, pw) values (?,?)";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, pw);

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
