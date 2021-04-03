import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

public class Withdraw {

    public void withdraw_money(float money,int id_client_account) throws SQLException {
        Accounts a = new Accounts();
        a.setMoney(-money);
        a.setId(id_client_account);
        DBAdapter db = new DBAdapter();
        db.connect();
        db.conn.setAutoCommit(false);
        a.update_commit_f(db);
        int a1 = insert_operation(db);
        recharge_change(id_client_account,money,a1,db);
        db.conn.commit();
        db.conn.close();
    }

    public void recharge_change(int ac_id,float money, int op_id,DBAdapter db) throws SQLException {
        Recharge r = new Recharge();
        r.setOperation_id(op_id);
        r.setAmount(money);
        r.setAccount_id(ac_id);
        r.insert(db);
    }

    public int insert_operation(DBAdapter db) throws SQLException {
        Operations o = new Operations();
        System.out.println(Time.valueOf(LocalTime.now()));
        o.setDate(Date.valueOf(LocalDate.now()));
        o.setTime(Time.valueOf(LocalTime.now()));
        o.setType("r");
        int ac_id = o.insert_commit_mode(db);
        return ac_id;
    }

}

