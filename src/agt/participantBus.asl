/* This is part of a set of 3 asl codes that together build a MAS 
 * tha abstract a reconfiguration approach for a generic power grid */
/*beliefs and_or rules */
voltage(0,0.00).     // initilize Bealief Voltage 
current(1,1,0.000,"null","null").
simulationStatus(0).
zone  (None). 	
// capacyties (Max(Normal), Max(Emergency ))	
capacityEmergency(0.000,0.000).
capacityNormal(0.000,0.000).

				/*-------------- Plans ---------------------*/

//	+!initiate(ArtName) : true 
//			       <-  
//			         .my_name(Me);
//			         .print(" Detected!! >>>> Agent BussBar & sensor Artifact::", ArtName," created and initiented !!! [",Me,"]... ");
//		             .
		         
	+!focus(A)		   : true
				   <- 
				  	  joinWorkspace("grid_environment",WspID1);
	     	 	  	  lookupArtifact(A,ToolId);
	     	 	  	  incSim[artifact_id(ToolId)];
	     	 	  	 ?current_wsp(_,NameEnvi,_);
	     	 	  	 .concat(Me," Just got in the Dimension::",Name1);
	     	 	  	 .concat(Name1,NameEnvi,NameEnvi2);
	     	 	  	  printMsg(NameEnvi2)[artifact_id(ToolId)];
			 	      printMsg("sensor Artifact Created!! geting Voltages and Currents has started...")[artifact_id(ToolId)];
			 	  	  focus(ToolId);
   					 .
   		+!senseForThefirstTime(ToolId): true <-
   		 	 				 .my_name(Me);
   		 	 				 .print(">>Agent & sensorr Artifact Created ...[",ToolId,"].");
	     	 	  	 		  senseVoltages(Me)[artifact_name(ToolId)]; 
			 	        	  senseCurrents(Me)[artifact_name(ToolId)];
			 	        	  getBusLines(AS)[artifact_name(ToolId)];
			 	        	  .print(AS);
			 	        	 !perceiveData
			 	        	.
						 
   			 +?sendDMS	: true	<-	?count(FF)[artifact_id(ToolId)];
								    !statusSystem(FF).
		     -?sendDMS: true	<- 	.wait(100);  ?sendDMS. 
				
			 +?callSimCount(WaitTime)	: true	<-	?countSim(X)[artifact_id(ArtId)];
			 							.print(">>> Sensoring cycle [ ",X," ] started...Setting coordenation time for sensing msgs and validate data ------->> ",WaitTime).
		     -?callSimCount(WaitTime): true	<- 	.wait(200);		?callSimCount(WaitTime). 	
		     
		     
				
	+!perceiveData : true 
				  <-
				    
				  	 WaitTime = 1000+math.random(1000)+math.random(1000)+math.random(1000);
      				 ?callSimCount(WaitTime);
      				 .wait(WaitTime);
				  	
					 ?statusVolt(Nl)[artifact_id(ToolId)];
					 ?statusCurrent(Vl)[artifact_id(ToolId)];
					 .concat(" Volages  Status ------>",Nl,VoltagesList);
					 .concat(" Currents Status ------>",Vl,CurrentList);
					 
					 .wait(10);
					 ?voltagePhase1(X1)[artifact_id(ToolId)];
					 ?voltagePhase2(X2)[artifact_id(ToolId)];
					 ?voltagePhase3(X3)[artifact_id(ToolId)];
					 .wait(10);
					 ?current1Phase1(Ph1a)[artifact_id(ToolId)];
					 ?current1Phase2(Ph2a)[artifact_id(ToolId)];
					 ?current1Phase3(Ph3a)[artifact_id(ToolId)];
					 ?cuurLine1(LineA)[artifact_id(ToolId)];
					 .wait(10);					 
			         ?current2Phase1(Ph1b)[artifact_id(ToolId)];
					 ?current2Phase2(Ph2b)[artifact_id(ToolId)];
					 ?current2Phase3(Ph3b)[artifact_id(ToolId)];
					 ?cuurLine2(LineB)[artifact_id(ToolId)];
					 .wait(10);
					 ?current3Phase1(Ph1c)[artifact_id(ToolId)];
					 ?current3Phase2(Ph2c)[artifact_id(ToolId)];
					 ?current3Phase3(Ph3c)[artifact_id(ToolId)];
					 ?cuurLine3(LineC)[artifact_id(ToolId)];
					 .wait(10);
					 ?current4Phase1(Ph1d)[artifact_id(ToolId)];
					 ?current4Phase2(Ph2d)[artifact_id(ToolId)];
					 ?current4Phase3(Ph3d)[artifact_id(ToolId)];	
					 ?cuurLine4(LineD)[artifact_id(ToolId)];				 
					 
					 +voltage(1,X1,ToolId);
					 +voltage(2,X2,ToolId);
					 +voltage(3,X3,ToolId);
					 
					 +current(1,1,Ph1a,"Phase 1",LineA,ToolId);
					 +current(1,1,Ph2a,"Phase 2",LineA,ToolId);
					 +current(1,1,Ph3a,"Phase 3",LineA,ToolId);

					 +current(1,2,Ph1b,"Phase 1",LineB,ToolId);
					 +current(1,2,Ph2b,"Phase 2",LineB,ToolId);
					 +current(1,2,Ph3b,"Phase 3",LineB,ToolId);

					 +current(1,3,Ph1c,"Phase 1",LineC,ToolId);
					 +current(1,3,Ph2c,"Phase 2",LineC,ToolId);
					 +current(1,3,Ph3c,"Phase 3",LineC,ToolId);

					 +current(1,4,Ph1d,"Phase 1",LineD,ToolId);
					 +current(1,4,Ph2d,"Phase 2",LineD,ToolId);
					 +current(1,4,Ph3d,"Phase 3",LineD,ToolId);
					 .wait(200);
					  ?sendDMS;
					  incClear[artifact_id(ToolId)];
					 .wait(100);
					 .term2string(X1,Ph1);
					 .term2string(X2,Ph2);
					 .term2string(X3,Ph3);
				
					 .concat(" Phase 1 Bus Voltage (p.u.)  ------>",Ph1,Ph1_2);
					 .concat(" Phase 2 Bus Voltage (p.u.)  ------>",Ph2,Ph2_2);
					 .concat(" Phase 3 BUs Voltage (p.u.)  ------>",Ph3,Ph3_2);

					  printMsg(Ph1_2)[artifact_id(ToolId)];
					  printMsg(Ph2_2)[artifact_id(ToolId)];
					  printMsg(Ph3_2)[artifact_id(ToolId)];
					  
					
					  
					 .concat("FAlt Count ------>",Falt,FaltVount);
				 	  printMsg(FaltVount)[artifact_id(ToolId)];
					  printMsg(VoltagesList)[artifact_id(ToolId)];
					  printMsg(CurrentList)[artifact_id(ToolId)];
					  incSim[artifact_id(ToolId)];
					 . 
			 	    
		 	    +!statusSystem(H): H== 0 <-  .send("dMS",achieve,simulationStatus(H)).
		 	    +!statusSystem(H): H > 0 <-  .send("dMS",achieve,simulationStatus(H)).
		        +!statusSystem(H): true  <-  !statusSystem(H).		

    +!testBelifadequacyCur(A,B,C,F,H,ToolId): (C*100.0/601.0) > 100.0 & (C*100.0/400.0) > 100.0 
		            <-   
					 .term2string(B,X);
				     .term2string(C,V);
				     .concat(" FALT Fond!! in Phase:",X,Result);
					 .concat(Result," Current of contingency:",Result2);
					 .concat(Result2," ----> ",Result3);
					 .concat(Result3,V,Result4);
		              printMsg(Result4)[artifact_id(ToolId)];
				      inc[artifact_id(ToolId)];
				     .print(">> FAULT OCCURENCY !!! Current above emergency limit [>600Amp] Line",B,"--->",H,"   PHASE:   ",F,"  Value::",C);
				     +showConsole;
				     ?showConsole;
				     .
    +!testBelifadequacyCur(A,B,C,F,H,ToolId):  C < 10  <- true.

	+!testBelifadequacyCur(A,B,C,F,H,ToolId):  (C*100.0/601.0) < 100.0 & (C*100.0/400.0) > 100.0 & C > 10
					<-   
				     .term2string(C,V);
				     .concat("Falt Alert Fond!! in Phase: ",F,Result);
					 .concat(Result," Falt Current :",Result2);
					 .concat(Result2," ----> ",Result3);
					 .concat(Result3,V,Result4);
		              printMsg(Result4)[artifact_id(ToolId)];
				     .print(">> Fault ALERT* Current little OUT OF NORMAL RANGE [400 - 600Amp] Line",B,"--->",H,"   PHASE:   ",F,"  Value::",C);
				     .
	
	+!testBelifadequacyCur(A,B,C,F,H,ToolId): (C*100.0/400.0) < 100.0 & (C*100/600) < 100.0 & C > 10
		            <-   
				     .term2string(C,V);
					 .concat("Current Levels Normalized in Phase: ",F,Result);
					 .concat(Result," Current value: ",Result2);
					 .concat(Result2," ----> ",Result3);
					 .concat(Result3,V,Result4);
		              printMsg(Result4)[artifact_id(ToolId)];
					 .print(">> Current: Normal Range [ 0 - 400Amp]  <<>>  Line",B,"--->",H,"   Ph:   ",F,"  Value::",C);
                     .
         
     +!testBelifadequacyCur(A,B,C,F,H,ToolId): true <- !testBelifadequacyCur(A,B,C,F,H,ToolId).

	 
	 
	 +!testBelifadequacyVoltage(A,B,ToolId):  B<0.95 | B>1.05
					<-   
                     .term2string(A,X);
				     .term2string(B,V);
					 .concat("Falt Fond in Phase: ",X,Result);
					 .concat(Result,"Voltage : ",Result2);
				     .concat(Result2,"---->  ",Result3);
				     .concat(Result3,V,Result4);
				      printMsg(Result4)[artifact_id(ToolId)];
				      inc[artifact_id(ToolId)];
				     .print(">> FALT ALERT!  VOLTAGE WITHIN TOLERANCE RANGE <<>> ",B,"   PHASE:   ",A," ");
				     +showConsole;
				     
				     .

	 +!testBelifadequacyVoltage(A,B,ToolId): B>0.95 & B<1.05
		            <-  
		             .term2string(A,X);
				     .term2string(B,V);
					 .concat("Voltage Levels Normalized in Phase: ",X,Result);
					 .concat(Result," ---->",Result2);
					 .concat(Result2,V,Result3);
		              printMsg(Result3)[artifact_id(ToolId)];
					 .print(">> Voltage Levels in Regulatory Standard Range ---->",B,"   PHASE:   ",A);
                     .
             
     +!testBelifadequacyVoltage(A,B,ToolId): true <- !testBelifadequacyVoltage(A,B,ToolId).
     
    					 /*-------------------Belifs/Behaves-----------------*/
     
		 	    +voltage(A,B,ToolId):      B\==0      <-  
		 	    					  -voltage(A,B,ToolId);
			 						 .term2string(A,X);
			 						 .concat("TEST VOLTAGE ADEQUACY CALL PHASE :",X,Result);
			 						  printMsg(Result)[artifact_id(ToolId)];
			 				     	 !testBelifadequacyVoltage(A,B,ToolId)
			 						 .

		 	    +current(A,B,C,F,H,ToolId): C > 0.000 <-  
		 	    					  -current(A,B,C,F,H,ToolId);
			 						 .term2string(B,X);
			 						 .concat("TEST CURRENT ADEQUACY CALL... ", H,Result);
			 						 .concat(Result,"  PHASE :",Result2);
			 						 .concat(Result2,"---->",Resultado);
			 						 .concat(Resultado,F,Resultado2);
			 						  printMsg(Resultado2)[artifact_id(ToolId)];
			 				     	 !testBelifadequacyCur(A,B,C,F,H,ToolId)
			 						 .
     
	     
				//to Invoque use ::: .send("Ag_XXX",tell,showConsole) at NEW REPL agent
		 	    +showConsole : true <-
			 						 .my_name(Me);
	     							  consoleData[artifact_id(ToolId)];
	     						      printMsg("\n      terminal visualization GUI Evoked       \n")[artifact_id(ToolId)];
	     						 	 .
		 	     			
				//to Invoque use ::: .send("Ag_XXX",tell,sendResponse) at NEW REPL agent
 	   			+sendResponse : true <-  
 	   								  getBusRealName(RealName)[artifact_id(ToolId)];
		 	     					 .send("dMS",tell,getPlots(RealName));
		 	     					 .
		 	     					    
