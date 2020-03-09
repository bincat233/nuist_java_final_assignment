package me.xiongzj.dao;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import me.xiongzj.model.Account;

public class AccountDAOCollectionImpl implements AccountDAO {

	private HashMap<Long, Account> accounts; // 账户编号, 账户类
	private static long nextId = 100000;

	AccountDAOCollectionImpl() {
		accounts = new HashMap<>();
	}

	@Override
	public void addAccount(Account account) {
		accounts.put(account.getId(), account);
	}

	@Override
	public Account findAccountById(long id) {
		return accounts.get(id);
	}

	@Override
	public List<Account> getAllAccounts() {
		return new LinkedList<Account>(accounts.values());
	}

	@Override
	public int getSize() {
		return accounts.size();
	}

	@Override
	public long nextId() {
		return nextId++;
	}

	@Override
	public void replaceAccount(Account acc) {
		addAccount(acc);
	}

}
