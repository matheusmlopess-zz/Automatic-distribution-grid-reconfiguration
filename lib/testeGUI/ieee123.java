package testeGUI;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.graphstream.algorithm.Dijkstra;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.spriteManager.SpriteManager;

import com.google.common.base.Splitter;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multimap;


public class ieee123{

	private static int kk =0;
	private static List<String> swithces= new ArrayList<String>();
	private static Multimap< String , List<String>> swT = ArrayListMultimap.create();
	private static int ii =0;
	private static int ik;
	private static SpriteManager sman;
	private static Multimap< Integer , String> zonaWthPower = ArrayListMultimap.create();
	private static String sourceNodeof;
	static List<Node> list1 = new ArrayList<Node>();
	static List<Edge> list2 = new ArrayList<Edge>();
	private static int countNopower;
	private static int countPower;
	private static int ok;


	public static void main(String[] args) {
	
		Graph g = exampleGraph();
		g.display(false);
		sman = new SpriteManager(g);

		// Edge lengths are stored in an attribute called "length"
		// The length of a path is the sum of the lengths of its edges
		Dijkstra dijkstra = new Dijkstra(Dijkstra.Element.EDGE, null, "length");

		// Compute the shortest paths in g from A to all nodes
		dijkstra.init(g);

		boolean normlyOpenON= true;

		// 99 --->> all Normaly open swts close
		getSwt(g,normlyOpenON,0);
		//		System.out.println(swT.size());
		//		System.out.println(swT);

		Iterator<String> ds = swT.keySet().iterator();
		String[] as= new String[swT.size()];
		ik=0;

		while(ds.hasNext()){ 

			as[ik]=ds.next();
			ik++;
		}

		//		System.out.println(Arrays.toString(as));
		//		Line.SW1 	150R - 149
		//		Line.SW2 	13 -152
		//		Line.SW3 	18 - 135
		//		Line.SW4 	60 - 160
		//		Line.SW5    97-	197
		//		Line.SW6	61- 61S
		//		Line.SW7	151-300_OPEN
		//		Line.SW8	54-	94_OPEN

		//		String faltswEdge1  = "null";
		String faltswEdge1  = "Line.SW2";
//		String faltswEdge1  = "Line.SW3";
//		String faltswEdge2  = "null" ;
		String faltswEdge2  = "Line.SW4";

		ok=0;
		while(ok<as.length) {
			if(faltswEdge1.equals(as[ok].toString()) | faltswEdge2.equals(as[ok].toString())) {
				g.removeEdge(as[ok].toString());
				System.out.println("Switch turnerd OFF--->"+as[ok].toString());
			}
			ok++;
		}



		runSolution(g, dijkstra,as);
		outerloop:
			for (Edge edge : dijkstra.getTreeEdges()) {
				if(edge.getId().equals("Line.SW1"))
					break outerloop;

				edge.addAttribute("ui.style", " size: 2px; fill-color: #CCC; stroke-mode: plain; stroke-color: gray; shadow-mode: plain; shadow-width: 3px; shadow-color: gray; shadow-offset: 0px;");
				edge.setAttribute("ui.class", "marked");
				sleep();
			}

		dijkstra.setSource(g.getNode("149"));
		dijkstra.compute();

		for (Edge edge : dijkstra.getTreeEdges()) {
			edge.addAttribute("ui.style", "fill-color: red;");
			edge.addAttribute("ui.style", " size: 0.5px; fill-color: #CCC; stroke-mode: plain; stroke-color: #999; shadow-mode: plain; shadow-width: 3px; shadow-color: #FC0; shadow-offset: 0px;");
			edge.setAttribute("ui.class", "marked");
			sleep();
		}

		Iterator<Integer> keySetIterator32 = zonaWthPower.keySet().iterator(); 
		while(keySetIterator32.hasNext()) {
			int asd = keySetIterator32.next();
			System.out.println("Key->"+ asd+ " Zone Swt found ->"+zonaWthPower.get(asd).iterator().next());
		}

		System.out.println("\n Zone Swt eletrified found ->"+countPower +" & the number total of zones not energized is "+ (countNopower));

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		getSwt(g,normlyOpenON,1);
		runSolution(g, dijkstra,as);

		System.out.println("\n Zone Swt eletrified found ->"+countPower +" & the number total of zones not energized is "+ (countNopower));


		for (Edge edge : dijkstra.getTreeEdges()) {
			edge.addAttribute("ui.style", "fill-color: red;");
			edge.addAttribute("ui.style", " size: 0.5px; fill-color: #CCC; stroke-mode: plain; stroke-color: #999; shadow-mode: plain; shadow-width: 3px; shadow-color: #FC0; shadow-offset: 0px;");
			edge.setAttribute("ui.class", "marked");
			sleep();
		}
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		getSwt(g,normlyOpenON,2);
		runSolution(g, dijkstra,as);

		System.out.println("\n Zone Swt eletrified found ->"+countPower  +"& the number total of zones not energized is "+ (countNopower));


		for (Edge edge : dijkstra.getTreeEdges()) {
			edge.addAttribute("ui.style", "fill-color: red;");
			edge.addAttribute("ui.style", " size: 0.5px; fill-color: #CCC; stroke-mode: plain; stroke-color: #999; shadow-mode: plain; shadow-width: 3px; shadow-color: #FC0; shadow-offset: 0px;");
			edge.setAttribute("ui.class", "marked");
			sleep();
		}

		// Print the shortest path from A to B
		//		System.out.println(dijkstra.getPath(g.getNode("1")));

		// Build a list containing the nodes in the shortest path from A to B
		// Note that nodes are added at the beginning of the list
		// because the iterator traverses them in reverse order, from B to A
		//		List<Node> list1 = new ArrayList<Node>();
		//		for (Node node : dijkstra.getPathNodes(g.getNode("300")))
		//			list1.add(0, node);

		// A shorter but less efficient way to do the same thing
		//		List<Node> list2a = dijkstra.getPath(g.getNode("300")).getNodePath();
		//		System.out.println(list2);
		// cleanup to save memory if solutions are no longer needed
		
		dijkstra.clear();

		// Now compute the shortest path from A to all the other nodes
		// but taking the number of nodes in the path as its length
		//		dijkstra = new Dijkstra(Dijkstra.Element.NODE, null, null);
		//		dijkstra.init(g);
		//		dijkstra.setSource(g.getNode("1"));
		//		dijkstra.compute();

		// Print the lengths of the new shortest paths
		//		for (Node node : g)
		//			System.out.printf("%s->%s:%10.2f%n", dijkstra.getSource(), node,dijkstra.getPathLength(node));

		// Print all the shortest paths between A and F
		//		Iterator<Path> pathIterator = dijkstra.getAllPathsIterator(g.getNode("300"));
		//		while (pathIterator.hasNext())
		//			System.out.println(pathIterator.next());

	}

