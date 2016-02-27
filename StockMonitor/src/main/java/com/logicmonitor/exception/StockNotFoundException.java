package com.logicmonitor.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="Stock Not Found")
public class StockNotFoundException extends Exception {
	private static final long serialVersionUID = 5968000547444142953L;
}
