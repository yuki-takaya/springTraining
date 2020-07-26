package com.example.demo.common;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class DbCommon {
	
	private Connection conn = null;
	// プロパティリソースの取得
	private String getValue() throws IOException {
		Resource resource = new ClassPathResource("/application.properties");
		Properties props = PropertiesLoaderUtils.loadProperties(resource);
		return props.getProperty("spring.datasource.url");
	}
	
	protected Connection getConn() throws SQLException, IOException {
		dbConnection();
		return conn;
	}

	protected void setConn(Connection conn) {
		this.conn = conn;
	}

	private void dbConnection() throws SQLException, IOException {
		conn = DriverManager.getConnection(getValue());
		setConn(conn);
	}
	
	private void disConnection() throws SQLException {
		conn.close();
	}
	
	protected void dbDisCon() throws SQLException {
		disConnection();
	}
}
