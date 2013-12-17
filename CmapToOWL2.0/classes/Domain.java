/**
 * 
 */
package classes;

import java.util.ArrayList;

/**
 * @author Amhed
 * 
 * Clase para manipular la información
 * referente a los dominios en MC
 *
 */
public class Domain extends DomainEntity{
	
	private int weight;
	private ArrayList<String> terms;
	private double frequence;
	
	public Domain(int weight, String domain) {
		super(domain);
		this.weight = weight;
		this.terms = new ArrayList<String>();
		this.setFrequence(0.0);
	}
	public void addTerm(String term) {
		this.terms.add(term);
	}
	public ArrayList<String> getTerms() {
		return this.terms;
	}

	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	public void setFrequence(double frequence) {
		this.frequence = frequence;
	}
	public double getFrequence() {
		return frequence;
	}
	
	
}
