package me.xiongzj.gui;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import me.xiongzj.model.Account;
import me.xiongzj.model.Message;
import me.xiongzj.server.Bank;

public class ConnectionManger {
	
	private final static String SERVER_IP = "127.0.0.1";
	private final static int SERVER_PORT = 9000;

	private static ConnectionManger instance = null;
	private InputStream is;
	private ObjectInputStream ois;
	private OutputStream os;
	private ObjectOutputStream oos;
	private Socket socket;

	private ConnectionManger() {
		
	}

	// 双检锁实现单例
	public static ConnectionManger getInstance() {
		if (instance == null) {
			synchronized (Bank.class) {
				if (instance == null) {
					instance = new ConnectionManger();
				}
			}
		}
		return instance;
	}

	public void close() {
		try {
			if (ois != null) {
				ois.close();
				ois = null;
			}
			if (is != null) {
				is.close();
				is = null;
			}
			if (oos != null) {
				oos.close();
				oos = null;
			}
			if (os != null) {
				os.close();
				os = null;
			}
			if (socket != null) {
				socket.close();
				socket = null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Account operate(Message msg) {
		Account ret = null;
		Object obj = null;
		try {
			socket = new Socket(SERVER_IP, SERVER_PORT);
			os = socket.getOutputStream();
			oos = new ObjectOutputStream(os);
			oos.writeObject(msg);
			oos.flush();
			is = socket.getInputStream();
			ois = new ObjectInputStream(is);
			obj = ois.readObject();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		if (obj != null)
			ret = (Account) obj;
		return ret;
	}
}
