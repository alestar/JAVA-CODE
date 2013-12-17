package classes;
/**
 * Clase para trabajar con los sentidos asociados 
 * a cada concepto del MC
 * 
 * */

public class Sense extends SenseEntity{
	    
	
	private boolean removed;
	
		
	public Sense(String sense) {
		super(sense);
		
		this.removed = false;
		// TODO Auto-generated constructor stub
	}

	public boolean isRemoved() {
		return removed;
	}
	public void setRemoved(boolean removed) {
		this.removed = removed;
	}
}
