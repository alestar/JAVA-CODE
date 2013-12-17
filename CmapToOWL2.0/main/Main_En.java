package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import cmaps.CMap;
import cmaps.CMapHandler;
import cmaps.CmapProposition;


/**
 * @author Alejandro Ruiz Coutin
 *
 */
public class Main_En {

	/**
	 * @param args
	 */
	private static ObjectOwl owlOntology;
	public static void main(String[] args) {
	
		owlOntology = new ObjectOwl();
		// TODO Auto-generated method stub

		File[] libraries = (new File("lib")).listFiles();
		for (File file : libraries) {
			try {
				DynamicPackagesLoader.addFile(file);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		libraries = (new File("xfire")).listFiles();
		if(libraries != null)
			for (File file : libraries) {
				try {
					DynamicPackagesLoader.addFile(file);
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		File[] maps = (new File("maps")).listFiles();//Carga los mapas para k sean procesados
		if(maps != null){
			
			for (File file : maps) {
				String fileName = file.getName();//aki se obtine el nombre del fichero
				System.out.println(fileName);
				//check if xml
				if(fileName.length()>4 && fileName.substring(fileName.length()-4, fileName.length()).equalsIgnoreCase(".xml") || fileName.length()>4 && fileName.substring(fileName.length()-4, fileName.length()).equalsIgnoreCase(".cxl")){
					//	codificar
					processMap(fileName);//Finalmente ete es el metodo que inicaliza todo el proceso para formalizar el mapa
					System.out.println(fileName.substring(0,fileName.length()-4));
				}
			}
		}
	}
	
	public static void processMap(String MapName){

		FileReader entryFile;
		String xmlFile = "";
		try {
			entryFile = new FileReader("maps/"+MapName);
			BufferedReader salida = new BufferedReader(entryFile);
			String lineReader = salida.readLine();
			while(lineReader != null ){
				xmlFile += lineReader;
				lineReader = salida.readLine();
			}

			CMapHandler HanParser= new CMapHandler();
			CMap mapparsed= HanParser.loadCXL(xmlFile);
		
			if(mapparsed!= null && mapparsed.getPropositions().size()>0 && mapparsed.getConcepts().size()>0){
					owlOntology.setMap(mapparsed);
				}
//String url="http://10.8.190.15:8080/CmapSenses/services/cmapsenses?WSDL";
/*ServiceRunner serv = new ServiceRunner(xmlFile,owlOntology);
Imdservice srv = WebService.GetService(serv.getUrl());
String desambiguated= srv.disambiguate(xmlFile);*/
//owlOntology.getConection().loadFromFile("connection.properties");

//Extraccion del Modelo de Conocimiento
//OWLModel model =ProtegeOWL.createJenaOWLModel();
//owlOntology.createConceptRepository();
			
//			...Fase de prepocesamiento ..
			owlOntology.loadAllRelations();//Cargar todos los conjuntos de los distintos repositorios
			ArrayList<CmapProposition> restPrepositions =owlOntology.executeRules();//ejecutar las reglas
			owlOntology.codifyRules(restPrepositions);//codificar las reglas y generar la ontología
			System.out.println("ok");
		} 
		catch (Exception e) {
			System.out.println(e.getMessage());
			// TODO: handle exception
		}
	}
}
