/*beliefs and_or rules */

cycle(0,1).
statusSimulation(0,1).
				
				/*-------------- Plans ---------------------*/
	
	
    +!perceiveData : true <- .print("All agents Created started analyse its  Data").

	+initiateDMS(ArtName) : true 
			       <-  
			        -initiateDMS(ArtName);
			         .my_name(Me);
			         .print(" DMS Agent Initiated --->>[",Me,"]... ");
			         +statusSimulation(0,1);
		             !focusDMS(ArtName) 
		             .
		         
	+!focusDMS(A)   : true
				   <- 
				     .my_name(Me);
				  	  joinWorkspace("grid_environment",WspID1);
	     	 	  	  lookupArtifact(A,ArtId);
	     	 	     .print(Me," FOUND and focusing on---> ",ArtId," Or -- >",A);
	     	 	  	 .print(Me," Just got in the Dimension:: grid_environment");
			 	  	  focus(ArtId);
   					 .
   					 
  	+!simulationStatus(H)[source(G)] : H > 0 			
				<-
				 +statusSimulation(1,1);
				 ?countSimWhile(G);	
				 ?countWhile(G).
				+?countSimWhile(G)	    : true	<-	?countSim(X)[artifact_id(ArtId)];
											        .print("Cycle::",X,"-->Agent sensors retrieved: {",G,"} status Checked!... fault Alert! ");
					 						         inc[artifact_id(ArtId)];.
		        -?countSimWhile(G)      : true	<- 	.wait(10);  ?countSimWhile(G). 
					 
	+!simulationStatus(H)[source(G)]  : H== 0 	
				  <-
				  ?countSimWhile2(G);
				  ?countWhile(G).
				 +?countSimWhile2(G)	: true	<-	?countSim(X)[artifact_id(ArtId)];
				 									.print("Cycle::",X,"-->Agent sensors retrieved: {",G,"}  status Checked.. everything is fine");
				 								 	 inc[artifact_id(ArtId)].
			     -?countSimWhile2       : true	<- 	.wait(50);  ?countSimWhile2(G). 
			     
			 +?countWhile(G)	        : true	<-	 ?count(VV)[artifact_id(ArtId)];
			 								         ?agentNumber(Num)[artifact_id(ArtId)];
										 	         !status(VV,Num,G);.
		     -?countWhile(G)            : true	<- 	.wait(50);  ?countWhile. 
			
			     
	+!status(BB,Num,Ag): statusSimulation(A,1) & A==0 & BB==Num  
  				 <- 
  				 -statusSimulation(A,1);
  				 .print("Status of the Cycle: NORMAL STATE & last Agent(s) checked::",Ag);
			     .print("-------->",Num," Agents checked! sensoring cycle finisched**---- \n\n\n");
			      incClear[artifact_id(ArtId)];
			     !drawGridplot(BB,Num,ArtId);
			     . 
  	 										 
	+!status(BB,Num,Ag): statusSimulation(A,1) & A==1 & BB>128
  				 <- 
  				  -statusSimulation(A,1);
  				  .print("\n Status of the Cycle: FALT STATE in more than one Agent:\n");
  				   incClear[artifact_id(ArtId)];
  				  !drawGridplot(BB,Num,ArtId);
  				  .
  				
	+!status(BB,Num,Ag): true <-  !status(BB,Num,Ag).
 
								
 	+getPlots(S): true <- cmdView(S)[artifact_id(ArtId)].
    
//   ou alternativa: REPL Agent: .send("dMS",achieve,drawGridplot(132,132,"initSim"))
	+drawGridplotFalt: true <- -drawGridplotFalt(S);  plotGrid[artifact_id("initSim")].	
	
	+!drawGridplot(K,Num,S): K\==Num <-   true.									
	+!drawGridplot(K,Num,S): K ==Num <-  .wait(15000); plotGrid[artifact_id(S)].
	+!drawGridplot(K,Num,S):   true  <-  !drawGridplot(K,Num,S).
	
	
	
	+getFalts(BusFalt,LineOfFalt,BusZone)[source(G)]: true <- 
	-getFalts(BusFalt,LineOfFalt,BusZone);
