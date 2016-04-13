package server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;

public class XMLReader extends DefaultHandler {

	String inputFile;
	VideoFile currentVideo;
	String currentSubElement;
	List<VideoFile> videoList;

	public XMLReader(String inputFile) {
		this.inputFile = inputFile;
		try {
			// use the default parser
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			// parse the input
			saxParser.parse(inputFile, this);
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (SAXException saxe) {
			saxe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public void startDocument() throws SAXException {
		System.out.println("Started parsing: " + inputFile);
		videoList = new ArrayList<VideoFile>();
	}

	public void startElement(String uri, String localName, String qName,
			Attributes attrs) throws SAXException {

		// Sort out element name if (no) namespace in use
		String elementName = localName;
		if ("".equals(elementName)) {
			elementName = qName;
		}

		// Work out what to do with this element
		switch (elementName) {
		case "video":
			currentVideo = new VideoFile();
			break;
		case "title":
			currentSubElement = "title";
			break;
		case "filename":
			currentSubElement = "filename";
			break;
		default:
			currentSubElement = "none";
			break;
		}

		// This assumes only one attribute - it will not work for more than one.
		if (attrs != null) {
			// Sort out attribute name
			String attributeName = attrs.getLocalName(0);
			if ("".equals(attributeName)) {
				attributeName = attrs.getQName(0);
			}

			// Store value
			String attributeValue = attrs.getValue(0);
			switch (elementName) {
			case "video":
				currentVideo.setID(attributeValue);
				break;
			default:
				break;
			}
		}
	}

	public void characters(char ch[], int start, int length)
			throws SAXException {

		String newContent = new String(ch, start, length);

		switch (currentSubElement) {
		case "title":
			currentVideo.setTitle(newContent);
			break;
		case "filename":
			currentVideo.setFilename(newContent);
			break;
		default:
			// TODO no action appears necessary
			break;
		}
	}

	public void endElement(String uri, String localName, String qName)
			throws SAXException {

		// Finishing an element means we're definitely not in a sub-element
		// anymore
		currentSubElement = "none";

		// Sort out element name if (no) namespace in use
		String elementName = localName;
		if ("".equals(elementName)) {
			elementName = qName;
		}

		if (elementName.equals("video")) {
			videoList.add(currentVideo);
			// We've finished and stored this student, so remove the reference
			currentVideo = null;
		}
	}

	public void endDocument() throws SAXException {
		System.out.println("Finished parsing, stored " + videoList.size()
				+ " videos.");

		for (VideoFile thisVideo : videoList) {
			System.out.println("ID: " + thisVideo.getID());
			System.out.println("Title: " + thisVideo.getTitle());
			System.out.println("Filename: " + thisVideo.getFilename());
		}
	}

	public List<VideoFile> getList() {
		return videoList;
	}
}
