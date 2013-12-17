package mdservice;


public class WebService {
 /**
 * @param url url del servicio web "http://host:port/application/services/servicename?wsdl"
 * @return IMapToOwlService interfaz del servicio 
 * @example Imdservice srv = WebService.GetService("http://localhost:8080/MapDis/services/mdservice?wsdl");
 * @example	srv.disambiguate(getXmlTextPane().getText());
 */
	private String url;
	/*public static Imdservice GetService(String url) {

	    Service srvcModel = new ObjectServiceFactory().create(Imdservice.class); 
	    XFireProxyFactory factory = new XFireProxyFactory(XFireFactory.newInstance().getXFire()); 
	    try { 
	    	Imdservice srvc = (Imdservice)factory.create(srvcModel, url); 
	    	return srvc;
	    } catch (MalformedURLException e) { 
	       e.printStackTrace(); 
	    }
	    return null;
	 }
	public Imdservice GetService() {

	    Service srvcModel = new ObjectServiceFactory().create(Imdservice.class); 
	    XFireProxyFactory factory = new XFireProxyFactory(XFireFactory.newInstance().getXFire()); 
	    try { 
	    	Imdservice srvc = (Imdservice)factory.create(srvcModel, url); 
	    	return srvc;
	    } catch (MalformedURLException e) { 
	       e.printStackTrace(); 
	    }
	    return null;
	 }*/
	public WebService(String url) {
		super();
		this.url = url;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
/*	public boolean ExistsService(){
		Imdservice srv = GetService();
		if(srv != null)
			if(srv.example("OK").equals("OK"))
				return true;
		return false;
	}*/
}

