import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Betting_odds {
    private Integer id;
    private Integer bet_option_id;
    private Integer ticket_id;
    //funkcia pre tzby statistiku


    public Integer getBet_option_id(){ return bet_option_id;}
    public void setBet_option_id(Integer bet_option_id){this.bet_option_id=bet_option_id;}

    public Integer getId(){ return id;}
    public void setId(Integer id){this.id=id;}

    public Integer getTicket_id(){ return ticket_id;}
    public void setTicket_id(Integer ticket_id){this.ticket_id=ticket_id;}

    public void insert() throws SQLException {
        int ac_id = 0;
        DBAdapter db = new DBAdapter();
        db.connect();
        try(PreparedStatement s = db.conn.prepareStatement("Insert INTO Betting_odds(bet_option_id,ticket_id)"+
                "Values(?,?)", Statement.RETURN_GENERATED_KEYS)){
            s.setInt(1,getBet_option_id());
            s.setInt(2,getTicket_id());
            s.executeUpdate();
            try(ResultSet r = s.getGeneratedKeys()){
                r.next();
                ac_id = r.getInt(1);
            }
        }
        db.disconnect();
    }

    public void delete(DBAdapter db) throws SQLException {
        try(PreparedStatement s = db.conn.prepareStatement("Delete from Betting_odds WHERE id = ?")) {
            s.setInt(1, getId());
            s.executeUpdate();
        }
    }

    public static List<Betting_odds> findAllByTicket_id(int ticket_id) throws SQLException {
        DBAdapter db = new DBAdapter();
        db.connect();
        try (PreparedStatement s = db.conn.prepareStatement("SELECT * FROM Betting_odds WHERE ticket_id=?")) {
            s.setInt(1, ticket_id);
            try (ResultSet r = s.executeQuery()) {

                List<Betting_odds> elements = new ArrayList<>();

                while (r.next()) {
                    Betting_odds c = new Betting_odds();

                    c.setId(r.getInt("id"));
                    c.setTicket_id(r.getInt("ticket_id"));
                    c.setBet_option_id(r.getInt("bet_option_id"));


                    elements.add(c);
                }
                db.disconnect();
                return elements;
            }
        }
    }

    public int findByIds(int bet_id,int ticket_id) throws Exception {
        DBAdapter db = new DBAdapter();
        db.connect();
        try (PreparedStatement s = db.conn.prepareStatement("SELECT id FROM betting_odds WHERE bet_option_id=? and ticket_id=?")) {
            s.setInt(1, bet_id);
            s.setInt(2, ticket_id);

            try (ResultSet r = s.executeQuery()) {
                if (r.next()) {
                    Betting_odds c = new Betting_odds();
                    c.setId(r.getInt("id"));

                    if (r.next()) {
                        throw new RuntimeException("Move than one row was returned");
                    }
                    db.disconnect();
                    return c.getId();
                } else {
                    db.disconnect();
                    throw new Exception("Nothing");
                    //return null;
                }
            }
        }
    }

//pre auto commit mode insert
    public void insert_commit_mode(DBAdapter db) throws SQLException {
        int ac_id = 0;
        //DBAdapter db = new DBAdapter();
        //db.connect();
        try(PreparedStatement s = db.conn.prepareStatement("Insert INTO Betting_odds(bet_option_id,ticket_id)"+
                "Values(?,?)", Statement.RETURN_GENERATED_KEYS)){
            s.setInt(1,getBet_option_id());
            s.setInt(2,getTicket_id());
            s.executeUpdate();
            try(ResultSet r = s.getGeneratedKeys()){
                r.next();
                ac_id = r.getInt(1);
            }
        }
       // db.disconnect();
    }

    public static float find_win_money(int bet_option_id_id, DBAdapter db, float bet_money) throws Exception {
        List<Betting_odds> pole = findAllByOption_id_commit(bet_option_id_id,db);
        float win = Float.valueOf(1);
        for (Betting_odds i : pole) win = win*bet_options.findById(i.getBet_option_id()).getBet_oods();
        return win;
    }


    public static List<Betting_odds> findAllByOption_id_commit(int tid, DBAdapter db) throws SQLException {
        try (PreparedStatement s = db.conn.prepareStatement("SELECT * FROM Betting_odds WHERE bet_option_id = ?")) {
            s.setInt(1, tid);
            try (ResultSet r = s.executeQuery()) {

                List<Betting_odds> elements = new ArrayList<>();

                while (r.next()) {
                    Betting_odds c = new Betting_odds();

                    c.setId(r.getInt("id"));
                    c.setTicket_id(r.getInt("ticket_id"));
                    c.setBet_option_id(r.getInt("bet_option_id"));

                    elements.add(c);
                }
                //db.disconnect();
                return elements;
            }
        }
    }
}
