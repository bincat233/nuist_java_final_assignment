package me.xiongzj.exception;

// ATM业务异常基类
@SuppressWarnings("serial")
public class ATMException extends Exception {

	public ATMException(String message) {
		super(message);
	}

	@Override
	public String getMessage() {
		return super.getMessage();
	}
}
