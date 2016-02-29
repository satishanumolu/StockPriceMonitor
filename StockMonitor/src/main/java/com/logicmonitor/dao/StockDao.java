package com.logicmonitor.dao;

import java.util.List;

import com.logicmonitor.entity.StockData;

public interface StockDao {
	
	public List<StockData> findAllStocks();
	
	public StockData findStockBySymbol(String symbol);
	
	public void addStock(StockData stock);
	
	public void updateStock(StockData stock);
	
	public void deleteStock(String symbol);

}
