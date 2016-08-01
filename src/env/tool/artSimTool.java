package tool;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;
//import java.util.concurrent.TimeUnit;

import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import com.google.common.base.Splitter;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multimap;
import OpenDSS.ClassFactory;
import cartago.Artifact;
import cartago.CartagoException;
import cartago.INTERNAL_OPERATION;
import cartago.OPERATION;
import cartago.ObsProperty;
import cartago.OpFeedbackParam;
//import testeGUI.ieee123;
//import testeGUI.ieee123dmsGUIvalidation;
import testeGUI.ieee123dmsGUIvalidation;

/**
 * This class abstracts an artifact model for the distribution system simulation Agent.
 * 	Common Artifact observable proprieties and funtions of  simulationCall and dMS agents.
 * @author Matheus Macedo Lopes
 * @version final 1.0
 **/

//@SuppressWarnings("unused")
public class artSimTool extends Artifact {

	private List<String> cmd          = new ArrayList<String>();
	private List<String> nameElem     = new ArrayList<String>();
	private List<String> elementData  = new ArrayList<String>();
	private List<String> lineDataux   = new ArrayList<String>();
	private List<String> pathResults  = new ArrayList<String>();
	private List<String> toFillData   = new ArrayList<String>();
	private List<String> coordXY      = new ArrayList<String>();
	private List<String> coordXY2     = new ArrayList<String>();
	private List<String> elementData2 = new ArrayList<String>();
	private List<String> auxc         = new ArrayList<String>();
	private List<String> auxListZone         = new ArrayList<String>();
	private Multimap<String, String> linesToFalt		    = ArrayListMultimap.create();
	private Multimap<String, String> Zones 					= ArrayListMultimap.create();
	private Multimap< String  , List<String>> lineAll       = ArrayListMultimap.create();
	private Multimap< Integer , List<String>> XY            = ArrayListMultimap.create(); 
	private Multimap< Integer , List<String>> Points        = ArrayListMultimap.create(); 
	private Multimap< String, String> switchPlan			= ArrayListMultimap.create();
	private HashMap < String  , List<String>> coordinates   = new HashMap<String, List<String >>();
	private HashMap < String  , List<String>> lineData      = new HashMap<String, List<String >>();
	private HashMap < String  , List<String>> lineDataTobus = new HashMap<String, List<String >>(); 
	private String x1y1;
	private String x2y2;
	private String XYcord;
	private String location = tool.artSimTool.class.getProtectionDomain().getCodeSource().getLocation().toString();
	private String pathFind;
	private String runingCase;
	private String origin;
	private String textFile;
	private String textFileCoord;
	private String elements = "_Elements";
	private int entraceGUI=0;
	private int posPanel=0;
	private int cmdSize= 0;
	private int count;
	private int i = 0;
	private int j = 0;
	private int scenario;
	private int systemChosen;
	private int gridBusSize;
	private int entrace2 =0;
	private CharSequence lineToFalt ;
	private String lineToFaltDraw ="soPraTerAlgo";
	private String[] getSWs = new String[2];
	private Multimap<String, String> plans		    = ArrayListMultimap.create();
	private String LineOfFalt;
	private int turnONaux;
	long start = System.currentTimeMillis();

	/*****************************************************************************************
	 *                              --OpenDSS engine--                                       *
	 *										---												  *
	 *  --Build/initialize COM elements of OpenDSS engine, in order to perform simulations   */

	private static  OpenDSS.IDSS 		 dss      	  =  ClassFactory.createDSS();
	OpenDSS.IText dssText =  dss.text();
	OpenDSS.ICircuit DSSCircuit = dss.activeCircuit();
	OpenDSS.IDSSProgress DSSProg = dss.dssProgress();
	OpenDSS.ISettings DSSset = dss.activeCircuit().settings();
	OpenDSS.ILines DSSLine = DSSCircuit.lines();
	OpenDSS.ICktElement DSSelementCk = DSSCircuit.activeCktElement();
	OpenDSS.IDSSElement DSSelement = DSSCircuit.activeDSSElement();
	OpenDSS.ISolution DSSSolution = DSSCircuit.solution();   
	OpenDSS.IBus DSSBus = DSSCircuit.activeBus();  
	OpenDSS.ISwtControls DSSSwt	= DSSCircuit.swtControls();
	OpenDSS.ITopology DSSTop = DSSCircuit.topology();

	/****************************************************************************************/
	/**----------------------------Artifat Variables & Operations--------------------------
	 * @throws InterruptedException 
	 * @throws IOException **/		

	// Metodo que inicia com o artifato sem precisar chama-lo
	// Method that initiate along side artifact creation ... no need to call

	/**Operation Observable properties and first and initiation of the artifact*/
	@OPERATION public void init() throws InterruptedException{
	 // Observable proprieties of the Artifact: simulationCall and dMS
		defineObsProperty("scenario",    Double.MAX_VALUE);
		defineObsProperty("agentNumber",   0);
		defineObsProperty("running",   false);
		defineObsProperty("countSim",   0);
		defineObsProperty("count",   0);

		log("\n\n\n	...Artifact responsable to Sense & Build the grid initiated...\n\n\n	");
		System.out.println("                    Runing Agent-Based Program from source code location:  \n"
				+location.substring(6));
		System.out.println("\n                                              Current IDE workspace:   \n"
				+System.getProperty("user.dir").replace("\\", "/")+"\n\n");

		/**Read text file with all defined zonees******/
						readZones();
	}

	@OPERATION public void start(int scenario, int systemChosen ) throws IOException, CartagoException {

		this.scenario     = scenario;
		this.systemChosen = systemChosen;

		if (getObsProperty("running").booleanValue())
			failed("The System is aither already running or stoped so... you cannot start it!");

		getObsProperty("running" ).updateValue(true);
		getObsProperty("scenario").updateValue(scenario);

		if (scenario==0) {
			SwitchConfigRestore();
			LinesReverseIsolation();
			System.out.format("\n %100s \n","      >>> ...Steady State simulation, Calling...              ");
			/**Get the simulation engine to perform....*/
			cmdSize=0;
			cmd.clear();
			runSimulation(cmd,systemChosen);

		} else if (scenario==1) {
			System.out.format("\n %100s \n","      >>> Short circuit simulation scenario, Calling...              ");
			/**Get the simulation engine to perform....*/
			cmdSize = cmd.size();
			pathResults.clear();
			runSimulation(cmd,systemChosen);

		} else if (scenario==2) {
			System.out.format("\n %100s \n","       >>> Reconfiguration Simulation Approach, Zone Isolated Calling...              ");
			/**Get the simulation engine to perform....*/
			cmdSize=0;
			cmd.clear();	
			pathResults.clear();
			runSimulation(cmd,systemChosen);
		} else if (scenario==3) {
			SwitchConfigRestore();
			System.out.format("\n %100s \n","       >>> Reconfiguration Simulation Approach, Line Isolated & Normal Switch Cofiguration... Calling...              ");
			/**Get the simulation engine to perform....*/
			cmdSize=0;
			cmd.clear();
			pathResults.clear();
			SwitchConfigRestore();
			runSimulation(cmd,systemChosen);
		}
	}

