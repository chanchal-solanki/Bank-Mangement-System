import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class App {
    public static void main(String[] args)  {
        String url = "jdbc:mysql://localhost:3306/BankSystem";
        String user = "root";
        String password = "Keshav@123";
        try{
            Connection con = DriverManager.getConnection(url, user, password);
            Statement st = con.createStatement();
            String query = "SELECT * FROM Employee";
            ResultSet res = st.executeQuery(query);
            res.next();
            System.out.println(res.getString("name"));

        }
        catch(Exception e){
            e.printStackTrace();
        }

    }
}
