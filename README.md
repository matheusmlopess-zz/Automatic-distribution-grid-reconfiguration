## Scope
The multi-Agent system designed based on automatic reconfiguration principles was programmed as a set of AgentSpeak codes in which work and cooperate inside a common simulated distribution grid environment. Creating the application meant programming the agents on the one side, including the actions logic control to be executed relying on the environment changes, and  on the other side the environment itself. That stated, the environment  notion was solely based on the chosen distribution grid concept and explored according to the proposed problematic.

Programming languges used:

- Java

- JaCaMo framework for Multi-Agent Programming that combines three separate technologies:
   http://jacamo.sourceforge.net/
--- Jason – for programming autonomous agents
--- Cartago – for programming environment artifacts
--- Moise – for programming multi-agent organisations

- OpenDSS script language.
  https://sourceforge.net/projects/electricdss/
  An power Distribution System Simulator for supporting distributed resource integration and grid modernization efforts.

- Graphstream API making use of Dijkstra's Shortest Path Algorithm
   http://graphstream-project.org/doc/Algorithms/Shortest-path/Dijkstra/

## Dissertation Project 

The Application model has been conducted in a context which promotes the use of agent-based technology to model a smart approach for the purpose of reconfiguration. For that matter, a framework for MAS that combines three separate state of the art technologies was choosen to suit this purpose: Jason, CArTaGo, Moise. Also an OpenDSS distribution grid model was integrated in the simulation platform, including all structures responsible to represent a three-phase distribution grid.

The first proposed approach implemented a centralized model  which uses a graph-theoretic distribution restoration that applies the shortest-path algorithm search technique to find the network topologies  capable of minimizing the number of out of service loads. This provides an optimal solution, that is a restoration plan with minimum switching operations for a restoration course. 

The second proposed approach provided a simple and effective method to handle outage management and was implemented based on a decentralized model for reconfiguration. The fundamental ideas implies on having agents widespread on the network which communicate to ask permission for zone energization. Power flow calculations with detailed network model are performed to ensure that the network topologies suggested either by the proposed algorithm or the decentralized approach will be operated within the electrical and operation limits.
