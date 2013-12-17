package connection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 * Clase para la conexión a la BD
 * */

public class Connection_MC {
	private static Connection connection;
	private String url;     //Data Source Name
    private String user;
    private String password;
    private String lock;
    private String driver;
    
    public Connection_MC(String server, String userName, String password) {
    
    	this.url = server;
    	this.user = userName;
    	this.password = password;
    	this.lock = "lock";
		this.driver = "sun.jdbc.odbc.JdbcOdbcDriver";
    }
    
    private String getDriver()
    {
    	return "jdbc:odbc:" + url;
    }
    
    
	
	public boolean setConnection(){
		boolean conected=true;
		try {
		  try {
			Class.forName(this.driver);
		} catch (ClassNotFoundException e) {
			conected=false;
			e.printStackTrace();
		}
		connection = DriverManager.getConnection(this.getDriver(),this.user,this.password);
			//System.out.println("Conexión establecida.");
		} catch (SQLException e) {
			conected=false;
			System.out.println("No se pudo cargar el puente JDBC-ODBC.");
			e.printStackTrace();
		}
		return conected;
	}
	
	   
    
    public void closeConnection() { // these close the current conection
		synchronized (lock) {
			try {
				if ((connection != null) && (!connection.isClosed())) {
					connection.close();
					System.out.println("Conexión cerrada.");
				}
			} catch (SQLException e) {
				System.out.println("Error, conexión no cerrada: \""
						+ e.getMessage() + "\"");
			}
		}

	}
	
	public Connection getConnection() {
		return connection;
	}
   
	

}