import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Odds_del {
    private Integer id;
    private Integer operation_id;
    private Integer bet_option_id;

    public Integer getOperation_id(){ return operation_id;}
    public void setOperation_id(Integer operation_id){this.operation_id=operation_id;}

    public Integer getId(){ return id;}
    public void setId(Integer id){this.id=id;}

    public Integer getBet_option_id(){ return bet_option_id;}
    public void setBet_option_id(Integer bet_option_id){this.bet_option_id=bet_option_id;}

    public void insert() throws SQLException {
        int ac_id = 0;
        DBAdapter db = new DBAdapter();
        db.connect();
        try(PreparedStatement s = db.conn.prepareStatement("Insert INTO Recharge(operation_id,bet_option_id)"+
                "Values(?,?,?)", Statement.RETURN_GENERATED_KEYS)){
            s.setInt(1,getOperation_id());
            s.setInt(2,getBet_option_id());
            s.executeUpdate();
            try(ResultSet r = s.getGeneratedKeys()){
                r.next();
                ac_id = r.getInt(1);
            }
        }
        db.disconnect();
    }
}
