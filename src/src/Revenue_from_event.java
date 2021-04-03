import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Revenue_from_event {
    //trzba z udalosti
    public static float Result_stat_event_revenue(int id_event) {
        return find_stat_lost(id_event) - find_win(id_event);
    }
    //najde prehrane tikety
    public static Float find_stat_lost(int id_event) {
        DBAdapter db = new DBAdapter();
        db.connect();
        try (
                PreparedStatement s = db.conn.prepareStatement("SELECT sum(bet_money) from" +
                        "(Select ticket_id from bet_options JOIN betting_odds  ON bet_options.id = betting_odds.bet_option_id " +
                        "WHERE bet_options.event_id=?) t1 JOIN " +
                        "tickets ON t1.ticket_id=tickets.id " +
                        "Where tickets.ticket_status='lost';")) {
            s.setInt(1, id_event);
            try (ResultSet r = s.executeQuery()) {
                if (r.next()) {
                    float suma = r.getFloat("sum");
                    if (r.next()) {
                        throw new RuntimeException("Move than one row was returned");
                    }

                    db.disconnect();
                    return suma;
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Float.valueOf(0);
    }
    //najde vyhrate tikety
    public static float find_win(int id_event){
        float vysledok = 0;
            DBAdapter db = new DBAdapter();
            db.connect();
            try (
                    PreparedStatement s = db.conn.prepareStatement("SELECT bet_odds,bet_money from " +
                            "(Select ticket_id, bet_odds from bet_options JOIN betting_odds  ON bet_options.id = betting_odds.bet_option_id " +
                            "WHERE bet_options.event_id=? and bet_options.status='win') t1 JOIN " +
                            "tickets ON t1.ticket_id=tickets.id " +
                            "Where tickets.ticket_status='win'; ")) {
                s.setInt(1, id_event);
                try (ResultSet r = s.executeQuery()) {
                    if (r.next()) {
                        float bet_odd = r.getFloat("bet_odds");
                        float bet_money = r.getFloat("bet_money");
                        vysledok = vysledok + ((bet_odd*bet_money)-bet_money);
                        if (r.next()) {
                            throw new RuntimeException("Move than one row was returned");
                        }
                        db.disconnect();
                    }
                    //return vysledok;
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        return vysledok;
    }


}
