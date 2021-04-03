import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class bet_options {
    private Integer id;
    private String name;
    private Float bet_oods;
    private Integer event_id;
    private String status;

    public Integer getId(){ return id;}
    public void setId(Integer id){this.id=id;}

    public String getName(){ return name;}
    public void setName(String name){this.name=name;}

    public Float getBet_oods(){ return bet_oods;}
    public void setBet_oods(Float bet_oods){this.bet_oods=bet_oods;}

    public Integer getEvent_id(){ return event_id;}
    public void setEvent_id(Integer event_id){this.event_id=event_id;}

    public String getStatus(){ return status;}
    public void setStatus(String status){this.status=status;}

    public void insert() throws SQLException {
        int ac_id = 0;
        DBAdapter db = new DBAdapter();
        db.connect();
        try(PreparedStatement s = db.conn.prepareStatement("Insert INTO Bet_options(name,bet_odds,status,event_id)"+
                "Values(?,?,?,?)", Statement.RETURN_GENERATED_KEYS)){
            s.setString(1,getName());
            s.setFloat(2,getBet_oods());
            s.setString(3,getStatus());
            s.setInt(4,getEvent_id());
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
        try(PreparedStatement s = db.conn.prepareStatement("UPDATE Bet_options SET status = ? WHERE id = ?")) {
            s.setString(1, getStatus());
            s.setInt(2, getId());
            s.executeUpdate();
        }
        db.disconnect();
    }
    public void delete() throws SQLException {
        DBAdapter db = new DBAdapter();
        db.connect();
        try(PreparedStatement s = db.conn.prepareStatement("Delete from Bet_options WHERE id = ?")) {
            s.setInt(1, getId());
            s.executeUpdate();
        }
        db.disconnect();
    }

    public static bet_options findById(int bet_option_id) throws Exception {
        DBAdapter db = new DBAdapter();
        db.connect();
        try (PreparedStatement s = db.conn.prepareStatement("SELECT * FROM Bet_options WHERE id=?")) {
            s.setInt(1, bet_option_id);
            try (ResultSet r = s.executeQuery()) {
                if (r.next()) {
                    bet_options c = new bet_options();
                    c.setId(r.getInt("id"));
                    c.setName(r.getString("name"));
                    c.setBet_oods(r.getFloat("bet_odds"));
                    c.setStatus(r.getString("status"));
                    c.setEvent_id(r.getInt("event_id"));

                    if (r.next()) {
                        throw new RuntimeException("Move than one row was returned");
                    }
                    db.disconnect();
                    return c;
                } else {
                    db.disconnect();
                    throw new Exception("Nothing");
                    //return null;
                }
            }

        }

    }
}
