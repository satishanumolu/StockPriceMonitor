package com.logicmonitor.util;

import java.io.FileInputStream;
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
	
	public static Properties loadPropertiesFile() throws Exception {
		 
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
			String url = prop.getProperty("url");
			String username = prop.getProperty("username");
			String password = prop.getProperty("password");
			con = DriverManager.getConnection(url, username, password);
			
		}catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Error in getting JDBC Connection");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return con;
	}
}
