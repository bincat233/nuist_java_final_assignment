package me.xiongzj.model;

import me.xiongzj.exception.ATMException;
import me.xiongzj.exception.BalanceNotEnoughException;
import me.xiongzj.exception.NegativeAmountException;
import me.xiongzj.exception.OverpayException;

//可贷款不可透支
public class LoanSavingAccount extends SavingAccount implements Loanable {

	private static final long serialVersionUID = 1L;

	private double loan;

	public LoanSavingAccount(long id,String password, String name, String personId, String email, double loan) {
		super(id,password, name, personId, email);
		this.loan = loan;
	}

	@Override
	public Account requestLoan(double money) throws NegativeAmountException {
		if (money < 0) 
			throw new NegativeAmountException();
		setBalance(getBalance() + money);
		loan += money;
		return this;
	}

	@Override
	public Account payLoan(double money) throws ATMException {
		if (money < 0) // 想干什么???
			throw new NegativeAmountException();;
		if (money > loan) // 根本没欠这么多
			throw new OverpayException();
		if (getBalance() < money) // 不够
			throw new BalanceNotEnoughException();
		setBalance(getBalance() - money);
		loan -= money; // 还了
		return this;
	}

	@Override
	public double getLoan() {
		return loan;
	}

	@Override
	public Type getType() {
		return Type.LOAN_SAVING;
	}
	
	@Override
	public String toString() {
		return super.toString() + ", subclass: LoanSavingAccount";
	}

}
