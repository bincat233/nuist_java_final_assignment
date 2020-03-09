package me.xiongzj.model;

import java.io.Serializable;

import me.xiongzj.exception.BalanceNotEnoughException;
import me.xiongzj.exception.NegativeAmountException;

public class CreditAccount extends Account implements Serializable {

	private static final long serialVersionUID = 1L;

	private double ceiling; // 透支额度

	public CreditAccount(long id, String password, String name, String personId, String email, double ceiling) {
		super(id, password, name, personId, email);
		this.ceiling = ceiling;
	}

	public double getCeiling() {
		return ceiling;
	}

	public void setCeiling(double ceiling) {
		this.ceiling = ceiling;
	}

	// 取款, 覆盖父类
	@Override
	public Account withdraw(double money) throws BalanceNotEnoughException, NegativeAmountException {
		if (money < 0) // 非法操作!!
			throw new NegativeAmountException();
		double expectedBalance = super.getBalance() - money; // 预期金额
		if (expectedBalance + ceiling < 0) // 额度不够
			throw new BalanceNotEnoughException();
		setBalance(expectedBalance);
		return this;
	}

	@Override
	public Type getType() {
		return Type.CREDIT;
	}
	
	@Override
	public String toString() {
		return super.toString()+", subclass: CreditAccount";
	}

}
