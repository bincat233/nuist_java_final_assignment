package me.xiongzj.server;

import java.util.*;

import me.xiongzj.dao.AccountDAO;
import me.xiongzj.dao.DAOFactory;
import me.xiongzj.dao.DAOFactory.DaoType;
import me.xiongzj.exception.ATMException;
import me.xiongzj.exception.NegativeAmountException;
import me.xiongzj.model.Account;
import me.xiongzj.model.CreditAccount;
import me.xiongzj.model.LoanCreditAccount;
import me.xiongzj.model.LoanSavingAccount;
import me.xiongzj.model.Loanable;
import me.xiongzj.model.SavingAccount;

public class Bank {

	private AccountDAO dao;
	private static Bank instance = null;

	private Bank() {
		dao = DAOFactory.createDAO(DaoType.COLLECTION);
	}

	// 双检锁实现单例
	public static Bank getInstance() {
		if (instance == null) {
			synchronized (Bank.class) {
				if (instance == null) {
					instance = new Bank();
				}
			}
		}
		return instance;
	}

	// 账户数量
	public int getSize() {
		return dao.getSize();
	}

	// register方法
	public Account register(String password, String name, String personId, String email, Account.Type type) {
		Account account = null;
		long id = dao.nextId();
		switch (type) {
		case SAVING:
			account = new SavingAccount(id, password, name, personId, email);
			break;
		case CREDIT:
			account = new CreditAccount(id, password, name, personId, email, 0); // 抱歉你不能透支
			break;
		case LOAN_SAVING:
			account = new LoanSavingAccount(id, password, name, personId, email, 0);
			break;
		case LOAN_CREDIT:
			account = new LoanCreditAccount(id, password, name, personId, email, 0, 0);
		}
		dao.addAccount(account);
		return account;
	}

	public Account login(Long id, String password) {
		Account account = dao.findAccountById(id);
		if (null == account) // 查无此人
			return null;

		if (password.equals(account.getPassword())) // 密码正确
			return account;
		else // 密码错误
			return null;
	}

	// 存款
	public Account deposit(Long id, double money) {
		Account account = dao.findAccountById(id); // 获取账户
		if (null == account) // 查无此人
			return null;

		try {
			account.deposit(money);
		} catch (NegativeAmountException e) {
			e.printStackTrace();
			return null;
		}
		return account;
	}

	// 取款
	public Account withdraw(Long id, double money) {
		Account account = dao.findAccountById(id);
		if (null == account) // 查无此人
			return null;
		try {
			account.withdraw(money);
		} catch (ATMException e) {
			e.printStackTrace();
			return null;
		}
		return account;
	}

	// 设置透支额度
	public Account setCeiling(Long id, double money) {
		Account account = dao.findAccountById(id);
		if (null == account) // 查无此人
			return null;
		if (!(account instanceof CreditAccount)) // 非信用账户
			return null;
		((CreditAccount) account).setCeiling(money);
		return account;
	}

	// 转账
	public Account transfer(Long from, Long to, double money) {
		Account fromAccount = dao.findAccountById(from);
		Account toAccount = dao.findAccountById(to);
		if (fromAccount == null || toAccount == null) // 查无此人
			return null;
		if (fromAccount instanceof CreditAccount) // 不能使用信用额度转账
			return null;
		try {
			fromAccount.withdraw(money); // 取出
			toAccount.deposit(money);
		} catch (ATMException e) {
			e.printStackTrace();
			return null;
		}
		return fromAccount; // 成功
	}

	// 统计余额总数
	public double totalBalance() {
		double sum = 0;
		for (Account account : dao.getAllAccounts()) {
			sum += account.getBalance();
		}
		return sum;
	}

	// 统计透支额度
	public double totalCeiling() {
		double sum = 0;
		for (Account account : dao.getAllAccounts()) {
			if (account instanceof CreditAccount) { // 只统计信用账户
				sum += ((CreditAccount) account).getCeiling();
			}
		}
		return sum;
	}

	public double totalLoan(long id) {
		double sum = 0;
		for (Account account : dao.getAllAccounts()) {
			if (account instanceof Loanable) { // 只统计
				sum += ((Loanable) account).getLoan();
			}
		}
		return sum;
	}

	// 资产排名
	public void printSortedBalanceDESC() {
		HashMap<String, Double> map = new HashMap<>();// 身份证号, 资产
		for (Account account : dao.getAllAccounts()) {
			String pid = account.getPersonId();
			double newBlance = account.getBalance();
			if (map.containsKey(pid)) // 加上已统计的其它资产
				newBlance += map.get(pid);
			map.put(pid, newBlance);
		}
		// 这段参考 https://blog.csdn.net/exceptional_derek/article/details/9852929
		List<Map.Entry<String, Double>> sortedlist = new LinkedList<>();
		sortedlist.addAll(map.entrySet());
		class BalanceComparator implements Comparator<Map.Entry<String, Double>> {
			public int compare(Map.Entry<String, Double> mp1, Map.Entry<String, Double> mp2) {
				return Double.compare(mp2.getValue(), mp1.getValue());
			}
		}
		BalanceComparator bc = new BalanceComparator();
		Collections.sort(sortedlist, bc);
		System.out.println("PersonId\tBalance\n--------------------------------");
		for (Map.Entry<String, Double> kv : sortedlist) {
			System.out.println(kv.getKey() + "\t" + kv.getValue());
		}
	}

	public Account requestLoan(long id, double money) {
		Account account = dao.findAccountById(id);
		if (null == account) // 查无此人
			return null;
		if (!(account instanceof Loanable)) // 账户类型不可贷款
			return null;
		try {
			((Loanable) account).requestLoan(money); // 放出贷款
		} catch (NegativeAmountException e) {
			e.printStackTrace();
			return null;
		}
		return account;
	}

	public Account payLoan(long id, double money) {
		Account account = dao.findAccountById(id);
		if (null == account) // 查无此人
			return null;
		if (!(account instanceof Loanable)) // 账户类型不可贷款
			return null;
		try {
			((Loanable) account).payLoan(money);
		} catch (ATMException e) {
			e.printStackTrace();
			return null;
		}
		return account;
	}

	public static void main(String[] args) {
		Bank b = Bank.getInstance();
		Account aa = b.register("asdf", "fadf", "293875017569274974", "asdf", Account.Type.CREDIT);

		b.setCeiling(123L, 1000);
		try {
			Account ab = b.register("adfs", "sdff", "293774299683510597", "ffdds", Account.Type.SAVING);
			b.register("adfs", "sdff", "457937439204529834", "ffdds", Account.Type.SAVING);
			ab.deposit(100000);
			b.transfer(ab.getId(), aa.getId(), 1000);
			b.printSortedBalanceDESC();
		} catch (ATMException e) {
			e.printStackTrace();
		}
	}

}
