import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;


public class OwlParser {

	public static void main(String args[]){
	String filename="";
    Model model=ModelFactory.createDefaultModel();
    OntModel model1=ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);

    try
    {
        File file=new File("agrovoc_wb_20110223_20110413_useOWLModelAllTripleStoresWriter.owl");
        FileInputStream reader=new FileInputStream(file);
        model.read(reader,null);

//        String query1=" PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX my: <http://www.semanticweb.org/rohit/ontologies/2014/4/untitled-ontology-10#> SELECT  ?ind WHERE { ?ind rdf:type my:Student .}";
//        xmlns="http://aims.fao.org/aos/common/"
//        	     xml:base="http://aims.fao.org/aos/common/"
//        	     xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#"
//        	     xmlns:xsd="http://www.w3.org/2001/XMLSchema#"
//        	     xmlns:owl="http://www.w3.org/2002/07/owl#"
//        	     xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
//        	     xmlns:agrovoc="http://aims.fao.org/aos/agrovoc/">
//        Query query=QueryFactory.create(query1);
//        QueryExecution exe=QueryExecutionFactory.create(query, model1);
//        ResultSet RES=exe.execSelect();
//        ResultSetFormatter.out(System.out, RES, query);
    }catch(Exception e)
    {
        e.printStackTrace();
    }
}
		
	public boolean containsIgnoreCase( String haystack, String needle ) {
		  if(needle.equals(""))
		    return true;
		  if(haystack == null || needle == null || haystack .equals(""))
		    return false; 

		  Pattern p = Pattern.compile(needle,Pattern.CASE_INSENSITIVE+Pattern.LITERAL);
		  Matcher m = p.matcher(haystack);
		  return m.find();
		}
	public boolean isSubstring(String string1, String string2) {
		string1=string1.toLowerCase();
		string2=string2.toLowerCase();
        char c;
        char d;
        boolean match = true;
        for (int i = 0; i < string1.length(); i++) {
            c = string1.charAt(i);
            for (int j = 0; i < string2.length(); i++) {
                d = string2.charAt(i);
                if (c == d) {
                    match = true;
                } else {
                    match = false;
                }   
            }
        }
        return match;
    }
		
}
