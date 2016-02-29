package com.logicmonitor.test.dao;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.junit.Assert;
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
import com.logicmonitor.dao.StockDaoImpl;
import com.logicmonitor.entity.StockData;
import com.logicmonitor.test.TestConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={TestConfig.class})
public class StockDaoTest {
	
	@Mock
	private EntityManager em;
	
	@InjectMocks
	private StockDao dao = new StockDaoImpl();
	
	@Mock
	private TypedQuery<StockData> query;
	
	@Mock
	private Query createQuery;
	
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
		List<StockData> expected = Arrays.asList(stockData);
		
		Mockito.when(em.createQuery("SELECT s FROM StockData s ORDER BY s.symbol ASC", StockData.class)).thenReturn(query);
		Mockito.when(query.getResultList()).thenReturn(expected);
		
		List<StockData> stocks = dao.findAllStocks();
		Assert.assertEquals(expected, stocks);
	}
	
	@Test
	public void testFindStockBySymbol(){
		Mockito.when(em.find(StockData.class, stockData.getSymbol())).thenReturn(stockData);
		
		StockData actual = dao.findStockBySymbol(stockData.getSymbol());
		Assert.assertEquals(stockData,actual);
	}
	
	@Test
	public void testAddStock(){
		dao.addStock(stockData);
		Mockito.verify(em).persist(stockData);
	}
	
	@Test
	public void testUpdateStock(){
		dao.updateStock(stockData);
		Mockito.verify(em).merge(stockData);
	}
	
}
