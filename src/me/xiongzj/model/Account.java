package me.xiongzj.model;

import java.io.Serializable;

import me.xiongzj.exception.ATMException;
import me.xiongzj.exception.NegativeAmountException;

public abstract class Account implements Serializable {
	private static final long serialVersionUID = 1L;
	private long id; // 账户号码
	private String password; // 账户密码
	private String name; // 姓名
	private String personId; // 身份证
	private String email; // 电邮
	private double balance; // 账户余额

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!(obj instanceof Account))
			return false;
		return this.id == ((Account) obj).id;
	}

	@Override
	public String toString() {
		return "class: Account, id: " + id + ", name: " + name;
	}

	public static enum Type { // 账户类型
		SAVING, CREDIT, LOAN_SAVING, LOAN_CREDIT;
		@Override
		public String toString() {
			switch (this) {
			case SAVING:
				return "SAVING";
			case CREDIT:
				return "CREDIT";
			case LOAN_SAVING:
				return "LOAN_SAVING";
			case LOAN_CREDIT:
				return "LOAN_CREDIT";
			}
			return null;
		}
		public Type toType(String str) {
			if("SAVING".equals(str))
				return SAVING;
			else if("CREDIT".equals(str))
				return CREDIT;
			else if("LOAN_SAVING".equals(str))
				return LOAN_SAVING;
			else if("LOAN_CREDIT".equals(str))
				return LOAN_CREDIT;
			return null;
		}
	}

	public abstract Type getType();

	public Account() {

	}

	public Account(long id, String password, String name, String personId, String email) {
		this.id = id;
		this.password = password;
		this.name = name;
		this.personId = personId;
		this.email = email;
		this.balance = 0;
	}

	public long getId() { // set(), get()
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public final Account deposit(double money) throws NegativeAmountException { // 存钱, 子类不能修改
		if (money < 0) // 这是要干什么???
			throw new NegativeAmountException("The money is " + money + ".");
		balance += money;
		return this;
	}

	public abstract Account withdraw(double money) throws ATMException; // 取款方法根据不同的子类而不同,改为抽象方法
}
