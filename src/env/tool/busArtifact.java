// CArtAgO artifact code for project masReconfigV2

package tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.awt.event.ActionEvent;

import com.google.common.base.Splitter;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multimap;
import com.opencsv.CSVReader;

import cartago.Artifact;
import cartago.INTERNAL_OPERATION;
import cartago.OPERATION;
import cartago.ObsProperty;
import cartago.OpFeedbackParam;
import jason.asSyntax.Atom;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;



/**
 * This class abstracts an artifact model for the ssensors of eache buss
 * @author Matheus Macedo Lopes
 * @version final 1.0
 **/
public class busArtifact extends Artifact {

	private Multimap<String, String> FromTo = ArrayListMultimap.create();
	private Multimap<String, String> Loads = ArrayListMultimap.create();
	private HashMap<String, String> logs   = new HashMap<String, String>();
	private List<String> busData;
	private List<String> busPath;
	private List<String> Lines = new ArrayList<String>();
	private List<String[]> CurrLines = new ArrayList<String[]>();
	private Display display;
	private String frombus;
	private String toBus;
	private String getBuslineFRom = null;
	private String getBuslineTo;
	private String byElement;
	private String bussRealName;
	private String[] retunData;
	private String[] retunData2;
	private Collection<List<String>> lineData;
	private int columNum;
	private int posPanel=0;
	private int faltCall =0;
	private String Zone;
	private List<String> linesThatFalt = new ArrayList<String>();
	private static Graph g;
	private List<String> aa = new ArrayList<String>();
	private Multimap<String, String> swAg = ArrayListMultimap.create();
	int it =0;
	
	@OPERATION public void init(String bussRealName, List<String> busData, List<String> busPath, Collection<List<String>> lineData, String Zone) throws InterruptedException {

		defineObsProperty("countSim",   0);
		defineObsProperty("count", 0);
		defineObsProperty("statusVolt", new Atom("Coud Not reach Voltage sensor"));
		defineObsProperty("statusCurrent", new Atom("Coud Not reach Current sensor"));
		defineObsProperty("numMsg", 0);
		defineObsProperty("realName", new Atom(bussRealName));
		defineObsProperty("zone", new Atom(Zone));
		
		defineObsProperty("voltagePhase1", 0.00);
		defineObsProperty("voltagePhase2", 0.00);
		defineObsProperty("voltagePhase3", 0.00);

		defineObsProperty("current1Phase1", -0.01);
		defineObsProperty("current1Phase2", -0.01);
		defineObsProperty("current1Phase3", -0.01);

		defineObsProperty("current2Phase1", -0.01);
		defineObsProperty("current2Phase2", -0.01);
		defineObsProperty("current2Phase3", -0.01);

		defineObsProperty("current3Phase1", -0.01);
		defineObsProperty("current3Phase2", -0.01);
		defineObsProperty("current3Phase3", -0.01);

		defineObsProperty("current4Phase1", -0.01);
		defineObsProperty("current4Phase2", -0.01);
		defineObsProperty("current4Phase3", -0.01);
		
		defineObsProperty("cuurLine1", new Atom("null"));
		defineObsProperty("cuurLine2", new Atom("null"));
		defineObsProperty("cuurLine3", new Atom("null"));
		defineObsProperty("cuurLine4", new Atom("null"));
		

		defineObsProperty("isOverloaded", false);

		// System.out.println(bussRealName +" "+busData+" "+lineData);
		this.bussRealName = bussRealName;
		this.busPath = busPath;
		this.busData = busData;
		this.lineData = lineData;
		this.Zone = Zone;

		display = new Display("Agent reposansble for BussBar" + bussRealName);
	
		Graph graph = new MultiGraph("Test");
		g = exampleGraph(graph);

//		System.out.println("\n\n ------->> "+ bussRealName );
		
		
//		try { 			Thread.sleep(3000); 		} catch (InterruptedException e) { 	e.printStackTrace();		}
		aa = getNodes(bussRealName.toUpperCase());
//		System.out.println("Node Attached to ------> "+ aa +"  Node(s)  ");
		
		swAg.put("150r","SW1");
		swAg.put("13","SW2");
		swAg.put("18","SW3");
		swAg.put("60","SW4");
		swAg.put("97","SW5");
		swAg.put("61","SW6");
		swAg.put("151","SW7");
		swAg.put("54","SW8");
		
	}
	
