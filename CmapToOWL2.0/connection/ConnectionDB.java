package connection;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
/**
 * Clase para la conexión a la BD
 * */

public class ConnectionDB {
	private static Connection connection;
	private String server; 
	private String database;
	private String port;
	private String user;
	private String password;
	private String lock;
	private String driver;

	public ConnectionDB(String server, String database, String userName, String password, String port) {

		this.server = server;
		this.database = database;	//+ ":5432/"
		this.port = port;
		this.user = userName;
		this.password = password;
		this.driver = "org.postgresql.Driver";
		this.lock = "lock";
	}

	private String getDriver()
	{
		return "jdbc:postgresql://" + server + ":" + port + "/" + database;	
	}

	public boolean connect(){
		boolean connected = false;
		try {
			Class.forName(this.driver);
			if(connection != null)
				connection.close();
			connection = DriverManager.getConnection(this.getDriver(),this.user,this.password);
			System.out.println("Conexión establecida.");
			connected = true;
		} catch (Exception e) {
			connected = false;
			System.out.println("No se pudo cargar el driver.");
			e.printStackTrace();
		}	
		return connected;
	}

	public boolean setConnection(){
		boolean conected=true;
		try {
			Class.forName(this.driver);
			//System.out.println("Conexión establecida.");
			if(connection == null || connection.isClosed()){
				connection = DriverManager.getConnection(this.getDriver(),this.user,this.password);
				System.out.println("Conexión establecida.");
			}
		} catch (Exception e) {
			conected=false;
			System.out.println("No se pudo cargar el driver.");
			e.printStackTrace();
		}	
		return conected;
	}

	public void closeConnection() { // these close the current conection
		synchronized (lock) {
			try {
				if ((connection != null) && (!connection.isClosed())) {
					connection.close();
				}
			} catch (SQLException e) {
				//System.out.println("Error, conexión no cerrada: \"" + e.getMessage() + "\"");
			}
		}

	}

	public Connection getConnection() {
		return connection;
	}

	public  synchronized int executeCmd(String Cmd){
		Statement s;

		try {
			s = connection.createStatement (); 
			int count = s.executeUpdate (Cmd);					
			//s.close ();
			return count;			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
	}

	public  synchronized ResultSet executeQuery(String Cmd){
		Statement s;

		try {
			s = connection.createStatement (); 
			ResultSet rs = s.executeQuery(Cmd);
			//s.close ();
			return rs;			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	public void setConnectionProperties(String server, String database, String userName, String password, String port){
		this.server = server;
		this.database = database;	
		this.user = userName;
		this.password = password;
		this.port = port;
		this.lock = "lock";
		this.driver = "org.postgresql.Driver";
	}

	public ConnectionDB clone(){
		ConnectionDB newConnection = new ConnectionDB(server, database, user, password, port);
		return newConnection;
	}

	public void loadFromFile(String filename){

		FileInputStream propFile;
		try {
			propFile = new FileInputStream(filename);

			Properties p = new Properties();
			p.load(propFile);
//			selecciona las propiedades del sistema
			if(p.getProperty("server") != null)
				this.server = p.getProperty("server");
			if(p.getProperty("database") != null)
				this.database = p.getProperty("database");	
			if(p.getProperty("user") != null)
				this.user = p.getProperty("user");
			if(p.getProperty("password") != null)
				this.password = p.getProperty("password");
			if(p.getProperty("port") != null)
				this.port = p.getProperty("port");
			this.lock = "lock";
			this.driver = "org.postgresql.Driver";
		} catch (Exception e) {
		}
	}

	public void saveToFile(String filename){

		FileOutputStream propFile;
		try {
			propFile = new FileOutputStream(filename);

			Properties p = new Properties();
			p.setProperty("server", this.server);
			p.setProperty("database", this.database);
			p.setProperty("user", this.user);
			p.setProperty("password", this.password);
			p.setProperty("port", this.port);
			p.store(propFile, "Database connection properties. Created by DBConnection class. Author Amhed Suarez.");

		} catch (Exception e) {
		}
	}

}