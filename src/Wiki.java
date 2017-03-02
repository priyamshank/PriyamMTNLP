import java.util.List;

import info.bliki.api.Page;
import info.bliki.api.User;
import info.bliki.wiki.model.WikiModel;

public class Wiki {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
				
		        System.setProperty("HTTP_PROXY", "10.185.190.10:8080");
		        
				String[] listOfTitleStrings = { "Web service" };
				User user = new User("", "", "http://en.wikipedia.org/w/api.php");
				
				user.login();
				List<Page> listOfPages = user.queryContent(listOfTitleStrings);
				for (Page page : listOfPages) {
				  WikiModel wikiModel = new WikiModel("${image}", "${title}");
				  String html = wikiModel.render(page.toString());
				  System.out.println(html);
				}
			}
			
				
			}
	