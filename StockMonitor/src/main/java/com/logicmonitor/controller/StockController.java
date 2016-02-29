package com.logicmonitor.controller;

import java.util.List;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.logicmonitor.entity.QuoteHistory;
import com.logicmonitor.entity.StockData;
import com.logicmonitor.exception.StockAlreadyExistsException;
import com.logicmonitor.exception.StockNotFoundException;
import com.logicmonitor.service.StockService;

@RestController
@RequestMapping("/stocks")
@Api(tags="stocks")
public class StockController {

	@Autowired
	private StockService service;
	
	@RequestMapping(method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value="Find All Stocks",
			notes="Returns a list of Stocks in the system.")
	@ApiResponses(value={
			@ApiResponse(code=200, message="Success"),
			@ApiResponse(code=500, message="Internal Server Error")
	})
	public List<StockData> findAllStocks() {
		return service.findAllStocks();
	}
	
	@RequestMapping(method=RequestMethod.POST, 
			consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value="Add Stock",
	notes="Add stock to the system")
	@ApiResponses(value={
		@ApiResponse(code=200, message="Success"),
		@ApiResponse(code=400, message="Bad Request"),
		@ApiResponse(code=404, message="Not Found"),
		@ApiResponse(code=500, message="Internal Server Error")
	})
	public void createStock (@RequestBody String symbol) throws StockAlreadyExistsException,StockNotFoundException {
		service.addStock(symbol);
	}
	
	@RequestMapping(value="{symbol}", method=RequestMethod.DELETE, produces=MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value="Delete Stock",
	notes="Delete an existing stock from the system")
	@ApiResponses(value={
		@ApiResponse(code=200, message="Success"),
		@ApiResponse(code=404, message="Not Found"),
		@ApiResponse(code=500, message="Internal Server Error")
	})
	public void deleteStock (@PathVariable("symbol") String symbol) throws StockNotFoundException {
		service.deleteStock(symbol);
	}
	
	@RequestMapping(value="{symbol}",method=RequestMethod.GET, 
			produces=MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value="Stock History",
	notes="get the history of the stock")
	@ApiResponses(value={
		@ApiResponse(code=200, message="Success"),
		@ApiResponse(code=400, message="Bad Request"),
		@ApiResponse(code=500, message="Internal Server Error")
	})
	public List<QuoteHistory> getHistory(@PathVariable("symbol") String symbol) throws StockNotFoundException {
		return service.getHistory(symbol);
	}
}