//								.wait(5000);
						 		  performFalt(BusFalt,LineOfFalt)[artifact_id(ArtId)];
						 		 .print("Fault occurency, reported location of fault by --->",G,"... DMS Trying to recover from fault");
						 		 .wait(1500);
//						 		 -statusSimulation(_,_);
						 		  incSim[artifact_id(ArtId)];
						 		 .my_name(Me);
						 		  stop(Me)[artifact_id(ArtId)];
						 		 .wait(300);
						 		  getBusData("1",_,_,_,_,BPstring)[artifact_id(ArtId)];
						 		 
				 	    						 	 		     .wait(3000);
				 	    						 	 		     .broadcast(tell,senseAgain(BPstring));
				                 .wait(3000);
//				                 +drawGridplotFalt(ArtId);
				                 !procedReconfiguration(BusZone,Sw1,Sw2,ArtId,G,LineOfFalt);
				                  
				                   .wait(20000);
				                   .print("Reconfiguration Performed Successfully... Awaiting for Restoration (Reconfigurated Power Flow)");
				                   .wait(10000);
	 						
				                  /** +procedReconfig(G);*/
				                  .print("System fully recovered from Falt ");
				                  .print("Showing mesageBoard..."); 
				                  +getPlots("1");
				                  .send("Ag_1",tell,showConsole);
				                 .
	 							
	 		+!procedReconfiguration(BusZone,Sw1,Sw2,ArtId,G,LineOfFalt): true
										<-
										         .print("\n\n\n");
								                 .print("******************************************************************************");
								                 .print("*------------**System Will automaticaly perform Reconfiguration**----------*");
								                 .print("*-------------**In order to keep Marjority of Loads energized!**---------------*");
								                 .print("*****************************************************************************\n");
								                 .print("\n>> DMD requested to Falt Agent Perceive plan to Closing Swichs on the Agent Adjacent Zone");
				 	    						 .print("DMS geting Agent Sw Actions...");
				 	    						 .wait(1000);
								                  callForIsolationZonePlan(BusZone,Sw1,Sw2)[artifact_id(ArtId)];
				 	    						 .send(G,tell,tellSwAgent(Sw1,Sw2,BusZone));
								                 .print("Falt Scenario... DMS Agent Calling Plan Falt API to generate Plan of Action For the specifc scenario");
								                 .wait(1000);
						 		                  callPlanAPI(LineOfFalt,Sw1,Sw2)[artifact_id(ArtId)];
						 		                    
						 		                     generatedPlan("SwitchON_OFF",PlanToSend,SWagnt)[artifact_id(ArtId)];
												    !createAgentPlanForSwitchON_OFF(G,ArtId,SWagnt);
												    .print("------------->> DMD requested to Falt-Agent Perceive generated SwitchON_OFF Plan");
				 	    						 	.send(SWagnt,tell,tellSwAgentOpen("simulationCall",PlanToSend));
				 	    						 	.wait(1000);
				 	    						 	 		     .my_name(Me);
				                  								 ?agentNumber(Num)[artifact_id(ArtId)];
				                  								  incSim[artifact_id(ArtId)];
				                  								  performReconPF[artifact_id(ArtId)];
				                  								  +statusSimulation(0,1);
				                   								  stop(Me)[artifact_id(ArtId)];
				 	    						 	 		      getBusData("2",_,_,_,_,BPstring2)[artifact_id(ArtId)];
				 	    						 	 		        .print(BPstring2);
				 	    						 	 		     .wait(1000);
				 	    						 	 		     .broadcast(tell,senseAgain(BPstring2));
					 							 .
	 							
	 					+procedReconfig(G): true <-
				                  -procedReconfig(G);
								  +statusSimulation(0,1);
								  
								           	!createAgentPlanForIsolation(G);
										 			.print("------------->>  to Falt-Agent requested Perceive generated plan to Isolate Adjacent Falt Lines");
				 	    						 	.send(G,tell,performIsolation("simulationCall",G));
				 	    		  .wait(3000);
				                  .my_name(Me);
