import org.postgresql.util.PSQLException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class MainMenu extends Menu {

    public void print() {
        System.out.println("****************************");
        System.out.println("* 1. list all the clients  *");
        System.out.println("* 2. show a client         *");
        System.out.println("* 3. add a client          *");
        System.out.println("* 4. edit a client         *");
        System.out.println("* 5. show admin money      *");
        System.out.println("* 6. show client money     *");
        System.out.println("* 7. insert money          *");
        System.out.println("* 8. exit                  *");
        System.out.println("* 9. client win statistic  *");
        System.out.println("* 10. create Event         *");
        System.out.println("* 11. Open Event           *");
        System.out.println("* 12  Add Bet option       *");
        System.out.println("* 13  delete event         *");
        System.out.println("* 14  delete bet option    *");
        System.out.println("* 15  change status bet option    *");
        System.out.println("* 16  show ticket          *");
        System.out.println("* 17  show active tickets   *");
        System.out.println("* 18  show closed tickets   *");
        System.out.println("* 19  delete ticket         *");
        System.out.println("* 20  10 accounts operations*");
        System.out.println("* 21  add ticket            *");
        System.out.println("* 22  delete bet from ticket*");
        System.out.println("* 23  delete not bet ticket*");
        System.out.println("* 24  bet ticket           *");
        System.out.println("* 25  withdraw money        *");
        System.out.println("* 26  AUTO result           *");
        System.out.println("* 27  revenue_from_event    *");
        System.out.println("****************************");
    }

    @Override
    public void handle(String option) throws Exception {
        try {
            switch (option) {
                case "1":
                    listAllClients(0, 5);
                    break;
                case "2":
                    showAClient();
                    break;
                case "3":
                    addAClient();
                    break;
                case "4":
                    editAClient();
                    break;
                case "5":
                    show_Admin_money();
                    break;
                case "6":
                    show_money();
                    break;
                case "7":
                    insertMoney();
                    break;
                case "8":
                    exit();
                    break;
                case "9":
                    Show_win_stat();
                    break;
                case "10":
                    create_event();
                    break;
                case "11":
                    open_event();
                    break;
                case "12":
                    add_bet_option();
                    break;
                case "13":
                    delete_event();
                    break;
                case "14":
                    delete_option();
                    break;
                case "15":
                    change_status_option();
                    break;
                case "16":
                    show_ticket();
                    break;
                case "17":
                    show_active_tickets();
                    break;
                case "18":
                    show_other_tickets();
                    break;
                case "19":
                    delete_ticket();
                    break;
                case "20":
                    show_operations();
                    break;
                case "21":
                    create_ticket();
                    break;
                case "22":
                    delete_bet_from_ticket();
                    break;
                case "23":
                    delete_notbet_ticket();
                    break;
                case "24":
                    bet_ticket();
                    break;
                case "25":
                    withdraw_money();
                    break;
                case "26":
                    Auto_check_tickets();
                    break;
                case "27":
                    revenue_from_event();
                    break;
                default:
                    System.out.println("Unknown option");
                    break;
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
    //trzba z udalosti
    private void revenue_from_event() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter event id :");
        int id = Integer.valueOf(br.readLine());
        System.out.println("Revenue of this event: "+Revenue_from_event.Result_stat_event_revenue(id)+ "eur");
    }
    //auto vyhodnocovac tiketov
    private void Auto_check_tickets() throws Exception {
        Auto_evaluate_tickets a = new Auto_evaluate_tickets();
        a.Auto();
    }
    //vyberie peniaze z uctu klienta.
    private void withdraw_money() throws IOException, SQLException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter client id :");
        try {
            Client c = Client_finder.getInstance().findById(Integer.valueOf(br.readLine()));
            int account_id = c.getAccount_id();
            System.out.println("Enter withdraw money :");
            float money = Float.valueOf(br.readLine());
            Withdraw w = new Withdraw();
            Float a_money = Accounts.find_by_id(account_id);
            if (money > 0 && a_money>money) {
                w.withdraw_money(money, account_id);
            }
            else{System.out.println("wrong value");}
        } catch (Exception e) {
            System.out.println("wrong value");
        }
    }
    //poda pripraveny tiket
    private void bet_ticket() throws IOException, SQLException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter ticket id :");
        int ticket_id = Integer.valueOf(br.readLine());
        System.out.println("Enter how much money you want to bet :");
        Float money = Float.valueOf(Integer.valueOf(br.readLine()));
        Bet_ticket b = new Bet_ticket();
        b.change_state(ticket_id,money);
        System.out.println("Ticket is bet");
    }
    //vymaze nepodany tiket
    private void delete_notbet_ticket() throws IOException, SQLException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter client id :");
        try {
            Client c = Client_finder.getInstance().findById(Integer.valueOf(br.readLine()));
            System.out.println("Enter ticket id :");
            Tickets t = Tickets.findById(Integer.valueOf(br.readLine()), c.getId());
            Delete_notbet_ticket d = new Delete_notbet_ticket();
            if(t.getTicket_status().equals("open")) {
                d.delete(t.getId());
                System.out.println("Ticket is delete");
            }
            else{
                System.out.println("not possible delete");
            }
        } catch (Exception e) {
            System.out.println("wrong value");
        }

    }
    //vymaze stavku z tiketu
    private void delete_bet_from_ticket() throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter bet option id :");
        bet_options b = bet_options.findById(Integer.valueOf(br.readLine()));
        System.out.println("Enter client id :");
        Client c = Client_finder.getInstance().findById(Integer.valueOf(br.readLine()));
        System.out.println("Enter Ticket id:");
        Tickets t_id = Tickets.findById(Integer.valueOf(br.readLine()),c.getId());
       // int ticket_id = Integer.valueOf(br.readLine());
        Delete_bet_from_Ticket t = new Delete_bet_from_Ticket();
        t.delete_from_betting_odds(b.getId(),t_id.getId(), t_id.getTicket_status());
    }
    //vytvori tiket ale este sa nepoda. Je v stave set
    private void create_ticket() throws SQLException {
        Create_ticket_player.create_ticket();
    }

    //ukaze poslednych 10operacii vykonanych v aplikacii
    private void show_operations() throws IOException, SQLException {
        List<Operations> pole = Operations.findAll();
        for (Operations i : pole){
            System.out.println(i.getType()+" / "+i.getDate()+ " / "+ i.getTime());
        }

    }
    //vymazanie tiketu pokial nie je podany
    private void delete_ticket() throws SQLException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter client id:");
        try {
            Client c = Client_finder.getInstance().findById(Integer.valueOf(br.readLine()));
            System.out.println("Enter ticket id:");
            Tickets t = Tickets.findById(Integer.valueOf(br.readLine()), c.getId());
            if(t.getTicket_status().equals("open")){
                t.delete();
                System.out.println("Ticket is delete");
            }
            else{System.out.println("the ticket does not have a status set");}

        } catch (IOException e) {
            System.out.println("wrong value");
        } catch (Exception e) {
            System.out.println("wrong value");
        }
    }
    // ukaze zvysne tikety klienta. Vsetky okrem aktivnych
    private void show_other_tickets() throws  SQLException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter client id:");
        try {
            Client c = Client_finder.getInstance().findById(Integer.valueOf(br.readLine()));
            int id = c.getId();
            List<Tickets> t = Tickets.findAll(id, "win");
            List<Tickets> t1 = Tickets.findAll(id, "lost");
            for (Tickets i : t) {
                System.out.println("id ticket: " + i.getId() + "  bet money: " + i.getBet_money() + "  status: " + i.getTicket_status());
            }
            for (Tickets i : t1) {
                System.out.println("id ticket: " + i.getId() + "  bet money: " + i.getBet_money() + "  status: " + i.getTicket_status());
            }
        } catch (IOException e) {
            System.out.println("wrong value");
        } catch (Exception e) {
            System.out.println("wrong value");
        }
    }
    //ukaze vsetky aktivne tikety klienta
    private void show_active_tickets()  {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter client id:");
        try {
            Client c = Client_finder.getInstance().findById(Integer.valueOf(br.readLine()));
            int id = c.getId();
            List<Tickets> t = Tickets.findAll(id, "bet");
            for (Tickets i : t) {
                System.out.println("id ticket: " + i.getId() + "  bet money: " + i.getBet_money());
            }
        } catch (SQLException throwables) {
            System.out.println("now its not possible show tickets");
        } catch (IOException e) {
            System.out.println("wrong value");
        } catch (Exception e) {
            System.out.println("wrong value");
        }
    }
    //ukaze tipy na danom tikete
    private void show_ticket(){
        Tickets t = new Tickets();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter ticket id:");
        try {
            t.setId(Integer.valueOf(br.readLine()));
            System.out.println("Enter client id:");
            t.setClient_id(Integer.valueOf(br.readLine()));
            Tickets t1 = t.findById(t.getId(), t.getClient_id());
            List<Betting_odds> b = Betting_odds.findAllByTicket_id(t1.getId());
            List<bet_options> b1 = new ArrayList<>();
            for (Betting_odds i : b) {
                bet_options bp = new bet_options();
                b1.add(bp.findById(i.getBet_option_id()));
            }
            System.out.println("Your ticket");
            for (bet_options i : b1) {
                Events e = new Events();
                Events a = e.findById(i.getEvent_id());
                System.out.println(a.getName() + " / " + a.getType() + " / " + i.getName() + " / " + i.getBet_oods() + " / " + i.getStatus());
            }
        } catch (SQLException throwables) {
            System.out.println("now its not possible to open ticket menu");
        } catch (IOException e) {
           System.out.println("wrong value");
        } catch (Exception e) {
            System.out.println("not found");
        }

    }
    //zmeni status podacej moznosti. Moze to byt win/lost
    private void change_status_option()  {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter option id:");
        try {
            int id = Integer.valueOf(br.readLine());
            bet_options b = bet_options.findById(id);
            System.out.println("Enter new status (win/lost):");
            b.setStatus(br.readLine());
            b.update();
            System.out.println("Status is change");
        } catch (SQLException throwables) {
            System.out.println("option not exist");
        } catch (IOException e) {
            System.out.println("wrong value");
        } catch (Exception e) {
            System.out.println("wrong value");
        }
    }
    //vymaze moznost na tipnutie
    private void delete_option() throws IOException, SQLException {
        bet_options b = new bet_options();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter id:");
        b.setId(Integer.valueOf(br.readLine()));
        b.delete();
        System.out.println("Bet option is delete");
    }
    //vymaze podujatie. Musi byt v stave set
    private void delete_event() {
        Events e = new Events();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter event id:");
        try {
        int id = Integer.valueOf(br.readLine());
        Events e1 = null;
        e1 = Events.findById(id);
        if(e1.getEvent_status().equals("set")){
            e.setId(id);
            e.delete();
            System.out.println("event is delete");
        }
        else{ System.out.println("must be set");}
        } catch (Exception exception) {
            System.out.println("wrong value");
        }
    }
    //pridanie stavkovej moznosti
    private void add_bet_option() {
        bet_options b = new bet_options();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter option's name :");
        try {
            b.setName(br.readLine());
            System.out.println("Enter Event id:");
            int id = Integer.valueOf(br.readLine());
            Events e1 = Events.findById(id);
            if (e1.getEvent_status().equals("open")) {
                b.setEvent_id(id);
                System.out.println("Enter bet odds:");
                b.setBet_oods(Float.valueOf(br.readLine()));
                b.setStatus("N");
                try {
                    b.insert();
                } catch (Exception PSQLException) {
                    System.out.println("wrong value!!!!!");
                }
            }
        } catch (IOException e) {
            System.out.println("wrong value");
        }
        catch (Exception e) {
            System.out.println("Not find event");
        }

    }
    //Meni stav eventu na open
    private void open_event() throws SQLException {
        Events e = new Events();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter id:");
        try {
            int id = Integer.valueOf(br.readLine());
            Events e1 = e.findById(id);
            if(e1.getEvent_status().equals("set")){
                e1.setEvent_status("open");
                e1.update();
                System.out.println("Event is open");
            }
            else{System.out.println("Event must be set");}
        } catch (IOException ioException) {
            System.out.println("wrong value");
        } catch (Exception exception) {
            System.out.println("not find event");
        }

    }
    //vytvori podujatie
    private void create_event() {
        Events e = new Events();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("Enter Type:"); e.setType(br.readLine());
            System.out.println("Enter name:"); e.setName(br.readLine());
            System.out.println("Enter date:");
            Date date = Date.valueOf(br.readLine());
            check_date(date);
            e.setDate(date);
            System.out.println("Enter time:");e.setTime(Time.valueOf(br.readLine()));
            System.out.println("Enter max win:");e.setMax_win(Float.valueOf(br.readLine()));
            System.out.println("Enter min bet:");e.setMin_bet(Float.valueOf(br.readLine()));
            e.insert();
        } catch (SQLException throwables) {
            System.out.println("wrong value");
        } catch (IOException ioException) {
            System.out.println("wrong value");
        }

    }

    private void check_date(Date valueOf) throws IOException {
        if((valueOf.compareTo(Date.valueOf(LocalDate.now())) )<0){
            throw new IOException();
        }
    }
    //ukaze peniaze ktore ma momentalne stavkova kancelaria - v nasom pripade admin
    private void show_Admin_money() throws SQLException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter login:");
        try {
            if (br.readLine().equals("login")) {
                System.out.println("Enter password:");
                if (br.readLine().equals("heslo")) {
                    System.out.println("Admin money:");
                    Accounts a = new Accounts();
                    System.out.println(a.show_all_money());
                }
                else{System.out.println("wrong password");}
            }
            else{System.out.println("wrong login");}
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
        //Pridava klienta
        private void addAClient() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Client c = new Client();
        try {
            System.out.println("Enter first name:"); c.setFirst_name(br.readLine());
            System.out.println("Enter last name:"); c.setLast_name(br.readLine());
            System.out.println("Enter birth number:"); c.setBirth_number(Integer.valueOf(br.readLine()));
            System.out.println("Enter email:"); String mail = br.readLine();
            if(isValid(mail)) { c.setEmail(mail); }   else{ throw new Exception("wrong email"); }
            System.out.println("Enter login:"); c.setLogin(br.readLine());
            System.out.println("Enter password:"); c.setPassword(br.readLine());
            c.insert();
            System.out.println("The client has been sucessfully added");
            System.out.print("The client's id is: ");
            System.out.println(c.getId());
        }catch(Exception e){
            System.out.println("Wrong type your data!");
        }
    }

    private boolean isValid(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    // Vola funkciu findByid - vyberie hladaneho klienta podla id
    private void showAClient() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter a Client id:");
        try{
            int client_id = Integer.parseInt(br.readLine());
            Client c = Client_finder.getInstance().findById(client_id);
            if(c == null){
                System.out.println("No such client exists");
            }
            else{
                Client_printer.getInstance().print(c);
            }
        }catch(Exception e){
            System.out.println("No such client exists");
        }
    }

    //Vola funkciu na vylistovanie klientov. Robi strankovanie.
    private void listAllClients(int count,int count2) throws SQLException, IOException {
        List<Client> t = Client_finder.getInstance().findAll();
        for(int i = count; i <count2;i++){
            Client_printer.getInstance().print(t.get(i));
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Do you want next Client's?  Y/N");
        String next = br.readLine();
        if(next.equals("Y") & count2+5<=t.size()){
            listAllClients(count+5,count2+5);
        }
    }
    //Uprava klienta
    private void editAClient() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter a client's id:");
        try {
            int customerId = 0;
            customerId = Integer.parseInt(br.readLine());
            Client c = null;
            c = Client_finder.getInstance().findById(customerId);
            if (c == null) {
                System.out.println("No such client exists");
            } else {
                Client_printer.getInstance().print(c);
                try {
                    System.out.println("Enter first name:"); c.setFirst_name(br.readLine());
                    System.out.println("Enter last name:"); c.setLast_name(br.readLine());
                    System.out.println("Enter birth number:"); c.setBirth_number(Integer.valueOf(br.readLine()));
                    System.out.println("Enter email:"); String mail = br.readLine();
                    if(isValid(mail)) {  c.setEmail(mail); }
                    else{ throw new Exception("wrong email"); }
                    add_login(br,c);
                    add_password(br,c);
                    c.update();
                    System.out.println("The client has been successfully updated");

                } catch (Exception e) {
                    System.out.println("Wrong type your data!");
                }

            }
        }catch(Exception e){
            System.out.println("Wrong type your data!");
        }
    }

    private void add_login(BufferedReader br, Client c) throws Exception {
        System.out.println("Enter login:");
        String a =br.readLine();
        if(a.length() != 0){
            c.setLogin(a);}
        else{throw new Exception("wrong login");}
    }

    private void add_password(BufferedReader br, Client c) throws Exception {
        System.out.println("Enter password:");
        String b =br.readLine();
        if(b.length() != 0){
            c.setPassword(b);}
        else{throw new Exception("wrong password");}
    }
    // Ukaze Klientove peniaze podla jeho id
    private void show_money() throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter a client's login:");
        int customerId = Integer.parseInt(br.readLine());
        Client c = Client_finder.getInstance().findById(customerId);
        if (c == null) {
            System.out.println("No such client exists");
        } else {
            System.out.println("your money value:");
            Accounts a = new Accounts();
            System.out.println(a.find_by_id(c.getAccount_id()));
        }
    }

    //Vklad na ucet klienta
    private void insertMoney() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("Enter a client's id:");
            int customerId = Integer.parseInt(br.readLine());
            System.out.println("Enter your money value:");
            float money = Float.parseFloat(br.readLine());
            if (money < 0) {
                System.out.println("wrong money value!");
            } else {
                deposit_player_acc a = new deposit_player_acc();
                a.setClient_id(customerId);
                a.setMoney(money);
                a.insert_money();
            } }
        catch(Exception e){
            System.out.println("wrong value!");
        }
    }
    //ukaze vyhernu statistiku hraca podla id
    private void Show_win_stat() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter a client's id:");
        try {
            int customerId = Integer.parseInt(br.readLine());
            Client c = null;
            c = Client_finder.getInstance().findById(customerId);
            if (c == null) {
                System.out.println("No such client exists");
            } else {
                System.out.println("Enter date from (9999-99-99):");
                Date days = Date.valueOf(br.readLine());

                System.out.println("client statistic:");
                Win_statistic_player.show_statistic_win(days, customerId);

            }
        } catch (IOException e) {
            System.out.println("System error");
        } catch (Exception e) {
            System.out.println("Wrong data");
        }

    }



















}
