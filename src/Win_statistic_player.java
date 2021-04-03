import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Win_statistic_player {

    public static void show_statistic_win(Date date, int id) throws Exception {
        List<Float> l = Tickets.find_data_stat(date, id);
        System.out.println("win percentage stat: ");
        System.out.println(100*((float)l.get(1)/(l.get(0)+l.get(1)))+ " %");
        System.out.println("your total bet money: ");
        System.out.println(l.get(2) + " eur");
        System.out.println("total profit: ");
       // System.out.println(l.get(2));
        System.out.println(l.get(3)-l.get(2) + " eur");

    }


}

