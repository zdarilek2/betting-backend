import java.sql.*;

import static java.util.Calendar.getInstance;

public class Client {
    private Integer id;
    private String first_name;
    private String last_name;
    private Integer birth_number;
    private String email;
    private String login;
    private String password;
    private Integer account_id;

    public Integer getId(){ return id;}
    public void setId(Integer id){this.id=id;}

    public String getFirst_name(){ return first_name;}
    public void setFirst_name(String first_name){this.first_name=first_name;}

    public String getLast_name(){return last_name;}
    public void setLast_name(String last_name){this.last_name= last_name;}

    public Integer getBirth_number(){return birth_number;}
    public void setBirth_number(Integer birth_number){this.birth_number = birth_number;}

    public String getEmail(){return email;}
    public void setEmail(String email){this.email=email;}

    public String getPassword(){return password;}
    public void setPassword(String password){this.password=password;}

    public String getlogin(){return login;}
    public void setLogin(String login){this.login=login;}

    public Integer getAccount_id(){return account_id;}
    public void setAccount_id(Integer Account_id){this.account_id=Account_id;}

    public void insert() throws SQLException {
        Accounts a = new Accounts();
        a.setMoney((float) 0);
        int ac_id = a.insert();
        DBAdapter db = new DBAdapter();
        db.connect();
        try(PreparedStatement s = db.conn.prepareStatement("Insert INTO Client(first_name,last_name,birth_number,email,login, password, account_id)"+
                "Values(?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS)){
            s.setString(1,first_name);
            s.setString(2,last_name);
            s.setInt(3,birth_number);
            s.setString(4,email);
            s.setString(5,login);
            s.setString(6,password);
            s.setInt(7,ac_id);
            s.executeUpdate();

            try(ResultSet r = s.getGeneratedKeys()){
                r.next();
                id = r.getInt(1);
            }
            System.out.println(id);
            db.disconnect();
        }
    }



    public void update() throws SQLException {
        if (id == null) {
            throw new IllegalStateException("id is not set");
        }
        DBAdapter db = new DBAdapter();
        db.connect();
        try(PreparedStatement s = db.conn.prepareStatement("UPDATE Client SET first_name = ?, last_name = ?, birth_number = ?, email = ?,login = ?, password = ? WHERE id = ?")) {
            s.setString(1, first_name);
            s.setString(2, last_name);
            s.setInt(3, birth_number);
            s.setString(4,email);
            s.setString(5,login);
            s.setString(6,password);
            s.setInt(7, id);
            s.executeUpdate();
        }
        db.disconnect();
    }




}
