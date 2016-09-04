package testeGUI;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.graphstream.algorithm.Dijkstra;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.spriteManager.SpriteManager;
import org.graphstream.ui.view.Viewer;

import com.google.common.base.Splitter;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multimap;

public class ieee123dmsGUIvalidation{
	private static final String[] Plans = new String[7];
	private static String isolationPlan;
	private static int kk =0;
	private static List<String> swithces= new ArrayList<String>();
	private static Multimap< String , List<String>> swT = ArrayListMultimap.create();
	private static int ii =0;
	private static int ik;
	private static SpriteManager sman;
	private static Multimap< Integer , String> zonaWthPower = ArrayListMultimap.create();
	private static Multimap< String , String> plans = ArrayListMultimap.create();
	private static HashMap< String , Integer> solutionsw1 = new HashMap< String,Integer> ();
	private static Multimap< String , Integer> solutionsw2 = ArrayListMultimap.create();
	private static Multimap< String , String> solutionDMS = ArrayListMultimap.create();
	private static String sourceNodeof;
	static List<Node> list1 = new ArrayList<Node>();
	static List<Edge> list2 = new ArrayList<Edge>();
	private static int countNopower;
	private static int countPower;
	private static int ok;
	private static String getNodeSwitch1;
	private static String getNodeSwitch2;
	private static String restoraionPlan;
	private static HashMap<String, Integer> phase1;
	private static HashMap<String, Integer> phase2;
//	private static HashMap<String, Integer> phase3;
	private static Graph g ;
	private static String LineOfFalt;

