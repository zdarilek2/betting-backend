import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Operations {
    private Integer id;
    private Date date;
    private Time time;
    private String type;

    public Date getDate(){ return date;}
    public void setDate(Date date){this.date=date;}

    public Integer getId(){ return id;}
    public void setId(Integer id){this.id=id;}

    public String getType(){ return type;}
    public void setType(String type){this.type=type;}

    public Time getTime(){ return time;}
    public void setTime(Time time){this.time=time;}

    public int insert() throws SQLException {
        int ac_id = 0;
        DBAdapter db = new DBAdapter();
        db.connect();
        try(PreparedStatement s = db.conn.prepareStatement("Insert INTO Operations(date,time,type)"+
                "Values(?,?,?)", Statement.RETURN_GENERATED_KEYS)){
            s.setDate(1,getDate());
            s.setTime(2,getTime());
            s.setString(3,getType());
            s.executeUpdate();
            try(ResultSet r = s.getGeneratedKeys()){
                r.next();
                ac_id = r.getInt(1);
            }
        }
        db.disconnect();
        return ac_id;
    }

    public static List<Operations> findAll() throws SQLException {
        DBAdapter db = new DBAdapter();
        db.connect();
        try (PreparedStatement s = db.conn.prepareStatement("SELECT * FROM Operations ORDER by date DESC LIMIT 10;")) {
            try (ResultSet r = s.executeQuery()) {
                List<Operations> elements = new ArrayList<>();
                while (r.next()) {
                    Operations c = new Operations();

                    c.setId(r.getInt("id"));
                    c.setTime(r.getTime("time"));
                    c.setDate(r.getDate("date"));
                    c.setType(r.getString("type"));
                    elements.add(c);
                }
                db.disconnect();
                return elements;
            }
        }
    }


    //pre autocommit mod
    public int insert_commit_mode(DBAdapter db) throws SQLException {
        int ac_id = 0;
       // db.connect();
        try(PreparedStatement s = db.conn.prepareStatement("Insert INTO Operations(date,time,type)"+
                "Values(?,?,?)", Statement.RETURN_GENERATED_KEYS)){
            s.setDate(1,getDate());
            s.setTime(2,getTime());
            s.setString(3,getType());
            s.executeUpdate();
            try(ResultSet r = s.getGeneratedKeys()){
                r.next();
                ac_id = r.getInt(1);
            }
        }
        //db.disconnect();
        return ac_id;
    }
}