	@OPERATION public void stop(String name)  {

		if (! getObsProperty("running").booleanValue())
			failed("The Systen is not running, why to stop it?!");

		getObsProperty("running").updateValue(false);

		if(scenario == 0) {
			/**Read HashTable with coordinates data*/
			Iterator<String> keySetIter = coordinates.keySet().iterator(); 
			while(keySetIter.hasNext()){ 
				String key = keySetIter.next();
				System.out.println("Coordinates ,  Bus: " + key + " X : " + coordinates.get(key).get(0)+" Y : "+ coordinates.get(key).get(1));
			}
		}
		/**Read HashTable with XY data*/
		Iterator<Integer> keySetIterator = XY.keySet().iterator(); 
		while(keySetIterator.hasNext()){ 
			Integer key = keySetIterator.next();
			x1y1 = getCordinate(XY.get(key).iterator().next().get(0));
			x2y2 = getCordinate(XY.get(key).iterator().next().get(1));

			coordXY2.add(x1y1);
			coordXY2.add(x2y2);
			Points.put(j, coordXY2);
			coordXY2 = new ArrayList<String>();
			j++;
		}
		scenario = 99;
		//		System.out.println(Points);
		long elapsed = System.currentTimeMillis() - start;
		DateFormat df = new SimpleDateFormat("HH 'hours', mm 'mins,' ss 'seconds'");
		df.setTimeZone(TimeZone.getTimeZone("GMT+0"));
		System.out.format("\n %100s \n\n","     >>simulation ended successfully :) ----> thanks to agent:" +"{"+ name+"}");
		System.out.format("\n %100s \n\n","----->Agent action terinated " + df.format(new Date(elapsed)) );
	}

	@OPERATION public void cmd(String code)  {
		System.out.println(code);
		cmd.add(code);
		System.out.println("Command OpenDSS added to simulation---->"+ cmd.get(i) );
		i++;
	}

	@OPERATION public void cmdView(final String nameBusch ) {

		if(scenario != 1) {
			log("get comand view call from Agent:  "+nameBusch);

			final JFrame frame = new JFrame();
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			posPanel = (int) randDouble(1,5);
			frame.setLocation(8*posPanel,posPanel*8);

			JPanel panel = new JPanel();

			final JButton b1 = new JButton(" Show:"+ nameBusch+ " COM OpenDSS Graph of Voltages" );
			final JButton b2 = new JButton(" Show:"+ nameBusch+ " COM OpenDSS Graph of Cuurents" );
			final JButton b3 = new JButton(" Show:"+ nameBusch+ " COM OpenDSS Graph of PowerFlow");
			final JButton b4 = new JButton(" Close"												 );

			b1.setVerticalTextPosition(AbstractButton.CENTER);
			b1.setHorizontalTextPosition(AbstractButton.LEADING);
			b1.setMnemonic(KeyEvent.VK_D );
			b1.setActionCommand("disable");

			b2.setMnemonic(KeyEvent.VK_E );
			b2.setActionCommand("disable");
			b2.setEnabled(true);

			b3.setMnemonic(KeyEvent.VK_E );
			b3.setActionCommand("disable");
			b3.setEnabled(true);

			b4.setMnemonic(KeyEvent.VK_E );
			b4.setActionCommand("disable");
			b4.setEnabled(true);

			panel.add(b1);
			panel.add(b2);
			panel.add(b3);
			panel.add(b4);

			b1.addActionListener(new ActionListener() {	public  void actionPerformed(ActionEvent e) {

				Iterator<List<String>> keySetIteratorch = lineAll.get(nameBusch).iterator(); 
				while(keySetIteratorch.hasNext()) {
					auxc =keySetIteratorch.next();
					String[] auxcd=auxc.get(0).split("[.]");
					dssText.command("visualize Voltages Line."+auxcd[1].toLowerCase());
				}
			}});

			b2.addActionListener(new ActionListener() {	public  void actionPerformed(ActionEvent e) {
				Iterator<List<String>> keySetIteratorch = lineAll.get(nameBusch).iterator(); 
				while(keySetIteratorch.hasNext()) {
					auxc =keySetIteratorch.next();
					String[] auxcd=auxc.get(0).split("[.]");
					dssText.command("visualize Currents Line."+auxcd[1].toLowerCase());
				}
			}});

			b3.addActionListener(new ActionListener() {	public  void actionPerformed(ActionEvent e) {
				Iterator<List<String>> keySetIteratorch = lineAll.get(nameBusch).iterator(); 
				while(keySetIteratorch.hasNext()) {
					auxc =keySetIteratorch.next();
					String[] auxcd=auxc.get(0).split("[.]");
					dssText.command("visualize Powers Line."  +auxcd[1].toLowerCase());
				}
			}});

			b4.addActionListener(new ActionListener() {	public  void actionPerformed(ActionEvent e) {
				frame.setVisible(false);}});

			frame.getContentPane().add(panel);
			frame.pack();
			frame.setVisible(true);
			posPanel++;
		}
	}

	@OPERATION public void cmdClear()  {
		cmd.clear();
	}

	@OPERATION public void setAgentNumber()  {
		getObsProperty("agentNumber").updateValue(gridBusSize);
	}

	@OPERATION public void getAgentName(int AgentNumber,  OpFeedbackParam<String> name, OpFeedbackParam<String> realname)  {
		String auxx = "empty";
		String aux = nameElem.get(AgentNumber).toUpperCase();
		realname.set(nameElem.get(AgentNumber));

		if(lineData.containsKey(aux))
			auxx = lineData.get(aux).get(0);

		if(auxx.contains("sw")) 
			name.set(auxx.toUpperCase());
		if(!auxx.contains("sw"))
			name.set(nameElem.get(AgentNumber));
	}