//		 	    .send("Ag_62",tell,callFalt)
		 	    +callFalt: true <- 
		 	     -callFalt;
			 						 .my_name(Me); 
									  getBusRealName(RealName)[artifact_id(S)];
			 	    				  getFaltLine(FaltLine)[artifact_id(S)];
			 	    				  ?zone(BusZone)[artifact_id(S)];
			 	    				 .print("******Falt simulation with line Bus Agent ----->",RealName);
			 	    				 .print("sending DSM request for Plan Of Action of Falt in that zone");
			 	    				 .send("dMS",tell,getFalts(RealName,FaltLine,BusZone));
			 	    				 .
			 	    				 
//			 .send("Ag_62",tell,callFalt2)				 
			 +callFalt2: true <- 
		 	     -callFalt2;
			 						 .my_name(Me); 
									  getBusRealName(RealName)[artifact_id(S)];
			 	    				  getFaltLine(FaltLine)[artifact_id(S)];
			 	    				  ?zone(BusZone)[artifact_id(S)];
			 	    				 .print("******Falt simulation with line Bus Agent ----->",RealName);
//			 	    				 .print("sending DSM request for Plan Of Action of Falt in that zone");
			 	    				 .send("dMS",tell,getFalts2(RealName,FaltLine,BusZone));
			 	    				 .
			 	    				 
			 	    				 
			 	    					
			 	 +senseAgain(NewbusPath): true <- 
			 	  -senseAgain(NewbusPath);	
			 	   	 				  
			 	   	 				  WaitTime = 300+math.random(5000)+math.random( 300)+math.random(300);
      				                 .wait(WaitTime);
			 						 .my_name(Me);
			 						 .wait(500);
			   					      busPath(NewbusPath)[artifact_id(ToolId)];
			   					      ?senseVolt(Me);
			   					      ?senseCurr(Me);
						 	      	  !perceiveData
						 	      	 . 
						 	      	 
						 	      	 			 
   			 +?senseVolt(Me)	 : true	<-	senseVoltages(Me)[artifact_id(ToolId)]; .
		     -?senseVolt(Me)  : true	<- 	.wait(200);  ?senseVolt(Me,ToolId). 
		     +?senseCurr(Me)  : true	<-	senseCurrents(Me)[artifact_id(ToolId)].
		     -?senseCurr(Me)  : true	<- 	.wait(200);  ?senseCurr(Me,ToolId). 
			 	      		
			 	 +performPlan(Plan)[source(G)]: true <- 
	 	  							 .print("DMS Plan recived");
	 	  							 .print("Plan --> \n",Plan,"\n");
	 	    						 .add_plan(Plan,G);
