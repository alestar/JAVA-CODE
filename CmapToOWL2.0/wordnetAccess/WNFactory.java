package wordnetAccess;


public class WNFactory {
	public static String wn16 = "WordNet 1.6";
	public static String wn21 = "WordNet 2.1";
	public static String wn30 = "WordNet 3.0";
	public static String wnsp200611 = "esWN-200611";

	/*Metodo que fabrica una instancia para el acceso a WordNet
	 * segun sea la version
	 * */
	public static WNAccess getInstance(String version, String dataPath){
		WNAccess instance = null;
		if(version.equals(wn16)){
			instance = new wordnetAccess.specific.WN16Access(dataPath);
		}
		if(version.equals(wn21)){
			instance = new wordnetAccess.specific.WN21Access(dataPath);
		}
		if(version.equals(wn30)){
			instance = new wordnetAccess.specific.WN30Access(dataPath);
		}
	/*	if(version.equals(wnsp200611)){
			instance = new wordnetAccess.specific.esWN200611Access(dataPath);
		}*/
		return instance;
	}
}