	@OPERATION public void getBusData(String AgentName, 
			OpFeedbackParam< List<String> > busData ,
			OpFeedbackParam< List<String> > busPath , 
			OpFeedbackParam< Collection<List<String>>> linData,
			OpFeedbackParam< String > Zonesd,
			OpFeedbackParam< String > BPstring){

		busPath.set(pathResults);
		linData.set(lineAll.get(AgentName.toUpperCase()));

		BPstring.set(pathResults.toString().replace("[", ""));

		Iterator<String> keySetIteratore = Zones.keySet().iterator(); 
		while(keySetIteratore.hasNext()){ 
			String key = keySetIteratore.next();
			if(Zones.get(key).iterator().next().contains(AgentName.toUpperCase().trim())) {
				Zonesd.set(key.toString());
				break;
				//	     		System.out.println(key);
			}
		}

		//		System.out.println("\n FROM gets Bus DAta Operation");
		//		System.out.println(pathResults.toString().replace("[", ""));
		//		System.out.println("\n\n");

		if(!lineData.containsKey(AgentName)){
			toFillData.add("there is no data for these Buss");
			busData.set(toFillData);
			toFillData = new ArrayList<String>();
		}

		if(lineData.containsKey(AgentName)){
			busData.set(lineData.get(AgentName));
		}
	}

	@OPERATION public void incSim() {
		ObsProperty prop = getObsProperty("countSim");
		prop.updateValue(prop.intValue()+1);
	}

	@OPERATION public void inc() {
		ObsProperty prop = getObsProperty("count");
		prop.updateValue(prop.intValue()+1);
	}	    

	@OPERATION public void incClear() {
		ObsProperty prop = getObsProperty("count");
		prop.updateValue(0);
	}	  
	
	@OPERATION public void openALLsw() throws IOException, InterruptedException {
		SwitchAlloff();
	}	

	@OPERATION public void plotGrid(){

		if(entraceGUI<1) {
			SwingUtilities.invokeLater(new Runnable() { public void run() { new LinesDrawing().setVisible(true);}});
			entraceGUI++;
		}	
	}

	@OPERATION public void getActionForZone(String isolationZone, OpFeedbackParam< Integer > entraceNum ){
		//		System.out.println("Storing data to isolation Zone");
		auxListZone.add(isolationZone);
		entraceNum.set(auxListZone.size());
		//		System.out.println(auxListZone);
	}

	@OPERATION public void zoneIsolationActionFor(){

		if(auxListZone.size()==2){
			log("Got a request to Isolate the Zone(s): {"+auxListZone.get(0)+","+auxListZone.get(1)+"}");
			try   {	SwitchConfig(auxListZone.get(0),auxListZone.get(1));	} 
			catch ( IOException | InterruptedException e) {		e.printStackTrace();}	
		}
	}

	@OPERATION public void lineIsolationActionFor(){

		log("\n Got a request to Isolate: "+lineToFalt);
		try {	LinesIsolation(lineToFalt);	} catch (IOException e) {	e.printStackTrace(); 	}	
	}

	@OPERATION public void performIsolationPF(){
		// Run a dynamics mode simulation
		try   { start(3,systemChosen); } 
		catch (IOException e) {	e.printStackTrace();} 
		catch (CartagoException e) { e.printStackTrace();}
	}
	
	@OPERATION public void performReconPF(){
		// Run a dynamics mode simulation
		try   { start(2,systemChosen); } 
		catch (IOException e) {	e.printStackTrace();} 
		catch (CartagoException e) { e.printStackTrace();}
	}

	@OPERATION public void callPlanAPI(String LineOfFalt, String Sw1, String Sw2) throws InterruptedException{

//		System.out.println(LineOfFalt);
//		System.out.println(Sw1);
//		System.out.println(Sw2);
//		
		
		String[] getSsWs = {"Line."+Sw1,"Line."+Sw2};
		plans  = ieee123dmsGUIvalidation.main(getSsWs,lineToFaltDraw);
//		System.out.println("call Api plan"+plans);
	}

	@OPERATION public void getActionSwtOpen(String turnONstring) throws InterruptedException, IOException{

		System.out.println("command CLOSE ---->"+turnONstring);
		if(turnONstring.contains("ALL"))
			turnONaux=3;
		if(turnONstring.contains("SW7"))
			turnONaux=1;
		if(turnONstring.contains("SW8"))
			turnONaux=2;

		System.out.println(turnONaux);

		SwitchRecon(turnONaux); 
	}

	@OPERATION public void generatedPlan(String kindaPlan, OpFeedbackParam< String > Plan , OpFeedbackParam< String > swAgent ) throws InterruptedException, IOException{

		System.out.println("genereted Plan--->"+kindaPlan);
		System.out.println("genereted Plan--->"+plans);
		
		
		if(kindaPlan.equals("SwitchON_OFF")) {
			System.out.println(plans.get("plan1").toString());
			String aux = plans.get("plan1").iterator().next();
			System.out.println(aux.replace("Line.", "Ag_"));
			Plan.set(aux);
			swAgent.set(aux.toString().replace("Line.", "Ag_"));
		}
//		if(kindaPlan.equals("RestoringPlan")) {
//			Plan.set("RestoringPlan");
//			Plan.set(plans[3].toString());
//			swAgent.set("Ag_SW7");
//		}
	}

	@OPERATION public void reconfigurationPF() throws InterruptedException{

//		System.out.println("Wait and see");

		try   { start(3,systemChosen); } 
		catch (IOException e) {	e.printStackTrace();} 
		catch (CartagoException e) { e.printStackTrace();}
	}

	@OPERATION public void performFalt(String bussRealName, String LineOfFalt){
		//		Line.L10
		this.setLineOfFalt(LineOfFalt);
		readCoord();
		entraceGUI=0;
		String[] keyAux = new String[5] ;
		entrace2++;
		System.out.println("\n\n-------*/-/-/-- Falt Analysis processing for Bus/BusAgent nº: "+bussRealName);
		//		System.out.println(linesToFalt.get(bussRealName));

		//		linesToFalt.get(bussRealName).size();
		if(entrace2==1) {
			int kl=0;
			String lineOfFalt = null;
			Iterator<String> keySetIteratore = linesToFalt.get(bussRealName).iterator(); 
			while(keySetIteratore.hasNext()){ 
				String key = keySetIteratore.next();
				//				System.out.println(key);
				if(kl == 0) {
					lineOfFalt= key;
					keyAux = key.split("[ . _]");
				}
				kl++;
			}

			
			lineToFalt= "Line.L"+keyAux[2].toString();
			lineToFaltDraw = "Line.L"+keyAux[2].toString();
			
			log("Line Generated for falt simulation ---->"+lineToFalt);

			//      Calculate and export xr values
			cmd.add("Set genmult=0.001");
			cmd.add("Set loadmult=0.001");
			cmd.add("Solve");
			cmd.add("Set mode= faultstudy");
			cmd.add("Solve");
			// Run a dynamics mode simulation
			cmd.add("Set mode= dynamics number=1 stepsize=0.00002");
			cmd.add("Solve");
			cmd.add(lineOfFalt);
			cmd.add("Solve");
			cmd.add("Redirect BusCoordinates");

			try   { start(1,systemChosen); } 
			catch (IOException e) {	e.printStackTrace();} 
			catch (CartagoException e) { e.printStackTrace();}

		}

	}

