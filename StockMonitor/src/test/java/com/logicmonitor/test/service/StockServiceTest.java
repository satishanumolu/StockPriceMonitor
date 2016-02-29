package com.logicmonitor.test.service;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.logicmonitor.dao.StockDao;
import com.logicmonitor.entity.StockData;
import com.logicmonitor.exception.StockAlreadyExistsException;
import com.logicmonitor.exception.StockNotFoundException;
import com.logicmonitor.service.StockService;
import com.logicmonitor.service.StockServiceImpl;
import com.logicmonitor.test.TestConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={TestConfig.class})
public class StockServiceTest {
	
	@Mock
	private StockDao dao;
	
	@InjectMocks
	private StockService service = new StockServiceImpl();
	
	private StockData stockData;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		stockData = new StockData();
		stockData.setSymbol("test");
		stockData.setName("test");
		stockData.setPrice(new BigDecimal(0));
		stockData.setChangeValue(new BigDecimal(0));
		stockData.setChangePercent(new BigDecimal(0));
		stockData.setVolume(0);
		stockData.setOpen(new BigDecimal(0));
		stockData.setPrevClose(new BigDecimal(0));
		stockData.setDayHigh(new BigDecimal(0));
		stockData.setDayLow(new BigDecimal(0));
	}
	
	@Test
	public void testFindAllStocks(){
		service.findAllStocks();
		Mockito.verify(dao,Mockito.atLeast(2)).findAllStocks();
	}
	
	@Test(expected=StockAlreadyExistsException.class)
	public void testAddStockException() throws StockAlreadyExistsException, StockNotFoundException {
		Mockito.when(dao.findStockBySymbol(stockData.getSymbol())).thenReturn(stockData);
		service.addStock(stockData.getSymbol());
	}
	
	@Test(expected=StockNotFoundException.class)
	public void testAddStockNotFoundException() throws StockAlreadyExistsException, StockNotFoundException {
		Mockito.when(dao.findStockBySymbol(stockData.getSymbol())).thenReturn(null);
		service.addStock(stockData.getSymbol());
	}
	
	@Test
	public void testDeleteStock() throws StockNotFoundException {
		Mockito.when(dao.findStockBySymbol(stockData.getSymbol())).thenReturn(stockData);
		service.deleteStock(stockData.getSymbol());
		Mockito.verify(dao).deleteStock(stockData.getSymbol());
	}
	
	@Test(expected=StockNotFoundException.class)
	public void testDeleteStockException() throws StockNotFoundException {
		Mockito.when(dao.findStockBySymbol(stockData.getSymbol())).thenReturn(null);
		service.deleteStock(stockData.getSymbol());
	}
	
}