	/**
	 * This class abstracts the DMS plan creation graphic Validation 
	 * @author Matheus Macedo Lopes
	 * @version final 1.0
	 **/
	public static Multimap<String, String> main(String[] args, String line ) throws InterruptedException {
		
				
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		Graph graph = new MultiGraph("Test");
		
		sman = new SpriteManager(graph);
		g = exampleGraph(graph);
        // g.display(false);
		sman = new SpriteManager(g);
		
		Viewer viewer = new Viewer(g, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
		
		viewer = g.display(false);

		
		
		g.getEdge("Line.SW1").changeAttribute("ui.style","size: 8; fill-color: green;");
		g.getEdge("Line.SW2").changeAttribute("ui.style","size: 8; fill-color: green;");
		g.getEdge("Line.SW3").changeAttribute("ui.style","size: 8; fill-color: green;");
		g.getEdge("Line.SW4").changeAttribute("ui.style","size: 8; fill-color: green;");
		g.getEdge("Line.SW5").changeAttribute("ui.style","size: 8; fill-color: green;");
		g.getEdge("Line.SW6").changeAttribute("ui.style","size: 8; fill-color: green;");
		g.getEdge("Line.SW7").changeAttribute("ui.style","size: 8; fill-color: blue;");
		g.getEdge("Line.SW8").changeAttribute("ui.style","size: 8; fill-color: blue;");
		Thread.sleep(5000);
		
		// The default action when closing the view is to quit
		// the program.
		viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.CLOSE_VIEWER);

		// connect back the viewer to the graph,
		// the graph becomes a sink for the viewer.
		// also install  as a viewer listener to
		// intercept the graphic events.
		// Edge lengths are stored in an attribute called "length"
		// The length of a path is the sum of the lengths of its edges
		Dijkstra dijkstra = new Dijkstra(Dijkstra.Element.EDGE, null, "length");

		// Compute the shortest paths in g from A to all nodes
		dijkstra.init(g);
		boolean normlyOpenON= true;
		String faltswEdge1  = args[0].toString();
		String faltswEdge2  = args[1].toString();
		 LineOfFalt = line;

		int plan1=1;
		int plan2=2;

		if(faltswEdge1.equals("Line.SW5")| faltswEdge2.equals("Line.SW5")) { 	
			plan1=2;  
			plan2=1;}
		
		if(faltswEdge1.equals("Line.SW4") & faltswEdge2.equals("Line.SW8")) {
			plan1=1; 
			Thread.sleep(1000);
			g.removeEdge("Line.SW5");
			plan2=1;}
		
		/**************************************************************/
		/**************************************************************/
		/**************************************************************/

		getSwt(g,normlyOpenON,0);

		Iterator<String> ds = swT.keySet().iterator();
		String[] as= new String[swT.size()];
		
		ik=0;
		while(ds.hasNext()){ 	
			as[ik]=ds.next();	
			ik++;   }

		System.out.println("Genarating coodination Plan for Switch Agents [ "+faltswEdge1.replace("Line.", "Ag_")  +" & "+ faltswEdge2.replace("Line.", "Ag_") +" ]");
		System.out.println("Getting switches Actual Condition ...");

		if(faltswEdge1.contains("Line"))
			getNodeSwitch1 = g.getEdge(faltswEdge1).getNode1().getId().toString();
		if(faltswEdge2.contains("Line"))
			getNodeSwitch2 = g.getEdge(faltswEdge2).getNode1().getId().toString();


		ok=0;
		System.out.println("Agent isolation Action Executed Already...");
		while(ok<as.length) {
			if(faltswEdge1.equals(as[ok].toString()) | faltswEdge2.equals(as[ok].toString())) {
				g.getEdge(as[ok].toString()).changeAttribute("ui.style", " size: 5px; fill-color: red; stroke-mode: plain; stroke-color: gray; shadow-mode: plain; shadow-width: 3px; shadow-color: gray; shadow-offset: 0px;");
				Thread.sleep(1000);
				g.removeEdge(as[ok].toString());
				if(faltswEdge1.contains(as[ok].toString()))
					System.out.println("Switch in falt Zone turnerd OFF, by Agent [" +faltswEdge1.replace("Line.", "Ag_") +"] ---> "+as[ok].toString());
				if(faltswEdge2.contains(as[ok].toString()))
					System.out.println("Switch in falt Zone turnerd OFF, by Agent [" +faltswEdge2.replace("Line.", "Ag_") +"] ---> "+as[ok].toString());
//				ok++;
			}
			if(as[ok].toString().contains("Line.SW8") | as[ok].toString().contains("Line.SW7")) {
				System.out.println("Switch Normally OPEN >> "+as[ok].toString());
//				ok++;
			}
			Thread.sleep(300);
			ok++;
		}
		ok=0;
		
		System.out.println("      Agent DMS graphic API of the  generating solution using Dijkstra Short path Algorithm for : \n "
				+ "(1) ---> Zone Isolation & Swtih Coordination \n"
				+ "(2) ------> Line Isolation \n"
				+ "Generated Plans To be executed in Ocurrency of Falt...");
		
		System.out.println("\n/*********Reading Grid state after zone Isolation occurency the ...***********/");
		
		/**************************************************************/
		/**************************************************************/
		/**************************************************************/
		runSolution(g, dijkstra,as,0);
		
		Iterator<String> getPlan = phase1.keySet().iterator();
		while(getPlan.hasNext()) {
			String key = getPlan.next().toString();
			System.out.println("#Switch Plan1 : Got to pe Close----> " +key);
		}

		
		
        Edge e = g.getEdge(LineOfFalt);
		dijkstra.setSource(g.getNode(e.getNode0().getId().toString()));
		dijkstra.compute();
		
//		outerloop:
			for (Edge edge : dijkstra.getTreeEdges()) {
//				if(edge.getId().equals("Line.SW1"))
//					break outerloop;
				edge.addAttribute("ui.style", " size: 2px; fill-color: #CCC; stroke-mode: plain; stroke-color: gray; shadow-mode: plain; shadow-width: 3px; shadow-color: gray; shadow-offset: 0px;");
				edge.setAttribute("ui.class", "marked");
				sleep();
			}

		
		dijkstra.setSource(g.getNode("149"));
		dijkstra.compute();



		for (Edge edge : dijkstra.getTreeEdges()) {
			//			edge.addAttribute("ui.style", "fill-color: blue;");
			edge.addAttribute("ui.style", " size: 0.5px; fill-color: #CCC; stroke-mode: plain; stroke-color: #999; shadow-mode: plain; shadow-width: 3px; shadow-color: #FC0; shadow-offset: 0px;");
			edge.setAttribute("ui.class", "marked");
			sleep();
		}
				
		System.out.println("\n Zone Swt eletrified found ->"+countPower +" & the number total of zones not energized is "+ (countNopower-2));
		
		/**************************************************************/
		/**************************************************************/
		System.out.println("\n ----> Geting Zones by Agents ... ");
		String[] ZonesBydefault= {
				"Line.SW1 (150R 149) Agents ---> 150,150R,149,1,2,3,4,5,6,7,8,9,9R,14,11,10,12,13,34,16,17,18,19,20,21,22,23,24,25,25R,26,27,31,32,37,33,28,29,30,250 "
				,"Line.SW2 (13   152) Agents ---> 152,52,53,54,55,56,57,58,59,60,61,61S,62,63,64,65,66,160,160R,97,197,610,197,197R "
				,"Line.SW3 (18   135) Agents ---> 135,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,151,300_OPEN"
				,"Line.SW4 (60   160) Agents ---> 67,68,69,70,71,97,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,94_OPEN "
				,"Line.SW5 (97   197) Agents ---> 98,99,100,450,101,102,103,104,105,106,107,108,109,110,111,112,113,114,300_OPEN"
				,"Line.SW6 (61   61S) Agents ---> 61S,610"
				,"Line.SW7 (151 300_OPEN) Agents ---> 300,300_OPEN"
				,"Line.SW8 (54 94_OPEN) Agents ---> 94,94_OPEN"};


		for(int kk=0 ; kk<ZonesBydefault.length ; kk++)
			System.out.println(ZonesBydefault[kk].toString());
		/**************************************************************/
		/**************************************************************/

		System.out.println("\n ----> Geting NOT  Energyzed Switch Zones... ");
		Iterator<Integer> keySetIterator32 = zonaWthPower.keySet().iterator(); 
		while(keySetIterator32.hasNext()) {
			int asd = keySetIterator32.next();
			if(!zonaWthPower.get(asd).iterator().next().toString().contains("Line.SW8") & !zonaWthPower.get(asd).iterator().next().toString().contains("Line.SW7"))
				System.out.println("Zone ->"+ asd+ " Zone Swt with No Power found ->"+zonaWthPower.get(asd).iterator().next());
//			Thread.sleep(300);
		}

//		Thread.sleep(300);
		System.out.println("\n Zone Swt eletrified found ->"+countPower +" & not energized : "+ (countNopower-2));
//		Thread.sleep(300);
		System.out.println("Calculating Solution Using Dijkstra short path algorithm and Agents coorination...");
//		Thread.sleep(300);
		System.out.println("Setting source Node...Ag/Bus 149 ---> Transformer ");
		
		System.out.println("Generation DMS plan for isolation NORMALY OPEN SWITCH coordination ");
		/**************************************************************/
		/**************************************************************/
		/**************************************************************/
		
		getSwt(g,normlyOpenON,plan1);
		
		
		dijkstra.setSource(g.getNode("149"));
		dijkstra.compute();
		for (Edge edge : dijkstra.getTreeEdges()) {
			//			edge.addAttribute("ui.style", "fill-color: blue;");
			edge.addAttribute("ui.style", " size: 0.5px; fill-color: #CCC; stroke-mode: plain; stroke-color: #999; shadow-mode: plain; shadow-width: 3px; shadow-color: #FC0; shadow-offset: 0px;");
			edge.setAttribute("ui.class", "marked");
//			sleep();
		}
	  
			
		//paintEdges(faltswEdge1, faltswEdge2, g, dijkstra);    
		runSolution(g, dijkstra,as,1);
	//	paintEdges(faltswEdge1, faltswEdge2, g, dijkstra);   

		/*
		Iterator<String> getPlan2 = phase2.keySet().iterator();
		while(getPlan2.hasNext()) {
			String key = getPlan2.next().toString();
			System.out.println(key);
		}
		*/
		    
			/**************************************************************/
			/**************************************************************/
			/**************************************************************/
		    
		System.out.println("\n Zone Swt eletrified found ->"+countPower +" & the number total of zones not energized is "+ (countNopower-2));
		System.out.println("Generated Plan for task (1) ---> Zone Isolation & Swtih Coordination");
		System.out.println("The Plan Generated to be executed in this Scenario are ask Agent responsable for --->"+ isolationPlan +" Close Normalçy Open Switch\n");
		Thread.sleep(500);

		
		if(plan1==2) {
//		    Plans[plan1]="2";
		    Plans[plan2]="NONE";
		}
		if(plan1==1) {
		       Plans[plan2]="NONE";
		}
		
		Plans[3]=" NONE ";
		Plans[4]=" NONE ";
		System.out.println("DMS Graphic plan API finisched and sending plan to DMS Agent to orquestrate reconfiguration...");
		System.out.println(Arrays.toString(Plans));
		System.out.println("PLANS:");
		System.out.println("PLAN (1 & 2): Close SW ----> "+Plans);
		plans.put("plan1", Plans[plan1].toString());
	//	System.out.println("PLAN (2): Isolate Line e Close SW ---->"+"Linee: "+line+" SWts"+Plans[plan1].toString() +" & "+ Plans[plan2].toString());
		//plans.put("plan2", Plans[plan2].toString());
		//plans.put("plan3", Plans[3].toString());
		//plans.put("plan4", Plans[4].toString());
		
		
		
		return plans;
		
		
		/*************************************************************/
		/*************************************************************/
		/*************************************************************/
//		    Thread.sleep(5000);
//		    viewer.close();
		/*************************************************************/
		/*************************************************************/
		/*************************************************************
		    
		    
		    
		//System.out.println("\n--------* Generatin Plan for line isolation in order to Maximize Load Continuity  *----------");
//		Thread.sleep(30);
//		List<Node> list1s = new ArrayList<Node>();
//		for (Node node : dijkstra.getPathNodes(g.getNode("62")))
//			list1s.add(0, node);
		//System.out.println(list1s);
			//Thread.sleep(300);

			isolateLine(g,LineOfFalt);

//			ok=0;
//			Iterator<String> keySetIterator = swT.keySet().iterator(); 
//			while(keySetIterator.hasNext()){ 
//				String key = keySetIterator.next();
//
//				String aa =swT.get(key).iterator().next().get(0).toString().trim();
//				String bb =swT.get(key).iterator().next().get(1).toString().trim();
//				String cc =swT.get(key).iterator().next().get(2).toString().trim();
//				System.out.println("Switch coodination needed----> "+aa +" , "+"("+bb+","+cc+")");
//				solutionDMS.put(aa, (bb+","+cc));
//			}

			//System.out.println("Runing line isolation simulation to generate Plan");

			getSwt(g,normlyOpenON,plan2);
			//sleep();
			//System.out.println("      ---->Generating swithing coordination...");
			//sleep();
			/**
			ok=0;
			while(ok<as.length) {
				if(!faltswEdge1.equals("Line.SW5")){	
					if(faltswEdge1.equals(as[ok].toString()) ) {
						String[] aux = solutionDMS.get(as[ok].toString()).iterator().next().split("[,]");
						//g.addEdge(as[ok].toString(),aux[0].toString(),aux[1].toString()).addAttribute("ui.style","size: 8; fill-color: red;");
						if(plan2==2) {
						//	g.removeEdge("94_OPEN_aux");
							Plans[3]= "Re-Closing Switch Line.SW8";
							}
						if(plan2==1) {
						//	g.removeEdge("300_OPEN_aux");
						//System.out.println("Switch turnerd ON--->"+as[ok].toString());
						    Plans[3]= "Re-Closing Switch Line.SW7";
						}
					}
				}
				//System.out.println(as[ok].toString());

				if(faltswEdge2.equals(as[ok].toString()) ) {
					String[] aux = solutionDMS.get(as[ok].toString()).iterator().next().split("[,]");
					//g.addEdge(as[ok].toString(),aux[0].toString(),aux[1].toString()).addAttribute("ui.style","size: 8; fill-color: red;");
					if(plan1==2) {
						//g.removeEdge("94_OPEN_aux");
						Plans[4]= "Re-Closing Switch Line.SW8";
					}
					if(plan1==1) {
						//g.removeEdge("300_OPEN_aux");
					//System.out.println("Switch turnerd ON--->"+as[ok].toString());
					    Plans[4]= "Re-Closing Switch Line.SW7";
					}
				}
				//System.out.println(as[ok].toString());
				ok++;
			}*/
			
			
			
			
			/*******************************************************/
			/*******************************************************/
			/*******************************************************/
//			Plans[3]=" NONE ";
//			Plans[4]=" NONE ";
//			
//			
//			  return plans;
			
			//System.out.println("   -----> generating Plan");
			//Thread.sleep(300);
//			runSolution(g, dijkstra,as,2);
//          
//			/*
//			Iterator<String> getPlan3 = phase2.keySet().iterator();
//			while(getPlan3.hasNext()) {
//				String key = getPlan3.next().toString();
//				System.out.println(key);
//			}*/
//			
//			System.out.println("\n Zone Swt eletrified found ->"+countPower  +"& the number total of zones not energized is "+ (countNopower-2));
//			System.out.println("Generated Plan for task (2) ---> Line Isolation");
//			System.out.println("The Plan Generated to be executed in this Scenario are ask Agent responsable for --->\n"
//					+"DONT KWOW oque por aqui ainda\n");
//		
//			System.out.println("DMS Graphic plan API finisched and sending plan to DMS Agent to orquestrate reconfiguration...");
//			System.out.println(Arrays.toString(Plans));
//			System.out.println("PLANS:");
//			System.out.println("PLAN (1): Close SW ----> "+Plans[plan1].toString());
//			plans.put("plan1", Plans[plan1].toString());
//			System.out.println("PLAN (2): Isolate Line e Close SW ---->"+"Linee: "+line+" SWts"+Plans[plan1].toString() +" & "+ Plans[plan2].toString());
//			plans.put("plan2", Plans[plan2].toString());
//			System.out.println("PLAN (3): Restore Original Statr of the grid By "+ Plans[3].toString()+" << and >> "+ Plans[4].toString());
//			
//	   /**********COLORING SWITCHES NOTHING TO BE DONE HERE***********/		
//		
//			/**
//			for (Edge edge : dijkstra.getTreeEdges()) {
//			edge.addAttribute("ui.style", " size: 0.5px; fill-color: #CCC; stroke-mode: plain; stroke-color: #999; shadow-mode: plain; shadow-width: 3px; shadow-color: #FC0; shadow-offset: 0px;");
//		}
//
//
//		dijkstra.setSource(g.getNode("83"));
//		dijkstra.compute();
//
//		for (Edge edge : dijkstra.getTreeEdges()) {
//			edge.addAttribute("ui.style", " size: 5px; fill-color: red; stroke-mode: plain; stroke-color: #999; shadow-mode: plain; shadow-width: 3px; shadow-color: #FC0; shadow-offset: 0px;");
//			sleep();
//			edge.addAttribute("ui.style", " size: 0.5px; fill-color: #CCC; stroke-mode: plain; stroke-color: #999; shadow-mode: plain; shadow-width: 3px; shadow-color: #FC0; shadow-offset: 0px;");
//			edge.setAttribute("ui.class", "marked");
//		}
//
//		/**
//		//list1.clear();
//		if(faltswEdge1.contains("Line"))
//			for (Node node : dijkstra.getPathNodes(g.getNode(getNodeSwitch1))) {
//				list1.add(0, node);
//
//				if(node.getId().toString().contains("_OPEN"))
//					setRestoraionPlan(node.getId().toString());
//				System.out.printf("%s->%s:%10.2f%n", dijkstra.getSource(), node,dijkstra.getPathLength(node));
//				//					System.out.println(node.getId().toString());
//			}
//		//System.out.println(list1);
//
//		//list1.clear();
//		if(faltswEdge2.contains("Line"))
//			for (Node node : dijkstra.getPathNodes(g.getNode(getNodeSwitch2))) {
//				list1.add(0, node);
//
//				if(node.getId().toString().contains("_OPEN"))
//					setRestoraionPlan(node.getId().toString());
//				System.out.printf("%s->%s:%10.2f%n", dijkstra.getSource(), node,dijkstra.getPathLength(node));
//				//							System.out.println(node.getId().toString());
//			}
//		//System.out.println(list1); 
//		 * */
//		
//		
//    //    System.out.println(getRestoraionPlan());
//    /**    Thread.sleep(10000);*/
//		
//		dijkstra.clear();
//        return plans;
	}

