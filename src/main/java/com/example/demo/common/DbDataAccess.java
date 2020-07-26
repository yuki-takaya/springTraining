package com.example.demo.common;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class DbDataAccess extends DbCommon{
	// select処理実行
	public ArrayList<HashMap<String, String>> select (String sql, String[] retData, String[] param) throws SQLException {
		
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		// DB接続オープン
		try(PreparedStatement ps = getConn().prepareStatement(sql)){
			// パラメタセット
			for(int i=0; i < param.length; i++) {
				int p = i + 1;
				if(param[i].equals(Common.strNull)) {
					ps.setNull(p, java.sql.Types.NULL);
				}else {
					ps.setObject(p, param[i]);
				}
			}
			// SQL実行
            try(ResultSet rs = ps.executeQuery()){
                while (rs.next()) {
                	HashMap<String,String> map = new HashMap<String,String>();
                	for(int j=0; j < retData.length; j++) {
                		map.put(retData[j], rs.getString(retData[j]));
                	}
                	list.add(map);
                }
                // ResultSet クローズ
                rs.close();
            };
            // PreparedStatement クローズ
            ps.close();
        } catch (SQLException e) {
            throw new SQLException("SELECT処理に失敗しました。");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	// DB接続クローズ
        	dbDisCon();
        }
		return list;
	}
	// insert/update/delete実行
	public void execute(String sql, String[] param) throws SQLException {
		// DB接続オープン
		try(PreparedStatement ps = getConn().prepareStatement(sql)){
			// パラメタセット
			for(int i=0; i < param.length; i++) {
				int p = i + 1;
				if(param[i].equals(Common.strNull)) {
					ps.setNull(p, java.sql.Types.NULL);
				}else {
					ps.setObject(p, param[i]);
				}
			}
			// クエリ実行
			ps.executeUpdate();
            // PreparedStatement クローズ
            ps.close();
        } catch (SQLException e) {
        	throw new SQLException("更新処理に失敗しました。");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	// DB接続クローズ
        	dbDisCon();
        }
	}
}