	@OPERATION public void incSim() {
		ObsProperty prop = getObsProperty("countSim");
		prop.updateValue(prop.intValue()+1);
	}

	@OPERATION public void getBusRealName(OpFeedbackParam<String> name) {
		name.set(bussRealName);
	}
	
	@OPERATION public void attachedTo() {
		it=0;
		
		while(aa.size() > it) {
			
			if(swAg.containsKey(aa.get(it).toLowerCase())) {
				String ok = swAg.get(aa.get(it).toLowerCase()).toString().replace("[","");
				signal(getOpUserId(),"tickAt",ok.replace("]",""));
			}
			
			if(!swAg.containsKey(aa.get(it).toLowerCase())) {
				
				
				
				signal(getOpUserId(),"tickAt",aa.get(it).toLowerCase());
				
				
			}
			
			it++;
		}
		
	}
	
	@OPERATION public void faltCommunication() {
		it=0;
		
		while(aa.size() > it) {
			
			if(swAg.containsKey(aa.get(it).toLowerCase())) {
				String ok = swAg.get(aa.get(it).toLowerCase()).toString().replace("[","");
				signal(getOpUserId(),"tickFalt",ok.replace("]",""));
			}
			
			if(!swAg.containsKey(aa.get(it).toLowerCase())) {
				signal(getOpUserId(),"tickFalt",aa.get(it).toLowerCase());
			}
			
			it++;
		}
		
	}
	
	@OPERATION public void isclosetosw(OpFeedbackParam<String> YesNo) throws IOException, InterruptedException {
		
		String oye = "no";
		it=0;
		while(aa.size() > it) {
			
//		Msg = "Are you close to energyzed switch... if yes close it now!" ;
		if(swAg.containsKey(aa.get(it).toLowerCase())) {
			Collection<String> testA = swAg.get(aa.get(it).toLowerCase());
			String testeB = testA.iterator().next();
//			System.out.println(testeB);
			SwitchRecon2(testeB);
			oye = "yes";
			break;
		}
		it++;
		}
		Thread.sleep(500);
		YesNo.set(oye);
	}
	
	@OPERATION public void getFaltLine(OpFeedbackParam<String> faltline) {
		faltline.set(linesThatFalt.get(0));
	}
	
	@OPERATION public void getBusLines(OpFeedbackParam<List<String>> name) {
//		Neighbor
		name.set(Lines);
	}

