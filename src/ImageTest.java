import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.UUID;

public class ImageTest {

	public void image(String url1,String filename) throws IOException {
		// TODO Auto-generated method stub
		String host = "10.185.190.10";
	    String port = "8080";
	    System.out.println("Using proxy: " + host + ":" + port);
	    System.setProperty("HTTP_PROXY", "10.185.190.10:8080");
	    System.setProperty("http.proxyPort", port);
	    System.setProperty("http.proxyHost", host);
		System.setProperty("HTTPS_PROXY", "10.185.190.10:8080");
		 URL url = new URL(url1);
		 InputStream in = new BufferedInputStream(url.openStream());
		 ByteArrayOutputStream out = new ByteArrayOutputStream();
		 byte[] buf = new byte[1024];
		 int n = 0;
		 while (-1!=(n=in.read(buf)))
		 {
		    out.write(buf, 0, n);
		 }
		 out.close();
		 in.close();
		 byte[] response = out.toByteArray();

		
	//	 String filename="C:\\Users\\ghfiy\\Pictures\\"+UUID.randomUUID()+".jpg";
		 FileOutputStream fos = new FileOutputStream(filename);
		 fos.write(response);
		 fos.close();
	}

}
