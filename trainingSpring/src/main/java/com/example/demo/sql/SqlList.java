package com.example.demo.sql;

public class SqlList {
	// メモ取得SQL
	public String getNoteData() {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT NOTE_DATA FROM NOTE");
		
		return sb.toString();
	}
	// 収支合計取得SQL
	public String getIncomeExpenditure() {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT ");
		sb.append(" CAST(SUM(CASE WHEN EXPENSE_ITEM = 1 THEN AMOUNT ELSE 0 END) ");
		sb.append("  - SUM(CASE WHEN EXPENSE_ITEM = 2 THEN AMOUNT ELSE 0 END) AS INT) AS MONTH_SUM_AMOUNT ");
		sb.append("FROM HOUSEHOLD_ACCOUNTS ");
		sb.append("WHERE INPUT_DATE >= ? ");
		sb.append("AND   INPUT_DATE <= ? ");
		 
		return sb.toString();
	}
	// メモ削除
	public String deleteNote() {
		StringBuffer sb = new StringBuffer();
		sb.append("DELETE FROM NOTE ");
		 
		return sb.toString();
	}
	// メモ登録
	public String insertNote() {
		StringBuffer sb = new StringBuffer();
		sb.append("INSERT INTO NOTE (NOTE_DATA) VALUES (?) ");
		 
		return sb.toString();
	}
	// 家計簿登録処理
	public String insertHouseholdAccounts() {
		StringBuffer sb = new StringBuffer();
		sb.append("INSERT INTO HOUSEHOLD_ACCOUNTS ( ");
		sb.append("  ID   ");
		sb.append(" ,INPUT_DATE   ");
		sb.append(" ,EXPENSE_ITEM ");
		sb.append(" ,AMOUNT       ");
		sb.append(" ,REMARK       ");
		sb.append(" ) VALUES (    ");
		sb.append("   ?    ");
		sb.append("  ,?    ");
		sb.append("  ,?    ");
		sb.append("  ,?    ");
		sb.append("  ,?    ");
		sb.append(" )      ");
		
		return sb.toString();
	}
	// MAX+1ID取得処理
	public String getId() {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT  ");
		sb.append("  IFNULL(MAX(ID),0) + 1 AS MAX_ID  ");
		sb.append("FROM HOUSEHOLD_ACCOUNTS ");
		
		return sb.toString();
	}
	// 家計簿検索処理
	public String entryHouseholdAccounts() {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT  ");
		sb.append("  ID   ");
		sb.append(" ,SUBSTR(CAST(INPUT_DATE AS CHAR),1,4) || '/' || SUBSTR(CAST(INPUT_DATE AS CHAR),5,2) || '/' || SUBSTR(CAST(INPUT_DATE AS CHAR),7,2) AS INPUT_DATE ");
		sb.append(" ,CASE WHEN EXPENSE_ITEM = 1 THEN '1:収入' ELSE '2:支出' END AS EXPENSE_ITEM ");
		sb.append(" ,AMOUNT       ");
		sb.append(" ,REMARK       ");
		sb.append(" ,EXPENSE_ITEM AS EXPENSE_ITEM_VAL ");
		sb.append("FROM HOUSEHOLD_ACCOUNTS ");
		sb.append("WHERE ID = ?   ");
		
		return sb.toString();
	}
	// 家計簿削除
	public String deleteHouseholdAccounts() {
		StringBuffer sb = new StringBuffer();
		sb.append("DELETE FROM HOUSEHOLD_ACCOUNTS WHERE ID = ? ");
		return sb.toString();
	}
	// 家計簿履歴検索処理
	public String searchHouseholdAccounts() {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT  ");
		sb.append("  ID   ");
		sb.append(" ,SUBSTR(CAST(INPUT_DATE AS CHAR),1,4) || '/' || SUBSTR(CAST(INPUT_DATE AS CHAR),5,2) || '/' || SUBSTR(CAST(INPUT_DATE AS CHAR),7,2) AS INPUT_DATE ");
		sb.append(" ,CASE WHEN EXPENSE_ITEM = 1 THEN '1:収入' ELSE '2:支出' END AS EXPENSE_ITEM ");
		sb.append(" ,AMOUNT       ");
		sb.append(" ,REMARK       ");
		sb.append("FROM HOUSEHOLD_ACCOUNTS ");
		sb.append("WHERE (INPUT_DATE  >= ?  OR ? IS NULL) ");
		sb.append("AND   (INPUT_DATE  <= ?  OR ? IS NULL) ");
		sb.append("AND   (EXPENSE_ITEM = ?  OR ? IS NULL) ");
		sb.append("AND   (AMOUNT      >= ?  OR ? IS NULL) ");
		sb.append("AND   (AMOUNT      <= ?  OR ? IS NULL) ");
		sb.append("AND   (REMARK     LIKE ? OR ? IS NULL) ");
		sb.append("ORDER BY INPUT_DATE     ");
		
		return sb.toString();
	}
	// 家計簿更新
	public String updateHouseholdAccounts() {
		StringBuffer sb = new StringBuffer();
		sb.append("UPDATE HOUSEHOLD_ACCOUNTS ");
		sb.append("SET    INPUT_DATE   = ? ");
		sb.append("      ,EXPENSE_ITEM = ? ");
		sb.append("      ,AMOUNT       = ? ");
		sb.append("      ,REMARK       = ? ");
		sb.append("WHERE  ID           = ? ");
		return sb.toString();
	}
}
