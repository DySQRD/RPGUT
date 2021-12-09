package BD;
import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Exceptions.ImprevuDBError;

public class JEUTEST {
	private static final Pattern safePattern = Pattern.compile("^[a-zA-Z0-9]{1,30}$");
	
	public static void main(String[] args) throws SQLException, ImprevuDBError, IOException {
		//BD.identifier("camarche","vraiment");
		
		System.out.println(BD.entreeSafe("Dylan"));
		//System.out.println(Pattern.compile("[a-zA-Z0-9]{1,30}").matcher("a").matches());
		
	}
}