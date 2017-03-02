import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class MySingleTon {
	private static Connection con;
	static Properties prop = new Properties();
	static InputStream input = null;
    /**
     * Create private constructor
     */
    private MySingleTon(){
         
    }
    /**
     * Create a static method to get instance.
     * @throws IOException 
     */
    public static Connection getInstance() throws IOException{
    	

        if(con == null){
        	
        	try {
        		input = new FileInputStream("Config.properties");

        		// load a properties file
        		prop.load(input);

				Class.forName(prop.getProperty("DRIVER_NAME") );
			
				 con = DriverManager.getConnection(
						 prop.getProperty("CONNECTION_URL"), prop.getProperty("DB_NAME"),
						 prop.getProperty("DB_PASSWORD"));
			} catch (SQLException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Opened database successfully");
			 return con;
        }
        return con;
    }
     
    public void getSomeThing(){
        // do something here
        System.out.println("I am here....");
    }
     
}
