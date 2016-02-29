package com.logicmonitor.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBUtils {

	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.err.println("Error in loading JDBC Driver");
		}
	}
	
	public static Properties loadPropertiesFile() throws IOException {
		 
		 Properties prop = new Properties();
		 InputStream in = new FileInputStream("src/main/resources/mysql.properties");
		 prop.load(in);
		 in.close();
		 return prop;
	}
	
	public static Connection connect () {
		Connection con = null;
		
		try {
			
			Properties prop = loadPropertiesFile();			 
			String url = prop.getProperty("DB.url");
			String username = prop.getProperty("DB.username");
			String password = prop.getProperty("DB.password");
			con = DriverManager.getConnection(url, username, password);
			
		}catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Error in getting JDBC Connection");
		} catch (IOException ioe) {
			ioe.printStackTrace();
			System.err.println("Error in accessing the properties file");
		}
		
		return con;
	}
}
