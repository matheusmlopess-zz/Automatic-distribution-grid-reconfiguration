/*         ---- Initial beliefs and rules ----				    */
 statusSimulation(0).
 system(0).
  
/*                ---- Initial goals ----	
 * startSimulation(Id of the artifact,
 * scenario of simulation(0-steady state 1- Falt study 3-reconfig,
 * Distribution Grid (0 - ieee34 1-ieee37 2-ieee123 3-LVTESTcase(900+Busses) )
 */
!startSimulation(initSim,0,2).

/*        				--- Plans ---							*/
   
    /**First achievement goal of simulation call Cycle 1: Building Phase_1 */
    +!startSimulation(Id,SimulationType,SystemChose)  : true  
			     <-
			     /**First achievement goal: couse of action to build simulationCall artifact and bound it to the agent */
			     !makeArtifactBuild(Id,SimulationType,SystemChose);
			     .my_name(Me);
			     /**Second achievement goal: wait 5 sec before trigger and stop the simulation together with the post processing */
	             .at("now + 1 seconds", {+!simulationStop(Me,Id)})
				 .
	/**Achievement goal 1: Make simulationCall and dMS Artifact & Join the Workspace */
	+!makeArtifactBuild(Id,B,C): true 
				 <- 
				 makeArtifact(Id, "tool.artSimTool", [], ArtId);
				 joinWorkspace("grid_environment",WspID1);
				 .send("dMS",tell,initiateDMS(Id));
				 
				?current_wsp(_,Name1,_);
                .print("\n\n\nWorkspace Dimenssion Created and Named: ",Name1);
 				
 				 start(B,C)[artifact_id(ArtId)];
 				 +system(C);
 				 ?system(C);
				 setAgentNumber[artifact_id(ArtId)];
				.print("Exogenous Environment Artifact Created & set for the Power Distribution Study System.....\n\n");
 				 focus(ArtId);
				.	 	  

	+!simulationStop(Me,Id)  : true
       			 <-
       			  incSim;
       			  stop(Me);
		         ?agentNumber(Num)[artifact_id(ArtId)];
		         !createAgents(Num);       .wait(5000);
		         .print("Attchedt to.. calling");
		         .wait(2000);
		         .print("Attachment communication test validation.. callin");
		         .broadcast(achieve,attachTo);
		         .

	+!createAgents(Num)   : true
     			<-  
     			 for (  .range(I,0,Num-1)   ){
     				     getAgentName(I,Name,IdName)[artifact_id(ArtId)];
     			        .concat("Ag_",Name,AgName);
         			    .concat("Art_",IdName,ArtName);
				         intAct.create_agent2(AgName,"src/agt/inc/participantBus.asl",[agentArchClass("c4jason.CAgentArch")]);
				         joinWorkspace("grid_environment",WspID1);
				         getBusData(IdName,BusData,BusPath,ElemDat,Zonesd,BPstring)[artifact_id(ArtId)];
				         makeArtifact(ArtName,"tool.busArtifact",[IdName,BusData,BusPath,ElemDat,Zonesd],ArtId2);
				       	.send(AgName,achieve,focus(ArtName));
				       	.wait(100);
				       	.send(AgName,achieve,senseForThefirstTime(ArtName));
		                 }
		                 .print("\n\n _____________. Endogenous Environment ALL set...\n _____________. MAS System Ready for Working towards the Power Grid Reconfiguration Approach ...\n\n ");
		                 .
                 
                 +senseAgain(NewbusPath): true <- 	true.
                 
                 +swichAction(SW)[source(G)]: true <- 
      						.print("\n****Simulation Artifact Received request to Change Grid state, SWICTH ZONE ISOLATION, by Agent: [",G,"]***\n");	
      						 getActionForZone(SW,Num)[artifact_id(ArtId)];
      						!zoneSWgotcommand(Num,G);
      						.print("\n\n >>Action Performed Successfully");	
      						.
     
                 +isolationActionFor(AgentCalling): true <- 
                  						.print("\n #Simulation Artifact Received request to Change Grid state, LINE ISOLATION, by Agent: [",G,"] #\n");	
                  						 lineIsolationActionFor[artifact_id(ArtId)];
                  						.wait(1000);
                  						.print("\n\n >>Action Performed Successfully");	
                  						.
                 +swichOpenAction(SW)[source(G)]: true <- 
      						    .print("\n****Simulation Artifact Received request to Change Grid state, SWICTH Closing Action, by Agent: [",G,"]***\n");	
      						     getActionSwtOpen(SW)[artifact_id(ArtId)];
      						    .wait(300);
      						    .print("\n\n >>Action Performed Successfully");	
      						.
              
              	  +!zoneSWgotcommand(S,A): S==1  <- .print("\n ---**Zone Not completely Isolated yet... SWtch----->  ",A," OPened**---").			
	 	    	  +!zoneSWgotcommand(S,A): S>1  <-  .wait(5000); zoneIsolationActionFor; .print("\n----***Zone Isolated. SWtch----->  ",A," OPened***----\n").
	 	    	  +!zoneSWgotcommand(S,A): true <- !zoneSWgotcommand(S,A).
    			 
    			  +!attachTo: true <- true.
    			  +!faltCommunication : true <- true.
    			  
     /***** Code tranfered to DMS agent... ******/
