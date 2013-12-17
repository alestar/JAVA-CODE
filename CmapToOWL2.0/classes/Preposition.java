package classes;

public class Preposition {

	private AmbConcept origConcept;
	private Link linkWord;
	private AmbConcept destConcept;
//	private String 
	
	
	public Preposition(AmbConcept origConcept, Link linkWord, AmbConcept destConcept) {
		super();
		this.origConcept = origConcept;
		this.linkWord = linkWord;
		this.destConcept = destConcept;
	}
	
	public Preposition() {
		
	}

	public AmbConcept getDestConcept() {
		return destConcept;
	}
	public void setDestConcept(AmbConcept destConcept) {
		this.destConcept = destConcept;
	}
	public Link getLinkWord() {
		return linkWord;
	}
	public void setLinkWord(Link linkWord) {
		this.linkWord = linkWord;
	}
	public AmbConcept getOrigConcept() {
		return origConcept;
	}
	public void setOrigConcept(AmbConcept origConcept) {
		this.origConcept = origConcept;
	}
}
