package me.xiongzj.exception;

// 用户登录异常的情况,例如id错误,密码错误
@SuppressWarnings("serial")
public class LoginException extends Exception {

	public LoginException() {
		super("Login exception.");
	}

	public LoginException(String message) {
		super("Login exception. " + message);
	}

	@Override
	public String getMessage() {
		return super.getMessage();
	}

}
