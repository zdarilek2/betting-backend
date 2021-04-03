import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Accounts {
    private Integer id;
    private float money;

    public Integer getId(){ return id;}
    public void setId(Integer id){this.id=id;}

    public Float getMoney(){ return money;}
    public void setMoney(Float mon){this.money=mon;}

    public int insert() throws SQLException {
        int ac_id = 0;
        DBAdapter db = new DBAdapter();
        db.connect();
        try(PreparedStatement s = db.conn.prepareStatement("Insert INTO Accounts(money)"+
                "Values(?)", Statement.RETURN_GENERATED_KEYS)){
            s.setInt(1,0);
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
        try(PreparedStatement s = db.conn.prepareStatement("UPDATE Accounts SET money = money+? WHERE id = ?")) {
            s.setFloat(1, getMoney());
            s.setInt(2, getId());
            s.executeUpdate();
        }
        db.disconnect();
    }
    public void delete(){}

    public static Float find_by_id(int ac_id) throws SQLException {
        DBAdapter db = new DBAdapter();
        db.connect();
        float money = 0;
        try(PreparedStatement s = db.conn.prepareStatement("SELECT money FROM Accounts WHERE id=?")){
            s.setInt(1,ac_id);

            try(ResultSet r = s.executeQuery()){
                r.next();
                money = r.getFloat(1);
            }
        }
        db.disconnect();
        return money;
    }

    public Float show_all_money() throws SQLException {
        DBAdapter db = new DBAdapter();
        db.connect();
        float money = 0;
        try(PreparedStatement s = db.conn.prepareStatement("SELECT SUM(money) FROM Accounts")){
            try(ResultSet r = s.executeQuery()){
                r.next();
                money = r.getFloat(1);
            }
        }
        db.disconnect();
        return money;
    }
    //pouzivane pri autocommite
    public void update_commit_f(DBAdapter db) throws SQLException {
        try(PreparedStatement s = db.conn.prepareStatement("UPDATE Accounts SET money = money+? WHERE id = ?")) {
            s.setFloat(1, getMoney());
            s.setInt(2, getId());
            s.executeUpdate();
        }
    }
}
