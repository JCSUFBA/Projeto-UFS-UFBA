package projects.examplecode;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Source;

import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

import com.sun.xml.internal.bind.api.Bridge;
import com.sun.xml.internal.ws.api.message.Message;
import com.sun.xml.internal.ws.api.message.MessageHeaders;
import com.sun.xml.internal.ws.spi.db.XMLBridge;

public class NodeMessage extends Message{

	@Override
	public Message copy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MessageHeaders getHeaders() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPayloadLocalPart() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPayloadNamespaceURI() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasHeaders() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasPayload() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public SOAPMessage readAsSOAPMessage() throws SOAPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Source readEnvelopeAsSource() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public XMLStreamReader readPayload() throws XMLStreamException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T readPayloadAsJAXB(Unmarshaller arg0) throws JAXBException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T readPayloadAsJAXB(Bridge<T> arg0) throws JAXBException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T readPayloadAsJAXB(XMLBridge<T> arg0) throws JAXBException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Source readPayloadAsSource() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void writePayloadTo(XMLStreamWriter arg0) throws XMLStreamException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void writeTo(XMLStreamWriter arg0) throws XMLStreamException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void writeTo(ContentHandler arg0, ErrorHandler arg1) throws SAXException {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
	

}
