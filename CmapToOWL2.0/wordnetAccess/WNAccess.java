package wordnetAccess;

import java.util.ArrayList;

import cmaps.CmapLink;

import wordnetDataFile.Synset;

/*Interfaz para cargar los datos de los ficheros de WordNet
 * segun sea la version y la estructura de los mismos
 * 
 * */
public interface WNAccess {
//	--------------------------------TRABAJO con WORD--------------------------------------------------		
	public ArrayList<Synset> getAllSynsets(String lemma);
	public ArrayList<Synset> getSynsets(String lemma, char pos);
//	--------------------------------TRABAJO con SYNSET--------------------------------------------------	
	public ArrayList<Synset> getAllFullSynsets(String offset);
	public Synset getFullSynset(String offset, char pos);
	
//	--------------------------------TRABAJO con LINK--------------------------------------------------			
	public ArrayList<CmapLink> getCmapLinks();
	public String getlink(String link);
	public ArrayList<String> getLinks();
	
	public void addAllLinks(ArrayList<CmapLink> cmaplinks);
	public boolean addLink(String linkName, String category);
//	--------------------------------TRABAJO con CATEGORY--------------------------------------------------	
	public String getCategory(String category);
	public ArrayList<String> getCategories();
	public String getLinkCategory(String link);
}
