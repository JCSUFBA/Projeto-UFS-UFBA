package projects.examplecode.util;

import java.text.DecimalFormat;

import sinalgo.tools.logging.Logging;

public class WeightedEdge  {
	Logging myLog = Logging.getLogger("logxxx.txt");
	int startNode;
	int endNode;
	double weight;
	
	/**
	 * @param msg The message to send
	 */
	public WeightedEdge(int snp, int endp, double weightp) {
		this.startNode = snp;
		this.endNode = endp;
		this.weight = weightp;
	}
	
	public int getStartNode() {
		return startNode;
	}
	
	public int getEndNode() {
		return endNode;
	}
	
	public double getWeight() {
		return weight;
	}
	
	public void print() {
		DecimalFormat df = new DecimalFormat("0.00");
		
		myLog.log("("+startNode+", "+endNode+") : "+df.format(weight));
	}
}
