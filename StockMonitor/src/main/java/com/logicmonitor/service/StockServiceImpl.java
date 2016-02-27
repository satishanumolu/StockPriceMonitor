package com.logicmonitor.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;

import com.logicmonitor.dao.StockDao;
import com.logicmonitor.entity.StockData;
import com.logicmonitor.exception.StockAlreadyExistsException;
import com.logicmonitor.exception.StockNotFoundException;

@Service
public class StockServiceImpl implements StockService{

	@Autowired
	private StockDao dao;
	
	@Override
	public List<StockData> findAllStocks() {
		
		return dao.findAllStocks();
	}

	@Override
	public void addStock(String symbol) throws StockAlreadyExistsException {
		StockData existing =  dao.findStockBySymbol(symbol);
		//System.out.println("Name is ->"+existing.getName());
		if(existing == null) {
			
			StockData stock = new StockData();
			try
			{
				
				Stock stock1 = YahooFinance.get(symbol);
				
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
			}
			catch(IOException ioe)
			{
				ioe.printStackTrace();
			}
			dao.addStock(stock);
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
	public List<HistoricalQuote> getHistory(String symbol) {
		
		List<HistoricalQuote> l = null;
		Stock s;
		try {
			s = YahooFinance.get(symbol);
			l = s.getHistory();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return l;
	}

	

}