	@OPERATION public void callForIsolationZonePlan( String AgentZone ,OpFeedbackParam<String> Line_SW1,OpFeedbackParam<String> Line_SW2){


		getSWs = switchPlan.get(AgentZone).iterator().next().split("[ , ]");

		Line_SW1.set(getSWs[0].toString().replace("Line.", ""));
		Line_SW2.set(getSWs[1].toString().replace("Line.", ""));

		//		ieee123.main(getPoints,lineToFalt.toString());

	}

	@INTERNAL_OPERATION void waitIngtime(){	await_time(1000); }

	/********************************************************************
	 * Run Simulation Case tooling an COM server for the OpenDSS engine 
	 * @param cmd 
	 * ******************************************************************/
	public void runSimulation(List<String> cmd, int sysChose) throws IOException{
		entrace2=0;
		//*************COM initialization process******************
		/**repair process environment after odds¬ mess it up*/
		dss.dataPath(System.getProperty("user.dir"));
		/** disallow the engine to mess with the GUI.*/
		dss.allowForms(false);  // doesn't work for every error.*/
		/**initialize simulation tool*/
		dss.start(0);
		/** Set simulation ... */
		dssText.command("clear");

		/*********************************************************************/
		/**---------------OPendDSS Java Abstraction - API -------------------*/
		/**Load IEEE OPenDSS Script for Didtrinbution Grid system simulation */

		//			     Compile IEEE 123 Distribution Grid System OpenDSS Script
		//			     dssText.command("compile [C:/Users/matheus/Desktop/IEEETestCases/123Bus/test123.dss]");
		//			     Compile IEEE 37 Distribution Grid System OpenDSS Script

		pathFind = System.getProperty("user.dir").replace("\\", "/" )+"/lib/";

		if(sysChose ==0)
			origin = pathFind + "34Bus/ieee34.dss";
		if(sysChose ==1)
			origin = pathFind + "37Bus/ieee37.dss";
		if(sysChose ==2)
			origin = pathFind + "123Bus/ieee123.dss";
		if(sysChose ==3)
			origin = pathFind + "LVTestCase/LVTest.dss";
		if (cmdSize > 0)
			origin = pathFind + "ShortCircuitCases/ieee123.dss";

		String auxName = origin.replace(pathFind,"");
		String[] systemName= auxName.split("[ / . ]");
		runingCase = systemName[1].toString();


		/**RUN simulation****/
		dssText.command("compile ["+origin+"]");
		String pathRead = dss.dataPath(); // set path folder of simulation case to read xxxxxxx.dss

		if (cmdSize > 0){
			for(int ii = 0; ii < cmdSize ; ii++){
				dssText.command(cmd.get(ii));
				//			System.out.println(cmd.get(ii));
				//				System.out.println(dssText.result());
			}
		}
		System.out.format("\n %80s \n","     Runing Case: "+runingCase.toUpperCase()+" Distribution system");
		System.out.format("\n %80s \n","     >>>COM OpenDSS engine:::  Running Simulation...");

		/*************************************************************************************************************/
		//if solution is valid print the event log Generated to obtain the steady state or current state of simunation
		if(DSSSolution.converged()) {


			String V = Arrays.deepToString(((Object[]) DSSSolution.eventLog()));
			System.out.println(V);


			/**Returns the number of buses of the proposed network*/
			gridBusSize = DSSCircuit.numBuses();  //System.out.println( " IEEE "+ gridBusSize+" Bus case system");
			/** Path of the stored simulation object results values generated through COM engine  */

			dssText.command("Show Elements");
			textFile = pathRead+systemName[1].toString()+elements+".Txt";


			//			System.out.println(pathRead);		
			//			C:\Users\matheus\Desktop\TESE\workspace\masReconfig_phase3\lib\ShortCircuitCases\

			textFileCoord = pathRead+"BusCoords.dat";

			Runtime.getRuntime().exec("taskkill /IM notepad.exe"); 

			System.out.println("Show Elements --->"+textFile);
			System.out.println("Coordinates --->"+textFileCoord);


			dssText.command("Export Voltages");
			System.out.println("Export Voltages --->"+dssText.result());
			pathResults.add(dssText.result());

			dssText.command("Export Currents");
			System.out.println("Export Currents --->"+dssText.result());
			pathResults.add(dssText.result());

			dssText.command("Export Overloads");
			System.out.println("Export Overloads--->"+dssText.result());
			pathResults.add(dssText.result());

			dssText.command("Export Capacity");
			System.out.println("Export Capacity --->"+dssText.result());
			pathResults.add(dssText.result());

			dssText.command("Export Profile");
			System.out.println("Export Profile   --->"+dssText.result());
			pathResults.add(dssText.result());

			dssText.command("Export Summary");

			System.out.format("\n %100s \n","     >>>COM OpenDSS engine Sumary Simulation Call...");
			dssText.command("Summary");
			System.out.println("\n"+dssText.result());

			readFile();
			// 		         /**Read HashTable with Lines data*/
			// 		         Iterator<String> keySetIterator = lineAll.keySet().iterator(); 
			// 		         while(keySetIterator.hasNext()){ 
			// 		        	 String key = keySetIterator.next();
			// 		        	 System.out.println("key: " + key + " value: " + lineAll.get(key));
			// 		         }
			/**Read HashTable with cordinates X Y data*/
			//		         Iterator<Integer> keySetIteratorx = XY.keySet().iterator(); 
			// 		         while(keySetIteratorx.hasNext()){ 
			// 		        	 Integer key = keySetIteratorx.next();
			// 		        	 System.out.println("key: " + key + " value: " + XY.get(key));
			// 		         }
			readCoord(); 

			//		         /**Read HashTable with coordinates data*/
			//		         Iterator<String> keySetIteratore = coordinates.keySet().iterator(); 
			//		         while(keySetIteratore.hasNext()){ 
			//		        	 String key = keySetIteratore.next();
			//		        	 System.out.println("key: " + key + " value: " + coordinates.get(key));
			//		         }

			readFalt();

			/**Read HashTable with cordinates From To data*/
			//		Iterator<Integer> keySetIteratore = Points.keySet().iterator(); 
			//		while(keySetIteratore.hasNext()){ 
			//			int key = keySetIteratore.next();
			//			System.out.println("key: " + key + " value: " + Points.get(key));
			//		}
			Runtime.getRuntime().exec("taskkill /IM notepad.exe"); 
			//		System.out.println("\n\n FROM run simulation");
			//		System.out.println(pathResults); 
			//		System.out.println("\n\n\n");

			if(scenario==0) {
				System.out.format("\n %80s \n\n","     >>>COM OpenDSS engine:::  Retrieving Results...");
				System.out.println( Arrays.deepToString((Object[])DSSLine.allNames())+"\n");
				System.out.println( Arrays.deepToString((Object[])DSSCircuit.allBusNames())+"\n");
				System.out.println( Arrays.deepToString((Object[])DSSCircuit.allElementNames())+"\n");
				System.out.println( Arrays.deepToString((Object[])DSSCircuit.allNodeNames())+"\n");


				System.out.format("\n %100s \n","     >>>COM OpenDSS engine Line Results ...");


				System.out.format("\n %15s%15s%15s%15s%15s%15s%15s \n","  name   |","  busFrom   |","  busTo   |","  phases   |","  length   |","  normAmps   |","  emergAmps ");
			}
			dss.activeClass();
			DSSLine.first();
			DSSCircuit.firstElement();
			count=0;


			for (int j = 0 ; j<=gridBusSize; j++) { 

				DSSCircuit.setActiveBusi(j); //  arbitrary bus
				nameElem.add(DSSCircuit.activeBus().name()); 	

				for (int jj = 0 ; jj<=DSSLine.count(); jj++)  {


					if(DSSLine.bus1()==null && jj==0 ){

						if(count==0){
							lineDataux.add(lineAll.get(DSSCircuit.activeBus().name().toUpperCase()).iterator().next().get(0));
							lineDataux.add(lineAll.get(DSSCircuit.activeBus().name().toUpperCase()).iterator().next().get(1));
							lineDataux.add(lineAll.get(DSSCircuit.activeBus().name().toUpperCase()).iterator().next().get(2));
							lineDataux.add(Double.toString(DSSCircuit.activeElement().numPhases()));
							lineDataux.add("---");
							lineDataux.add(Double.toString(DSSCircuit.activeElement().normalAmps()));
							lineDataux.add(Double.toString(DSSCircuit.activeElement().emergAmps()));
							if(scenario==0)
								System.out.format(" %10.13s %20.10s %20.10s %20.1s %20.4s %20.3s %20.3s \n", lineDataux.get(0),lineDataux.get(1),lineDataux.get(2),lineDataux.get(3),lineDataux.get(4),lineDataux.get(5),lineDataux.get(6));

							lineData.put(DSSCircuit.activeBus().name().toUpperCase(),lineDataux);
							lineDataTobus.put(lineAll.get(DSSCircuit.activeBus().name().toUpperCase()).iterator().next().get(2).toLowerCase(),lineDataux);
							lineDataux = new ArrayList<String>();

						}
						count++;
					}

					if(DSSLine.bus1()!=null){

						String[] splt1 = DSSLine.bus1().split("[.]");
						String[] splt2 = DSSLine.bus2().split("[.]");
						String fromBus =  splt1[0].toString();
						String toBus   =  splt2[0].toString();

						if(fromBus.equals(DSSCircuit.activeBus().name())){

							lineDataux.add(DSSLine.name());
							lineDataux.add(DSSLine.bus1());
							lineDataux.add(DSSLine.bus2());
							lineDataux.add(Double.toString(DSSLine.phases()));
							lineDataux.add(Double.toString(DSSLine.length()));
							lineDataux.add(Double.toString(DSSLine.normAmps()));
							lineDataux.add(Double.toString(DSSLine.emergAmps()));
							if(scenario==0)
								System.out.format("  %10.4s %20.10s %20.10s %20.1s %20.4s %20.3s %20.3s \n", lineDataux.get(0),lineDataux.get(1),lineDataux.get(2),lineDataux.get(3),lineDataux.get(4),lineDataux.get(5),lineDataux.get(6));
							lineData.put(fromBus.toUpperCase(),lineDataux);
							lineDataTobus.put(toBus.toUpperCase(),lineDataux);
							lineDataux = new ArrayList<String>();
						}

					}

					DSSLine.next();
				}
				DSSLine.first();
				DSSCircuit.nextElement();
			}

			//				System.out.format("\n %100s \n\n","     >>> BussBarr's names retrieved in these simulation.. ");
			//		System.out.println(nameElem);
			//				System.out.println("\n\n");
		}
		if(!DSSSolution.converged())
			System.out.println("Simulatin scenario do not Converged! sorry grid script command not valid");
	} 

