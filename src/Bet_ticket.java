import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Bet_ticket {
//zmeni stav tiketu na bet-podany
    public void change_state(int id, Float money) throws SQLException {
        Tickets t = new Tickets();
        t.setId(id);
        t.setBet_money(money);
        t.setTicket_status("bet");
        Float m = multip(find_odds(id));
        if(money*m<10000){
            DBAdapter db = new DBAdapter();
            db.connect();
            db.conn.setAutoCommit(false);
            t.update_commit_f(db);
            int ac_id = insert_operation(db);
            insert_ticket_open(ac_id,db);
            db.conn.commit();
            db.conn.close();
        }
        else{ System.out.println("you have to bet less money ");}
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
        d.setStatus("bet");
        d.insert_commit_mode(db);
    }

    public static List<Float>  find_odds(int id) throws SQLException {
        DBAdapter db = new DBAdapter();
        db.connect();
        try (PreparedStatement s = db.conn.prepareStatement("Select bet_odds from bet_options " +
                "join betting_odds ON betting_odds.bet_option_id=bet_options.id " +
                "where betting_odds.ticket_id=?;")) {
            s.setInt(1, id);
            try (ResultSet r = s.executeQuery()) {

                List<Float> elements = new ArrayList<>();

                while (r.next()) {
                    elements.add(r.getFloat("bet_odds"));
                }
                db.disconnect();
                return elements;
            }
        }
    }

    public Float multip(List<Float> pole){
        float m=1;
        for( float i:pole){
            m = m*i;
        }
        return m;
    }
}
