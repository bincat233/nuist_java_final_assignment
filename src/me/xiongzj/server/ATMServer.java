package me.xiongzj.server;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.xiongzj.model.Account;
import me.xiongzj.model.Message;

public class ATMServer {
	private final static Logger logger = Logger.getLogger(ATMServer.class.getName());
	private static Bank bank;

	public static void main(String[] args) throws IOException {
		bank = Bank.getInstance();
		@SuppressWarnings("resource")
		ServerSocket server = new ServerSocket(9000);
		while (true) {
			Socket socket = server.accept();
			invoke(socket);
		}
	}

	private static void invoke(final Socket socket) throws IOException {
		new Thread(new Runnable() {
			public void run() {
				ObjectInputStream is = null;
				ObjectOutputStream os = null;
				try {
					is = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
					os = new ObjectOutputStream(socket.getOutputStream());

					Object obj = is.readObject();
					Message msg = (Message) obj;
					System.out.println("Get object! ");
					
					Account acc = null;
					HashMap<String, String> p = msg.getProperties();
					switch (msg.getOperation()) {
					case REGISTER:
						// 0:储蓄 1:信用 2:贷款储蓄 3:贷款信用 !!!! 写的真丑, 但是我不知道怎么办啦
						int i =Integer.parseInt(p.get("type"));
						Account.Type[] arr = {Account.Type.SAVING,Account.Type.CREDIT,Account.Type.LOAN_SAVING,Account.Type.LOAN_CREDIT};
						Account.Type t = arr[i];
						acc = bank.register(p.get("password"), p.get("name"), p.get("personId"), p.get("email"), t);
						break;
					case LOGIN:
						acc = bank.login(Long.parseLong(p.get("accountId")), p.get("password"));
						break;
					case DEPOSIT:
						acc = bank.deposit(Long.parseLong(p.get("id")), Double.parseDouble(p.get("money")));
						break;
					case WITHDRAW:
						acc = bank.withdraw(Long.parseLong(p.get("id")), Double.parseDouble(p.get("money")));
						break;
					case SET_CEILING:
						acc = bank.setCeiling(Long.parseLong(p.get("id")), Double.parseDouble(p.get("money")));
						break;
					case REQUEST_LOAN:
						acc = bank.requestLoan(Long.parseLong(p.get("id")), Double.parseDouble(p.get("money")));
						break;
					case PAY_LOAN:
						acc = bank.payLoan(Long.parseLong(p.get("id")), Double.parseDouble(p.get("money")));
						break;
					case TRANSFER:
						acc = bank.transfer(Long.parseLong(p.get("from")), Long.parseLong(p.get("to")), Double.parseDouble(p.get("money")));
						break;
					}

					System.out.println("Sand "+acc);
					os.writeObject(acc);
					os.flush();
				} catch (IOException ex) {
					logger.log(Level.SEVERE, null, ex);
				} catch (ClassNotFoundException ex) {
					logger.log(Level.SEVERE, null, ex);
				} finally {
					try {
						is.close();
					} catch (Exception ex) {
					}
					try {
						os.close();
					} catch (Exception ex) {
					}
					try {
						socket.close();
					} catch (Exception ex) {
					}
				}
			}
		}).start();
	}
}