	public static Graph exampleGraph() {
		//		Graph g = new SingleGraph("example");
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		Graph graph = new MultiGraph("Test");
		sman = new SpriteManager(graph);


		//		graph.setStrict(true);
		//		graph.setAutoCreate( true );

		//		graph.addAttribute("layout.gravity", 2);
		//		layout.weight
		//		A.addAttribute("layout.frozen");
		graph.addAttribute("ui.antialias", true);
		//		graph.addAttribute("ui.stylesheet", "node { size-mode: dyn-size; text-background-mode: plain; text-visibility-mode: at-zoom; text-alignment: under;} edge {fill-color:grey;}");
		graph.addAttribute("ui.stylesheet", "node { "
				+ "size-mode: dyn-size; "
				//				+ "text-background-mode: rounded-box;"
				//				+ "text-background-color: white; "
				+ "text-alignment: right; "
				+ "text-color: blue; "
				+ "text-offset: 8, 7;"
				+ "text-size:10 "
				+ ";} "
				+ "edge {fill-color:grey;"
				+ "text-size:8;}"
				+ "graph {padding :9px; }"
				);


		String auxRead ="empty";
		while (auxRead != null ){ 
			Iterable<String> pieces= Splitter.onPattern("[,]").trimResults().omitEmptyStrings().split(auxRead);
			if(auxRead!="empty") {
				String nome = Iterables.get(pieces, 0);
				String corX = Iterables.get(pieces, 1);
				String corY = Iterables.get(pieces, 2);

				//				if(nome.contains("R")) {
				//					System.out.println(nome);
				//					graph.addNode(nome).addAttribute("xy",Double.parseDouble(corX),Double.parseDouble(corY));
				//					Node aux = graph.getNode(nome);
				//					aux.addAttribute("ui.stylesheet", "shape: triangle");
				//					
				//				}
				//				if(nome.contains("_OPEN"))
				//					System.out.println(nome);

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

				//				if(eleName.contains("TRANFORMER"))
				//					System.out.println(eleName);
				//				
				//				if(eleName.contains(".SW"))
				//					System.out.println(eleName);
				//				
				//				if(eleName.contains("Transformer")) {
				//					System.out.println(eleName);
				//					System.out.println(FromBus);
				//					System.out.println(to_Buss);
				//					System.out.println(lenghLn);
				//					
				//				}
				graph.addEdge(eleName,FromBus,to_Buss).addAttribute("length",Double.parseDouble(lenghLn));
			}
			kk++;	
			auxRead2 = readLine(kk,"C:/Users/matheus/Desktop/TESE/workspace/testeGUI/src/testeGUI/coordFromTo_ieee123.txt");
		}

		for (Node n : graph) {

			n.addAttribute("ui.label", n.getId());
			n.addAttribute("ui.size", 13);
			Iterable<?> b =n.getEachEdge();

			//			String ss = Iterables.get(b, 0).toString();

			//			System.out.println(n.getId().toString());

			if(n.hasEdgeToward(n.getId()))
				System.out.println("Capacitor at:" + n.getId());


			//			("ui.style", "shape: triangle; size: 12, 12; fill-color: black; sprite-orientation: from ;");
			//			("ui.style", "shape: circle; size: 12, 12; fill-color: #CCC; sprite-orientation: from ;");
			//			("ui.style", "shape: triangle;size: 20, 20; fill-color: red; sprite-orientation: to;");

			if(n.getId().contains("R") ) {

				Iterable<?> a =n.getEachEdge();
				String test = a.iterator().next().toString();


				if(test.contains("TR")) {
					n.addAttribute( "ui.hide" );
					sman.addSprite(b.iterator().next().toString()).attachToNode(n.getId());
					sman.getSprite(b.iterator().next().toString().replace(".", "")).setAttribute("ui.style", "shape: triangle;size: 20, 20; fill-color: red; sprite-orientation: to;");
				}
				if(!test.contains("TR")) {
					sman.addSprite(test.replace(".", "")).attachToNode(n.getId());
					sman.getSprite(test.replace(".", "")).setAttribute("ui.style"," shape: circle; size: 13, 13; fill-color: #CCC; sprite-orientation: to; stroke-mode: plain; shadow-color: black; shadow-width: 3px; stroke-color: #999;");
				}
			}

			if(b.iterator().next().toString().contains("Tran")) {
				n.addAttribute( "ui.hide" );
				sman.addSprite(b.iterator().next().toString().replace(".", "")).attachToNode(n.getId());
				sman.getSprite(b.iterator().next().toString().replace(".", "")).setAttribute("ui.style", "shape: triangle; size: 12, 12; fill-color: black; sprite-orientation: from ;");
			}

		}


		for (Edge e : graph.getEachEdge()) {
			e.addAttribute("ui.label", "" + e.getNumber("length"));

			//						System.out.println("From--->"+e.getNode0().toString()+"  To---->"+e.getNode1().toString());
			//						System.out.println(e.getId().toString());

//			if(e.getId().toString().contains("SW")) {
				//								e.addAttribute( "ui.hide" );

				//								System.out.println(e.getId());
				//						        System.out.println(e.getNode0().toString());
				//								System.out.println(e.getNode1().toString());
				//								System.out.println("**********************");

				//				if(!e.getId().toString().equals("Line.SW2") | !e.getId().toString().equals("Line.SW4")) {
				//					swithces.add(e.getId());
				//					swithces.add(e.getNode0().toString());
				//					swithces.add(e.getNode1().toString());
				//					swT.put(e.getId(), swithces);
				//					swithces = new ArrayList<String>();
				//				}

				//				if(e.getNode1().toString().contains("_OPEN")) {
				//					sman.addSprite(e.getId().toString().replace(".", "")).attachToEdge(e.getId());
				//					sman.getSprite(e.getId().toString().replace(".", "")).setPosition(1);
				//					sman.getSprite(e.getId().toString().replace(".", "")).setAttribute("ui.style"," shape: circle; size: 8, 8; fill-color: red; sprite-orientation: to; stroke-mode: plain; shadow-color: black; shadow-width: 3px; stroke-color: #999;");
				//
				//					String swAux =e.getNode1().toString();
				//					String[] splitSw=swAux.split("[ _ ]");
				//
				//					graph.addEdge(swAux+"_aux",e.getNode1().toString(),splitSw[0].toString()).addAttribute("length",0.008);
				//				}
//				if(!e.getNode1().toString().contains("_OPEN")) {
//					sman.addSprite(e.getId().toString().replace(".", "")).attachToEdge(e.getId());
//					sman.getSprite(e.getId().toString().replace(".", "")).setAttribute("ui.style"," shape: box; size: 9,9; fill-color: red; sprite-orientation: projection;");
//					sman.getSprite(e.getId().toString().replace(".", "")).setPosition(0.5);
//				}
//			}
		}
		//		System.out.println(swT);
		return graph;
	}

