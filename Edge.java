package processingXML;

public class Edge {
	Integer source;
	Integer target;
	String edgetype;
	
	public Edge(Integer source, Integer target, String edgetype){
		this.source = source;
		this.target = target;
		this.edgetype = edgetype;
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

	@Override
	public String toString() {
		return "Edge [source=" + source + ", target=" + target + ", edgetype="
				+ edgetype + "]";
	}
	
	

}
