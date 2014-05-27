/**
 * VpsSpec.java
 * 2 nov 2013
 */
package lnu.vps;

/**
 * @author jonasl
 *
 */
public class VpsSpec {
	public final String projectName;
	public final String projectDesc;
	//public final String entryPoint;
	public final String[] entryPoints;
	public final String[] classPaths;
	public final String[] includeStrings;
	public final String[] excludeStrings;

	public VpsSpec(String pName, String pDesc, String[] ePoints, 
			String[] cPaths, String[] include, String[] exclude) {
		projectName = pName;
		projectDesc = pDesc;
		entryPoints = ePoints;
		classPaths = cPaths;
		includeStrings = include;
		excludeStrings = exclude;
	}
	
	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append("*** vps Project Spec ***").append('\n');
		buf.append("Project Name: ").append(projectName).append('\n');
		buf.append("Project Description: ").append(projectDesc).append('\n');
		//buf.append("Program Entry Point: ").append(entryPoint).append('\n');
		
		buf.append("Entry Points: ").append('\n');
		for (String path : entryPoints)
			buf.append('\t').append(path).append('\n');
		
		buf.append("Class Paths: ").append('\n');
		for (String path : classPaths)
			buf.append('\t').append(path).append('\n');
		
		if (includeStrings != null && includeStrings.length>0) {
			buf.append("Include Strings: ");
			for (String str : includeStrings)
				buf.append(str).append(", ");
			buf.append('\n');
		}
		
		if (excludeStrings != null && excludeStrings.length>0) {
			buf.append("Exclude Strings: ");
			for (String str : excludeStrings)
				buf.append(str).append(", ");
			buf.append('\n');
		}

		return buf.toString();
	}

}
