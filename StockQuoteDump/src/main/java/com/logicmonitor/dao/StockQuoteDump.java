package com.logicmonitor.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Statement;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import com.logicmonitor.util.DBUtils;

/**
 * Hello world!
 *
 */
public class StockQuoteDump 
{
    public static void main( String[] args )
    {
        Connection con = DBUtils.connect();
		Statement stmt = null;
		String sql = null;
		String[] stockArr = new String[]{"AMZN","BAC","HPQ","IBN","C","FB","AAPL","GRPN","MSFT","EBAY"};
		
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
		    	  Stock stock = YahooFinance.get(stockArr[i]);
		    	  
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
		catch(Exception e)
		{
			e.printStackTrace();
		}
    	
    }
}
