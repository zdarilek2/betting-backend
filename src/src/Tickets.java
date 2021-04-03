import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Tickets {
    private Integer id;
    private Integer max_win_events;
    private String ticket_status;
    private Float bet_money;
    private Date date;
    private Integer client_id;

    public Integer getId(){ return id;}
    public void setId(Integer id){this.id=id;}

    public Integer getMax_win_events(){ return max_win_events;}
    public void setMax_win_events(Integer max_win_events){this.max_win_events=max_win_events;}

    public String getTicket_status(){ return ticket_status;}
    public void setTicket_status(String ticket_status){this.ticket_status=ticket_status;}

    public Float getBet_money(){ return bet_money;}
    public void setBet_money(Float bet_money){this.bet_money=bet_money;}

    public Date getDate(){ return date;}
    public void setDate(Date id){this.date=date;}

    public Integer getClient_id(){ return client_id;}
    public void setClient_id(Integer client_id){this.client_id=client_id;}

    public int insert() throws SQLException {
        int ac_id = 0;
        DBAdapter db = new DBAdapter();
        db.connect();
        try(PreparedStatement s = db.conn.prepareStatement("Insert INTO Tickets(max_win_events,ticket_status,bet_money,date,client_id)"+
                "Values(?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS)){
            s.setInt(1,getMax_win_events());
            s.setString(2,getTicket_status());
            s.setFloat(3,getBet_money());
            s.setDate(4,getDate());
            s.setInt(5,getClient_id());
            s.executeUpdate();
            try(ResultSet r = s.getGeneratedKeys()){
                r.next();
                ac_id = r.getInt(1);
            }
        }
        db.disconnect();
        return ac_id;
    }

    public void update() throws SQLException {
        DBAdapter db = new DBAdapter();
        db.connect();
        try(PreparedStatement s = db.conn.prepareStatement("UPDATE Tickets SET ticket_status = ? , bet_money = ? WHERE id = ?")) {
            s.setString(1, getTicket_status());
            s.setFloat(2, getBet_money());
            s.setInt(3, getId());
            s.executeUpdate();
        }
        db.disconnect();
    }
    public void delete() throws SQLException {
        DBAdapter db = new DBAdapter();
        db.connect();
        try(PreparedStatement s = db.conn.prepareStatement("Delete from Tickets WHERE id = ?")) {
            s.setInt(1, getId());
            s.executeUpdate();
        }
        db.disconnect();
    }
    public static Tickets findById(int id, int client_id) throws Exception {
        DBAdapter db = new DBAdapter();
        db.connect();
        try (PreparedStatement s = db.conn.prepareStatement("SELECT * FROM Tickets WHERE id=? and client_id=?")) {
            s.setInt(1, id);
            s.setInt(2,client_id);

            try (ResultSet r = s.executeQuery()) {
                if (r.next()) {
                    Tickets c = new Tickets();

                    c.setId(r.getInt("id"));
                    c.setMax_win_events(r.getInt("max_win_events"));
                    c.setTicket_status(r.getString("ticket_status"));
                    c.setBet_money(r.getFloat("bet_money"));
                    c.setDate(r.getDate("date"));
                    c.setClient_id(r.getInt("client_id"));

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


    public static List<Tickets> findAll(int client_id, String status) throws SQLException {
        DBAdapter db = new DBAdapter();
        db.connect();
        try (PreparedStatement s = db.conn.prepareStatement("SELECT * FROM Tickets where ticket_status=? and client_id=?")) {
            s.setString(1, status);
            s.setInt(2,client_id);
            try (ResultSet r = s.executeQuery()) {

                List<Tickets> elements = new ArrayList<>();

                while (r.next()) {
                    Tickets c = new Tickets();

                    c.setId(r.getInt("id"));
                    c.setMax_win_events(r.getInt("max_win_events"));
                    c.setTicket_status(r.getString("ticket_status"));
                    c.setBet_money( r.getFloat("bet_money"));
                    elements.add(c);
                }
                db.disconnect();
                return elements;
            }
        }
    }

    //hlada podla statusu
    public static List<Tickets> findAll_not_id(String status) throws SQLException {
        DBAdapter db = new DBAdapter();
        db.connect();
        try (PreparedStatement s = db.conn.prepareStatement("SELECT * FROM Tickets where ticket_status=?")) {
            s.setString(1, status);
            try (ResultSet r = s.executeQuery()) {

                List<Tickets> elements = new ArrayList<>();

                while (r.next()) {
                    Tickets c = new Tickets();

                    c.setId(r.getInt("id"));
                    c.setMax_win_events(r.getInt("max_win_events"));
                    c.setTicket_status(r.getString("ticket_status"));
                    c.setBet_money( r.getFloat("bet_money"));
                    c.setClient_id(r.getInt("client_id"));
                    elements.add(c);
                }
                db.disconnect();
                return elements;
            }
        }
    }

    //vyhlada data pre statistiku
    public static List<Float> find_data_stat(Date date, int id) throws Exception {
        DBAdapter db = new DBAdapter();
        db.connect();
        float lost = 0;
        float win = 0;
        float bet_m = 0;
        float win_money = 0;
        db.conn.setAutoCommit(false);
        try(PreparedStatement s = db.conn.prepareStatement("SELECT bet_money, ticket_status, date, id FROM Tickets WHERE client_id=?")){
            s.setInt(1,id);
            try(ResultSet r = s.executeQuery()){
                while(r.next()) {
                    if (r.getDate(3).compareTo(date) > 0) {
                        bet_m += r.getFloat(1);
                        String a = r.getString(2);
                        if (r.getString(2).equals("lost")) {
                            lost=lost+1;
                        } else if (r.getString(2).equals("win")) {
                            win=win+1;
                            win_money = Betting_odds.find_win_money(r.getInt(4), db, r.getFloat(1));
                        }
                    }
                }
            }

        }
        List<Float> l = new ArrayList<>();
        System.out.println(win_money);
        l.add(lost);l.add(win);l.add(bet_m);l.add(win_money);
        db.conn.commit();
        db.disconnect();
        return l;

    }


    public int insert_commit_mode(DBAdapter db) throws SQLException {
        int ac_id = 0;
       // db.connect();
        try(PreparedStatement s = db.conn.prepareStatement("Insert INTO Tickets(max_win_events,ticket_status,bet_money,date,client_id)"+
                "Values(?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS)){
            s.setInt(1,getMax_win_events());
            s.setString(2,getTicket_status());
            s.setFloat(3,getBet_money());
            s.setDate(4,getDate());
            s.setInt(5,getClient_id());
            s.executeUpdate();
            try(ResultSet r = s.getGeneratedKeys()){
                r.next();
                ac_id = r.getInt(1);
            }
        }
        //db.disconnect();
        return ac_id;
    }

    public void delete_commit_f(DBAdapter db) throws SQLException {
        try(PreparedStatement s = db.conn.prepareStatement("Delete from Tickets WHERE id = ?")) {
            s.setInt(1, getId());
            s.executeUpdate();
        }
    }

    public void update_commit_f(DBAdapter db) throws SQLException {
        try(PreparedStatement s = db.conn.prepareStatement("UPDATE Tickets SET ticket_status = ? , bet_money = ? WHERE id = ?")) {
            s.setString(1, getTicket_status());
            s.setFloat(2, getBet_money());
            s.setInt(3, getId());
            s.executeUpdate();
        }
    }
}
