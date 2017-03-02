import java.util.Date;
import java.util.List;

public class TweetInfo {
	
	Long ID;
	String keyword;
	String tweetmsg;
	String createdby;
	Date createddate;
	String userlocation;
	List<String> countries;
	String locdesc;
	String userdesc; byte[] imageInByte;
	public TweetInfo(String keyword,
			String createdby,String tweetmsg,
			String userlocation,Date createddate, String userdesc
			) {
				super();
				
				this.createdby=createdby;
				this.createddate=createddate;
				this.keyword=keyword;
				this.tweetmsg=tweetmsg;
				this.userlocation=userlocation;
				this.userdesc=userdesc;
			//	this.imageInByte=imageInByte;
			}
//	public TweetInfo(Long ID,String keyword,
//	String createdby,String tweetmsg,
//	String userlocation,Date createddate,List<String> countries,
//	String locdesc, String userdesc
//	) {
//		super();
//		this.ID=ID;
//		this.createdby=createdby;
//		this.createddate=createddate;
//		this.keyword=keyword;
//		this.tweetmsg=tweetmsg;
//		this.userlocation=userlocation;
//		this.countries=countries;
//		this.locdesc=locdesc;
//		this.userdesc=userdesc;
//	//	this.imageInByte=imageInByte;
//	}
	
	public String getUserdesc() {
		return userdesc;
	}

	public void setUserdesc(String userdesc) {
		this.userdesc = userdesc;
	}

//	public byte[] getImageInByte() {
//		return imageInByte;
//	}
//
//	public void setImageInByte(byte[] imageInByte) {
//		this.imageInByte = imageInByte;
//	}

	public List<String> getCountries() {
		return countries;
	}

	public void setCountries(List<String> countries) {
		this.countries = countries;
	}

	public String getLocdesc() {
		return locdesc;
	}

	public void setLocdesc(String locdesc) {
		this.locdesc = locdesc;
	}

	public TweetInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Long getID() {
		return ID;
	}

	public void setID(Long ID) {
		this.ID = ID;
	}
	
	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getTweetmsg() {
		return tweetmsg;
	}

	public void setTweetmsg(String tweetmsg) {
		this.tweetmsg = tweetmsg;
	}

	public String getCreatedby() {
		return createdby;
	}

	public void setCreatedby(String createdby) {
		this.createdby = createdby;
	}

	public Date getCreateddate() {
		return createddate;
	}

	public void setCreateddate(Date createddate) {
		this.createddate = createddate;
	}

	public String getUserlocation() {
		return userlocation;
	}

	public void setUserlocation(String userlocation) {
		this.userlocation = userlocation;
	}

}
