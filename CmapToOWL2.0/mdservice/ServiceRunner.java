 package mdservice;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Properties;

import main.ObjectOwl;
import cmaps.CmapProposition;

public class ServiceRunner implements Runnable{
	@SuppressWarnings("unused")
	private String entry;//fichero del mapa(XML o CXL) convertido a unformatedxml
	private String result;//Resultado del fichero .owl generado convertido en cadena
	private String url;//"http://10.8.190.15:8080/CmapSenses/services/cmapsenses"
	private boolean ended;
	private boolean stoped;
	private ObjectOwl owlEntry;

	public ServiceRunner() {
		super();
		entry = "";
		result = "";
		ended = false;
		stoped = false;
	}
	
	public ServiceRunner(String entry, ObjectOwl owlEntry) {
		super();
		this.entry = entry;
		this.owlEntry =owlEntry;
		result = "";
		url = "http://10.8.190.15:8080/CmapSenses/services/cmapsenses?WSDL";
		ended = false;
		stoped = false;
	}
	
	public String getResult() {
		return result;
	}

	public void setEntry(String entry) {
		this.entry = entry;
	}

	public boolean isEnded() {
		return ended;
	}
	
	public void setEnded(boolean ended) {
		this.ended = ended;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}

	public ObjectOwl getOwlEntry() {
		return owlEntry;
	}

	public void setOwlEntry(ObjectOwl owlEntry) {
		this.owlEntry = owlEntry;
	}

	public String getUrl() {
		return url;
	}
	
//:::::::::::::::::::::::::::::::::::::::: MEtodos::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	@SuppressWarnings("static-access")
	public void run(){
		
	//	loadFromFile("url.properties");
	//	saveToFile("url.properties");
	/*	try{
		Imdservice srv = WebService.GetService(url);*/
		/*	if(srv != null){
				String mapDesambiguated =srv.disambiguate(entry);*/
		    	//owlOntology.getConection().loadFromFile("connection.properties");
		    	//OWLModel model =ProtegeOWL.createJenaOWLModel();
				//owlEntry.createConceptRepository();
				//Extraccion del Modelo de Conocimiento
		
//			...Fase de prepocesamiento... 
		while (!this.isEnded() && !this.isStoped()){//...Este ciclo es infinito hasta que se finalice o se detenga progressGen 
			Thread.currentThread().yield();
			try {
				Thread.currentThread().sleep(100);

				if(!this.isStoped()){
					owlEntry.finalize();
					if(!this.isStoped()){
						owlEntry.initialize();
						if(!this.isStoped()){
						//  owlEntry.createConceptRepository();//..hay que concectarse a la base de datos primero
						if(!this.isStoped()){
							 owlEntry.loadWordNetRelations();//Cargar las relaciones de WordNet 1)
							if(!this.isStoped()){
								owlEntry.loadOWLRelations(); //Cargar las relaciones del repositorio de ontologias  2)
								if(!this.isStoped()){	
									owlEntry.loadOpenCycRelations();//Cargar las relaciones del Repositorio de Ontologías  3)
									if(!this.isStoped()){
									  owlEntry.loadCRSConjunction();//...Agrupa las relaciones cargadas anteriormente...
										ArrayList<CmapProposition> restPrepositions=new ArrayList<CmapProposition>();
										if(!this.isStoped()){//...ejecutar las reglas...	
											restPrepositions =owlEntry.executeRules();
											if(!this.isStoped()){
												result=owlEntry.codifyRules(restPrepositions);
												setEnded(true);
											}
										}
									}
								}
							}
						}
					}
				}
			}
			} catch (InterruptedException e) {//esto devuelve una excepción de que el hilo es interrumpido cuando processGen.isStoped()==true... 
				e.printStackTrace();//...pero no se trata por que la ideas es en algun momento que se desee interrumpirlo
				//Thread.currentThread().wait();
			}
			Thread.currentThread().yield();
		}

		    	//result = srv.owlOntology(mapDesambiguated);
	/*		}
	/*	}
		catch(Exception e){
			System.out.println("Error in web service request. The URL specified in url.properties file is not available.");
			e.printStackTrace();
		}*/
		    	
	}

	public void loadFromFile(String filename){

		FileInputStream propFile;
		try {
			propFile = new FileInputStream(filename);

			Properties p = new Properties();
			p.load(propFile);
//			selecciona las propiedades del sistema
			if(p.getProperty("url") != null)
				this.url = p.getProperty("url");
		} catch (Exception e) {
		}
	}

	public void saveToFile(String filename){

		FileOutputStream propFile;
		try {
			propFile = new FileOutputStream(filename);

			Properties p = new Properties();
			p.setProperty("url", this.url);
			p.store(propFile, "URL for web service access. Created by ServiceRunner class. Author: Manuel de la Iglesia Campos.");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean isStoped() {
		return stoped;
	}

	public void setStoped(boolean stoped) {
		this.stoped = stoped;
	}

}
