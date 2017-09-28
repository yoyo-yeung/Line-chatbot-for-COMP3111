package com.example.bot.spring;

import lombok.extern.slf4j.Slf4j;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.*;
import java.net.URISyntaxException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

@Slf4j
public class SQLDatabaseEngine extends DatabaseEngine {
	@Override
	String search(String txt) throws Exception {
		//Write your code here
	
		Connection connection=getConnection();
		
		PreparedStatement stt=
				connection.prepareStatement("SELECT response FROM chatbotDB WHERE LOWER(request) LIKE LOWER( CONCAT('%',?,'%') )");
		
		stt.setString(1,txt);
		
		ResultSet res = stt.executeQuery();
		String result=null;
		if(!res.next()) {	// not found
			connection.close();
			stt.close();
			res.close();
			throw new Exception("NOT FOUND"); //end1
		}			
		result=res.getString(1);//found
		
		connection.close();
		stt.close();
		res.close();
		return result;	//official end
	}
	
	
	private Connection getConnection() throws URISyntaxException, SQLException {
		Connection connection;
		URI dbUri = new URI(System.getenv("DATABASE_URL"));

		String username = dbUri.getUserInfo().split(":")[0];
		String password = dbUri.getUserInfo().split(":")[1];
		String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath() +  "?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";

		log.info("Username: {} Password: {}", username, password);
		log.info ("dbUrl: {}", dbUrl);
		
		connection = DriverManager.getConnection(dbUrl, username, password);

		return connection;
	}
	
}