//	 	    						 -performPlan(Plan);	
	 	    						 .
	 	    	
	 	    	 +planForSWagent(SW)[source(G)]: true <- 
	 	    	  -planForSWagent(SW);					 .my_name(Me);
	 	    	  										 .print("--->> Received Message::: {",G,"} Requested Me {",Me,"}Swicth OFF manouver" );
	 	    	 										 .wait(100);
	 	    	  									 	 .send("simulationCall",tell,swichAction(SW));
	 	    	  									 	 .send("dMS",tell,gotActionGoingOn(" Reporting to DMS Agent Performed Action Agree Betweeing Me {",Me,"} and {",G,"}"));
	 	    	  									 	 .send(G,tell,gotActionGoingOnAg(" Manouver Performed As Requested Ageement..."));
	 	    	  									 	 .
	 	    	  									 	 
	 	    	  									 	 
	 	    	+tellSwAgent(Sw1,Sw2,BusZone):true 	<- 
	 	    	 	-tellSwAgent(Sw1,Sw2,BusZone);
	 	    	  				 			  	 .concat("Ag_",Sw1,SwtAg1);
	 	    	  				 			  	 .concat("Ag_",Sw2,SwtAg2);
	 	    	  				 			  	 .concat("Line.",Sw1,Swt1);
	 	    	  				 			  	 .concat("Line.",Sw2,Swt2); 
	 	    	  				 			  	 .print("telling LineSwicth agent to OPEN--->",Swt1);
	 	    	  				 			  	 .send(SwtAg1,tell,planForSWagent(Swt1)); 
	 	    	  				 			  	 .print("telling LineSwicth agent to OPEN---->",Swt2);
	 	    	  				 			  	 .send(SwtAg2,tell,planForSWagent(Swt2))

	 	    	  				 			  	 .
	 	    	  									 	 
	 	    	  									 	 
	 	    	 
	 	    	   +gotActionGoingOnAg(Action)[source(G)]:    true <- .print(" Messagen received From Agent {", G ,"} : \n Reporting Executed Action/Or something ---> ", Action);.
	 	    	  												 
	 	    	   +gotCommunicationFault(Action)[source(G)]: true <- .print(" Agent *{", G ,"}*  Asked :", Action );
	 	    	  												       isclosetosw(YesNo)[artifact_id(ToolId)];
	 	    	  												      .wait(100);
	 	    	  												      .send(G,tell,gotCommunicationFaultResposta(YesNo));
	 	    	  												      .
	 	    	  												 
	 	    	   
	 	    	   +gotCommunicationFaultResposta(Action)[source(G)]: true <- 
	 	    	   															.wait(100);
	 	    	   															.print(" ***Received answer From Agent {", G ,"} : ", Action);
	 	    	  															.												 
	 	    	  												 
	 	    	  				     
	 	    	   +tickFalt(S): true <- 
	 	    	    				
	 	      			              Msg = "Are you close to energyzed switch!?...";
	 	    	  					 .print("Asking  {Ag_",S,"} if neighbors Agents are close to energyzed switch");
	 	    	 					 .concat("Ag_",S,Ag);
	 	    	 					 .send(Ag,tell,gotCommunicationFault(Msg))
	 	    	 					 .
	 	    	  									 