//
//    +simulationStatus(H,S)[source(G)] : H > 0 			
//				<- 
//				 +statusSimulation(0,1);	
//				 .wait(8000);
//				 ?countSim(X)[artifact_id(ArtId)];
//				 .print("Cycle::",X,"-->Branch Agent: {",G,"} Voltage Checked!... ");
//				  inc[artifact_id(ArtId)];
//				 ?agentNumber(Num)[artifact_id(ArtId)];
//				 ?count(VV)[artifact_id(ArtId)];
//				 !status(VV,Num,G);
//				 .
//				 
//	+simulationStatus(H,S)[source(G)]  : H== 0 	
//				<-  
//				 ?countSim(X)[artifact_id(ArtId)];
//				 ?agentNumber(Num)[artifact_id(ArtId)];
//				 .wait(5000);
//			     .print("Cycle::",X,"-->Branch Agent: {",G,"} Voltage Checked... ");
//			      inc[artifact_id(ArtId)];
//			      
//				 ?agentNumber(Num)[artifact_id(ArtId)];
//				 ?count(VV)[artifact_id(ArtId)];
//				 !status(VV,Num,G);
//			     .
//							     
//	+!status(BB,Num,Ag): statusSimulation(A,1) & A==0 & BB==Num
//  				 <- 
//  				 .print("Status of the Cycle: NORMAL STATE & last Agent(s) checked::",Ag);
//			     .print(Num," e o inc ",BB);
//			     !drawGridplot(BB,Num,ArtId);
//			      incClear[artifact_id(ArtId)];
//			      inc
//			      
//			      . 
//  	 										 
//	+!status(BB,Num,Ag): statusSimulation(A,1) & A==1 & BB==Num 
//  				 <- 
//  				  incClear[artifact_id(ArtId)];
//  				  !drawGridplot(BB,Num,ArtId);
//  				  .print("Status of the Cycle: CONTINGECY STATE Agent:: ",Ag)
//  				  .
//  				
//	+!status(BB,Num,Ag): true <-  !status(BB,Num,Ag).
// 
//	+printLine(D)[source(G)] : true  
//								<- 
//								?count(VV)[artifact_id(ArtId)];
//								.print(VV);
//								?agentNumber(Num)[artifact_id(ArtId)];
//								!drawGridplot(VV,Num);
//  									.
// 	+getPlots(S): true <- cmdView(S)[artifact_id(ArtId)].
//    
//    +!drawGridplot(K,Num,S): system(C) & C==0 <-   plotGrid[artifact_id(S)].
//     ou alternativa: REPL Agent: .send("simulationCall",achieve,drawGridplot(37,37,ArtId))
//	+!drawGridplot(K,Num,S): K\==Num <-   true.									
//	+!drawGridplot(K,Num,S): K ==Num <-   plotGrid[artifact_id(S)].
//	+!drawGridplot(K,Num,S): true <-  !drawGridplot(K,Num,S).
//	
//	.send("simulationCall",tell,getFalts("110"))
//	+getFalts(BusFalt): true <- 
//								.wait(5000);
//								.print("trying to retriece falt");
//						 		 performFalt(BusFalt)[artifact_id(ArtId)];
//						 		.broadcast(achieve,focus(_));
//	  							 cmd("Show Voltage LN Nodes")[artifact_id(ArtId)];
//	  							 cmd("Show Currents Elements")[artifact_id(ArtId)];
//	 							.
  
						 //PAra o 34 barras é preciso verifcar o Num
                         //.send("simulationCall",achieve,drawGridplot(37,37,ArtId))
					     //cmd("Show Voltage LN Nodes")[artifact_id(ArtId)];
					     //cmd("Show Currents Elements")[artifact_id(ArtId)];
					     //cmd("Show taps")[artifact_id(ArtId)];
					     //cmdClear[artifact_id(ArtId)];
					     //start(B)[artifact_id(ArtId)];
					     //setAgentNumber[artifact_id(ArtId)];
			
                  
{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }
{ include("$jacamoJar/templates/org-obedient.asl") }

