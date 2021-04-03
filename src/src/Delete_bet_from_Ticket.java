import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

public class Delete_bet_from_Ticket {
    //vymaze stavku z tiketu
    public void delete_from_betting_odds(int bet_id, int ticket_id, String status) throws Exception {
        Betting_odds b = new Betting_odds();
        b.setTicket_id(ticket_id);
        b.setBet_option_id(bet_id);
        b.setId(b.findByIds(bet_id,ticket_id));
        if(status.equals("open")){
            DBAdapter db = new DBAdapter();
            db.connect();
            db.conn.setAutoCommit(false);
            b.delete(db);
            int a = insert_operation(db);
            insert_ticket_open(a,db);
            db.conn.commit();
            db.conn.close();
            System.out.println("bet is delete");
        }
        else{
            System.out.println("its not possible delete bet");
        }

    }
    public int insert_operation(DBAdapter db) throws SQLException {
        Operations o = new Operations();
        System.out.println(Time.valueOf(LocalTime.now()));
        o.setDate(Date.valueOf(LocalDate.now()));
        o.setTime(Time.valueOf(LocalTime.now()));
        o.setType("t");
        int ac_id = o.insert_commit_mode(db);
        return ac_id;
    }

    public void insert_ticket_open(int ac_id,DBAdapter db) throws SQLException {
        Ticket_update d = new Ticket_update();
        d.setOperation_id(ac_id);
        d.setStatus("open");
        d.insert_commit_mode(db);
    }
}

