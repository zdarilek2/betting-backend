import sun.security.krb5.internal.Ticket;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

public class Delete_notbet_ticket {
    //vymze nepodany tiket
    public void delete(int id) throws SQLException {
        Tickets t = new Tickets();
        t.setId(id);
        DBAdapter db = new DBAdapter();
        db.connect();
        db.conn.setAutoCommit(false);
        t.delete_commit_f(db);
        int ac_id = insert_operation(db);
        insert_ticket_open(ac_id,db);
        db.conn.commit();
        db.conn.close();
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
        d.setStatus("del");
        d.insert_commit_mode(db);
    }
}
