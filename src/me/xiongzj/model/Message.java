package me.xiongzj.model;

import java.io.Serializable;
import java.util.HashMap;

public class Message implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public enum Operation {
		REGISTER,LOGIN,DEPOSIT,WITHDRAW,SET_CEILING,REQUEST_LOAN,PAY_LOAN,TRANSFER
	}

	Operation operation;
	HashMap<String, String> properties;

	public Message(Operation operation, HashMap<String, String> properties) {
		this.operation = operation;
		this.properties = properties;
	}

	public Operation getOperation() {
		return operation;
	}

	public HashMap<String, String> getProperties() {
		return properties;
	}
}
