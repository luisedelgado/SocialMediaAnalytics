package controllers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.microsoft.sqlserver.jdbc.SQLServerDriver;

/**
 * Servlet implementation class DataGetter
 */
@WebServlet("/DataGetter")
public class DataGetter extends HttpServlet 
{
	static enum Drivers 
	{
		MICROSOFT(
				SQLServerDriver.class.getName(),
				"jdbc:sqlserver://dadbdevdb8001;databaseName=SocialMediaAnalytics"), MICROSOFT_INTEG(
						SQLServerDriver.class.getName(),"jdbc:sqlserver://dadbdevdb8001;databaseName=SocialMediaAnalytics;"
								+ "user="+dataProps.get("username")+";password="+dataProps.get("password"));

		private String driverClass;
		private String driverURL;

		private Drivers(String driverClass, String driverURL) 
		{
			this.driverClass = driverClass;
			this.driverURL = driverURL;
		}

		public String getDriverClass() 
		{
			return driverClass;
		}

		public String getDriverURL()
		{
			return driverURL;
		}
	}
	
	private static TreeMap<String, String> dataProps;
	private static final long serialVersionUID = 1L;
       
    public DataGetter() 
    {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
    	dataProps = loadProperties();
    	
    	try
		{
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			Connection con = DriverManager.getConnection(Drivers.MICROSOFT_INTEG.driverURL);
			
			PreparedStatement ps = con.prepareStatement("SELECT DISTINCT Category FROM RawData");
			ResultSet rs = ps.executeQuery();
			
			StringBuilder builder = new StringBuilder();
			int numberOfRows = 0;
			
			while(rs.next())
			{
				String category = rs.getString(1);
				
				PreparedStatement getCount = con.prepareStatement("SELECT SUM(\"Favorites_Count\"), SUM(\"Retweet_Count\") "
						+ "FROM RawData where Category = ?");
				getCount.setString(1, category);
				
				ResultSet rsGetCount = getCount.executeQuery();
				
				if(rsGetCount.next())
				{
					if(numberOfRows > 0)
					{
						builder.append(",");
					}
				
					builder.append("[\""+category+"\",\""+rsGetCount.getString(1)+"\",\""+rsGetCount.getString(2)+"\"]");
					++numberOfRows;
				}
			}

			response.getWriter().write("[["+numberOfRows+"],"+builder.toString()+"]"); 
			con.close();
		}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
	}
	
	private TreeMap<String,String> loadProperties() 
	{
		TreeMap<String,String> _dataProps = new TreeMap<String,String>();

		try 
		{
			URL url = DataGetter.class.getResource("credentials.xml");
			File file = new File(url.getPath());
			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(file);
			doc.getDocumentElement().normalize();
 			NodeList nodeLst = doc.getElementsByTagName("property");
 
			for (int s = 0; s < nodeLst.getLength(); s++) 
			{
				Node fstNode = nodeLst.item(s);
				if (fstNode.getNodeType() == Node.ELEMENT_NODE) 
				{
					Element fstElmnt = (Element) fstNode;
 					
					NodeList usernameList = fstElmnt.getElementsByTagName("username");
					Element usernameElem = (Element) usernameList.item(0);
					NodeList username = usernameElem.getChildNodes();
					_dataProps.put("username", ((Node) username.item(0)).getNodeValue());
					
					NodeList passwordList = fstElmnt.getElementsByTagName("password");
					Element passwordElem = (Element) passwordList.item(0);
					NodeList password = passwordElem.getChildNodes();
					_dataProps.put("password", ((Node) password.item(0)).getNodeValue());
				}
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		return _dataProps;
	}

}
