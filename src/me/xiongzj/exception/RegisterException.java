package me.xiongzj.exception;

// 用于开户异常的情况,例如密码两次输入不一致等情况
@SuppressWarnings("serial")
public class RegisterException extends Exception {

	public RegisterException() {
		super("Register exception.");
	}

	public RegisterException(String message) {
		super("Register exception. " + message);
	}

	@Override
	public String getMessage() {
		return super.getMessage();
	}
}
