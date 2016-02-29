package com.logicmonitor.test.controller;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.logicmonitor.controller.StockController;
import com.logicmonitor.entity.StockData;
import com.logicmonitor.service.StockService;
import com.logicmonitor.test.TestConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={TestConfig.class})
public class StockControllerTest {

		//mock the dependencies of the controller
		@Mock
		private StockService service;

		//controller that needs to be tested
		@InjectMocks
		private StockController controller;
		
		private MockMvc mockMvc;

		private StockData stock;

		@Before
		public void setup() {
			//init mockito based mocks
			MockitoAnnotations.initMocks(this);
			
			stock = new StockData();
			stock.setSymbol("test");
			stock.setName("test");
			stock.setPrice(new BigDecimal(0));
			stock.setChangeValue(new BigDecimal(0));
			stock.setChangePercent(new BigDecimal(0));
			stock.setVolume(0);
			stock.setOpen(new BigDecimal(0));
			stock.setPrevClose(new BigDecimal(0));
			stock.setDayHigh(new BigDecimal(0));
			stock.setDayLow(new BigDecimal(0));
			
			mockMvc = MockMvcBuilders
						.standaloneSetup(controller)
						.build();
		}
		
		@Test
		public void testFindAllStocks() throws Exception {
			mockMvc.perform(MockMvcRequestBuilders.get("/stocks"))
			.andExpect(MockMvcResultMatchers.status().isOk());

			Mockito.verify(service).findAllStocks();
		}
		
		@Test
		public void testCreateStock() throws Exception {
			
			mockMvc.perform(MockMvcRequestBuilders.post("/stocks").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(stock.getSymbol()))
					.andExpect(MockMvcResultMatchers.status().isOk());
			
			Mockito.verify(service).addStock(stock.getSymbol());
		}
		
			
		@Test
		public void testDeleteStock() throws Exception {
			
			mockMvc.perform(MockMvcRequestBuilders.delete("/stocks/" + stock.getSymbol()))
			.andExpect(MockMvcResultMatchers.status().isOk());
			
			Mockito.verify(service).deleteStock(stock.getSymbol());
					
		}
		
		@Test
		public void testGetHistory() throws Exception {
		
			mockMvc.perform(MockMvcRequestBuilders.get("/stocks/" + stock.getSymbol()))
			.andExpect(MockMvcResultMatchers.status().isOk());
			
			Mockito.verify(service).getHistory(stock.getSymbol());
		}
		
}
