package me.xiongzj.model;

import java.io.Serializable;

import me.xiongzj.exception.BalanceNotEnoughException;

// 储蓄账户
public class SavingAccount extends Account implements Serializable {

	private static final long serialVersionUID = 1L;

	public SavingAccount(long id, String password, String name, String personId, String email) {
		super(id,password, name, personId, email); // 通过Super
	}

	// 取
	@Override
	public Account withdraw(double money) throws BalanceNotEnoughException {
		double expectedBalance = getBalance() - money; // 预期金额
		if (expectedBalance >= 0) {
			setBalance(expectedBalance);
		} else { // 不够
			throw new BalanceNotEnoughException();
		}
		return this;
	}

	@Override
	public Type getType() {
		return Type.SAVING;
	}

	@Override
	public String toString() {
		return super.toString() + ", subclass: SavingAccount";
	}
}
