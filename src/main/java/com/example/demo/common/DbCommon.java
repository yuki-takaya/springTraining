package com.example.demo.common;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
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
	
	public Connection getConn() throws SQLException, IOException {
		dbConnection();
		return conn;
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}

	private void dbConnection() throws SQLException, IOException {
		conn = DriverManager.getConnection(getValue());
		setConn(conn);
	}
	
	private void disConnection() throws SQLException {
		conn.close();
	}
	
	public void dbDisCon() throws SQLException {
		disConnection();
	}
}
