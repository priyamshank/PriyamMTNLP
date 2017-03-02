

public class Geoinfo {

	String place;
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLang() {
		return lang;
	}
	public void setLang(double lang) {
		this.lang = lang;
	}
	public boolean isValidloc() {
		return validloc;
	}
	public void setValidloc(boolean validloc) {
		this.validloc = validloc;
	}
	public boolean isAgriloc() {
		return agriloc;
	}
	public void setAgriloc(boolean agriloc) {
		this.agriloc = agriloc;
	}
	String country;
	double lat;
	double lang;
	boolean validloc;
	boolean agriloc;
	public Geoinfo(String place,String country,double lat,double lang,boolean agriloc,boolean validloc ) {
		// TODO Auto-generated constructor stub
		this.agriloc=agriloc;
		this.country=country;
		this.lang=lang;
		this.lat=lat;
		this.place=place;
		this.validloc=validloc;
	}

}
