package me.xiongzj.dao;

public class DAOFactory {
	public enum DaoType {
		ARRAY, COLLECTION, FILE, DATABASE
	}

	public static AccountDAO createDAO(DaoType t) {
		AccountDAO ret = null;
		switch (t) {
		case ARRAY:
			ret = new AccountDAOArrayImpl();
			break;
		case COLLECTION:
			ret = new AccountDAOCollectionImpl();
			break;
		case FILE:
			ret = new AccountDAOFileImpl();
			break;
		case DATABASE:
			ret = new AccountDAODatabaseImpl();
			break;
		}
		return ret;
	}
}
