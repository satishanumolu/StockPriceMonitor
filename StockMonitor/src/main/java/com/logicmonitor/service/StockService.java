package com.logicmonitor.service;

import java.util.List;

import yahoofinance.histquotes.HistoricalQuote;

import com.logicmonitor.entity.StockData;
import com.logicmonitor.exception.StockAlreadyExistsException;
import com.logicmonitor.exception.StockNotFoundException;

public interface StockService {
	
	public List<StockData> findAllStocks();
	
	public void addStock(String symbol) throws StockAlreadyExistsException;
	
	public void deleteStock(String symbol) throws StockNotFoundException;
	
	public List<HistoricalQuote> getHistory(String symbol);

}
