package com.logicmonitor.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;

import com.logicmonitor.dao.StockDao;
import com.logicmonitor.entity.QuoteHistory;
import com.logicmonitor.entity.StockData;
import com.logicmonitor.exception.StockAlreadyExistsException;
import com.logicmonitor.exception.StockNotFoundException;

@Service
public class StockServiceImpl implements StockService{

	@Autowired
	private StockDao dao;
	
	@Override
	public List<StockData> findAllStocks() {
		
		List<StockData> stocks = dao.findAllStocks();
		
		for(StockData s : stocks)
		{
			try {
				Stock stock = YahooFinance.get(s.getSymbol());
				s.setSymbol(stock.getSymbol());
				s.setName(stock.getName());
				s.setPrice(stock.getQuote().getPrice());
				s.setChangeValue(stock.getQuote().getChange());
				s.setChangePercent(stock.getQuote().getChangeInPercent());
				s.setVolume(stock.getQuote().getVolume());
				s.setPrevClose(stock.getQuote().getPreviousClose());
				s.setOpen(stock.getQuote().getOpen());
				s.setDayHigh(stock.getQuote().getDayHigh());
				s.setDayLow(stock.getQuote().getDayLow());
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			dao.updateStock(s);
		}
		
		return dao.findAllStocks();
	}

	@Override
	public void addStock(String symbol) throws StockAlreadyExistsException, StockNotFoundException {
		StockData existing =  dao.findStockBySymbol(symbol);
		//System.out.println("Name is ->"+existing.getName());
		if(existing == null) {
			
			StockData stock = new StockData();
			try
			{
				
				Stock stock1 = YahooFinance.get(symbol);
				
				if(stock1.getName().equalsIgnoreCase("N/A"))
				{
					throw new StockNotFoundException();
				}
				else
				{
				stock.setSymbol(stock1.getSymbol());
				stock.setName(stock1.getName());
				stock.setPrice(stock1.getQuote().getPrice());
				stock.setChangeValue(stock1.getQuote().getChange());
				stock.setChangePercent(stock1.getQuote().getChangeInPercent());
				stock.setVolume(stock1.getQuote().getVolume());
				stock.setPrevClose(stock1.getQuote().getPreviousClose());
				stock.setOpen(stock1.getQuote().getOpen());
				stock.setDayHigh(stock1.getQuote().getDayHigh());
				stock.setDayLow(stock1.getQuote().getDayLow());
				dao.addStock(stock);
				}
			}
			catch(IOException ioe)
			{
				ioe.printStackTrace();
			}
			
		}
		else {
			throw new StockAlreadyExistsException();
		}
	}

	@Override
	public void deleteStock(String symbol) throws StockNotFoundException {
		StockData existing =  dao.findStockBySymbol(symbol);
		if(existing == null) {
			throw new StockNotFoundException();
		}
		else {
			dao.deleteStock(symbol);
		}
	}

	@Override
	public List<QuoteHistory> getHistory(String symbol) throws StockNotFoundException {
		
		List<HistoricalQuote> hisQuotes = null;
		List<QuoteHistory> quotes = new ArrayList<QuoteHistory>();
	
		Stock s;
		
		Calendar c = Calendar.getInstance();
		
		
		SimpleDateFormat sdf;
		
		try {
			s = YahooFinance.get(symbol);
			if(s.getName().equalsIgnoreCase("N/A"))
			{
				throw new StockNotFoundException();
			}
			
			hisQuotes = s.getHistory();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for(HistoricalQuote hq:hisQuotes)
		{
			QuoteHistory qh = new QuoteHistory();
			sdf = new SimpleDateFormat("yyyy-MM-dd");	
			
			c.setTime(hq.getDate().getTime());
			c.add(Calendar.DATE, 1);
			
			qh.setDate((sdf.format(c.getTime())));
        	qh.setOpen(hq.getOpen());
        	quotes.add(qh);
		}
		
		return quotes;
	}
	

}
