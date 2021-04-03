import java.sql.*;

public class DBAdapter {
    String jdbUrl = "jdbc:postgresql://localhost:5432/postgres";
    String username = "postgres";
    String password = "postgres";

    Connection conn = null;
    Statement stat = null;
    ResultSet rs = null;

    public DBAdapter(){
    }

    public void connect(){
        try {
            conn = DriverManager.getConnection(jdbUrl,username,password);
            //System.out.println("connection is correct");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void disconnect(){
        try{
            if(stat != null){
                stat.close();
            }
            if(rs != null){
                rs.close();
            }
            if(conn != null){
                conn.close();
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