	public static Multimap<String, List<String>> getSwt(Graph g, boolean allopen, int i){
		int i_aux=1;
		for (Edge e : g.getEachEdge()) {
			if(e.getId().toString().contains("SW")) {
				swithces.add(e.getId());
				swithces.add(e.getNode0().toString());
				swithces.add(e.getNode1().toString());
				swT.put(e.getId(), swithces);
				swithces = new ArrayList<String>();

				if(e.getNode1().toString().contains("_OPEN") & allopen ) {
					//						   Normaly Open switches for the 123 bus bar
					//						   g.removeEdge("Line.SW7");
					//						   g.removeEdge("Line.SW8");
					if(i==i_aux & i!=0 | i==99) {
						sman.addSprite(e.getId().toString().replace(".", "")).attachToEdge(e.getId());
						sman.getSprite(e.getId().toString().replace(".", "")).setPosition(1);
						sman.getSprite(e.getId().toString().replace(".", "")).setAttribute("ui.style"," shape: circle; size: 8, 8; fill-color: cyan; sprite-orientation: to; stroke-mode: plain; shadow-color: black; shadow-width: 3px; stroke-color: #999;");
						System.out.println("Closing Switch simulation in ---->>"+e.getId().toString());
						String swAux =e.getNode1().toString();
						String[] splitSw=swAux.split("[ _ ]");

						g.addEdge(swAux+"_aux",e.getNode1().toString(),splitSw[0].toString()).addAttribute("length",0.008);
						g.getEdge(swAux+"_aux").changeAttribute("ui.style","size: 8; fill-color: red;");
					}
					i_aux++;
				}

			}

		}
		//		System.out.println(swT);
		return swT;
	}

