package com.example.demo.common;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

class DbDataAccessTest {

	private final String note_data = "TEST_NOTE_DATA";
	
	// 正常終了テスト
	@Test
	void selectOKTest() {
		
		DbDataAccess da = new DbDataAccess();
		
		String sql = "SELECT NOTE_DATA FROM NOTE";
		String[] retData = new String[] {
				"NOTE_DATA"
		};
		try {
			List<HashMap<String, String>> list = da.select(sql, retData, new String[] {});
			// 件数一致
			assertSame(list.size(),1);
			// データ一致
			list.forEach(data ->  {
				assertEquals(note_data, data.get("NOTE_DATA").toString());
			}) ;
		} catch (SQLException e) {
			// NG
			fail("NG");
		}
	}
	
	// 例外エラーテスト
	@Test
	void selectNGTest() {
		
		DbDataAccess da = new DbDataAccess();
		
		String sql = "SELECT NOTE FROM NOTE";
		String[] retData = new String[] {
				"NOTE"
		};
		try {
			da.select(sql, retData, new String[] {});
			fail("NG");
		} catch (SQLException e) {
			// OK
			assertSame(e.getMessage(),"SELECT処理に失敗しました。");
		}
	}
	
	// 正常終了テスト
	@Test
	void executeOKTest() {
		
		DbDataAccess da = new DbDataAccess();
		
		String sql = "UPDATE NOTE SET NOTE_DATA = ?";
		String[] param = new String[] {
				note_data
		};
		try {
			da.execute(sql, param);
		} catch (SQLException e) {
			// NG
			fail("NG");
		}
	}
	
	// 例外エラーテスト
	@Test
	void executeNGTest() {
		
		DbDataAccess da = new DbDataAccess();
		// SQL構文エラー
		String sql = "INSERT INTO NOTE (NOTE) VALUES (?)";
		String[] param = new String[] {
				"TEST_NOTE_DATA"
		};
		try {
			da.execute(sql, param);
			fail("NG");
		} catch (SQLException e) {
			// OK
			assertSame(e.getMessage(),"更新処理に失敗しました。");
		}
	}
}
