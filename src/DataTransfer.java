import java.io.IOException;
import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
//import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.postgresql.util.Base64;

//import java.util.Date;
import java.sql.Date;



public class DataTransfer {
 @SuppressWarnings("deprecation")
public void insertdataallincluded(String keyword,String user,String msg,String location,Date date,String userdesc) throws SQLException, IOException {
	 Connection c = MySingleTon.getInstance();
	 Statement stmt = c.createStatement();
	
	 String idcheck="select max(id) as id from cstweets";
	 ResultSet rs = stmt.executeQuery(idcheck);
	
		int maxid = 0;
		 while(rs.next()){
				maxid=rs.getInt("id");
		 }
		 String ins_data = "insert into cstweets" + "(ID,KEYWORD,TWEETCREATOR,MSG,USERLOCATION,DATE,USERDESC) values"
				+ "(?,?,?,?,?,?,?)";

		PreparedStatement preparedStatement_ins_data = c
				.prepareStatement(ins_data);
		preparedStatement_ins_data.setInt(1, maxid+1);
		preparedStatement_ins_data.setString(2, keyword);
		preparedStatement_ins_data.setString(3, user);
		preparedStatement_ins_data.setString(4, msg);
		preparedStatement_ins_data.setString(5, location);
		preparedStatement_ins_data.setDate(6, date);
		preparedStatement_ins_data.setString(7, userdesc);
		//preparedStatement_ins_data.setString(10, im);
		preparedStatement_ins_data.executeUpdate();
		//preparedStatement_ins_data.execute(ins_data);
 }
 public void insertdataagri(Long ID,String keyword,String user,String msg,String location,Date date,List countries,String locdesc,String userdesc) throws SQLException, IOException {
	 Connection c = MySingleTon.getInstance();
	 Array array = null;
	 String im=null;
	 if(!countries.isEmpty()){
	 array = c.createArrayOf("VARCHAR", countries.toArray());
	 }
//	 if(imageInByte.length>0){
//		 im=Base64.encodeBytes(imageInByte);
//	 }
	 
	 String ins_data = "insert into tweetinfocopy1" + "(ID,KEYWORD,TWEETCREATOR,MSG,USERLOCATION,DATE,COUNTRIES,LOCATIONDESC,USERDESC) values"
				+ "(?,?,?,?,?,?,?,?,?)";

		PreparedStatement preparedStatement_ins_data = c
				.prepareStatement(ins_data);
		preparedStatement_ins_data.setLong(1, ID);
		preparedStatement_ins_data.setString(2, keyword);
		preparedStatement_ins_data.setString(3, user);
		preparedStatement_ins_data.setString(4, msg);
		preparedStatement_ins_data.setString(5, location);
		preparedStatement_ins_data.setDate(6, date);
		preparedStatement_ins_data.setArray(7, array);
		preparedStatement_ins_data.setString(8, locdesc);
		preparedStatement_ins_data.setString(9, userdesc);
		//preparedStatement_ins_data.setString(10, im);
		preparedStatement_ins_data.executeUpdate();
		//preparedStatement_ins_data.execute(ins_data);
 }
 public void getcountry() throws SQLException, JSONException, IOException{
	 Connection c = MySingleTon.getInstance();
	 LocationDetection ld=new LocationDetection();
	 Statement stmt = c.createStatement();
	 String locname="";
	 long id;
//	 List<LocationDT> item=new ArrayList<>();
	 LocationDT res=new LocationDT();
	 String loc="select id,userlocation from tweetsinfoallincluded";
	 ResultSet rs = stmt.executeQuery(loc);
	 while(rs.next()){
		 locname=rs.getString("userlocation");
		 id=rs.getLong("id");
		 if(!locname.isEmpty()){
			 if(Character.isAlphabetic(locname.charAt(0)) || Character.isDigit(locname.charAt(0))){
		 res=ld.locationfinder(locname);
		 insertcountry(res,id);
		 }
	 }
 }
 }
 public void insertcountry(LocationDT dt,long id) throws SQLException, IOException{
	 Connection c = MySingleTon.getInstance();
	 String ins_data = "update tweetsinfoallincluded" + " set countries=?,locationdesc=? "
				+ " where id like '%"+id+"%'";
	 System.out.println(ins_data);
	 Array array = null;
	 List<String> countries=dt.getCountries();
	 if(countries.size()>0){
	 array = c.createArrayOf("VARCHAR", countries.toArray());
	 }
	 PreparedStatement preparedStatement_ins_data = c
				.prepareStatement(ins_data);
	 preparedStatement_ins_data.setArray(1, array);
	 preparedStatement_ins_data.setString(2, dt.getDesc());
	 preparedStatement_ins_data.executeUpdate();
 }
 public void insertuserdesc(String desc,String id) throws SQLException, IOException{
	 Connection c = MySingleTon.getInstance();
	 String ins_data = "update tweetsinfoallincluded" + " set userdesc=?"
				+ " where tweetcreator like '%"+id+"%'";
	 System.out.println(ins_data);
	 PreparedStatement preparedStatement_ins_data = c
				.prepareStatement(ins_data);
	 preparedStatement_ins_data.setString(1, desc);
	 preparedStatement_ins_data.executeUpdate();
 }
 public void insertgdata(String gplace,int id,String gcountry,double glat,double glang,boolean agriloc,boolean validloc) throws SQLException, IOException{
	 Connection c = MySingleTon.getInstance();
	 String ins_data = "update cstweets" + " set gplace=?,gcountry=?,glat=?,glang=?,agriloc=?,validloc=?"
				+ " where id ="+id;
	 System.out.println(ins_data);
	 PreparedStatement preparedStatement_ins_data = c
				.prepareStatement(ins_data);
	 preparedStatement_ins_data.setString(1, gplace);
	 preparedStatement_ins_data.setString(2, gcountry);
	 preparedStatement_ins_data.setDouble(3, glat);
	 preparedStatement_ins_data.setDouble(4, glang);
	 preparedStatement_ins_data.setBoolean(5, agriloc);
	 preparedStatement_ins_data.setBoolean(6, validloc);
	 System.out.println(preparedStatement_ins_data);
	 preparedStatement_ins_data.executeUpdate();
 }
 public void insertdatanewtable(Long ID,String keyword,String user,String msg,String location,Date date,List countries,String locdesc,String userdesc) throws SQLException, IOException {
	 Connection c = MySingleTon.getInstance();
	 Array array = null;
	 String im=null;
	 if(!countries.isEmpty()){
	 array = c.createArrayOf("VARCHAR", countries.toArray());
	 }
//	 if(imageInByte.length>0){
//		 im=Base64.encodeBytes(imageInByte);
//	 }
	 
	 String ins_data = "insert into newkeywordlisttweets" + "(ID,KEYWORD,TWEETCREATOR,MSG,USERLOCATION,DATE,COUNTRIES,LOCATIONDESC,USERDESC) values"
				+ "(?,?,?,?,?,?,?,?,?)";

		PreparedStatement preparedStatement_ins_data = c
				.prepareStatement(ins_data);
		preparedStatement_ins_data.setLong(1, ID);
		preparedStatement_ins_data.setString(2, keyword);
		preparedStatement_ins_data.setString(3, user);
		preparedStatement_ins_data.setString(4, msg);
		preparedStatement_ins_data.setString(5, location);
		preparedStatement_ins_data.setDate(6, date);
		preparedStatement_ins_data.setArray(7, array);
		preparedStatement_ins_data.setString(8, locdesc);
		preparedStatement_ins_data.setString(9, userdesc);
		//preparedStatement_ins_data.setString(10, im);
		preparedStatement_ins_data.executeUpdate();
		//preparedStatement_ins_data.execute(ins_data);
 }
 public void insertdatafromtext(int ID,String keyword,String user,String msg,String location,Date date,String gcountry,String userdesc,String gplace,double glat,double glang) throws SQLException, IOException {
	 Connection c = MySingleTon.getInstance();
	
	 String ins_data = "insert into oldtweet" + "(ID,KEYWORD,TWEETCREATOR,MSG,USERLOCATION,DATE,GCOUNTRY,USERDESC,GPLACE,GLAT,GLANG) values"
				+ "(?,?,?,?,?,?,?,?,?,?,?)";

		PreparedStatement preparedStatement_ins_data = c
				.prepareStatement(ins_data);
		preparedStatement_ins_data.setInt(1, ID);
		preparedStatement_ins_data.setString(2, keyword);
		preparedStatement_ins_data.setString(3, user);
		preparedStatement_ins_data.setString(4, msg);
		preparedStatement_ins_data.setString(5, location);
		preparedStatement_ins_data.setDate(6, date);
		preparedStatement_ins_data.setString(7, gcountry);
		preparedStatement_ins_data.setString(8, userdesc);
		preparedStatement_ins_data.setString(9, gplace);
		preparedStatement_ins_data.setDouble(10, glat);
		preparedStatement_ins_data.setDouble(11, glang);

		preparedStatement_ins_data.executeUpdate();
		//preparedStatement_ins_data.execute(ins_data);
 }
}