	public void readCoord() {
		elementData2.clear();
		coordinates.clear();
		int kk=0;
		String auxRead2 ="empty empty empty";

		while (auxRead2 != null){ 

			Iterable<String> pieces= Splitter.onPattern("[\\s+]")
					.trimResults()
					.omitEmptyStrings()
					.split(auxRead2);

			if(pieces.iterator().hasNext()){
				String	Bus= Iterables.get(pieces, 0);
				String X  =  Iterables.get(pieces, 1);
				String Y  = Iterables.get(pieces, 2);


				elementData2.add(X);
				elementData2.add(Y);
				//					 System.out.println(elementData2);
				if (!coordinates.containsValue(Bus.toUpperCase()) && !Bus.equals("empty") )
					coordinates.put(Bus.toUpperCase(), elementData2);
			}
			auxRead2          = new String();
			auxRead2 = readLine(kk,textFileCoord);
			elementData2 = new ArrayList<String>();
			kk++;
		}
		//			 			 System.out.println(coordinates);
	}

	public void readFalt() {

		int kk=0;


		String auxRead ="empty";
		while (auxRead != null ){ 
			Iterable<String> pieces= Splitter.onPattern("[. _]").trimResults().omitEmptyStrings().split(auxRead);
			int sizeIter = Iterables.size(pieces); 

			if(sizeIter > 2 ){
				String fromBuss= Iterables.get(pieces, 2);
				linesToFalt.put(fromBuss.toUpperCase(), auxRead.trim());
			}
			//		System.out.println(auxRead);
			kk++;	
			auxRead = readLine(kk,"C:/Users/matheus/Desktop/TESE/workspace/masReconfig_phase3/lib/ShortCircuitCases/faltlines.txt");


		}
		//        Iterator<String> keySetIteratore = linesToFalt.keySet().iterator(); 
		//        while(keySetIteratore.hasNext()){ 
		//       	 String key = keySetIteratore.next();
		//       	 System.out.println("key: " + key + " value: " + linesToFalt.get(key));
		//        }
		//		
		//		System.out.println(LinesToFAlt);
		//	    faltLines.set(LinesToFAlt);
	}

