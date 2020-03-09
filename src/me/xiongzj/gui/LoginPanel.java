package me.xiongzj.gui;

import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.Box;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import me.xiongzj.model.Account;
import me.xiongzj.model.Message;

import java.awt.GridLayout;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JPasswordField;

@SuppressWarnings("serial")
public class LoginPanel extends JPanel {
	private JTextField accountIdField;
	private JPasswordField passwordField;
	private ATMClient atm;
	private ConnectionManger connect;

	/**
	 * Create the panel.
	 */
	public LoginPanel(ATMClient atm) {
		this.atm = atm;
		connect = ConnectionManger.getInstance();
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		Box verticalBox = Box.createVerticalBox();
		add(verticalBox);

		Component verticalGlue_2 = Box.createVerticalGlue();
		verticalBox.add(verticalGlue_2);

		JPanel panel = new JPanel();
		verticalBox.add(panel);
		panel.setLayout(new GridLayout(2, 2, 0, 0));

		JLabel lblNewLabel = new JLabel("用户账号:");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(lblNewLabel);

		accountIdField = new JTextField();
		panel.add(accountIdField);
		accountIdField.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("用户密码:");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(lblNewLabel_1);

		passwordField = new JPasswordField();
		panel.add(passwordField);

		Component verticalGlue = Box.createVerticalGlue();
		verticalBox.add(verticalGlue);

		Component verticalGlue_4 = Box.createVerticalGlue();
		verticalBox.add(verticalGlue_4);

		Component verticalGlue_3 = Box.createVerticalGlue();
		verticalBox.add(verticalGlue_3);

		Box horizontalBox = Box.createHorizontalBox();
		verticalBox.add(horizontalBox);

		JButton btnNewButton = new JButton("确认");
		btnNewButton.addActionListener((event) -> {
			Account acc = login();
			if (acc != null) {
				clearAll();
				atm.getBusinessPanel().refresh(acc);
				atm.switchCard(ATMClient.PanelType.BUSINESS);
			} else {
				// 占位
			}
		});
		horizontalBox.add(btnNewButton);

		JButton btnNewButton_1 = new JButton("取消");
		btnNewButton_1.addActionListener((event) -> {
			clearAll();
			atm.switchCard(ATMClient.PanelType.MAIN);
		});
		horizontalBox.add(btnNewButton_1);

		Component verticalGlue_1 = Box.createVerticalGlue();
		verticalBox.add(verticalGlue_1);
	}

	public void clearAll() {
		accountIdField.setText("");
		passwordField.setText("");
	}

	private Account login() {
		String accountIdText = accountIdField.getText();
		String password = new String(passwordField.getPassword());
		// 检查
		if (accountIdText == null || "".equals(password)) {
			atm.showText("请完整输入帐号密码!");
			return null;
		}
		long accountId = Long.parseLong(accountIdText);
		HashMap<String, String> m = new HashMap<>();
		m.put("accountId", accountId + "");
		m.put("password", password + "");
		Account acc = connect.operate(new Message(Message.Operation.LOGIN, m));
		if (acc == null) {
			atm.showText("用户名或密码错误!");
			return null;
		}
		return acc;
	}

}
