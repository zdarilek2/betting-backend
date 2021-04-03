import java.sql.*;

public class Events {
    private Integer id;
    private String type;
    private String name;
    private Date date;
    private Time time;
    private String event_status;
    private Float max_win;
    private Float min_bet;

    public Integer getId(){ return id;}
    public void setId(Integer id){this.id=id;}

    public String getType(){ return type;}
    public void setType(String type){this.type=type;}

    public String getName(){ return name;}
    public void setName(String name){this.name=name;}

    public Date getDate(){ return date;}
    public void setDate(Date date){this.date=date;}

    public Time getTime(){ return time;}
    public void setTime(Time time){this.time=time;}

    public String getEvent_status(){ return event_status;}
    public void setEvent_status(String event_status){this.event_status=event_status;}

    public Float getMax_win(){ return max_win;}
    public void setMax_win(Float max_win){this.max_win=max_win;}

    public Float getMin_bet(){ return min_bet;}
    public void setMin_bet(Float min_bet){this.min_bet=min_bet;}

    public Integer insert() throws SQLException {
        int ac_id = 0;
        setEvent_status("set");
        DBAdapter db = new DBAdapter();
        db.connect();
        try(PreparedStatement s = db.conn.prepareStatement("Insert INTO Events(type,name,date,time,event_status,max_win,min_bet)"+
                "Values(?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS)){
            s.setString(1,getType());
            s.setString(2,getName());
            s.setDate(3,getDate());
            s.setTime(4,getTime());
            s.setString(5,getEvent_status());
            s.setFloat(6,getMax_win());
            s.setFloat(7,getMin_bet());
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
        try(PreparedStatement s = db.conn.prepareStatement("UPDATE Events SET event_status = ? WHERE id = ?")) {
            s.setString(1, getEvent_status());
            s.setInt(2, getId());
            s.executeUpdate();
        }
        db.disconnect();
    }

    public void delete() throws SQLException {
        DBAdapter db = new DBAdapter();
        db.connect();
        try(PreparedStatement s = db.conn.prepareStatement("Delete FROM Events WHERE id = ?")) {
            s.setInt(1, getId());
            s.executeUpdate();
        }
        db.disconnect();
    }

    public static Events findById(int event_id) throws Exception {
        DBAdapter db = new DBAdapter();
        db.connect();
        try (PreparedStatement s = db.conn.prepareStatement("SELECT * FROM Events WHERE id=?")) {
            s.setInt(1, event_id);
            try (ResultSet r = s.executeQuery()) {
                if (r.next()) {
                    Events c = new Events();
                    c.setId(r.getInt("id"));
                    c.setType(r.getString("type"));
                    c.setName(r.getString("name"));
                    c.setEvent_status(r.getString("event_status"));

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
