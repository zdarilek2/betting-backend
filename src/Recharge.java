import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Recharge {
    private Integer id;
    private Float amount;
    private Integer operation_id;
    private Integer account_id;

    public Integer getOperation_id(){ return operation_id;}
    public void setOperation_id(Integer operation_id){this.operation_id=operation_id;}

    public Integer getId(){ return id;}
    public void setId(Integer id){this.id=id;}

    public Float getAmount(){ return amount;}
    public void setAmount(Float amount){this.amount=amount;}

    public Integer getAccount_id(){ return account_id;}
    public void setAccount_id(Integer account_id){this.account_id=account_id;}

    public void insert(DBAdapter db) throws SQLException {
        int ac_id = 0;
        try(PreparedStatement s = db.conn.prepareStatement("Insert INTO Recharge(amount,operation_id,account_id)"+
                "Values(?,?,?)", Statement.RETURN_GENERATED_KEYS)){
            s.setFloat(1,getAmount());
            s.setInt(2,getOperation_id());
            s.setInt(3,getAccount_id());
            s.executeUpdate();
            try(ResultSet r = s.getGeneratedKeys()){
                r.next();
                ac_id = r.getInt(1);
            }
        }
    }

}