	@OPERATION public void senseCurrents(String busNameVolt) throws IOException {
		faltCall++ ;
		ObsProperty opCurrent1Value1 = getObsProperty("current1Phase1");
		ObsProperty opCurrent1Value2 = getObsProperty("current1Phase2");
		ObsProperty opCurrent1Value3 = getObsProperty("current1Phase3");

		ObsProperty opCurrent2Value1 = getObsProperty("current2Phase1");
		ObsProperty opCurrent2Value2 = getObsProperty("current2Phase2");
		ObsProperty opCurrent2Value3 = getObsProperty("current2Phase3");

		ObsProperty opCurrent3Value1 = getObsProperty("current3Phase1");
		ObsProperty opCurrent3Value2 = getObsProperty("current3Phase2");
		ObsProperty opCurrent3Value3 = getObsProperty("current3Phase3");

		ObsProperty opCurrent4Value1 = getObsProperty("current4Phase1");
		ObsProperty opCurrent4Value2 = getObsProperty("current4Phase2");
		ObsProperty opCurrent4Value3 = getObsProperty("current4Phase3");
		
		ObsProperty opCurrentLine1 = getObsProperty("cuurLine1");
		ObsProperty opCurrentLine2 = getObsProperty("cuurLine2");
		ObsProperty opCurrentLine3 = getObsProperty("cuurLine3");
		ObsProperty opCurrentLine4 = getObsProperty("cuurLine4");
		
		opCurrent1Value1.floatValue();
		opCurrent1Value2.floatValue();
		opCurrent1Value3.floatValue();
		
		opCurrent2Value1.floatValue();
		opCurrent2Value2.floatValue();
		opCurrent2Value3.floatValue();
		
		opCurrent3Value1.floatValue();
		opCurrent3Value2.floatValue();
		opCurrent3Value3.floatValue();
		
		opCurrent4Value1.floatValue();
		opCurrent4Value2.floatValue();
		opCurrent4Value3.floatValue();
		
		if(faltCall<2) {
		log(">>>                Artifact : exogenous environment Sensor (Art_"
				 + bussRealName
				 + ") geting Currents FROM BusBar ...          \n  ");

		System.out.println(
				"\n   Number of bounds/connections to other Busses or Loads or Elements::: " + (lineData.size() - 1));
		}
		// System.out.println(" ALL Data Lines value of Correspondent BussBar obteined from COM simularion:::"+lineData+"\n");
		Iterator<List<String>> keySetIterator = lineData.iterator();
		String realName = bussRealName.toUpperCase();

		// Ex.[[Line.L1, 701, 702], [Line.L35, 799R, 701], [Load.S701A, 701,
		// LOAD], [Load.S701B, 701, LOAD], [Load.S701C, 701, LOAD]]
		// from bus and to bus not Load
		// to bus if fromBus not found

		while (keySetIterator.hasNext()) {
			List<String> key = keySetIterator.next();
			byElement = key.get(0);
			frombus = key.get(1);
			toBus = key.get(2);

			// if(frombus.equals(realName) && !toBus.equals(realName) &&
			// !toBus.equals("LOAD")
			if (frombus.equals(realName) && !toBus.equals(realName)) {
				getBuslineFRom = frombus;
				getBuslineTo = null;
				FromTo.put(frombus, toBus + "," + byElement);
				Lines.add(byElement);
			}

			if (frombus.equals(realName) && toBus.equals("LOAD"))
				Loads.put(frombus, toBus + " by " + byElement);

			if (toBus.equals(realName) && getBuslineFRom == null && getBuslineTo == null) {
				getBuslineFRom = null;
				getBuslineTo = toBus;
				if (lineData.size() == 1) {
					FromTo.put(frombus, toBus + " by " + byElement);
					Lines.add(byElement);
				}
			}
		}
		
		if(faltCall<2) {
		System.out.println("  --------->Geting set reference FromBus::: " + getBuslineFRom +"or ToBus::: " + getBuslineTo);
//		System.out.println("  --------->Geting set reference ToBus::: " + getBuslineTo);
//		System.out.println("  --------->Lines FromBus={ToBus} envolved:::  " + FromTo);
		System.out.println("  --------->##Loads classified : " + Loads+" **Lines classified :  "+ Lines + "\n");
//		System.out.println("  --------->**Lines** set referenced by FromBus::: " + Lines + "\n");
		}
		String[] Currents = new String[21];
		int ls = 0;
		while (ls < Lines.size()) {

			Currents = readResultfileCurr(Lines.get(ls));
			CurrLines.add(Currents);
//			System.out.println(Arrays.toString(Currents));
		    	linesThatFalt.add(Currents[0].toString());
//
			if (busData.size() > 2)
				if (ls < 1)
					if(faltCall<2) {
					System.out.println("Number of Active Element Phases:::" + busData.get(3) + "\n");
			System.out.println(   "   | Element--------------> " + Currents[0].toString() + " \n  "
								+ "   | I(Amp) Phase 1 ------> " + Currents[1].toString() + "     " + "/__ Ang1_1 ----> " + Currents[2].toString()+ "   \n "
					            + "   | I(Amp) Phase 2 ------> " + Currents[3].toString() + "     " + "/__ Ang1_2 ----> " + Currents[4].toString()+ "   \n "
								+ "   | I(Amp) Phase 3 ------> " + Currents[5].toString() + "     " + "/__ Ang1_3 ----> " + Currents[6].toString()+ "   \n "
								// + " | I1_4 ------>  " + Currents[7].toString()   +"  "+ " Ang1_4 -------> " + Currents[8].toString()   +" \n "
								// + " | Iresid1 --->  " + Currents[9].toString()   +"  "+ " AngResid1 ----> " + Currents[10].toString()  +" \n "
								// + " | I2_1 ---->    " + Currents[11].toString()  +"  "+ " Ang2_1 ----> " + Currents[12].toString()  	  +" \n "
								// + " | I2_2 ---->    " + Currents[13].toString()  +"  "+ " Ang2_2 ----> " + Currents[14].toString()  	  +" \n "
								// + " | I2_3 ---->    " + Currents[15].toString()  +"  "+ " Ang2_3 ----> " + Currents[16].toString()     +" \n "
								// + " | I2_4 ---->    " + Currents[17].toString()  +"  "+ " Ang2_4 ----> " + Currents[18].toString()     +" \n "
								// + " | Iresid2 ----> " + Currents[19].toString()  +"  "+ AngResid2 ----> " + Currents[20].toString()    +" \n "
								);
					}
			if (ls == 0) {
				
				float ph1 = Float.parseFloat(Currents[1].toString());
				float ph2 = Float.parseFloat(Currents[3].toString());
				float ph3 = Float.parseFloat(Currents[5].toString());
				
				opCurrent1Value1.updateValue(ph1);
				opCurrent1Value2.updateValue(ph2);
				opCurrent1Value3.updateValue(ph3);
				opCurrentLine1.updateValue(new Atom(Currents[0].toString()));
			}

			if (ls == 1) {
				
			    float ph1 = Float.parseFloat(Currents[1].toString());
				float ph2 = Float.parseFloat(Currents[3].toString());
				float ph3 = Float.parseFloat(Currents[5].toString());
				
				opCurrent2Value1.updateValue(ph1);
				opCurrent2Value2.updateValue(ph2);
				opCurrent2Value3.updateValue(ph3);
				opCurrentLine2.updateValue(new Atom(Currents[0].toString()));
			}

			if (ls == 2) {
				float ph1 = Float.parseFloat(Currents[1].toString());
				float ph2 = Float.parseFloat(Currents[3].toString());
				float ph3 = Float.parseFloat(Currents[5].toString());
				
				opCurrent3Value1.updateValue(ph1);
				opCurrent3Value2.updateValue(ph2);
				opCurrent3Value3.updateValue(ph3);
				opCurrentLine3.updateValue(new Atom(Currents[0].toString()));
			}
			if (ls == 3) {
				float ph1 = Float.parseFloat(Currents[1].toString());
				float ph2 = Float.parseFloat(Currents[3].toString());
				float ph3 = Float.parseFloat(Currents[5].toString());
				
				opCurrent4Value1.updateValue(ph1);
				opCurrent4Value2.updateValue(ph2);
				opCurrent4Value3.updateValue(ph3);
				opCurrentLine4.updateValue(new Atom(Currents[0].toString()));
			}

			ls++;
		}
		getObsProperty("statusCurrent").updateValue(new Atom("~Current BusBarr sensor Finished sensing successfully..."));
	}

