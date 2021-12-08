package BD;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Exceptions.ImprevuDBError;

public class JEUTEST {

	
	public static void main(String[] args) throws SQLException, ImprevuDBError {
		//BD.identifier("camarche","vraiment");
		BD.inscrire("jkl", "mno");
		//System.out.println(Pattern.compile("[a-zA-Z0-9]{1,30}").matcher("a").matches());
		
	}
}