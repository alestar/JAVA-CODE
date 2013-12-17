package cmaps;

 public class CmapProposition {

	private CmapConcept origConcept;
	private CmapLink link;
	private CmapConcept destConcept;
	private boolean funtional; 
	private String type;// relacionado con WN pueden ser Hyponym,Instance Hyponym,Hypernym,Instance Hypernym
						//Member holonym,Substance holonym,Part holonym,Member meronym,Substance meronym,Part meronym
	public CmapProposition() {
		
	}
	public CmapProposition(CmapConcept origConcept, CmapLink link, CmapConcept destConcept) {
		
		this.origConcept = origConcept;
		this.link = link;
		this.destConcept = destConcept;
	}
	public CmapProposition(CmapConcept origConcept, CmapLink link, CmapConcept destConcept,  String type) {
		super();
		this.origConcept = origConcept;
		this.link = link;
		this.destConcept = destConcept;
		this.type = type;
	}
	public CmapConcept getDestConcept() {
		return destConcept;
	}
	public void setDestConcept(CmapConcept destConcept) {
		this.destConcept = destConcept;
	}
	public CmapLink getLink() {
		return link;
	}
	public void setLink(CmapLink link) {
		this.link = link;
	}
	public CmapConcept getOrigConcept() {
		return origConcept;
	}
	public void setOrigConcept(CmapConcept origConcept) {
		this.origConcept = origConcept;
	}
	public boolean isFuntiona() {
		return funtional;
	}
	public void setFuntiona(boolean funtional) {
		this.funtional = funtional;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
