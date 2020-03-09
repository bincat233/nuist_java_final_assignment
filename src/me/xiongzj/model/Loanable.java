package me.xiongzj.model;

import me.xiongzj.exception.ATMException;
import me.xiongzj.exception.NegativeAmountException;

public interface Loanable {

	public Account requestLoan(double money) throws NegativeAmountException;// 借

	public Account payLoan(double money) throws ATMException;// 还

	public double getLoan(); // 用户贷款总额

}
