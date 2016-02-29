package com.logicmonitor.service;

import java.util.List;

import com.logicmonitor.entity.QuoteHistory;
import com.logicmonitor.entity.StockData;
import com.logicmonitor.exception.StockAlreadyExistsException;
import com.logicmonitor.exception.StockNotFoundException;

public interface StockService {
	
	public List<StockData> findAllStocks();
	
	public void addStock(String symbol) throws StockAlreadyExistsException, StockNotFoundException;
	
	public void deleteStock(String symbol) throws StockNotFoundException;
	
	public List<QuoteHistory> getHistory(String symbol) throws StockNotFoundException;

}
