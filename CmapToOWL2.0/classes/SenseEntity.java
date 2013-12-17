package classes;

import java.util.ArrayList;
/**
 * Clase para manipular los sentidos almacenados en WordNet
 * */

public class SenseEntity {

	private String sense;
	private ArrayList<DomainEntity> domains;
	
	public SenseEntity(String sense) {
		this.sense = sense;
		this.domains = new ArrayList<DomainEntity>();
	}	
	public String getSense() {
		return sense;
	}
	public void setSense(String sense) {
		this.sense = sense;
	}
	public ArrayList<DomainEntity> getDomains() {
		return domains;
	}
	public void setDomains(ArrayList<DomainEntity> domains) {
		this.domains = domains;
	}
	public void addDomain(DomainEntity domain) {
		this.domains.add(domain);
	}
}
