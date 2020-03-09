package me.xiongzj.gui;

import java.awt.*;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class MainPanel extends JPanel {

	public MainPanel(ATMClient atm) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		Box verticalBox = Box.createVerticalBox();
		add(verticalBox);

		Component verticalGlue = Box.createVerticalGlue();
		verticalBox.add(verticalGlue);

		JLabel label = new JLabel("模拟 ICBC ATM 终端");
		label.setFont(new Font("Sarasa Gothic CL", Font.BOLD, 26));
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		verticalBox.add(label);

		Component verticalGlue_1 = Box.createVerticalGlue();
		verticalBox.add(verticalGlue_1);
		Component verticalGlue_2 = Box.createVerticalGlue();
		verticalBox.add(verticalGlue_2);

		Box horizontalBox = Box.createHorizontalBox();
		verticalBox.add(horizontalBox);

		JButton registerButton = new JButton("注册");
		horizontalBox.add(registerButton);
		JButton loginButton = new JButton("登录");
		horizontalBox.add(loginButton);

		Component verticalGlue_3 = Box.createVerticalGlue();
		verticalBox.add(verticalGlue_3);

		// Java8 函数式特性注册按钮
		registerButton.addActionListener((event) -> {
			atm.switchCard(ATMClient.PanelType.REGISTER);
		});
		loginButton.addActionListener((event) -> {
			atm.switchCard(ATMClient.PanelType.LOGIN);
		});
	}
}
