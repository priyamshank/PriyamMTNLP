import java.util.List;

/**
 * 
 */

/**
 * @author ghfiy
 *
 */
public class LocationDT {
	List<String> countries;
	String desc;
	public List<String> getCountries() {
		return countries;
	}
	public void setCountries(List<String> countries) {
		this.countries = countries;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public LocationDT(List countries,String desc){
		super();
		this.countries=countries;
		this.desc=desc;
	}
	public LocationDT(){
		super();
	}
}
