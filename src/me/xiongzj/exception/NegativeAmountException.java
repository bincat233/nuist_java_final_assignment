package me.xiongzj.exception;

// 有些地方输入金额不能为负
@SuppressWarnings("serial")
public class NegativeAmountException extends ATMException {

	public NegativeAmountException(){
		super("This amount cannot be negative.") ;
	}

	public NegativeAmountException(String message) {
		super("This amount cannot be negative. " + message);
	}
	
	@Override
	public String getMessage() {
		return super.getMessage();
	}
	
}
