/**
 * VpsParser.java
 * 2 nov 2013
 */
package lnu.vps;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * @author jonasl
 *
 */
public class VpsParser {
	private final String vps_path;

	private VpsParser(String vps_path) {
		this.vps_path = vps_path;
	}
	
	private File getVpsFile(String file_path) {
		if (!file_path.endsWith(".vps")) {
			String msg = "vps-file does not end with postfix .vps: ";
			throw new RuntimeException(msg+file_path);
		}
		
		File file = new File(file_path);
		if (file.exists() && file.canRead())
			return file;
		else
			throw new RuntimeException("Can't read file " + file_path);
	}
	
	private VpsSpec parse(XMLStreamReader reader) {
		VpsSpec spec = null;
		String project_name = null, project_description = null;
		ArrayList<String> entry_points = new ArrayList<String>();
		ArrayList<String> class_paths = new ArrayList<String>();
		ArrayList<String> exclude_strings = new ArrayList<String>();
		ArrayList<String> include_strings = new ArrayList<String>();

		try {
			String tagContent = null;
			while(reader.hasNext()){
				int event = reader.next();

				switch(event) {

				case XMLStreamConstants.CHARACTERS:
					tagContent = reader.getText().trim();
					//System.out.println("-----"+tagContent);
					break;

				case XMLStreamConstants.END_ELEMENT:
					String element_name = reader.getLocalName();
					//System.out.println(element_name);
					switch(element_name){
					case "projectname":
						project_name = tagContent;;
						break;
					case "projectdescription":
						project_description = tagContent;
						break;
					case "entrypoint":
						entry_points.add(tagContent);
						//entry_point = tagContent;
						break;
					case "classpath":
						class_paths.add(tagContent);
						break;
					case "include":
						include_strings.add(tagContent);
						break;
					case "exclude":
						exclude_strings.add(tagContent);
						break;
					}
					break;

				case XMLStreamConstants.END_DOCUMENT:
					String[] ePoints = entry_points.toArray(new String[0]);
					String[] cPaths = class_paths.toArray(new String[0]);
					String[] include = include_strings.toArray(new String[0]);
					String[] exclude = exclude_strings.toArray(new String[0]);
					spec = new VpsSpec(project_name, project_description,ePoints,cPaths,include,exclude);
					
					// print out vps file content
					System.out.println(spec);
					
					break;
				}

			}
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
		return spec;
	}
	
	private XMLStreamReader setup_reader() {
		XMLInputFactory factory = XMLInputFactory.newInstance();
		File vps_file = getVpsFile(vps_path);

		XMLStreamReader reader = null;
		try {
			//reader = factory.createXMLStreamReader( ClassLoader.getSystemResourceAsStream("vps_path") );
			reader = factory.createXMLStreamReader( new FileReader(vps_file) );
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Could not read: "+vps_path);
		}
		return reader;
	}

	public static VpsSpec parseFile(String vps_path) {
		VpsParser parser = new VpsParser(vps_path);
		XMLStreamReader reader = parser.setup_reader();
		return parser.parse(reader);
		
	}
	/**
	 * Simple Test Driver
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		String vps_path = "/Documents and Settings/Administrator/workspace/ASM_RTA/src/lnu/test/";
		String path = vps_path+"vps_files/observer.vps";
		System.out.println(path);
		
		parseFile(path);
	}
	


}
