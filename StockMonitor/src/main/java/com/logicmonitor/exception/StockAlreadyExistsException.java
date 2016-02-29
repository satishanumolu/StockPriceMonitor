package com.logicmonitor.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="Stock already exists in the system")
public class StockAlreadyExistsException extends Exception {
	private static final long serialVersionUID = 3932906165189258949L;
}
