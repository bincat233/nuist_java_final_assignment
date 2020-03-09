package me.xiongzj.gui;

import java.awt.*;
import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.*;
import javax.swing.border.Border;

// MVC 中的 Controller
public class ATMClient {

	private JFrame frame;
	private CardLayout card;
	private JPanel cards;
	private MainPanel mainPanel;
	private LoginPanel loginPanel;
	private RegisterPanel registerPanel;
	private BusinessPanel businessPanel;
	private ResourceBundle rb;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ATMClient window = new ATMClient();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public ATMClient() {
		initialize();
	}

	public void showText(String str) {
		JOptionPane.showConfirmDialog(null, str, rb.getString("INFO"), JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE);
	}

	private void initialize() {
		// 国际化
		Locale locale = new Locale("zh", "CN"); 
		rb=ResourceBundle.getBundle("res", locale);
		frame = new JFrame("ATM Client");
		// frame.setBounds(100, 100, 350, 350);
		frame.setSize(350, 350);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		card = new CardLayout();
		cards = new JPanel(card);
		Border padding = BorderFactory.createEmptyBorder(10, 30, 10, 30);
		cards.setBorder(padding);
		frame.setContentPane(cards);
		mainPanel = new MainPanel(this);
		loginPanel = new LoginPanel(this);
		registerPanel = new RegisterPanel(this);
		businessPanel = new BusinessPanel(this);
		cards.add(mainPanel, "main");
		cards.add(loginPanel, "login");
		cards.add(registerPanel, "register");
		cards.add(businessPanel, "business");
		frame.setVisible(true);

		// 自定义字体
		try {
			Font.createFont(Font.PLAIN, new File("fonts/sarasa-regular.ttc")); // Sarasa Gothic CL
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public MainPanel getMainPanel() {
		return mainPanel;
	}

	public LoginPanel getLoginPanel() {
		return loginPanel;
	}

	public RegisterPanel getRegisterPanel() {
		return registerPanel;
	}

	public BusinessPanel getBusinessPanel() {
		return businessPanel;
	}
	
	public ResourceBundle getResourceBundle() {
		return rb;
	}

	public enum PanelType {
		MAIN, LOGIN, REGISTER, BUSINESS
	}

	public void switchCard(PanelType type) {
		switch (type) {
		case MAIN:
			card.show(cards, "main");
			break;
		case LOGIN:
			card.show(cards, "login");
			break;
		case REGISTER:
			card.show(cards, "register");
			break;
		case BUSINESS:
			card.show(cards, "business");
			break;
		default:
			break;
		}
	}

}
