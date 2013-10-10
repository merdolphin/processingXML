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
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Processing {

	public static void main(String[] args) throws IOException {
		
		File xmlfile = new File("/home/lina/scratch/Network/autism/SimBoolNet CNV AG Autism/hsa04360.xml");
		File htmfile = new File("/scratch/lina/Network/autism/SimBoolNet CNV AG Autism/AG_show_pathway.htm");
		
		FileReader xmlreader = new FileReader(xmlfile);
		BufferedReader xmlin = new BufferedReader(xmlreader);
		
		FileReader htmreader = new FileReader(htmfile);
		BufferedReader htmin = new BufferedReader(htmreader);

		String line;
		String lineRegex1 = "    <entry id=\"([0-9]+)\" name=\"(h.*)\" type=\"([a-z]+)\".*";
		Pattern pattern1 = Pattern.compile(lineRegex1);
		String lineRegex2 = "        <graphics name=\"(.+)\" fgcolor.*";
		Pattern pattern2 = Pattern.compile(lineRegex2);
		String lineRegex3 = "    <relation entry1=\"([0-9]+)\" entry2=\"([0-9]+)\" type=\"([A-Za-z].+)\">";
		Pattern pattern3 = Pattern.compile(lineRegex3);
		
		String htmReg = "<area .*title=\"([0-9].*)\".*";
		Pattern hsaPattern = Pattern.compile(htmReg);
		
		Set <Node> allNodes = new HashSet<Node>();
		Set <Edge> allEdges = new HashSet<Edge>();
		Map <String, String>hsaIdNameMap = new HashMap<String, String>();
		
		Node tempnode = new Node(null, null, null, null);
		
		while( (line = htmin.readLine()) != null ){
			Matcher hsaIdNameMatcher = hsaPattern.matcher(line);
			if(hsaIdNameMatcher.matches()){
				//System.out.println(line);
				//System.out.println(hsaIdNameMatcher.group(1));
				String idnames[] = hsaIdNameMatcher.group(1).split(",");
				for(String s : idnames){
					String temps = s.replaceAll(" ", "");
					String tempsReg = "([0-9]+)\\((.*)\\)";
					Pattern tempsPattern = Pattern.compile(tempsReg);
					Matcher tempMatcher = tempsPattern.matcher(temps);
					if(tempMatcher.matches()){
						hsaIdNameMap.put(tempMatcher.group(1), tempMatcher.group(2));
					}
				}
			}
		}
		
		htmin.close();
		
		while ((line = xmlin.readLine()) != null) {
						
			Matcher matcher1 = pattern1.matcher(line);
			Matcher matcher2 = pattern2.matcher(line);
			Matcher matcher3 = pattern3.matcher(line);
			
			if( matcher1.matches() ){
				
				Node node = new Node(null, null, null, null);
				Set<String> hsaid = new HashSet<String>();
				node.setId(Integer.parseInt(matcher1.group(1)));
				String[] s1 = matcher1.group(2).split(" ");
				for(String temp : s1){
					hsaid.add(temp.replaceAll("hsa:", ""));
				}
				node.setHsaId(hsaid);
				node.setType(matcher1.group(3));	
				tempnode = node;
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
			
//			for(Node n : allNodes){
//				System.out.println(n.getHsaId());
//			}
			
			
			if( matcher3.matches() ){
				Edge edge = new Edge(null, null, null);
				edge.setSource( Integer.parseInt(matcher3.group(1)) );
				edge.setTarget( Integer.parseInt(matcher3.group(2)) );
				edge.setEdgetype( matcher3.group(3));
				allEdges.add(edge);
				//System.out.println(matcher3.group(1) + "\t" + matcher3.group(2));
			}
		}
		
		xmlin.close();
		
		Map <Integer, Set<String>> idnameMap = new HashMap<Integer, Set<String>>();
		
		for(Node n : allNodes){
			idnameMap.put( n.getId(), n.getHsaId());
		}
		
		File outputfile = new File("/home/lina/scratch/Network/autism/SimBoolNet CNV AG Autism/output.txt");
		
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(outputfile));
	
		bw.write("source" + "\t" + "target" + "\t" + "interaction");
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
							Pair tempPair = new Pair(null, null, null);
							tempPair.setSource(source);
							tempPair.setTarget(target);
							tempPair.setEdgeType(e.edgetype);
							pairs.add(tempPair);
					}
				}
			}
		}
		
		
		for(Pair p: pairs){
			
			System.out.println(p);
			bw.write( p.getSource() + "\t" + p.getTarget() + "\t" + p.getEdgeType() );
			bw.newLine();
		}
		
		bw.close();	
	}


}

