package com.logicmonitor;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import com.logicmonitor.util.DBUtils;

/**
 * Java Program to create and insert stock details in to MySQL database
 *
 */
public class StockQuoteDump 
{
    public static void main( String[] args )
    {
        Connection con = DBUtils.connect();
		Statement stmt = null;
		String sql = null;
		String[] stockArr = new String[]{"AMZN","BAC","HPQ","IBN","C","FB","AAPL","GRPN","MSFT","EBAY"};//Few initial stock details
		Stock stock;
		
		try {
			
			stmt = con.createStatement();
		      
		    sql = "CREATE TABLE StockData " +
		                   "(symbol VARCHAR(255) not NULL, " +
		                   " name VARCHAR(255), " +
		                   " price DECIMAL(10,2), " +
		                   " changeValue DECIMAL(6,2), " + 
		                   " changePercent DECIMAL(6,2), " + 
		                   " volume BIGINT(20), " +
		                   " prevClose DECIMAL(10,2), " +
		                   " open DECIMAL(10,2), " +
		                   " dayHigh DECIMAL(10,2), " +
		                   " dayLow DECIMAL(10,2), " +
		                   " PRIMARY KEY ( symbol ))"; 

		     stmt.executeUpdate(sql);
		      
		     for(int i=0;i<stockArr.length;i++)
		     {
		    	  stock = YahooFinance.get(stockArr[i]);
		    	  
		    	  String symbol = stock.getSymbol();
		          String name = stock.getName();
		          BigDecimal price = stock.getQuote().getPrice();
		          BigDecimal changeValue = stock.getQuote().getChange();
		          BigDecimal changePercent = stock.getQuote().getChangeInPercent();
		          long volume = stock.getQuote().getVolume();
		          BigDecimal prevClose = stock.getQuote().getPreviousClose();
		          BigDecimal open = stock.getQuote().getOpen();
		          BigDecimal dayHigh = stock.getQuote().getDayHigh();
		          BigDecimal dayLow = stock.getQuote().getDayLow();
		          
		          sql = "insert into StockData values ('"+symbol+"','"+name+"',"+price+","+changeValue+","+changePercent+","+volume+","+prevClose+","+open+","+dayHigh+","+dayLow+")";
		          
		          stmt.executeUpdate(sql);
		     }
	      
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			System.err.println("Error in executing SQL Queries");
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
			System.err.println("Error in accessing Yahoo Finance API");
		}
		finally 
		{
		try {
				if(con!=null)
					con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
    	
    }
}
