import java.io.IOException;

public class Main {

    public static void main(String[] args) throws Exception {
	// write your code here
        //Impl_tables dbadapter = new Impl_tables();
        //dbadapter.connect();
        //dbadapter.create_table();
        //dbadapter.insert_values();
        // dbadapter.disconnect();
        MainMenu mainMenu = new MainMenu();
        try {
            mainMenu.run();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    }