	@OPERATION public void senseVoltages(String busNameAmp) throws IOException {
		ObsProperty opVoltstatus = getObsProperty("statusVolt");

		String[] Voltages = new String[20];
		Voltages = readResultfileVolt();
		if(faltCall<1) {
		log(">>>                Artifact : exogenous environment Sensor (Art_" + bussRealName
				+ ") geting Voltages for BusBar ...          \n  ");
		System.out.println("      | Bus ----> " + Voltages[0].toString() + "       " + "   | BasekV ----> "
				+ Voltages[1].toString() + " \n   " + "   | Node1 ----> " + Voltages[2].toString() + "      "
				+ "   | Magnitude1 ----> " + Voltages[3].toString() + "      " + "   | Angle1 ----> "
				+ Voltages[4].toString() + "       " + "   | pu1 ----> " + Voltages[5].toString() + " \n   "
				+ "   | Node2 ----> " + Voltages[6].toString() + "      " + "   | Magnitude2 ----> "
				+ Voltages[7].toString() + "      " + "   | Angle2 ----> " + Voltages[8].toString() + "     "
				+ "   | pu2 ----> " + Voltages[9].toString() + " \n   " + "   | Node3 ----> " + Voltages[10].toString()
				+ "      " + "   | Magnitude3  ----> " + Voltages[11].toString() + "     " + "   | Angle3 ----> "
				+ Voltages[12].toString() + "     " + "   | pu3 ----> " + Voltages[13].toString() + " \n   ");
		}
		ObsProperty opVolage_1_Value = getObsProperty("voltagePhase1");
		ObsProperty opVolage_2_Value = getObsProperty("voltagePhase2");
		ObsProperty opVolage_3_Value = getObsProperty("voltagePhase3");

		opVolage_1_Value.floatValue();
		opVolage_2_Value.floatValue();
		opVolage_3_Value.floatValue();

		float ph1 = Float.parseFloat(Voltages[5].toString());
		float ph2 = Float.parseFloat(Voltages[9].toString());
		float ph3 = Float.parseFloat(Voltages[13].toString());

		opVolage_1_Value.updateValue(ph1);
		opVolage_2_Value.updateValue(ph2);
		opVolage_3_Value.updateValue(ph3);

		opVoltstatus.updateValue(new Atom("~Voltage BusBarr sensor Finished sensing successfully..."));
	}
	/** implements the operation Console History available for the busBarr agent(s) if triggered*/
	@OPERATION public void printMsg(String msg) {
		String agentName = getOpUserName();
		ObsProperty prop = getObsProperty("numMsg");
		prop.updateValue(prop.intValue() + 1);
		display.addText("Agent action at " + System.currentTimeMillis() + " ms " + " from: " + agentName + " : " + msg);
		display.updateNumMsgField(prop.intValue());
	}