	public static Graph exampleGraph(Graph graph) {
		graph.clearAttributes();
		graph.clear();
		
		//graph.clear();
		graph.addAttribute("ui.antialias", true);
		graph.addAttribute("ui.stylesheet", "node { "
				+ "size-mode: dyn-size; "
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
			n.addAttribute("ui.size", 13);
			Iterable<?> b =n.getEachEdge();
			if(n.hasEdgeToward(n.getId()))
				System.out.println("Capacitor at:" + n.getId());
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
		}
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
					if(i==i_aux & i!=0 | i==99) {
						sman.addSprite(e.getId().toString().replace(".", "")).attachToEdge(e.getId());
						sman.getSprite(e.getId().toString().replace(".", "")).setPosition(1);
						sman.getSprite(e.getId().toString().replace(".", "")).setAttribute("ui.style"," shape: circle; size: 8, 8; fill-color: cyan; sprite-orientation: to; stroke-mode: plain; shadow-color: black; shadow-width: 3px; stroke-color: #999;");
						System.out.println("\n Nearest Open Switch Closing Simulation... ---->> ["+e.getId().toString()+"] \n");
						
						Plans[i] = e.getId().toString();
						System.out.println("*****////sadas/d/----->>>>>>>>>>>>>>>"+Plans[i].toString());
						
						String swAux =e.getNode1().toString();
						String[] splitSw=swAux.split("[ _ ]");

						g.addEdge(swAux+"_aux",e.getNode1().toString(),splitSw[0].toString()).addAttribute("length",0.008);
						g.getEdge(swAux+"_aux").changeAttribute("ui.style","size: 8; fill-color: red;");
					}
					i_aux++;
				}

			}

		}
		return swT;
	}
	
	public static void runSolution(Graph g, Dijkstra dijkstra, String[] as, int phase) {
		int kk = 0;
		Iterator<String> keySetIterator = swT.keySet().iterator(); 
		countNopower=0;
		countPower=0;
		zonaWthPower.clear();
		while(keySetIterator.hasNext()){ 
			String key = keySetIterator.next();
			String aa =swT.get(key).iterator().next().get(0).toString().trim();
			String cc =swT.get(key).iterator().next().get(2).toString().trim();
			
//			Seting source node for iteration
			if(cc.contains("_OPEN")) 
				dijkstra.setSource(g.getNode(cc.replace("_OPEN","")));
			if(!cc.contains("_OPEN")) 
				dijkstra.setSource(g.getNode(cc));
			    
			
//			System.out.println("\n       Iteration for Finish Node (BUS) ---> "+g.getNode(cc).getId().toString());
			dijkstra.compute();
			sourceNodeof = swT.get(as[kk]).iterator().next().get(0);
			System.out.println("\n Source* Node for Short Path Algorihm ---> "+sourceNodeof+" And Finish Node iteration --->"+swT.get(as[ii]).iterator().next().get(0).toString());
			if( g.hasArray((swT.get(as[kk]).iterator().next().get(2))));
			//			sourceNodeof = g.getNode(swT.get(as[ii]).iterator().next().get(2)).toString();
			while(ii < 1 ) {
				
				/****************************************************/
//				System.out.println("\n Source* Node for Short Path Algorihm ---> "+sourceNodeof+" And Finish Node iteration --->"+ g.getNode(cc).getId().toString());
//				System.out.println("\n Source* Node for Short Path Algorihm ---> "+sourceNodeof+" And Finish Node iteration --->"+swT.get(as[ii]).iterator().next().get(0).toString());
				sourceNodeof = g.getNode(swT.get(as[ii]).iterator().next().get(2)).toString();
				for (Node node : dijkstra.getPathNodes(g.getNode(swT.get(as[ii]).iterator().next().get(2)))) {
					node.addAttribute("ui.style", "fill-color: red;");
					sleep();
					node.changeAttribute("ui.style", "fill-color: black;");
					System.out.printf("From Bus %10.4s     To BUs -> %10s   :%10.2f  (Km)%n", dijkstra.getSource(), node,dijkstra.getPathLength(node));
//					System.out.println("      Retrived Short path node:----> "+node+" To get To Switch Node--->"+ g.getNode(swT.get(as[ii]).iterator().next().get(2)).toString()+"\n");
				}
//				System.out.println("\n Source* Node for Short Path Algorihm ---> "+sourceNodeof+" And Finish Node iteration --->"+g.getNode(swT.get(as[ii]).iterator().next().get(2)).getId().toString());
				for (Edge edge : dijkstra.getPathEdges((g.getNode(swT.get(as[ii]).iterator().next().get(2))))) {
					/****************************************************/
					if(!edge.getId().toString().contains("Line.SW")) 
					System.out.println("      Reading Lines of the paths  ---->"+edge);
					if(edge.getId().toString().contains("Line.SW")) { 
						list2.add(0, edge);
						System.out.println("                       >>>>Swicth Line  FOUND ---->"+edge);
						solutionsw1.put(edge.getId().toString(),ii );
						solutionsw2.put(edge.getId().toString(),ii );
						edge.addAttribute("ui.style", "fill-color: Blue;");
						sleep();
						edge.changeAttribute("ui.style", " size: 2px; fill-color: red; ");
						edge.setAttribute("ui.class", "marked");
					}
					sleep();
				}

				for (Edge edge : dijkstra.getPathEdges((g.getNode(swT.get(as[ii]).iterator().next().get(2))))) {
					edge.changeAttribute("ui.style", "fill-color: red;");
					sleep();
					edge.changeAttribute("ui.style", " size: 1px; fill-color: black; stroke-mode: plain; stroke-color: #999; shadow-mode: plain; shadow-width: 3px; shadow-color: #FC0; shadow-offset: 0px;");
					edge.changeAttribute("ui.class", "marked");
				}

				ii++;
			}
			ii=0;
			
			
			if(list2.isEmpty() & !cc.equals(sourceNodeof) ){
				countNopower++;
				zonaWthPower.put(countNopower,aa);
			}	

			if(list2.isEmpty() & cc.equals(sourceNodeof))
				countPower++;

			if(!list2.isEmpty() | list2.isEmpty() & cc.equals(sourceNodeof) & !keySetIterator.hasNext()) 
				countPower++;
			
			
			if(phase==0)
				phase1=solutionsw1;
			if(phase==1)
				phase2=solutionsw1;
//			if(phase==2)
//				phase3=solutionsw1;
			
			
			
			list2 = new ArrayList<Edge>();
			kk++;
		}
		System.out.println("Zones Energized (or Not Blank)-----> "+countPower);
		
		if(!solutionsw1.isEmpty() & !solutionsw2.isEmpty()) {
			System.out.println("iterations---"+kk);
			System.out.println("Switche(s) found working/or that should be Working (or part of Generated Plan) ->"+solutionsw1);
			System.out.println("Read Node- multiMap->"+solutionsw2);
			}
		kk=0;
	}

	protected static void sleep() {
		try { Thread.sleep(30); } catch (Exception e) {}
	}

	public static void isolateLine(Graph g, String lineToIsolate) {
		for (Edge e : g.getEachEdge()) {
			if(e.getId().toString().contains(lineToIsolate.toString())) {
				g.removeEdge(lineToIsolate);
			}
		}
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
	public static String getRestoraionPlan() {
		return restoraionPlan;
	}
	public static void  paintEdges(String faltswEdge1, String faltswEdge2, Graph g, Dijkstra dijkstra) {

		if(faltswEdge1.equals("Line.SW1") | faltswEdge2.equals("Line.SW1")) {

			dijkstra.setSource(g.getNode("114"));
			dijkstra.compute();
			for (Edge edge : dijkstra.getTreeEdges()) {
				edge.addAttribute("ui.style", " size: 2px; fill-color: #CCC; stroke-mode: plain; stroke-color: gray; shadow-mode: plain; shadow-width: 3px; shadow-color: gray; shadow-offset: 0px;");
				edge.setAttribute("ui.class", "marked");
				sleep();
			}
		}

		if(faltswEdge1.equals("Line.SW3") & faltswEdge2.equals("Line.SW7")) {
			dijkstra.setSource(g.getNode("107"));
			dijkstra.compute();

			for (Edge edge : dijkstra.getTreeEdges()) {
				edge.addAttribute("ui.style", " size: 0.5px; fill-color: #CCC; stroke-mode: plain; stroke-color: #999; shadow-mode: plain; shadow-width: 3px; shadow-color: #FC0; shadow-offset: 0px;");
				edge.setAttribute("ui.class", "marked");
			}
		}


		if(faltswEdge1.equals("Line.SW4") & faltswEdge2.equals("Line.SW8")) {
			dijkstra.setSource(g.getNode("107"));
			dijkstra.compute();

			for (Edge edge : dijkstra.getTreeEdges()) {

				edge.addAttribute("ui.style", " size: 5px; fill-color:  #CCC; stroke-mode: plain; stroke-color: #999; shadow-mode: plain; shadow-width: 3px; shadow-color: #FC0; shadow-offset: 0px;");
				sleep();
				edge.changeAttribute("ui.style", " size: 2px; fill-color: black; stroke-mode: plain; stroke-color: gray; shadow-mode: plain; shadow-width: 3px; shadow-color: gray; shadow-offset: 0px;");
				edge.setAttribute("ui.class", "marked");
			}
		}
	}
	public static void setRestoraionPlan(String restoraionPlan) {
		ieee123dmsGUIvalidation.restoraionPlan = restoraionPlan;
	}
	
	public static List<String> getNodes(String myBusNode) {
			
		 	List<String> nodesToask = new ArrayList<String>();
			Node myNode = g.getNode(myBusNode);
			System.out.println(myNode.getId().toString());
			Iterator<Node> nodeAtachd = myNode.getNeighborNodeIterator();
			
			while(nodeAtachd.hasNext())
				nodesToask.add(nodeAtachd.next().getId().toString());

			return nodesToask;
	}
	
	public static void display(int disp) {
		
		g.clear();
		g.clearAttributeSinks();
		
		if (disp==0)
			System.out.println("Graphic Validation (GUI) OFF");
		if (disp==1)
			g.display(false);
	}
}