	public void readFile() {
		lineAll.clear();
		elementData.clear();
		coordXY.clear();;
		XY.clear();
		int kk=0, ll=0, cc=0;
		String byLine = "empty";
		String auxRead ="empty";
		while (auxRead != null){ 

			if(auxRead != null && kk>8 && !auxRead.isEmpty() ){ 

				Iterable<String> pieces= Splitter.onPattern("[\" \\s+]").trimResults().omitEmptyStrings().split(auxRead);

				int sizeIter = Iterables.size(pieces); 
				if(sizeIter > 2 ){
					byLine  = Iterables.get(pieces, 0);
					String fromBuss= Iterables.get(pieces, 1);
					String toBuss  = Iterables.get(pieces, 2);
					if(!byLine.equals("Power") && !byLine.equals("Element")){

						elementData.add(byLine);
						elementData.add(fromBuss);
						elementData.add(toBuss);
						lineAll.put(toBuss,elementData);
						lineAll.put(fromBuss,elementData);
						//							 System.out.println(elementData+"\n");
						elementData = new ArrayList<String>();

						if(!XY.containsValue(byLine)){

							coordXY.add(fromBuss);
							coordXY.add(toBuss);
							XY.put(cc, coordXY);

							cc++;
							coordXY = new ArrayList<String>();

						}

					}
					if (byLine.equals("Element") && kk>30 )
						ll = kk;
				}

				if (sizeIter ==2 &&  ll>0  ){

					String  busLoad= Iterables.get(pieces, 0);
					String  byName = Iterables.get(pieces, 1);
					elementData.add(busLoad);
					elementData.add(byName);
					elementData.add("LOAD");
					lineAll.put(byName,elementData);
					elementData = new ArrayList<String>();
				}
			}
			//				 System.out.println(auxRead);
			auxRead = new String();
			byLine  = new String();
			auxRead = readLine(kk,textFile);
			kk++;
		}

	}

