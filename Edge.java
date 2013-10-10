package processingXML;

public class Edge {
	Integer source;
	Integer target;
	String edgetype;
	String subTypeName;
	String subTypeValue;
	String subsubTypeName;
	String subsubTypeValue;
	
	public Edge(Integer source, Integer target, 
			String edgetype, String subTypeName, String subTypeValue,
			String subsubTypeName, String subsubTypeValue){
		this.source = source;
		this.target = target;
		this.edgetype = edgetype;
		this.subTypeName = subTypeName;
		this.subTypeValue = subTypeValue;
		this.subsubTypeName = subsubTypeName;
		this.subsubTypeValue = subsubTypeValue;
	}

	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}

	public Integer getTarget() {
		return target;
	}

	public void setTarget(Integer target) {
		this.target = target;
	}

	public String getEdgetype() {
		return edgetype;
	}

	public void setEdgetype(String edgetype) {
		this.edgetype = edgetype;
	}
	
	public String getSubTypeName() {
		return subTypeName;
	}

	public void setSubTypeName(String subTypeName) {
		this.subTypeName = subTypeName;
	}

	public String getSubTypeValue() {
		return subTypeValue;
	}

	public void setSubTypeValue(String subTypeValue) {
		this.subTypeValue = subTypeValue;
	}
	
	

	public String getSubsubTypeName() {
		return subsubTypeName;
	}

	public void setSubsubTypeName(String subsubTypeName) {
		this.subsubTypeName = subsubTypeName;
	}

	public String getSubsubTypeValue() {
		return subsubTypeValue;
	}

	public void setSubsubTypeValue(String subsubTypeValue) {
		this.subsubTypeValue = subsubTypeValue;
	}

	@Override
	public String toString() {
		return "Edge [source=" + source + ", target=" + target + ", edgetype="
				+ edgetype + "]";
	}
	
	

}
