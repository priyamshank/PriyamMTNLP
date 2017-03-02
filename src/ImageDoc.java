	import java.io.*;
	import java.io.FileInputStream;
	import java.io.FileOutputStream;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

	public class ImageDoc 
	{
	    public static void main(String[] args) throws IOException, InvalidFormatException 
	    {
//	        XWPFDocument docx = new XWPFDocument();
//	        XWPFParagraph par = docx.createParagraph();
//	        XWPFRun run = par.createRun();
//	        run.setText("Hello, World. This is my first java generated docx-file. Have fun.");
//	        run.setFontSize(13);
//	        InputStream pic = new FileInputStream("C:\\Users\\Public\\Pictures\\Sample Pictures\\Desert.jpg");
//	        
//	        //byte [] picbytes = IOUtils.toByteArray(pic);
//	        //run.addPicture(picbytes, Document.PICTURE_TYPE_JPEG);
//	        run.addPicture(pic, Document.PICTURE_TYPE_JPEG, "3", 0, 0);
//	        FileOutputStream out = new FileOutputStream("C:\\Users\\Public\\Pictures\\Sample Pictures\\finallyhurray.docx"); 
//	        docx.write(out); 
//	        out.close(); 
//	        pic.close();
	    	  XWPFDocument doc = new XWPFDocument();
	          XWPFParagraph p = doc.createParagraph();
	          XWPFRun xwpfRun = p.createRun();
	          String[] IMageargs={
	                  "C:\\Users\\Public\\Pictures\\Sample Pictures\\Desert.jpg"
	          };
	          for (String imgFile : IMageargs) {
	              int format=XWPFDocument.PICTURE_TYPE_JPEG;
	              xwpfRun.setText(imgFile);
	              xwpfRun.addBreak();
	              xwpfRun.addPicture (new FileInputStream(imgFile), format, imgFile, Units.toEMU(200), Units.toEMU(200)); // 200x200 pixels
	              //xwpfRun.addBreak(BreakType.PAGE);
	          }
	          FileOutputStream out = new FileOutputStream("C:\\test.docx");
	          doc.write(out);
	          out.close();
	    }
	}