	public String readLine(int line, String File){
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

	public String getCordinate(String nameBus) {
		Iterator<String> keySetIterator2 = coordinates.keySet().iterator(); 
		while(keySetIterator2.hasNext()){ 
			String key = keySetIterator2.next();
			//  	        	System.out.println(nameBus);
			if(key.equals(nameBus)){

				if(lineData.containsKey(nameBus))
					XYcord = coordinates.get(key).get(0) +"," +coordinates.get(key).get(1)+","+nameBus+","+lineData.get(nameBus).get(3);

				if(lineDataTobus.containsKey(nameBus) && !lineData.containsKey(nameBus))
					XYcord = coordinates.get(key).get(0) +"," +coordinates.get(key).get(1)+","+nameBus+","+lineDataTobus.get(nameBus).get(3);
			}

		}
		return XYcord;
	}

	public double randDouble(double bound1, double bound2) {
		//make sure bound2> bound1
		double min = Math.min(bound1, bound2);
		double max = Math.max(bound1, bound2);
		//math.random gives random number from 0 to 1
		return min + (Math.random() * (max - min));
	}

	public class LinesDrawing extends JFrame implements ActionListener{

		private static final long serialVersionUID = 6765297452402581191L;

		int    centrace  =  0;
		double scalex  = 0.036;
		double scaley  = 0.036;
		double X1, Y1, X2, Y2;
		double numPh , numPh2;
		Graphics2D    g2d, g2;
		AffineTransform tx = new AffineTransform();
		JButton buttonXM, buttonYM, buttonXm, buttonYm;
		double factor = 0.5;

		public LinesDrawing() {
			super("Agent Based Power Grid Drawing - brought by Matheus M. Lopes  ");

			setSize(890, 660);
			setBackground(Color.WHITE);  
			setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			setLocationRelativeTo(null);
			setResizable(true);
			setForeground(Color.BLACK);

			buttonXM = new JButton(" X+ ");
			buttonYM = new JButton(" Y+ ");
			buttonXm = new JButton(" X- ");
			buttonYm = new JButton(" Y- ");

			buttonXM.addActionListener(this);
			buttonYM.addActionListener(this);
			buttonXm.addActionListener(this);
			buttonYm.addActionListener(this);  


			Box box = Box.createHorizontalBox();
			box.add(Box.createHorizontalGlue());
			box.add(buttonXM);
			box.add(Box.createHorizontalStrut(10));
			box.add(buttonYM);
			box.add(Box.createHorizontalStrut(10));
			box.add(buttonXm);
			box.add(Box.createHorizontalStrut(10));
			box.add(buttonYm);
			box.add(Box.createHorizontalGlue());

			add(box, BorderLayout.SOUTH);
			add(new JLabel("\n"   ), BorderLayout.NORTH);
			add(new JLabel("     "), BorderLayout.EAST );
			add(new JLabel("     "), BorderLayout.WEST );

			this.addMouseWheelListener(new ZoomHandler());
			//     			 pack();
		}
		private class ZoomHandler implements MouseWheelListener {

			public void mouseWheelMoved(MouseWheelEvent e) {
				if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {

					Point2D p1 = e.getPoint();
					Point2D p2 = null;
					try   { p2 = tx.inverseTransform(p1, null);}
					catch (NoninvertibleTransformException ex) { ex.printStackTrace();
					return;}

					scalex -= (0.1*e.getWheelRotation());
					scalex  =  Math.max(0.014, scalex);

					scaley -= (0.1*e.getWheelRotation());
					scaley  =  Math.max(0.014, scaley);
					tx.setToIdentity();
					tx.translate(p1.getX(), p1.getY());

					tx.translate(-p2.getX(), -p2.getY());
					tx.scale(scalex, scalex);
					tx.scale(scaley, scaley);

					LinesDrawing.this.revalidate();
					LinesDrawing.this.repaint();
				}
			}
		}

		public void actionPerformed(ActionEvent e) {

			if(e.getSource() == buttonXM) 
				scalex += 0.1;

			if(e.getSource() == buttonYM) 
				scaley += 0.1;

			if(e.getSource() == buttonXm) 
				scalex -= 0.1;

			if(e.getSource() == buttonYm) 
				scaley -= 0.1;

			LinesDrawing.this.repaint();

		}

		void drawLines(Graphics g) {
			g2d = (Graphics2D) g  ;
			g2  = (Graphics2D) g2d;

			g2.drawString(" GRID CREATED BY THE AGENT COMUNICATION AND GIVEN COODINATES ", 210, 50);
			g2d.setTransform(tx);
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			g2d.translate(250,500);
			g2d.rotate(180 * Math.PI / 180.0);
			g2d.scale( 1, -1);
			g2d.scale(-1,  1);
			g2d.scale( 1, -1);

			Font font = new Font(null, Font.PLAIN, 15);    
			AffineTransform affineTransform = new AffineTransform();
			affineTransform.rotate(Math.toRadians(210), 0, 0);
			affineTransform.scale(-1, 1);
			Font rotatedFont = font.deriveFont(affineTransform);
			g2.setFont(rotatedFont);

			Color blue = new Color(77,176,230);

			Iterator<Integer> keySetIterator3 = Points.keySet().iterator(); 
			while(keySetIterator3.hasNext() ){ 

				int key = keySetIterator3.next();

				String X1Y1f = Points.get(key).iterator().next().get(0);

				if(runingCase.equals("LVTest")) {
					try { Thread.sleep(150);}
					catch (InterruptedException e) {e.printStackTrace();}
				}

				String X2Y2f = Points.get(key).iterator().next().get(1); 

				Iterable<String> X1Y1= Splitter.onPattern(",").trimResults().omitEmptyStrings().split(X1Y1f);
				Iterable<String> X2Y2= Splitter.onPattern(",").trimResults().omitEmptyStrings().split(X2Y2f);

				double		X1  =  (Double.parseDouble( Iterables.get(X1Y1, 0))* scalex);
				double		Y1  =  (Double.parseDouble( Iterables.get(X1Y1, 1))* scaley);
				String		nameFrom  = "Ag"+Iterables.get(X1Y1, 2) ;
				numPh  =    (Double.parseDouble(Iterables.get(X2Y2, 3)));

				double		X2  =  (Double.parseDouble( Iterables.get(X2Y2, 0))* scalex);
				double		Y2  =  (Double.parseDouble( Iterables.get(X2Y2, 1))* scaley);
				String		nameTo    = "Ag"+Iterables.get(X2Y2, 2) ;
				numPh2  =   (Double.parseDouble(Iterables.get(X2Y2, 3)));
				//				System.out.println(X1Y1f+"  :::"+Iterables.get(X1Y1, 2)+"   "+ X2Y2f+"  :::"+Iterables.get(X2Y2, 2));



				if(nameTo.equals("Ag150R") ){
					g2d.setColor(Color.RED);
					g2d.fillPolygon(new int[] {(int) (X1+10), (int) (X1+20), (int) (X1+30)}, new int[] {(int) Y1-10, (int) (Y1+15), (int) Y1-10}, 3);
				}

				if(nameTo.equals("Ag61S") ){

					g2d.setColor(Color.BLACK);
					g2d.fillPolygon(new int[] {(int) (X1+10), (int) (X1+15), (int) (X1+20)}, new int[] {(int) Y1, (int) (Y1+10), (int) Y1}, 3);
				}


				if(nameFrom.equals("Ag13") | nameTo.equals("Ag135") | nameTo.equals("Ag300_OPEN") | nameFrom.equals("Ag160R") | nameTo.equals("Ag101") | nameTo.equals("Ag94_OPEN") ){

					g2d.setColor(Color.RED);
					g2d.fillRect((int)X1-(14/2), (int) Y1+-(3*12/2), 14, 5);
				}


				if(X1==X2 && Y1==Y2  ){
					g2d.setColor(Color.BLACK);
					g2d.fillRect((int)X1-(13/2), (int) Y1-(3*12/2), 14, 5);
					g2d.fillRect((int)X1-(13/2), (int) Y1-(4*12/2), 14, 5);
				}

				if(!nameTo.contains(lineToFaltDraw) | !nameFrom.contains(lineToFaltDraw) & scenario==1 ){

					g2d.setColor(Color.BLACK);

					/**----Draw Linew Acording with the number of Phases----------*/	
					g2d.draw(new Line2D.Double(X1, Y1, X2, Y2));

					if ((numPh>1 ) |( numPh2>1 ))
						g2d.draw(new Line2D.Double(X1-3, Y1-3, X2-3, Y2-3));

					if (numPh==3.0 | numPh2== 3.0 )
						g2d.draw(new Line2D.Double(X1+3, Y1+3, X2+3, Y2+3)); 

					/**----Draw Agents Acording totheir Coordinates Position-------*/	
					g2d.setColor(Color.GREEN.darker().darker());
					g2.drawString(nameFrom,(int) X1-10, (int) Y1+9);
					g2d.setColor(Color.BLACK.brighter());
					g2d.drawString(nameTo  ,(int) X2-10, (int )Y2+9);

					g2d.setColor(blue.darker());
					g2d.fillOval((int)X1-(5/2 ), (int) Y1-(4/2 ), 8 , 9);
					g2d.fillRect((int)X1-(13/2), (int) Y1-(12/2), 14, 5);
					g2d.fillOval((int)X2-(5/2 ), (int) Y2-(4/2 ), 8 , 9);
					g2d.fillRect((int)X2-(13/2), (int) Y2-(12/2), 13, 5);
					g2d.setColor(Color.BLACK);
				}
			}
		}

		public void paint(Graphics g) {
			super.paint(g);
			drawLines(g);
		}

	}

	public void LinesReverseIsolation() throws IOException {

		int kk=0;
		String auxRead3 ="empty";
		File myFoo = new File("C:/Users/matheus/Desktop/TESE/workspace/masReconfig_phase3/lib/123Bus/DefineLines.DSS");

		FileWriter fooWriter;	
		try {

			fooWriter = new FileWriter(myFoo, false);
			while (auxRead3 != null){ 
				if( !auxRead3.equals("empty") & kk>1 )
					fooWriter.write(auxRead3+" \n");

				auxRead3 = new String();
				auxRead3 = readLine(kk,"C:/Users/matheus/Desktop/TESE/workspace/masReconfig_phase3/lib/123Bus/DefLines.DSS");
				kk++;
			}
			fooWriter.close();
		}catch (IOException e1) { 	e1.printStackTrace();} 
	}

	public void LinesIsolation(CharSequence lineToFalt2) throws IOException {
		int kk=0;
		String auxRead3 ="empty";
		File myFoo = new File("C:/Users/matheus/Desktop/TESE/workspace/masReconfig_phase3/lib/123Bus/DefineLines.DSS");
		FileWriter fooWriter;	
		try {

			fooWriter = new FileWriter(myFoo, false);
			while (auxRead3 != null){ 

				if(!auxRead3.contains(lineToFalt2) & !auxRead3.equals("empty") & kk>1 ) {
					fooWriter.write(auxRead3+" \n");
				}
				if(auxRead3.contains(lineToFalt2) & !auxRead3.equals("empty") & kk>1 ) {
					log("\n--------*Line Isolated in OpenDSS script!*----------");
					System.out.println(auxRead3);
				}

				auxRead3 = new String();
				auxRead3 = readLine(kk,"C:/Users/matheus/Desktop/TESE/workspace/masReconfig_phase3/lib/123Bus/DefLines.DSS");
				kk++;
			}
			fooWriter.close();
		}catch (IOException e1) { 	e1.printStackTrace();} 
	}

	public void SwitchConfig(String turnOff, String turnOff2) throws IOException, InterruptedException {

		int kk=0;
		String auxRead3 ="empty";
		File myFoo = new File("C:/Users/matheus/Desktop/TESE/workspace/masReconfig_phase3/lib/123Bus/recon.DSS");
		File myFooSux = new File("C:/Users/matheus/Desktop/TESE/workspace/masReconfig_phase3/lib/123Bus/swrecon.DSS");
		FileWriter fooWriter;	
		FileWriter fooWriteraux;	
		try {

			fooWriter = new FileWriter(myFoo, false);
			fooWriteraux = new FileWriter(myFooSux, false);
			while (auxRead3 != null){ 

				if(( auxRead3.contains(turnOff) |auxRead3.contains(turnOff2)) & !auxRead3.equals("empty") & kk>1 ) 
					System.out.println("Tuning OFF ---->"+auxRead3);
				if(( !auxRead3.contains(turnOff) & !auxRead3.contains(turnOff2)) & !auxRead3.equals("empty") & kk>1) {
					fooWriter.write(auxRead3+"\n");
					fooWriteraux.write(auxRead3+"\n");
					System.out.println(auxRead3);
				}

				auxRead3 = new String();
				auxRead3 = readLine(kk,"C:/Users/matheus/Desktop/TESE/workspace/masReconfig_phase3/lib/123Bus/reconOri.DSS");
				kk++;

				Thread.sleep(300);
			}
			fooWriteraux.close();
			fooWriter.close();
		}catch (IOException e1) {e1.printStackTrace();} 
	}

	public void SwitchRecon(int turnON) throws IOException, InterruptedException {

		log(""+turnON+"");

		int kk=0;
		int kl=0;
		String auxRead3 ="empty";
		File myFoo = new File("C:/Users/matheus/Desktop/TESE/workspace/masReconfig_phase3/lib/123Bus/recon.DSS");
		FileWriter fooWriter;	
		try {

			fooWriter = new FileWriter(myFoo, false);
			while (auxRead3 != null){ 

				if(auxRead3.contains("_OPEN") & !auxRead3.equals("empty") & kk>1 ) {
					kl++;
					System.out.println(kl);
					if(turnON==3 ) {
						fooWriter.write(auxRead3.replace("_OPEN","")+"\n");
						System.out.println("Closing Switch Normaly Open ----> "+auxRead3.replace("_OPEN",""));
					}
					if(kl==1 & turnON!=3 & turnON==1) {
						fooWriter.write(auxRead3.replace("_OPEN","")+"\n");
						System.out.println("Closing Normaly Open Closes----> "+auxRead3.replace("_OPEN",""));
					}
					if(kl==2 &turnON!=3 & turnON==2) {
						fooWriter.write(auxRead3+"\n");
						System.out.println("Closing Switch Normaly Open ----> "+auxRead3.replace("_OPEN",""));	
						//			    		break;
					}
				}
				if( auxRead3.contains("_OPEN")& kl==2 & !auxRead3.equals("empty") & kk>1) {
					fooWriter.write(auxRead3+"\n");
					System.out.println(auxRead3);
				}
				if( !auxRead3.contains("_OPEN") & !auxRead3.equals("empty") & kk>1) {
					fooWriter.write(auxRead3+"\n");
					System.out.println(auxRead3);
				}

				auxRead3 = new String();
				auxRead3 = readLine(kk,"C:/Users/matheus/Desktop/TESE/workspace/masReconfig_phase3/lib/123Bus/swrecon.DSS");
				kk++;

				Thread.sleep(300);
			}
			fooWriter.close();
		}catch (IOException e1) {e1.printStackTrace();} 
	}
	
	public void SwitchAlloff() throws IOException, InterruptedException {

		log("ALL switches OFF");

		int kk=0;
		String auxRead3 ="empty";
		File myFoo = new File("C:/Users/matheus/Desktop/TESE/workspace/masReconfig_phase3/lib/123Bus/recon.DSS");
		FileWriter fooWriter;	
		try {

			fooWriter = new FileWriter(myFoo, false);
			while (auxRead3 != null){ 

				if(!auxRead3.equals("empty") & kk>1 ) {
						fooWriter.write("!"+auxRead3);
						System.out.println("!"+auxRead3);
				}

				auxRead3 = new String();
				auxRead3 = readLine(kk,"C:/Users/matheus/Desktop/TESE/workspace/masReconfig_phase3/lib/123Bus/swrecon.DSS");
				kk++;

				Thread.sleep(300);
			}
			fooWriter.close();
		}catch (IOException e1) {e1.printStackTrace();} 
	}

	public void SwitchConfigRestore() throws IOException {

		int kk=0;
		String auxRead3 ="empty";
		File myFoo = new File("C:/Users/matheus/Desktop/TESE/workspace/masReconfig_phase3/lib/123Bus/recon.dss");

		FileWriter fooWriter;	
		try {

			fooWriter = new FileWriter(myFoo, false);
			while (auxRead3 != null){ 

				if(auxRead3 != "empty" & kk >1)
					fooWriter.write(auxRead3+"\n");
					System.out.println("Resoring switch starting position --->");
					System.out.println(auxRead3);

				auxRead3 = new String();
				auxRead3 = readLine(kk,"C:/Users/matheus/Desktop/TESE/workspace/masReconfig_phase3/lib/123Bus/reconOri.dss");
				kk++;
				Thread.sleep(500);
			}
			fooWriter.close();
		}catch (IOException | InterruptedException e1) { 	e1.printStackTrace();} 
	}

	public void readZones() {

		int kk=0;

		String auxRead ="empty";
		while (auxRead != null ){ 
			Iterable<String> pieces= Splitter.onPattern("[ \\s+ ]").trimResults().omitEmptyStrings().split(auxRead);

			if(auxRead != "empty") {
				switchPlan.put(Iterables.get(pieces, 0) , Iterables.get(pieces, 4));
				Zones.put(Iterables.get(pieces, 0),Iterables.get(pieces, 3));
			}
			kk++;	
			auxRead = readLine(kk,"C:/Users/matheus/Desktop/TESE/workspace/masReconfig_phase3/lib/123Bus/zones.txt");
		}
		//		System.out.println(Zones+"\n");
		//		System.out.println(switchPlan);
	}

	public String getLineOfFalt() {
		return LineOfFalt;
	}

	public void setLineOfFalt(String lineOfFalt) {
		LineOfFalt = lineOfFalt;
	}
} // public class artSimTool extends Artifact

