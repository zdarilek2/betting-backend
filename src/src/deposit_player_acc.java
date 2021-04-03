import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class deposit_player_acc {
    private Integer client_id;
    private Float money;

    public Integer getClient_id(){ return client_id;}
    public void setClient_id(Integer id){this.client_id=id;}

    public Float getMoney(){ return money;}
    public void setMoney(Float mon){this.money=mon;}

    public void insert_money() throws Exception {
        if (client_id == null) {
            throw new IllegalStateException("id is not set");
        }
        Client c = Client_finder.getInstance().findById(getClient_id());
        DBAdapter db = new DBAdapter();
        db.connect();
        db.conn.setAutoCommit(false);
        change_data(db,getMoney(),c.getAccount_id());
        db.conn.commit();

    }

    public void change_data(DBAdapter db, float money, int account_id) throws SQLException {
        int ac_id=0;
        Accounts a = new Accounts();
        a.setMoney(money);
        a.setId(account_id);
        a.update();

        Operations o = new Operations();
        System.out.println(Time.valueOf(LocalTime.now()));
        o.setDate(Date.valueOf(LocalDate.now()));
        o.setTime(Time.valueOf(LocalTime.now()));
        o.setType("d");
        ac_id = o.insert();

        Deposit d = new Deposit();
        d.setAmount(money);
        d.setAccount_id(account_id);
        d.setOperation_id(ac_id);
        d.insert();

    }
}
