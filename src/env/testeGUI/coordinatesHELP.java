package testeGUI;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph; 
import org.graphstream.graph.Node; 
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.view.Viewer;
//import org.graphstream.ui.swingViewer.Viewer; 
//import org.graphstream.ui.swingViewer.ViewerListener; 
//import org.graphstream.ui.swingViewer.ViewerPipe; 
import org.graphstream.ui.view.ViewerListener;
import org.graphstream.ui.view.ViewerPipe;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;

import static org.graphstream.algorithm.Toolkit.*;

import java.io.BufferedReader;
import java.io.FileReader; 

public class coordinatesHELP implements ViewerListener { 

	public static void main(String args[]) { 
		new coordinatesHELP(); 
	} 

	
	private static int kk =0;
	
	public static String readLine(int line, String File){
		FileReader tempFileReader = null;
		BufferedReader tempBufferedReader = null;

		try { 
			tempFileReader = new FileReader(File); 
			tempBufferedReader = new BufferedReader(tempFileReader);
		} catch (Exception e) { }

		String returnStr = "ERROR";
		for(int i = 0; i < line - 1; i++){
			try { tempBufferedReader.readLine(); } catch (Exception e) { }
		}
		try { returnStr = tempBufferedReader.readLine(); }  catch (Exception e) { }

		return returnStr;
	}
	
	
	public static Graph exampleGraph() {
		//		Graph g = new SingleGraph("example");
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		Graph graph = new MultiGraph("Test");
		graph.addAttribute("ui.antialias", true);
		graph.addAttribute("ui.stylesheet", "node {fill-color: red; size-mode: dyn-size;} edge {fill-color:grey;}");
		
		String auxRead ="empty";
		while (auxRead != null ){ 
			Iterable<String> pieces= Splitter.onPattern("[,]").trimResults().omitEmptyStrings().split(auxRead);

			if(auxRead!="empty") {
				String nome = Iterables.get(pieces, 0);
				String corX = Iterables.get(pieces, 1);
				String corY = Iterables.get(pieces, 2);

				graph.addNode(nome).addAttribute("xy",Double.parseDouble(corX),Double.parseDouble(corY));
			}
			kk++;	
			auxRead = readLine(kk,"C:/Users/matheus/Desktop/TESE/workspace/testeGUI/src/testeGUI/coordXY_ieee123.txt");
		}
		kk=0;
		
		String auxRead2="empty";
		while (auxRead2 != null ){ 
			Iterable<String> pieces= Splitter.onPattern("[,]").trimResults().omitEmptyStrings().split(auxRead2);

			if(auxRead2!="empty") {
				String eleName = Iterables.get(pieces, 0);
				String FromBus = Iterables.get(pieces, 1);
				String to_Buss = Iterables.get(pieces, 2);
				String lenghLn = Iterables.get(pieces, 3);
				
				graph.addEdge(eleName,FromBus,to_Buss).addAttribute("length",Double.parseDouble(lenghLn));
			}
			kk++;	
			auxRead2 = readLine(kk,"C:/Users/matheus/Desktop/TESE/workspace/testeGUI/src/testeGUI/coordFromTo_ieee123.txt");
		}

		for (Node n : graph) {
			n.addAttribute("ui.label", n.getId());
			n.addAttribute("ui.size", 10);
			
		}

		for (Edge e : graph.getEachEdge()) {
			e.addAttribute("ui.label", "" + e.getNumber("length"));
			System.out.println(e.getNumber("length"));
		}

		return graph;
	}
	
	
	
	public coordinatesHELP() { 
		
		Graph graph = exampleGraph();
		
		Viewer viewer = graph.display(false); 
		ViewerPipe fromView = viewer.newViewerPipe(); 
		
		fromView.addAttributeSink(graph); 
		fromView.addViewerListener(this); 

		//viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.HIDE_ONLY); 

		while(true) { 
			fromView.pump(); 

			try { Thread.sleep(5000); } catch (Exception e) {} 

			   System.out.printf("Node positions:%n"); 
			   for(Node node:graph) { 
			    double[] pos = nodePosition(node); 
			    System.out.printf("    %s; %f ; %f ; %f)%n", node.getId(), pos[0] , pos[1], pos[2]); 
			   } 
		} 
	} 


	public void viewClosed(String viewName) { 
		System.err.printf("View closed%n"); 
	} 

	public void buttonPushed(String id) { 
		System.err.printf("pushed %s%n", id); 
	} 

	public void buttonReleased(String id) { 
		System.err.printf("released %s%n", id); 
	} 
}
 