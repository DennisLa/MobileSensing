package de.ms.ptenabler.locationtools;

import de.schildbach.pte.dto.Line;

public class TransportModeIdentifier{
	String type;
	public String linename;
	
	public TransportModeIdentifier(Line line){
		this.type =String.copyValueOf(new char[]{line.productCode()});
		this.linename= line.label;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLinename() {
		return linename;
	}

	public void setLinename(String linename) {
		this.linename = linename;
	}
}
