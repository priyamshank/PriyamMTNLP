import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Tweetinfoprocessing {

	public void tweetinfoprint(List<TweetInfo> abc) throws IOException, SQLException {
		DataTransfer dt = new DataTransfer();

		for (TweetInfo item : abc) {

			Date tweetdate = convertJavaDateToSqlDate(item.getCreateddate());
			System.out.println(item.getKeyword() + "  " + "@" + item.getCreatedby().toString() + "      "
					+ item.getTweetmsg().toString() + "   " + item.getUserlocation() + "  " + tweetdate + " "
					+ item.getUserdesc());

			dt.insertdataallincluded(item.getKeyword(), item.getCreatedby().toString(), item.getTweetmsg().toString(),
					item.getUserlocation(), tweetdate, item.getUserdesc());
		}

	}

	public HashSet<Long> processforagriterms(List<TweetInfo> abc) throws IOException, SQLException {
		Test t = new Test();
		DataTransfer dt = new DataTransfer();
		ArrayList<String> agriterm = t.AgriconnectedTermExtraction();
		HashSet<Long> remlist = new HashSet<>();
		List<TweetInfo> inter = new ArrayList<>();
		for (TweetInfo item : abc) {
			outerloop: for (int i = 0; i < agriterm.size(); i++) {
				if (item.getTweetmsg().toLowerCase().contains(agriterm.get(i).toLowerCase())) {
					// System.out.println(agriterm.get(i)+" is present in
					// message related to " + item.getTweetmsg()+"
					// "+item.getCreatedby());
					remlist.add(item.getID());
					break outerloop;
					// TweetInfo interitem = new TweetInfo(item.getKeyword(),
					// item.getTweetmsg(), item.getCreatedby(),
					// item.getCreateddate(), item.getUserlocation());
					// inter.add(interitem);
				}
			}
		}

		return remlist;
	}

	// public void tweetinfoprint(List<TweetInfo> abc, HashSet<Long> res) throws
	// IOException, SQLException{
	// DataTransfer dt=new DataTransfer();
	//
	// for (Long agitem : res) {
	// for(TweetInfo item : abc ){
	// if(item.getID()==agitem){
	// Date tweetdate = convertJavaDateToSqlDate(item.getCreateddate());
	// System.out.println("@"+item.getCreatedby()+ " "+tweetdate+"
	// "+item.getKeyword()+" "+item.getTweetmsg()+" "+item.getUserlocation()+ "
	// " +item.getID().hashCode());
	// dt.insertdataagri(item.getID(),item.getKeyword(),item.getCreatedby().toString(),item.getTweetmsg().toString(),item.getUserlocation(),tweetdate,item.getCountries(),item.getLocdesc(),item.getUserdesc());
	// //dt.insertdata(item.getID(),item.getKeyword(),item.getCreatedby().toString(),item.getTweetmsg().toString(),item.getUserlocation(),tweetdate);
	// }
	//
	// }
	// }
	// }
	public void tweetinfoprintnormal(List<TweetInfo> abc) throws IOException, SQLException {
		DataTransfer dt = new DataTransfer();
		for (TweetInfo item : abc) {
			Date tweetdate = convertJavaDateToSqlDate(item.getCreateddate());
			System.out.println(item.getID() + "  " + item.getKeyword() + "  " + "@" + item.getCreatedby().toString()
					+ "      " + item.getTweetmsg().toString() + "   " + item.getUserlocation() + "  " + tweetdate + " "
					+ item.getCountries() + " " + item.getLocdesc() + " " + item.getUserdesc());
			dt.insertdataallincluded(item.getKeyword(), item.getCreatedby().toString(), item.getTweetmsg().toString(),
					item.getUserlocation(), tweetdate, item.getUserdesc());
		}

	}

	public List<TweetInfo> tweetuserprocessing(List<TweetInfo> abc) throws IOException {
		System.out.println("entered loop");
		BufferedReader buf = new BufferedReader(new FileReader("Keywords"));
		ArrayList<String> SEARCH_TERM = new ArrayList<>();
		String lineJustFetched = null;
		String[] wordsArray;

		while (true) {
			lineJustFetched = buf.readLine();
			if (lineJustFetched == null) {
				break;
			} else {
				wordsArray = lineJustFetched.split("\t");
				for (String each : wordsArray) {
					if (!"".equals(each)) {
						SEARCH_TERM.add(each);
					}
				}
			}
		}

		for (int i = 0; i < SEARCH_TERM.size(); i++) {
			System.out.println(SEARCH_TERM.get(i));
			for (TweetInfo item : abc) {
				// System.out.println(item.getCreatedby());
				if (item.getCreatedby().toLowerCase().contains(SEARCH_TERM.get(i).toLowerCase())) {
					System.out.println(item.getCreatedby() + "is discarded" + item.getTweetmsg());
				}
			}
		}
		return abc;
	}

	public java.sql.Date convertJavaDateToSqlDate(java.util.Date date) {
		return new java.sql.Date(date.getTime());
	}

	public static java.sql.Date convertFromJAVADateToSQLDate(java.util.Date javaDate) {
		java.sql.Date sqlDate = null;
		if (javaDate != null) {
			sqlDate = new Date(javaDate.getTime());
		}
		return sqlDate;
	}
}