	@OPERATION public void busPath ( String  busPatha){
	   
	   busPath.clear();
//	   System.out.println(busPatha);
	   busPatha=busPatha.replace("]", "");
	   
//	   log("\n");
	   Iterable<String> pieces= Splitter.onPattern(",").trimResults().omitEmptyStrings().split(busPatha);
//	   System.out.println(Iterables.size(pieces));
	  
	   int l=0;
	   while(l!=Iterables.size(pieces)){ 
		   busPath.add(Iterables.get(pieces, l));
       l++;
	   }
	}

	@OPERATION public void incClear() {
		ObsProperty prop = getObsProperty("count");
		prop.updateValue(prop.intValue()*0);
	}	
	
	@OPERATION public void inc() {
		ObsProperty prop = getObsProperty("count");
		prop.updateValue(prop.intValue() + 1);
	}
	
	@OPERATION public void getZone( OpFeedbackParam< String > Zonesd) {
		Zonesd.set(Zone);
	}
	
	@INTERNAL_OPERATION void waitIngtime(){	await_time(1000); }

	/** implements the operation Console History available for the busBarr agent(s) if triggered*/
	@OPERATION public void consoleData() {

		if(faltCall !=2) {
		final JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		JPanel panel=new JPanel();
		posPanel = (int) randDouble(1,5);
		frame.setLocation(8*posPanel,8*posPanel*8);
		final JButton b1 = new JButton(" Show:"+ getOpUserName()+ " status Hitory");
		final JButton b4 = new JButton(" Close ");
		
		b1.setVerticalTextPosition(AbstractButton.CENTER);
		b1.setHorizontalTextPosition(AbstractButton.LEADING);
		b1.setMnemonic(KeyEvent.VK_D);
		b1.setActionCommand("disable");
	
		b4.setMnemonic(KeyEvent.VK_E);
		b4.setActionCommand("disable");
		b4.setEnabled(true);
		
		
		panel.add(b1);
		panel.add(b4);
		
		b1.addActionListener(new ActionListener() {		public  void actionPerformed(ActionEvent e) {	display.setVisible(true);	}	});
		b4.addActionListener(new ActionListener() {		public  void actionPerformed(ActionEvent e) {	frame.setVisible(false);	}	});
		
		frame.getContentPane().add(panel);
		frame.pack();
		frame.setVisible(true);
		}
		
	}

