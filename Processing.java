/***
 * written by lina <lina.oahz@gmail.com>
 * on Fri Oct  4 22:05:09 SGT 2013
 */
package processingXML;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Processing {

	public static void main(String[] args) throws IOException {
		
		Set <Node> allNodes = new HashSet<Node>();
		Set <Edge> allEdges = new HashSet<Edge>();
		Set <Group> allGroup = new HashSet<Group>();
		
		Map <String, String>hsaIdNameMap = new HashMap<String, String>();
	
		
		File xmlfile = new File("/home/lina/scratch/Network/autism/SimBoolNet CNV AG Autism/hsa04360.xml");
		File htmfile = new File("/scratch/lina/Network/autism/SimBoolNet CNV AG Autism/AG_show_pathway.htm");
		File outputfile = new File("/home/lina/scratch/Network/autism/SimBoolNet CNV AG Autism/output.txt");
		
		FileReader xmlreader = new FileReader(xmlfile);
		BufferedReader xmlin = new BufferedReader(xmlreader);
		
		FileReader htmreader = new FileReader(htmfile);
		BufferedReader htmin = new BufferedReader(htmreader);
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(outputfile));

		String line;
		String lineRegex1 = "    <entry id=\"([0-9]+)\" name=\"(path:)?(h.*)\" type=\"([a-z]+)\".*";
		Pattern pattern1 = Pattern.compile(lineRegex1);
		String lineRegex1group = "    <entry id=\"([0-9]+)\" name=\"undefined\" type=\"(.*)\">";
		Pattern pattern1g = Pattern.compile(lineRegex1group);
		String lineRegex1component = "        <component id=\"([0-9].+)\".*";
		Pattern pattern1c = Pattern.compile(lineRegex1component);
		
		String lineRegex2 = "        <graphics name=\"(.+)\" fgcolor.*";
		Pattern pattern2 = Pattern.compile(lineRegex2);
		
		String lineRegex3 = "    <relation entry1=\"([0-9]+)\" entry2=\"([0-9]+)\" type=\"([A-Za-z].+)\">";
		Pattern pattern3 = Pattern.compile(lineRegex3);
		
		String lineRegex4 = "        <subtype name=\"(.+)\" value=\"(.+)\".*";
		Pattern pattern4 = Pattern.compile(lineRegex4);
		
		String htmReg = "<area .*title=\"([0-9].*)\".*";
		Pattern hsaPattern = Pattern.compile(htmReg);
		
		while( (line = htmin.readLine()) != null ){
			Matcher hsaIdNameMatcher = hsaPattern.matcher(line);
			if(hsaIdNameMatcher.matches()){
				String idnames[] = hsaIdNameMatcher.group(1).split(",");
				for(String s : idnames){
					String temps = s.replaceAll(" ", "");
					String tempsReg = "([0-9]+)\\((.*)\\)";
					Pattern tempsPattern = Pattern.compile(tempsReg);
					Matcher tempMatcher = tempsPattern.matcher(temps);
					if(tempMatcher.matches()){
						hsaIdNameMap.put(tempMatcher.group(1), tempMatcher.group(2));
						//System.out.println(tempMatcher.group(1) + "\t" + tempMatcher.group(2));
					}
				}
			}
		}
		htmin.close();
		
		
		Node tempnode = new Node(null, null, null, null);
		Edge tempedge = new Edge(null, null, null, null, null, null, null);
		Group tempG = new Group(null, null);
		Set <Integer> tempc = new HashSet<Integer>();
		
		while ((line = xmlin.readLine()) != null) {
						
			Matcher matcher1 = pattern1.matcher(line);
			Matcher matcher1g = pattern1g.matcher(line);
			Matcher matcher1c = pattern1c.matcher(line);
			Matcher matcher2 = pattern2.matcher(line);
			Matcher matcher3 = pattern3.matcher(line);
			Matcher matcher4 = pattern4.matcher(line);
			
			if( matcher1.matches() ){
				
				Node node = new Node(null, null, null, null);
				Set<String> hsaid = new HashSet<String>();
				node.setId(Integer.parseInt(matcher1.group(1)));
				String[] s1 = matcher1.group(3).split(" ");
				for(String temp : s1){
					hsaid.add(temp.replaceAll("hsa:", ""));
				}
				node.setHsaId(hsaid);
				node.setType(matcher1.group(4));	
				tempnode = node;
			}
			
			if(matcher1g.matches()){
				//System.out.println(line);
				if(tempG.getId() != null){
					tempG.setComponents(tempc);
					allGroup.add(tempG);
					tempG = new Group(null, null);
					tempc = new HashSet<Integer>();
				}
				tempG.setId(Integer.parseInt( matcher1g.group(1) ) );			
			}
			
			if(matcher1c.matches()){
				tempc.add(Integer.parseInt( matcher1c.group(1) ) );
			}
			
			if (matcher2.matches()) {
				Set<String> tempnames = new HashSet<String>();
				String[] s2 = matcher2.group(1).split("[,.]");

				for (String temp : s2) {
					if(temp != null)
						tempnames.add(temp);
				}
				
				tempnode.setGraphNames(tempnames);
				
				allNodes.add(tempnode);
			}	
			
			if( matcher3.matches() ){
				if(tempedge.getSource() != null){
					allEdges.add(tempedge);
				}
				
				Edge edge = new Edge(null, null, null, null, null, null, null);
				edge.setSource( Integer.parseInt(matcher3.group(1)) );
				edge.setTarget( Integer.parseInt(matcher3.group(2)) );
				edge.setEdgetype( matcher3.group(3));
				tempedge = edge;
			}
			
			if(matcher4.matches()){
				//System.out.println(line);
				if(tempedge.getSubTypeName() == null){
					tempedge.setSubTypeName(matcher4.group(1));
					tempedge.setSubTypeValue(matcher4.group(2));
				}else{
					tempedge.setSubsubTypeName(matcher4.group(1));
					tempedge.setSubsubTypeValue(matcher4.group(2));
				}
			}
			
		}
		
		if(tempG.getId() != null){
			tempG.setComponents(tempc);
			allGroup.add(tempG);
		}
		
		xmlin.close();
		
		Map <Integer, Set<String>> idnameMap = new HashMap<Integer, Set<String>>();
		Map<Integer, String> idTypeMap = new HashMap<Integer, String>();
		
		for(Node n : allNodes){
			idnameMap.put( n.getId(), n.getHsaId());
			idTypeMap.put(n.getId(), n.getType());
		}
		
		
		for(Group g: allGroup){
			Set <String> gidname = new HashSet<String>();
			for(Integer c : g.getComponents()){
				for(String s: idnameMap.get(c)){
					gidname.add(s);
				}
			}
			idnameMap.put(g.getId(), gidname);
		}
		
	
		bw.write("source" + "\t" + "target" + "\t" + "interaction" + "\t" + "subtype name" + 
				"\t" + "subtype value" + "\t" + "subsub Type name" + "\t" + "subsub Type value"
				+ "\t" + "sourceType" + "\t" + "targetType");
		bw.newLine();
		
		Set<Pair> pairs = new HashSet<Pair>();
		
		for(Edge e : allEdges){
			if( idnameMap.containsKey(e.getSource()) && idnameMap.containsKey(e.getTarget()) )
			{				
				for( String sourcename : idnameMap.get(e.getSource()) )
				{
					for(String targetname : idnameMap.get(e.getTarget()))
					{
							String source = hsaIdNameMap.get(sourcename);
							String target = hsaIdNameMap.get(targetname);
							Pair tempPair = new Pair(null, null, null, null, null, null, null, null, null);
							tempPair.setSource(source);
							tempPair.setTarget(target);
							tempPair.setSourceType(idTypeMap.get(e.getSource()));
							tempPair.setTargetType(idTypeMap.get(e.getTarget()));
							tempPair.setEdgeType(e.edgetype);
							tempPair.setSubTypeName(e.getSubTypeName());
							tempPair.setSubTypeValue(e.getSubTypeValue());
							tempPair.setSubsubTypeName(e.getSubsubTypeName());
							tempPair.setSubsubTypeValue(e.getSubsubTypeValue());
							pairs.add(tempPair);
					}
				}
			}else{
				System.out.println(e.getSource() + "\t" + e.getTarget());
			}
		}
		
		
		for(Pair p: pairs){
			
			//System.out.println(p);
			bw.write( p.getSource() + "\t" + p.getTarget() + "\t" + p.getEdgeType() + "\t" +
			p.getSubTypeName() + "\t" + p.getSubTypeValue() + "\t" + p.getSubsubTypeName() + "\t" + p.getSubsubTypeValue() +
			"\t" + p.getSourceType() + "\t" + p.getTargetType());
			bw.newLine();
		}
		
		bw.close();	
	}


}

