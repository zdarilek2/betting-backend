import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Auto_evaluate_tickets {
//auto vyhodnocavanie tiketov
    public void Auto() throws Exception {

        List<Tickets> tickets_id = Tickets.findAll_not_id("bet");

            for (Tickets i : tickets_id) {

                List<Betting_odds> bet_ids = Betting_odds.findAllByTicket_id(i.getId());

                int count = bet_ids.size();
                int win = 0;
                int lost = 0;
                int open = 0;
                float odds = 1;

                    for (Betting_odds j : bet_ids) {
                        bet_options bet = bet_options.findById(j.getBet_option_id());

                        if (bet.getStatus().equals("win")) {
                            win++;
                            odds = odds * bet.getBet_oods();
                        } else if (bet.getStatus().equals("lost")) {
                            lost++;
                        } else {
                            open++;
                        }
                    }
                    if (count == win || i.getMax_win_events() <= win) {
                        i.setTicket_status("win");
                        i.update();

                        Client_finder cf = new Client_finder();
                        if(i.getClient_id()==null){System.out.println("je to");}
                        Client c = cf.findById(i.getClient_id());
                        Accounts a = new Accounts();
                        a.setId(c.getAccount_id());
                        a.setMoney(odds * i.getBet_money());
                        a.update();
                        int op_id = insert_operation();
                        insert_deposit(op_id, odds * i.getBet_money(), a.getId());
                        Accounts a1 = new Accounts();
                        a1.setId(1);
                        a1.setMoney(-odds * i.getBet_money());
                        a1.update();
                    }
                    if ((count - lost) < i.getMax_win_events()) {
                        System.out.println("som tu");
                        i.setTicket_status("lost");
                        i.update();
                        Accounts a = new Accounts();
                        a.setId(1);
                        a.setMoney(i.getBet_money());
                        a.update();
                    }
                }

        }


    public int insert_operation() throws SQLException {
        Operations o = new Operations();
        //System.out.println(Time.valueOf(LocalTime.now()));
        o.setDate(Date.valueOf(LocalDate.now()));
        o.setTime(Time.valueOf(LocalTime.now()));
        o.setType("d-v");
        int ac_id = o.insert();
        return ac_id;
    }

    public void insert_deposit(int op_id, float money,int ac_id) throws SQLException {
        Deposit d = new Deposit();
        d.setOperation_id(op_id);
        d.setAmount(money);
        d.setAccount_id(ac_id);
        d.insert();
    }
}
