package me.xiongzj.exception;

// 用于取钱的时候余额不足的情况(包括账户余额超过透支额的情况)
@SuppressWarnings("serial")
public class BalanceNotEnoughException extends ATMException {

	public BalanceNotEnoughException() {
		super("Balance is not enough.");
	}

	public BalanceNotEnoughException(String message) {
		super("Balance is not enough. " + message);
	}

	@Override
	public String getMessage() {
		return super.getMessage();
	}
}
