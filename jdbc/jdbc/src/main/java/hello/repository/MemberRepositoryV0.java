package hello.repository;

import hello.connection.DBConnectionUtil;
import hello.domain.Member;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.NoSuchElementException;

/**
 * JDBC - DriverManager 사용
 */
@Slf4j
public class MemberRepositoryV0 {

    public Member save(Member member) throws SQLException {
        String sql = "insert into member(member_id, money) values(?,?)";

        Connection con = null;
        PreparedStatement pstmt = null;

        try{
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, member.getMemberId());
            pstmt.setInt(2, member.getMoney());
            pstmt.executeUpdate();      //쿼리 실행문
            return member;
        }catch (SQLException e){
            log.error("DB Error", e);
            throw e;
        }finally {
            close(pstmt, con, null);
        }
    }

    public void update(String memberId, int money) throws SQLException {
        String sql = "update member set money=? where member_id=?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, money);
            pstmt.setString(2,memberId);
            int resultSize = pstmt.executeUpdate();//쿼리 실행문
            log.info("resultSize={}", resultSize);
        }catch (SQLException e){
            log.error("DB Error", e);
            throw e;
        }finally {
            close(pstmt, con, null);
        }
    }

    public void delete(String memberId) throws SQLException {
        String sql = "delete from member where member_id=?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1,memberId);

            pstmt.executeUpdate();//쿼리 실행문

        }catch (SQLException e){
            log.error("DB Error", e);
            throw e;
        }finally {
            close(pstmt, con, null);
        }
    }
    public Member findById(String memberId) throws SQLException {
         String sql = "select * from member where member_id = ?";

         Connection con = null;
         PreparedStatement pstmt = null;
         ResultSet rs = null;

         try {
             con = getConnection();
             pstmt = con.prepareStatement(sql);
             pstmt.setString(1, memberId);

             rs = pstmt.executeQuery();  //Select query의 결과값을 반환하는 것.
             if (rs.next()){
                 Member member = new Member();
                 member.setMemberId(rs.getString(1));
                 member.setMoney(rs.getInt(2));
                 return member;
             }else {
                 throw new NoSuchElementException("member not found memberId= " + memberId);
             }
         } catch (SQLException e) {
             log.error("DB Error", e);
             throw e;
         }
    }

    private void close(Statement stmt, Connection con, ResultSet rs) {

        if(rs != null){
            try{
                rs.close();
            }catch (SQLException e){
                log.error("DB Error", e);
            }
        }

        if(stmt != null){
            try{
                stmt.close();
            } catch(SQLException e){
                log.info("error", e);
            }
        }

        if(con != null){
            try{
                con.close();
            }catch (SQLException e){
                log.info("error", e);
            }
        }
    }

    private static Connection getConnection() {
        return DBConnectionUtil.getConnection();
    }
}
