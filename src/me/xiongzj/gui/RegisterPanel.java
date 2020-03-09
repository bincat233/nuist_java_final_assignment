package me.xiongzj.gui;

import javax.swing.JPanel;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import me.xiongzj.model.Account;
import me.xiongzj.model.Message;

import javax.swing.JComboBox;
import javax.swing.JPasswordField;

@SuppressWarnings("serial")
public class RegisterPanel extends JPanel {
	private ConnectionManger connect;
	private JComboBox<String> accountType;
	private JPasswordField passwordField;
	private JPasswordField passwordEnsureField;
	private JTextField eMailField;
	private JTextField personIdField;
	private JTextField nameField;
	private JButton submitbutton;
	private ATMClient atm;

	private Account register() {
		// 获取
		String password = new String(passwordField.getPassword());
		String passwordEnsure = new String(passwordEnsureField.getPassword());

		String name = nameField.getText();
		String personId = personIdField.getText();
		String email = eMailField.getText();
		// Account.Type t = null;
		// 0:储蓄 1:信用 2:贷款储蓄 3:贷款信用
		int index = accountType.getSelectedIndex();

		// 检查
		if ("".equals(password) || "".equals(passwordEnsure) // 是否填写完整
				|| name == null || personId == null || email == null) {
			atm.showText("请将信息填写完整!");
			return null;
		}
		if (!password.equals(passwordEnsure)) {
			atm.showText("密码不一致!");
			System.out.println(password + ":" + passwordEnsure);
			return null;
		}

//		// 正则匹配邮箱和身份证
//		Matcher personIdMatcher = Pattern.compile(
//				"(^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}$)")
//				.matcher(personId);
//		if (!personIdMatcher.matches()) {
//			atm.showText("身份证格式错误!");
//			return null;
//		}
		Matcher emailMatcher = Pattern
				.compile("^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$")
				.matcher(email);
		System.out.println("\""+email+"\"");
		if (!emailMatcher.matches()) {
			atm.showText("邮箱格式错误!");
			return null;
		}
		HashMap<String, String> m = new HashMap<>();
		m.put("password", password + "");
		m.put("name", name + "");
		m.put("personId", personId + "");
		m.put("email", email + "");
		m.put("type", index + ""); // 0:储蓄 1:信用 2:贷款储蓄 3:贷款信用 !!!!
		Account acc = connect.operate(new Message(Message.Operation.REGISTER, m));
		return acc;
	}

	public RegisterPanel(ATMClient atm) {
		this.atm = atm;
		connect = ConnectionManger.getInstance();
		setLayout(new GridLayout(8, 2, 0, 0));

		// 注册各种组件
		JLabel label_1 = new JLabel("账户类型:");
		label_1.setHorizontalAlignment(SwingConstants.CENTER);
		add(label_1);

		// 0:储蓄 1:信用 2:贷款储蓄 3:贷款信用
		String arr[] = { "储蓄账户", "信用账户", "贷款储蓄账户", "贷款信用账户" };
		accountType = new JComboBox<>(arr);
		add(accountType);

		JLabel label_2 = new JLabel("用户名:");
		label_2.setHorizontalAlignment(SwingConstants.CENTER);
		add(label_2);
		nameField = new JTextField();
		add(nameField);

		JLabel label_3 = new JLabel("密码:");
		label_3.setHorizontalAlignment(SwingConstants.CENTER);
		add(label_3);
		passwordField = new JPasswordField();
		add(passwordField);

		JLabel label_4 = new JLabel("确认密码:");
		label_4.setHorizontalAlignment(SwingConstants.CENTER);
		add(label_4);
		passwordEnsureField = new JPasswordField();
		add(passwordEnsureField);

		JLabel label_5 = new JLabel("身份证号:");
		label_5.setHorizontalAlignment(SwingConstants.CENTER);
		add(label_5);
		personIdField = new JTextField();
		add(personIdField);

		JLabel label_7 = new JLabel("E-Mail:");
		label_7.setHorizontalAlignment(SwingConstants.CENTER);
		add(label_7);
		eMailField = new JTextField();
		add(eMailField);

		// 重点, 提交逻辑
		submitbutton = new JButton("提交");
		submitbutton.addActionListener((event) -> {
			Account acc = register();
			// 使用新注册的账户刷新
			if (acc != null) {
				atm.getBusinessPanel().refresh(acc);
				clearAll();
				atm.switchCard(ATMClient.PanelType.BUSINESS);
			} else {
				atm.showText("服务器未连接!");
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

	public void clearAll() {
		accountType.setSelectedIndex(0);
		passwordField.setText("");
		passwordEnsureField.setText("");
		eMailField.setText("");
		personIdField.setText("");
		nameField.setText("");
	}

}
