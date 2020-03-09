package me.xiongzj.dao;

import java.io.FileOutputStream;
import java.util.LinkedList;
import java.util.List;
import org.dom4j.*;
import org.dom4j.io.*;
import me.xiongzj.model.Account;
import me.xiongzj.model.CreditAccount;
import me.xiongzj.model.LoanCreditAccount;
import me.xiongzj.model.LoanSavingAccount;
import me.xiongzj.model.Loanable;
import me.xiongzj.model.SavingAccount;

public class AccountDAOXMLImpl implements AccountDAO {
	static final String PATH = "xml/accounts.xml";
	
	@Override
	public void addAccount(Account acc) {
		Document document = getDocument();
		Element root = document.getRootElement();

		// List<Node> list = document.selectNodes("//id");
		// Element acce = null;
		// for (Node idNode : list) {
		// String s = idNode.getText();
		// if (s.equals(acc.getId() + ""))
		// acce = idNode.getParent();
		// }
		//
		// if (acce != null) {
		// Element f = acce.getParent();
		// f.remove(acce);
		// }

		// 增加一个账户节点
		Element account = root.addElement("account");
		// 各种属性
		account.addElement("type").setText(acc.getType().toString());
		account.addElement("id").setText("" + acc.getId());
		account.addElement("name").setText(acc.getName());
		account.addElement("password").setText(acc.getPassword());
		account.addElement("personid").setText(acc.getPersonId());
		account.addElement("email").setText(acc.getEmail());

		if (acc instanceof CreditAccount) {
			CreditAccount tmp = (CreditAccount) acc;
			account.addElement("ceiling").setText(tmp.getCeiling() + "");
		}

		if (acc instanceof Loanable) {
			Loanable tmp = (Loanable) acc;
			account.addElement("loan").setText(tmp.getLoan() + "");
		}

		write(document);
	}

	@Override
	public Account findAccountById(long id) {
		Document document = getDocument();

		List<Node> list = document.selectNodes("//id");
		Element acc = null;
		for (Node node : list) {
			String s = node.getText();
			if (s.equals(id + ""))
				acc = node.getParent();
		}
		Account account = element2Account(acc);
		return account;
	}

	private Account element2Account(Element e) {
		Account.Type t = Account.Type.valueOf(e.element("type").getText());
		Account account = null;
		switch (t) {
		case SAVING:
			account = new SavingAccount(Long.parseLong(e.element("id").getText()), e.element("password").getText(),
					e.element("name").getText(), e.element("personid").getText(), e.element("email").getText());
			break;
		case CREDIT:
			account = new CreditAccount(Long.parseLong(e.element("id").getText()), e.element("password").getText(),
					e.element("name").getText(), e.element("personid").getText(), e.element("email").getText(),
					Double.parseDouble(e.element("ceiling").getText()));
			break;
		case LOAN_SAVING:
			account = new LoanSavingAccount(Long.parseLong(e.element("id").getText()), e.element("password").getText(),
					e.element("name").getText(), e.element("personid").getText(), e.element("email").getText(),
					Double.parseDouble(e.element("loan").getText()));
			break;
		case LOAN_CREDIT:
			account = new LoanCreditAccount(Long.parseLong(e.element("id").getText()), e.element("password").getText(),
					e.element("name").getText(), e.element("personid").getText(), e.element("email").getText(),
					Double.parseDouble(e.element("ceiling").getText()),
					Double.parseDouble(e.element("loan").getText()));
			break;
		}
		return account;
	}

	@Override
	public List<Account> getAllAccounts() {
		Document document = getDocument();
		Element root = document.getRootElement();
		List<Element> list = root.elements("account");

		List<Account> ret = new LinkedList<>();
		for (Element node : list) {
			ret.add(element2Account(node));
		}
		return ret;
	}

	@Override
	public int getSize() {
		Document document = getDocument();
		return document.getRootElement().nodeCount(); // ????+1
	}

	@Override
	public long nextId() {
		// TODO Auto-generated method stub
		return 0;
	}

	// 返回 Document, 封装解析器，得到文件
	public static Document getDocument() {
		try {
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(PATH);
			return document;
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return null;
	}

	// 回写
	public static void write(Document document) {
		try {
			OutputFormat format = OutputFormat.createPrettyPrint();
			XMLWriter xmlWriter = new XMLWriter(new FileOutputStream(PATH), format);
			xmlWriter.write(document);
			xmlWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void replaceAccount(Account acc) {
		Document document = getDocument();
		Element root = document.getRootElement();
		List<Element> list = root.elements("account");

		// 删除节点
		for (Element e : list) {
			String s = e.element("id").getText();
			if (s.equals(acc.getId() + "")) {
				root.remove(e);
			}
		}
		write(document);
		addAccount(acc);
	}

	public static void main(String[] args) {
		AccountDAOXMLImpl ttt = new AccountDAOXMLImpl();
		ttt.replaceAccount(new CreditAccount(125, "1234", "1234", "1234", "1234", 13.2));
	}

}
