package me.xiongzj.dao;

import java.util.List;

import me.xiongzj.model.Account;

public interface AccountDAO {
	void addAccount(Account acc);
	void replaceAccount(Account acc);
	Account findAccountById(long id);
	List<Account> getAllAccounts();
	int getSize();
	long nextId();
}