//				                   ?agentNumber(Num)[artifact_id(ArtId)];
				                   incSim[artifact_id(ArtId)];
				                   reconfigurationPF[artifact_id(ArtId)];
				                   stop(Me)[artifact_id(ArtId)];
				                  .wait(3000);
				                  
				                   getBusData("3",_,_,_,_,BPstring3)[artifact_id(ArtId)];
				                   .print(BPstring3);
				                   .wait(3000);
				                   .broadcast(tell,senseAgain(BPstring3));
				                   .wait(3000);
				               	  .	
  				
  				+!createAgentPlanForIsolation(Agnt): true <- 
  								   .print("------------->> DMS sending Isolation Plan to Agent afected....");
  				 				    Plan = "+performIsolation(SimCall,AgentCalling):true<-.print(\"Executing DMS Plan::: >> Performing Isolation by Local Protection/Devices \");  .send(SimCall,tell,isolationActionFor(AgentCalling)); .print(\"         > >  Disconecting Lines  \"); .print(\"    > >  Isolation terminated Successfully... \")." ;
  				 				   .send(Agnt,tell,performPlan(Plan));
  					 			   .
  				+!createAgentPlanForSwitchON_OFF(G,ArtId,SWagnt): true <- 
								   .print("------------->> DMS sending Swiching Plan ....");
								    Plan = "+tellSwAgentOpen(SimCall,Plan):true<-.print(\"Executing DMS Plan::: >> Performing manouver  in Local Protection/Devices \");  .send(\"dMS\",tell,gotActionGoingOn(\"DMS requested of Closing Switch Received and in process... \")); .send(SimCall,tell,swichOpenAction(Plan)); .print(\"    > >  Action terminated Successfully...    \")." ;
				 				   .send(SWagnt,tell,performPlan(Plan));
					 			   .
				+!createAgentRestoration(G): true <- 
					 			   generatedPlan("RestoringPlan",Plan)[artifact_id(ArtId)];
					 			   .
					 			   
 	 			+gotActionGoingOn(S)[source(G)]: true <- .print("-----Msg received from: ",G," Reporting action/event --->",S);.
 	 			
 	 			
 	 			
 	 			+getFalts2(BusFalt,LineOfFalt,BusZone)[source(G)]: true <- 
	            -getFalts2(BusFalt,LineOfFalt,BusZone);
//								.wait(5000);
						 		  performFalt(BusFalt,LineOfFalt)[artifact_id(ArtId)];
						 		 .print("Fault occurency, reported location of fault by --->",G,"... Trying to recover from fault");
						 		 .wait(1500);
						 		 +statusSimulation(0,1);
						 		  incSim[artifact_id(ArtId)];
						 		 .my_name(Me);
						 		  stop(Me)[artifact_id(ArtId)];
						 		 .wait(300);
						 		  getBusData("1",_,_,_,_,BPstring)[artifact_id(ArtId)];
						 		   .wait(3000);
				 	    	       .broadcast(tell,senseAgain(BPstring));
				 	    	       .wait(3000);
				 	    	       .print("****OPENING ALL SWITCHES****");
				 	    	       .wait(10000);
				 	    	        openALLsw[artifact_id(ArtId)]; 
				 	    	       .print("****Atempting Falt restoration by Agents speach Act coordination****");
				 	    	       .wait(50000);
				 	    	       .broadcast(achieve,faltCommunication);
 								   .wait(5000);
				                   .print("Reconfiguration Performed Successfully... Awaiting for Restoration(Normal grid configuration State Agreement)");
				                   .wait(10000);
				                   .print("Agreement received From Mantenance ...Step one (3) Line Isolation ...");
				 	    	       +procedReconfig(G);	
				 	    	      .print("System fully recovered from Falt ");
				                  .print("Showing mesageBoard of primary feeder..."); 
				                  +getPlots("1");
				                  .send("Ag_1",tell,showConsole);			 	    	      
						 		  .
						 
						 		  +!attachTo: true <- true.
						 		  
 	
{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }
// uncomment the include below to have a agent that always complies with its organization  
{ include("$jacamoJar/templates/org-obedient.asl") }
