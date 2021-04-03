import sun.security.pkcs11.Secmod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Create_ticket_player {
    //vlozi tiket
    public int insert_ticket(int id, int type, DBAdapter db) throws SQLException {
        Tickets t = new Tickets();
        t.setClient_id(id);
        t.setMax_win_events(type);
        t.setTicket_status("open");
        t.setDate(Date.valueOf(LocalDate.now()));
        t.setBet_money((float) 0);
        int ac_id = t.insert_commit_mode(db);
        int ac_id1 = insert_operation(db);
        insert_ticket_open(ac_id1,db);
        return ac_id;
    }
    //vlozi betting odds - moznost do tiketu
    public void insert_betting_odds(int ac_id,int bet_id, DBAdapter db) throws SQLException {
        Betting_odds b =new Betting_odds();
        b.setBet_option_id(bet_id);
        b.setTicket_id(ac_id);
        b.insert_commit_mode(db);
    }
    //zapise do operacie
    public int insert_operation(DBAdapter db) throws SQLException {
        Operations o = new Operations();
        System.out.println(Time.valueOf(LocalTime.now()));
        o.setDate(Date.valueOf(LocalDate.now()));
        o.setTime(Time.valueOf(LocalTime.now()));
        o.setType("t");
        int ac_id = o.insert_commit_mode(db);
        return ac_id;
    }
    //zapise do operacie - ticket open
    public void insert_ticket_open(int ac_id,DBAdapter db) throws SQLException {
        Ticket_update d = new Ticket_update();
        d.setOperation_id(ac_id);
        d.setStatus("open");
        d.insert_commit_mode(db);
    }
    //vytvori tiket
    public static void create_ticket() throws SQLException {
        List<Integer> id_matches = new ArrayList<>();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter Client id:");
        try {
            Client c1 = Client_finder.getInstance().findById(Integer.valueOf(br.readLine()));
            int id = c1.getId();
            System.out.println("Enter type(1-single bet / 2-more accumulated bet / 2- more classic bet ):");
            int type = Integer.valueOf(br.readLine());
            Create_ticket_player c = new Create_ticket_player();
            DBAdapter db = new DBAdapter();
            db.connect();
            db.conn.setAutoCommit(false);
            int ac_id = c.insert_ticket(id, type,db);

            if (type == 1) {
                System.out.println("Enter id bet option:");
                bet_options b = bet_options.findById(Integer.valueOf(br.readLine()));
                int id_match = b.getId();
                if(b.getStatus().equals("N")){
                c.insert_betting_odds(ac_id, id_match, db);
                System.out.println("Create ticket");}
            } else {
                ticket_big(c,ac_id,id_matches,type,db);
                System.out.println("Create ticket");
            }
            db.conn.commit();
        } catch (IOException e) {
            System.out.println("Wrong value");
        } catch (Exception e) {
            System.out.println("Wrong value");
        }
    }
    //vytvori tiekt akumulovany/clasic velky
    private static void ticket_big(Create_ticket_player c, int ac_id, List<Integer> id_matches, int type, DBAdapter db) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int count = 0;
        boolean is_bet = false;
        while (true) {
            is_bet = false;
            System.out.println("Enter id bet option:");
            bet_options b = bet_options.findById(Integer.valueOf(br.readLine()));
            int id_match = b.getId();
            for (int i : id_matches){
                if (i==id_match){
                    is_bet=true;
                }
            }
            if(b.getStatus().equals("N") && is_bet==false){
                c.insert_betting_odds(ac_id, id_match, db);
                id_matches.add(id_match);
                count++;
            }
            else{
                System.out.println("You must change bet option");
            }

            if (count >= type) {
                System.out.println("Do you want add nex? (Y/N)");
                String a = br.readLine();
                if (a.equals("N")) {
                    break;
                }
            }
        }
    }
}
