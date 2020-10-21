/**
 * Assignment 2: Java regular expressions <br />
 * Test cookies using regular expressions
 */
import java.util.regex.*;

public class CookieTest {

    /**
     * Verify a cookie and return the verification result
     * @param cookie        {@code String}  The cookie string
     * @return              {@code boolean} True for a legal cookie; false for an illegal one
     */
    public static boolean verifyCookie(String cookie) {
        boolean legal = true;
        boolean ilegal = false;
        // TODO: Assignment 2 -- compose your regular expression, and use it to verify the cookie
        //char[] test = cookie.toCharArray();
		//Header
		String header = "(Set-Cookie:\\s)";
		// name and value
		String name = "([\\w[^\\p{Cntrl}\\(\\)\\<\\>@,;:\"/\\[\\]\\?=\\{\\}\\s\\t\\\\]]{1,})[=]"; //correct
		String value = "([^\\p{Cntrl}\\s,;\\\\])*"; //correct
		String nameValue = "("+header+name+value+")";
		//Domain
		String letter = "[a-zA-z]";
		String digit = "[\\d]";
		String letdig = "("+letter+"|"+digit+")";
		String hyp = "[-]";
		String letdighyp = "(" + letter +"|"+ digit + "|" + hyp + ")*";
		String ldhstr = "(" + letdighyp + ")+";
		String label = letter +"(("+ldhstr+")?"+letdig+")?";
		String subDomain = "(\\.?"+label+")*(\\."+ label +")";
		String domain = ";\\s(?:Domain=("+subDomain+")?)";
		// Expire date
		String month = "((Jan)|(Feb)|(Mar)|(Apr)|(May)|(Jun)|(Jul)|(Aug)|(Sep)|(Oct)|(Nov)|(Dec))";
		String wkday = "((Mon)|(Tue)|(Wed)|(Thu)|(Fri)|(Sat)|(Sun))";		
		String weekday = "((Monday)|(Tuesday)|(Wednesday)|(Thursday)|(Friday)|(Saturday)|(Sunday))";	
		String time = "[\\d]{2}:[\\d]{2}:[\\d]{2}"; // correct
		String date1 = "[\\d]{2}\\s"+month+"\\s[\\d]{4}";	
		String date2 = "[\\d]{2}[-]"+month+"[-][\\d]{2}";		
		String date3 = month+"\\s([[\\d]{2}[\\s[\\d]]])";
		String rfc1123date = "("+wkday+",\\s"+date1+"\\s"+time+"\\sGMT"+")"; // This is for test
		String rfc850date = "("+weekday+",\\s"+date2+",\\s"+time+"\\s\"GMT\""+"){1}";
		String asctimedate = "("+wkday + "\\s" + date3 + "\\s" +time + "\\s[\\d]{4}"+"){1}";
		String expire = "Expires[=]";
		String expireDate1 = "("+rfc1123date+")|";
		String expireDate2 = "("+rfc850date+")|";
		String expireDate3 = "("+asctimedate+")";
		String expireDate = "("+expireDate1+expireDate2+expireDate3+")"; // correct
		String maxAge = ";\\s(?:(;\\sMax[-]Age=)([0-9][\\d]*))"; //  modified
		// Path,Secure,HttpOnly,Extension
		String path = ";\\s(?:(Path=)([^\\p{Cntrl};]+))";//correct
		String httpOnly = ";\\s(?=(?i)httponly)(HttpOnly)";//correct
		String expireav = ";\\s(?:"+expire+"(?="+expireDate+")"+expireDate+")"; // correct

		String[] checklist = {nameValue,domain,expireav,path,httpOnly,maxAge};
		
		Pattern regex;
		Matcher match;
		
		for(int i = 0;i<checklist.length;i++)
		{
			regex = Pattern.compile(checklist[i]);
			match = regex.matcher(cookie);
			while(match.find())
			{
				if(match.group().length() != 1)
				{
					String temp = match.group();
					cookie = cookie.replace(temp,"");
					
				}
			}
			if(cookie.length() == 0)
			{
				return legal;
			}
		}
		

        return ilegal;
    }
  

    /**
     * Main entry
     * @param args          {@code String[]} Command line arguments
     */
    public static void main(String[] args) {
        String [] cookies = {
            // Legal cookies:
        	
            "Set-Cookie: ns1=\"alss/0.foobar^\"",                                           // 01 name=value
            "Set-Cookie: ns1=",                                                             // 02 empty value
            "Set-Cookie: ns1=\"alss/0.foobar^\"; Expires=Tue, 18 Nov 2008 16:35:39 GMT",    // 03 Expires=time_stamp
            "Set-Cookie: ns1=; Domain=",                                                    // 04 empty domain
            "Set-Cookie: ns1=; Domain=.srv.a.com-0",                                        // 05 Domain=host_name
            
            "Set-Cookie: lu=Rg3v; Expires=Tue, 18 Nov 2008 16:35:39 GMT; Path=/; Domain=.example.com; HttpOnly", // 06
            // Illegal cookies:
            
            "Set-Cookie:",                                              // 07 empty cookie-pair
            "Set-Cookie: sd",                                           // 08 illegal cookie-pair: no "="
            "Set-Cookie: =alss/0.foobar^",                              // 09 illegal cookie-pair: empty name
            "Set-Cookie: ns@1=alss/0.foobar^",                          // 10 illegal cookie-pair: illegal name
            "Set-Cookie: ns1=alss/0.foobar^;",                          // 11 trailing ";"
            "Set-Cookie: ns1=; Expires=Tue 18 Nov 2008 16:35:39 GMT",   // 12 illegal Expires value
            "Set-Cookie: ns1=alss/0.foobar^; Max-Age=01",               // 13 illegal Max-Age: starting 0
            "Set-Cookie: ns1=alss/0.foobar^; Domain=.0com",             // 14 illegal Domain: starting 0
            "Set-Cookie: ns1=alss/0.foobar^; Domain=.com-",             // 15 illegal Domain: trailing non-letter-digit
            "Set-Cookie: ns1=alss/0.foobar^; Path=",                    // 16 illegal Path: empty
            "Set-Cookie: ns1=alss/0.foobar^; httponly",                 // 17 lower case
            
            
        };
        for (int i = 0; i < cookies.length; i++) 	
            System.out.println(String.format("Cookie %2d: %s", i+1, verifyCookie(cookies[i]) ? "Legal" : "Illegal"));
    }

}

	
	