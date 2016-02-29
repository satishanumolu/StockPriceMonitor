package com.logicmonitor.entity;

import java.math.BigDecimal;

public class QuoteHistory {

	private String date;
	private BigDecimal open;
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public BigDecimal getOpen() {
		return open;
	}
	public void setOpen(BigDecimal open) {
		this.open = open;
	}
	
	
}
