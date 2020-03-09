package me.xiongzj.exception;

// 有些地方输入金额不能为负
@SuppressWarnings("serial")
public class OverpayException extends ATMException {

	public OverpayException(){
		super("You are not owed so much money.") ;
	}

	public OverpayException(String message) {
		super("You are not owed so much money. " + message);
	}
	
	@Override
	public String getMessage() {
		return super.getMessage();
	}
	
}
