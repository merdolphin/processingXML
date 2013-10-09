package processingXML;

import java.util.Set;

public class Node {
	private Integer id;
	private String type;
	private Set<String> hsaId;
	private Set<String> graphNames;
	private String firstname;
	
	public Node(Integer id, String type, Set<String> hsaId, Set<String> graphNames, String firstname){
		this.id = id;
		this.type = type;
		this.hsaId = hsaId;
		this.graphNames = graphNames;
		this.firstname = firstname;
	}
	
	
	public Set<String> getHsaId() {
		return hsaId;
	}

	public void setHsaId(Set<String> hsaId) {
		this.hsaId = hsaId;
	}


	public Set<String> getGraphNames() {
		return graphNames;
	}


	public void setGraphNames(Set<String> graphNames) {
		this.graphNames = graphNames;
	}


	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}



	@Override
	public String toString() {
		return "Node [id=" + id + ", type=" + type + ", hsaId=" + hsaId
				+ ", graphNames=" + graphNames + ", firstname=" + firstname
				+ "]";
	}

	

}
