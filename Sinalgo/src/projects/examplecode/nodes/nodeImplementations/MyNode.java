/*
 Copyright (c) 2007, Distributed Computing Group (DCG)
                    ETH Zurich
                    Switzerland
                    dcg.ethz.ch

 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions
 are met:

 - Redistributions of source code must retain the above copyright
   notice, this list of conditions and the following disclaimer.

 - Redistributions in binary form must reproduce the above copyright
   notice, this list of conditions and the following disclaimer in the
   documentation and/or other materials provided with the
   distribution.

 - Neither the name 'Sinalgo' nor the names of its contributors may be
   used to endorse or promote products derived from this software
   without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package projects.examplecode.nodes.nodeImplementations;

import java.awt.Color;
import java.awt.Graphics;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import sinalgo.configuration.WrongConfigurationException;
import sinalgo.gui.transformation.PositionTransformation;
import sinalgo.io.eps.EPSOutputPrintStream;
import sinalgo.nodes.messages.Inbox;
import sinalgo.nodes.messages.Message;
import sinalgo.tools.logging.Logging;
import sinalgo.nodes.*;

/**
 * The class to simulate the sample2-project.
 */
public class MyNode extends Node {

	Logging myLog = Logging.getLogger("logxxx.txt");
	private ArrayList<Integer> vizinhos;
	private boolean isColored;
	
	
	
	public MyNode() {
	
		this.isColored = false;
		this.vizinhos = new ArrayList<>();
	
	}
	
	public boolean isColored() {
		return isColored;
	}

	public void setColored(boolean isColored) {
		this.isColored = isColored;
	}

		
	

	

	public ArrayList<Integer> getVizinhos() {
		return vizinhos;
	}

	public void setVizinhos(ArrayList<Integer> vizinhos) {
		this.vizinhos = vizinhos;
	}

	@Override
	public void handleMessages(Inbox inbox) {
		while (inbox.hasNext()) {
			Message m = (Message) inbox.next();
		}
	}

	@Override
	public void init() {
	}

	@Override
	public void checkRequirements() throws WrongConfigurationException {
	}

	@Override
	public void neighborhoodChange() {
	}

	@Override
	public void preStep() {
	}

	@Override
	public void postStep() {
	}

	/**
	 * Colors all the nodes that this node has seen once.
	 */
	// @NodePopupMethod(menuText="Color Neighbors")

	/**
	 * Resets the color of all previously colored nodes.
	 */
	@NodePopupMethod(menuText = "Undo Coloring")
	public void UndoColoring() { // NOTE: Do not change method name!
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * sinalgo.nodes.Node#includeMethodInPopupMenu(java.lang.reflect.Method,
	 * java.lang.String)
	 */
	public String includeMethodInPopupMenu(Method m, String defaultText) {
		if (!isColored && m.getName().equals("UndoColoring")) {
			return null; // there's nothing to be undone
		}
		return defaultText;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see sinalgo.nodes.Node#draw(java.awt.Graphics,
	 * sinalgo.gui.transformation.PositionTransformation, boolean)
	 */
	public void draw(Graphics g, PositionTransformation pt, boolean highlight) {
		drawNodeAsSquareWithText(g, pt, highlight, Integer.toString(this.ID), 16, Color.WHITE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * sinalgo.nodes.Node#drawToPostScript(sinalgo.io.eps.EPSOutputPrintStream,
	 * sinalgo.gui.transformation.PositionTransformation)
	 */
	public void drawToPostScript(EPSOutputPrintStream pw, PositionTransformation pt) {
		// the size and color should still be set from the GUI draw method
		drawToPostScriptAsDisk(pw, pt, drawingSizeInPixels / 2, getColor());
	}

}