	public static void runSolution(Graph g, Dijkstra dijkstra, String[] as) {

		Iterator<String> keySetIterator = swT.keySet().iterator(); 
		countNopower=0;
		countPower=0;
		zonaWthPower.clear();
		//		int i =0;
		while(keySetIterator.hasNext()){ 
			String key = keySetIterator.next();

			//			System.out.println("**************************************");
			//			System.out.println("               "+i);
			String aa =swT.get(key).iterator().next().get(0).toString().trim();
			//			String bb =swT.get(key).iterator().next().get(1).toString().trim();
			String cc =swT.get(key).iterator().next().get(2).toString().trim();


			//			System.out.println("key: " + key + " value: " + aa);
			//			System.out.println("key: " + key + " value: " + bb);
			//			System.out.println("key: " + key + " value: " + cc);
			//			System.out.println(g.getEdge(aa).getId().toString());

			if(cc.contains("_OPEN")) {
				dijkstra.setSource(g.getNode(cc.replace("_OPEN","")));
			}
			if(!cc.contains("_OPEN")) {
				dijkstra.setSource(g.getNode(cc));
			}

			//			System.out.println("Node to Path* Node--------->>"+nodeUsing);
			//			System.out.println(nodeUsing);

			dijkstra.compute();
			//			System.out.println(as.length);

			while(ii < 1 ) {
				//			System.out.println(as[ii]);	

				//				if(swT.get(as[ii]).iterator().next().get(2).isEmpty()) {
				//				System.out.println(swT.get(as[ii]).iterator().next().get(2));
				//				System.out.println(g.getNode(swT.get(as[ii]).iterator().next().get(2)).toString());
				sourceNodeof = g.getNode(swT.get(as[ii]).iterator().next().get(2)).toString();
				//				System.out.println("Source Node--------->>"+sourceNodeof.replace("_OPEN",""));


				for (Node node : dijkstra.getPathNodes(g.getNode(swT.get(as[ii]).iterator().next().get(2)))) {
					node.addAttribute("ui.style", "fill-color: blue;");
					node.changeAttribute("ui.style", "fill-color: black;");
				}

				// Color in red all the edges in the shortest path tree
				for (Edge edge : dijkstra.getPathEdges((g.getNode(swT.get(as[ii]).iterator().next().get(2))))) {

					//					System.out.println("node Id--->"+edge.getId().toString());
					//					System.out.println(edge.getNode0().toString()); 
					//					System.out.println(edge.getNode1().toString()); 

					if(edge.getId().toString().contains("SW")) {
						list2.add(0, edge);

						//					System.out.println("\n\n"+list2+"\n\n");
					}
					edge.addAttribute("ui.style", "fill-color: red;");
					edge.addAttribute("ui.style", " size: 2px; fill-color: red; ");
					edge.setAttribute("ui.class", "marked");
					sleep();
				}

				for (Edge edge : dijkstra.getPathEdges((g.getNode(swT.get(as[ii]).iterator().next().get(2))))) {

					edge.changeAttribute("ui.style", "fill-color: red;");
					edge.changeAttribute("ui.style", " size: 1px; fill-color: black; stroke-mode: plain; stroke-color: #999; shadow-mode: plain; shadow-width: 3px; shadow-color: #FC0; shadow-offset: 0px;");
					edge.changeAttribute("ui.class", "marked");
				}

				ii++;
			}

			ii=0;

			//			System.out.println(list1.size());
			//			System.out.println(list1);
			//			System.out.println(list2.size());
			//			System.out.println(list2);
			//			System.out.println(list2.size());
			//			System.out.println(cc);
			//          System.out.println(sourceNodeof);
			//			System.out.println(sourceNodeof.equals(cc));
			//			System.out.println(cc.equals(sourceNodeof));

			if(list2.isEmpty() & !cc.equals(sourceNodeof) ){
				countNopower++;
				//            	System.out.println("---->> Zona desligada da fonte: "+ sourceNodeof +"  AO NÓ "+ nodeUsing);
				zonaWthPower.put(countNopower,aa);

			}	

			if(list2.isEmpty() & cc.equals(sourceNodeof) ){
				//        				System.out.println("---->> Zddddddddd "+ sourceNodeof +"  AO NÓ "+ nodeUsing);
				countPower++;
			}	

			if(!list2.isEmpty() | list2.isEmpty() & cc.equals(sourceNodeof) & !keySetIterator.hasNext()) {
				//				System.out.println("--->> ZONA LIGADA A FONTE: "+ sourceNodeof +"  o nó "+ nodeUsing);
				countPower++;
			}

			list1 = new ArrayList<Node>();
			list2 = new ArrayList<Edge>();

			//			System.out.println(zonaWthPower);
		}

	}

	protected static void sleep() {
		try { Thread.sleep(50); } catch (Exception e) {}
	}

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


}
