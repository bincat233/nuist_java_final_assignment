package me.xiongzj.gui;

import java.awt.GridLayout;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import me.xiongzj.model.Account;
import me.xiongzj.model.CreditAccount;
import me.xiongzj.model.Loanable;
import me.xiongzj.model.Message;

@SuppressWarnings("serial")
public class BusinessPanel extends JPanel {

	private JLabel accountId;
	private JLabel name;
	private JLabel banlance;
	private JLabel ceiling;
	private JLabel loan;
	private JComboBox<String> operation;
	private JTextField amount;
	private Account account; // 已经登录的那个账户

	private ConnectionManger connect;

	// private void refresh(String strAccountId, String strName, String strBanlance,
	// String strCeiling, String strLoan) {
	// accountId.setText(strAccountId);
	// name.setText(strName);
	// banlance.setText(strBanlance);
	// ceiling.setText(strCeiling);
	// loan.setText(strLoan);
	// }

	private String DEPOSIT = "存款";
	private String WITHDRAW = "取款(透支)";
	private String TRANSFER = "转账";
	private String SET_CEILING = "修改透支额度";
	private String REQUEST_LOAN = "贷款";
	private String PAY_LOAN = "还贷";
	private ATMClient atm;

	public BusinessPanel(ATMClient atm) {
		this.atm = atm;
		// 获取连接
		connect = ConnectionManger.getInstance();
		setLayout(new GridLayout(7, 2, 0, 0));

		// 注册各种组件
		JLabel label_1 = new JLabel("账户:");
		label_1.setHorizontalAlignment(SwingConstants.CENTER);
		add(label_1);
		accountId = new JLabel();
		accountId.setHorizontalAlignment(SwingConstants.CENTER);
		add(accountId);
		JLabel label_2 = new JLabel("姓名:");
		label_2.setHorizontalAlignment(SwingConstants.CENTER);
		add(label_2);
		name = new JLabel();
		name.setHorizontalAlignment(SwingConstants.CENTER);
		add(name);
		JLabel label_3 = new JLabel("余额:");
		label_3.setHorizontalAlignment(SwingConstants.CENTER);
		add(label_3);
		banlance = new JLabel();
		banlance.setHorizontalAlignment(SwingConstants.CENTER);
		add(banlance);
		// 透支
		JLabel label_4 = new JLabel("信用额度:");
		label_4.setHorizontalAlignment(SwingConstants.CENTER);
		add(label_4);
		ceiling = new JLabel();
		ceiling.setHorizontalAlignment(SwingConstants.CENTER);
		add(ceiling);
		// 贷款
		JLabel label_5 = new JLabel("贷款额:");
		label_5.setHorizontalAlignment(SwingConstants.CENTER);
		add(label_5);
		loan = new JLabel();
		loan.setHorizontalAlignment(SwingConstants.CENTER);
		add(loan);

		operation = new JComboBox<>();
		add(operation);

		amount = new JTextField();
		add(amount);
		// textField.setColumns(10);

		// 重点, 提交逻辑
		JButton submitbutton = new JButton("提交");
		submitbutton.addActionListener((event) -> {
			Account acc = submit();
			if (acc != null) {
				clearAll();
				refresh(acc);
			}
		});
		submitbutton.setBounds(84, 277, 93, 30);
		add(submitbutton);

		JButton backbutton = new JButton("返回");
		backbutton.setBounds(187, 277, 93, 30);
		backbutton.addActionListener((event) -> {
			clearAll();
			atm.switchCard(ATMClient.PanelType.MAIN);
		});
		add(backbutton);
	}

	public void refresh(Account account) {
		this.account = account;
		// 基本
		operation.addItem(DEPOSIT);
		operation.addItem(WITHDRAW);
		accountId.setText(Long.toString(account.getId()));
		name.setText(account.getName());
		banlance.setText(Double.toString(account.getBalance()));

		// 信用
		String strCeiling = "---";
		if (account instanceof CreditAccount) {
			strCeiling = Double.toString(((CreditAccount) account).getCeiling());
			operation.addItem(SET_CEILING);
		} else {
			// 非信用账户才可以转账
			operation.addItem(TRANSFER);
		}
		ceiling.setText(strCeiling);

		// 贷款
		String strLoan = "---";
		if (account instanceof Loanable) {
			strLoan = Double.toString(((Loanable) account).getLoan());
			operation.addItem(REQUEST_LOAN);
			operation.addItem(PAY_LOAN);
		}
		loan.setText(strLoan);
	}

	private void clearAll() {
		accountId.setText("");
		name.setText("");
		banlance.setText("");
		ceiling.setText("");
		loan.setText("");
		operation.removeAllItems();
		amount.setText("");
		account = null;
	}

	private Account submit() {
		Account acc = null;
		String amountStr = amount.getText();
		if (amountStr == null) {
			atm.showText("请输入金额!");
			return null;
		}
		Matcher matcher = Pattern.compile("^[+]{0,1}(\\d+)$|^[+]{0,1}(\\d+\\.\\d+)$").matcher(amountStr);
		if (!matcher.matches()) {
			atm.showText("金额必须为正数!");
			return null;
		}
		double money = Double.parseDouble(amountStr);
		long id = account.getId();
		String operationStr = (String) operation.getSelectedItem();
		if (DEPOSIT.equals(operationStr)) {
			HashMap<String, String> m = new HashMap<>();
			m.put("id", id + "");
			m.put("money", money + "");
			acc = connect.operate(new Message(Message.Operation.DEPOSIT, m));
		} else if (WITHDRAW.equals(operationStr)) {
			HashMap<String, String> m = new HashMap<>();
			m.put("id", id + "");
			m.put("money", money + "");
			acc = connect.operate(new Message(Message.Operation.WITHDRAW, m));
			
		} else if (TRANSFER.equals(operationStr)) {
			String to = JOptionPane.showInputDialog("输入转入账号:");
			Matcher matcher2 = Pattern.compile("^[1-9]\\d+$").matcher(amountStr);
			if (!matcher2.matches()) {
				atm.showText("金额必须为正数!");
				return null;
			}
			HashMap<String, String> m = new HashMap<>();
			m.put("from", id + "");
			m.put("to", to);
			m.put("money", money + "");
			acc = connect.operate(new Message(Message.Operation.TRANSFER, m));
			
			if (acc == null) {
				atm.showText("转账失败!");
				return null;
			}
		} else if (SET_CEILING.equals(operationStr)) {
			HashMap<String, String> m = new HashMap<>();
			m.put("id", id + "");
			m.put("money", money + "");
			acc = connect.operate(new Message(Message.Operation.SET_CEILING, m));
		} else if (REQUEST_LOAN.equals(operationStr)) {
			HashMap<String, String> m = new HashMap<>();
			m.put("id", id + "");
			m.put("money", money + "");
			acc = connect.operate(new Message(Message.Operation.REQUEST_LOAN, m));
		} else if (PAY_LOAN.equals(operationStr)) {
			HashMap<String, String> m = new HashMap<>();
			m.put("id", id + "");
			m.put("money", money + "");
			acc = connect.operate(new Message(Message.Operation.PAY_LOAN, m));
		}
		if (acc == null) {
			atm.showText("操作失败!");
			return null;
		}
		return acc;
	}

}
