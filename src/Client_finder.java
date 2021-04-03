import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Client_finder {
    private static final Client_finder INSTANCE = new Client_finder();

    public static Client_finder getInstance(){return INSTANCE;}

    public List<Client> findAll() throws SQLException {
        DBAdapter db = new DBAdapter();
        db.connect();
        try (PreparedStatement s = db.conn.prepareStatement("SELECT * FROM Client")) {
            try (ResultSet r = s.executeQuery()) {

                List<Client> elements = new ArrayList<>();

                while (r.next()) {
                    Client c = new Client();

                    c.setId(r.getInt("id"));
                    c.setFirst_name(r.getString("first_name"));
                    c.setLast_name(r.getString("last_name"));
                    c.setBirth_number(r.getInt("birth_number"));
                    c.setEmail(r.getString("email"));
                    c.setLogin(r.getString("login"));
                    c.setPassword(r.getString("password"));
                    c.setAccount_id(r.getInt("account_id"));


                    elements.add(c);
                }
                db.disconnect();
                return elements;
            }
        }
    }

    public Client findById(int id) throws Exception {
        DBAdapter db = new DBAdapter();
        db.connect();
        try (PreparedStatement s = db.conn.prepareStatement("SELECT * FROM Client WHERE id=?")) {
            s.setInt(1, id);

            try (ResultSet r = s.executeQuery()) {
                if (r.next()) {
                    Client c = new Client();

                    c.setId(r.getInt("id"));
                    c.setFirst_name(r.getString("first_name"));
                    c.setLast_name(r.getString("last_name"));
                    c.setBirth_number(r.getInt("birth_number"));
                    c.setEmail(r.getString("email"));
                    c.setLogin(r.getString("login"));
                    c.setPassword(r.getString("password"));
                    c.setAccount_id(r.getInt("account_id"));

                    if (r.next()) {
                        throw new RuntimeException("Move than one row was returned");
                    }
                    db.disconnect();
                    return c;
                } else {
                    db.disconnect();
                    throw new Exception("Nothing");
                    //return null;
                }
            }
        }
    }
}
