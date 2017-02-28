package com.hanbit.sp.exception;

public class SooException extends RuntimeException {
	
	private String errorCode = "UNKNOWN";
	
	public SooException(String errorMsg) {
		super(errorMsg);
	}

	public SooException(String errorMsg, String errorCode) {
		this(errorMsg);
		
		this.errorCode = errorCode;
	}
	
	public String getErrorCode() {
		return errorCode;
	}
	
}
