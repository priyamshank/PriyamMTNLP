import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import org.apache.commons.lang3.StringUtils;

public class OccupationCheck {
	public static void main(String[] args) throws SQLException, IOException{
		Connection conn = MySingleTon.getInstance();
		Statement stmt = conn.createStatement();
		Map<String,String> user=new HashMap<>();
		Map<String,String> agriusers=new HashMap<>();
		String uq="select id,userdesc from tweetinfocopy1";
		ResultSet uqrs=stmt.executeQuery(uq);
	String userdesc="";
	String id="";
	String desc,ID="";
	while(uqrs.next()){
	userdesc=uqrs.getString("userdesc");
	id=uqrs.getString("id");
	user.put(id, userdesc);
	}
	try (Scanner sc = new Scanner(new BufferedReader(
            new FileReader("Occupation")))) {
		
		while (sc.hasNextLine()) {
            String line = sc.nextLine(); 
            for (Map.Entry<String, String> entry : user.entrySet())
            {
            	
            	if(StringUtils.containsIgnoreCase(entry.getValue(),line.trim())){
            			desc=entry.getValue();
            			ID=entry.getKey();
            			//System.out.println(line + "-----------------"+entry.getValue());
            			//System.out.println(line);
                		agriusers.put(ID,desc);             		
            }

        }
	
	
	}
	}
	insertagriuserboolean(agriusers);
	for (Map.Entry<String, String> entry : agriusers.entrySet())
    {
		System.out.println(entry.getKey() + "------------------" + entry.getValue());
    }
}
	 public static void insertagriuserboolean(Map<String,String> agriusers) throws SQLException, IOException{
		 Connection c = MySingleTon.getInstance();
		 Boolean y = true;
		 for (Map.Entry<String, String> entry : agriusers.entrySet())
		    {
				
		 String ins_data = "update tweetinfocopy1" + " set agriuser=?"
					+ " where id like '%"+entry.getKey()+"%'";
		 System.out.println(ins_data);
		 PreparedStatement preparedStatement_ins_data = c
					.prepareStatement(ins_data);
		 preparedStatement_ins_data.setBoolean(1, y);
		 preparedStatement_ins_data.executeUpdate();
	 }
}
}
