import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Ticket_update {   //Ticket del/bet/open
    private Integer id;
    private String status;
    private Integer operation_id;

    public Integer getOperation_id(){ return operation_id;}
    public void setOperation_id(Integer operation_id){this.operation_id=operation_id;}

    public Integer getId(){ return id;}
    public void setId(Integer id){this.id=id;}

    public String getStatus(){ return status;}
    public void setStatus(String status){this.status=status;}


    public void insert() throws SQLException {
        int ac_id = 0;
        DBAdapter db = new DBAdapter();
        db.connect();
        try(PreparedStatement s = db.conn.prepareStatement("Insert INTO Ticket_del_bet_open(status,operation_id)"+
                "Values(?,?)", Statement.RETURN_GENERATED_KEYS)){
            s.setString(1,getStatus());
            s.setInt(2,getOperation_id());
            s.executeUpdate();
            try(ResultSet r = s.getGeneratedKeys()){
                r.next();
                ac_id = r.getInt(1);
            }
        }
        db.disconnect();
    }

    public void update() throws SQLException {
        DBAdapter db = new DBAdapter();
        db.connect();
        try(PreparedStatement s = db.conn.prepareStatement("UPDATE Ticket_del_bet_open SET status = ? WHERE id = ?")) {
            s.setString(1, getStatus());
            s.setInt(2, getId());
            s.executeUpdate();
        }
        db.disconnect();
    }
    public void delete() throws SQLException {
        DBAdapter db = new DBAdapter();
        db.connect();
        try(PreparedStatement s = db.conn.prepareStatement("Delete Ticket_del_bet_open WHERE id = ?")) {
            s.setInt(1, getId());
            s.executeUpdate();
        }
        db.disconnect();
    }


    public void insert_commit_mode(DBAdapter db) throws SQLException {
        int ac_id = 0;
        //DBAdapter db = new DBAdapter();
       // db.connect();
        try(PreparedStatement s = db.conn.prepareStatement("Insert INTO Ticket_del_bet_open(status,operation_id)"+
                "Values(?,?)", Statement.RETURN_GENERATED_KEYS)){
            s.setString(1,getStatus());
            s.setInt(2,getOperation_id());
            s.executeUpdate();
            try(ResultSet r = s.getGeneratedKeys()){
                r.next();
                ac_id = r.getInt(1);
            }
        }
       // db.disconnect();
    }
}