	public void SwitchRecon2(String whitch) throws IOException, InterruptedException {

		
		log(""+whitch+"");
		int kk=0;
		String auxRead3 ="empty";
		File myFoo = new File("C:/Users/matheus/Desktop/TESE/workspace/masReconfig_phase3/lib/123Bus/recon.DSS");
		FileWriter fooWriter;	
		try {

			fooWriter = new FileWriter(myFoo, false);
			while (auxRead3 != null){ 

				if(auxRead3.contains("whitch") & !auxRead3.equals("empty") & kk>1 ) {
						fooWriter.write(auxRead3.replace("!",""));
						System.out.println("Ag_"+whitch+" --->Already receivef call to close  ... Status: ! --> \"\" (CLosed) ");
				}
				if(!auxRead3.contains("whitch") & !auxRead3.equals("empty") & kk>1 ) {
					fooWriter.write(auxRead3);
//					System.out.println(auxRead3);
			   }

				auxRead3 = new String();
				auxRead3 = readLine(kk,"C:/Users/matheus/Desktop/TESE/workspace/masReconfig_phase3/lib/123Bus/swrecon.DSS");
				kk++;

				Thread.sleep(3000);
			}
			fooWriter.close();
		}catch (IOException e1) {e1.printStackTrace();} 
		
	}
	
	
	public double randDouble(double bound1, double bound2) {
		double min = Math.min(bound1, bound2);
		double max = Math.max(bound1, bound2);
		return min + (Math.random() * (max - min));
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

	@SuppressWarnings("resource")
	public String[] readResultfileVolt() throws IOException {

		Reader reader = new FileReader(busPath.get(0));
		List<String[]> rows = new CSVReader(reader).readAll();

		for (String[] column : rows) {

			if (column[0].equals(bussRealName.toUpperCase())) {
				columNum = (int) rows.indexOf(column);
				retunData = rows.get(columNum);
				// System.out.println(Arrays.toString(rows.get(columNum)));
				// System.out.println("Found row No. is " +
				// rows.indexOf(column));
			}
		}
		return retunData;
	}

	@SuppressWarnings("resource")
	public String[] readResultfileCurr(String nameLine) throws IOException {

		Reader reader = new FileReader(busPath.get(1));
		List<String[]> rows = new CSVReader(reader).readAll();

		for (String[] column : rows) {

			if (column[0].equals(nameLine)) {
				columNum = (int) rows.indexOf(column);
				retunData2 = rows.get(columNum);
				// System.out.println(Arrays.toString(rows.get(columNum)));
				// System.out.println("Found row No. is " +
				// rows.indexOf(column));
			}

		}

		return retunData2;

	}
	/** Abstract of Agent single console GUI 
	 * @throws InterruptedException **/
	
	public static List<String> getNodes(String myBusNode) throws InterruptedException {
		
	 	List<String> nodesToask = new ArrayList<String>();
		Node myNode = g.getNode(myBusNode);
		     myNode.addAttribute("ui.style", "fill-color: red;");
		     
		Iterator<Node> nodeAtachd = myNode.getNeighborNodeIterator();
		while(nodeAtachd.hasNext()) {
			String okay = nodeAtachd.next().getId().toString();
			nodesToask.add(okay);
		}
		return nodesToask;
}
	
	public static Graph exampleGraph(Graph graph) {
		graph.clear();
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
		int kk=0;

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


		for (Edge e : graph.getEachEdge()) {
			e.addAttribute("ui.label", "" + e.getNumber("length"));
		}
		return graph;
	}

	static class Display extends JFrame {

		private static final long serialVersionUID = 1L;

		private JTextArea text;
		private JLabel numMsg;
		private static int n = 0;

		public Display(String name) {
			setTitle(".:: " + name + " console ::.");

			JPanel panel = new JPanel();
			setContentPane(panel);
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

			numMsg = new JLabel("0");
			text = new JTextArea(15, 40);

			panel.add(text);
			panel.add(Box.createVerticalStrut(5));
			panel.add(numMsg);
			pack();
			setLocation(n * 8, n * 8);
			setVisible(false);

			n++;
		}

		public void addText(final String s) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					text.append(s + "\n");
				}
			});
		}

		public void updateNumMsgField(final int value) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					numMsg.setText("" + value);
				}
			});
		}
	}

}