//	 	    	    +!tickFalt: true <- 
//	 	    	    				  isclosetosw(YesNo);
//	 	      			              Msg = "Are you close to energyzed switch... if yes close it now!" ;
//	 	    	  					 .print("Asking  {Ag_",S,"} if neighbors Agents are close to energyzed switch");
//	 	    	 					 .concat("Ag_",S,Ag);
//	 	    	 					 .send(Ag,tell,gotCommunicationFault("Confirmed Attachment by tick ... "))
//	 	    	 					 .
	 	    	  									 
	 	    	  				 	
	 	       +!attachTo :true <- 
	 	        			  WaitTime = 1000+math.random(1000)+math.random(1000)+math.random(1000);
   		 	 				 .wait(WaitTime);
   		 	 				  attachedTo;
   		 	 				 .wait(WaitTime);
   		 	 				 .	
   		 	 				 
   		 	   +!faltCommunication :true <-
   		 	    			 
	 	        			  WaitTime = 1000+math.random(1000)+math.random(1000)+math.random(1000);
   		 	 				 .wait(WaitTime);
   		 	 				  faltCommunication;
   		 	 				 .wait(WaitTime);
   		 	 				 .
   		 	 				  
   		 	 				  				 			  	 
	 	    	 +tickAt(S): true <- 
	 	    	  					 .print("I am attachedTo Ag_",S);
	 	    	 					 .concat("Ag_",S,Ag);
	 	    	 					 .wait(100);
	 	    	 					 .send(Ag,tell,gotActionGoingOnAg("Confirmed Attachment by tick ... "))
	 	    	 					 .
	 	    	  					 
		        
{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }
{ include("$jacamoJar/templates/org-obedient.asl") }
