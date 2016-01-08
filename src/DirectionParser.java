import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;

public class DirectionParser {
	private static final Pattern REMOVE_TAGS = Pattern.compile("<.+?>");
	ConfigurationManager cm = null;

	/**
	 * The direction parser will first remove the file, then reads the route and
	 * afterwards processes the string into a route list. Which are the streets 
	 * with the distances to drive on.
	 * 
	 * @param locationToGetTo
	 * @param cm
	 * @throws IOException
	 */
	public DirectionParser(String locationToGetTo, ConfigurationManager cm) throws IOException {
		this.cm = cm;
		removeHtmlFile();
		DirectionsRoute[] routes = getRouteList(locationToGetTo);
		String routList = parseRouteStepsIntoString(routes);
		String htmlText = buildHTMLFile(routList);
		saveHtmlFile(htmlText);
	}

	/**
	 * Get the route from the google api
	 * 
	 * @param location
	 * @return
	 */
	private DirectionsRoute[] getRouteList(String location) {
		GeoApiContext context = new GeoApiContext().setApiKey(cm.configuration.getProperty("google_api_code"));

		DirectionsApiRequest request = DirectionsApi.getDirections(context,
				cm.configuration.getProperty("default_location"), location);
		request.language(cm.configuration.getProperty("language"));
		DirectionsRoute[] routes = request.awaitIgnoreError();
		return routes;
	}

	/**
	 * Saves the steps of the first route and the first leg into a string
	 * 
	 * @param routes
	 * @return
	 */
	private String parseRouteStepsIntoString(DirectionsRoute[] routes) {
		// System.out.println("size of routes " + routes.length);
		// System.out.println("size of legs " + routes[0].legs.length);
		StringBuilder sb = new StringBuilder();
//		int count = routes[0].legs[0].steps.length;
		for (DirectionsStep step : routes[0].legs[0].steps) {
			System.out.println(removeTags(step.htmlInstructions));
			sb.append(removeTags(step.htmlInstructions));
			sb.append("<br>");
			sb.append(step.distance);
			sb.append("<br>");
//			if (--count > 0) {
				// sb.append("<br>");
//			}
		}
		return sb.toString();
	}

	/**
	 * Remove the current existing html file
	 */
	private void removeHtmlFile() {
		String storagePath = new String(cm.configuration.getProperty("storage_path"));
		String storageFilename = new String(cm.configuration.getProperty("storage_file_name"));
		File file = null;
		if (storagePath.isEmpty()) {
			file = new File(storageFilename);
		} else {
			file = new File(storagePath + java.io.File.separator + storageFilename);
		}
		if (file.exists() && file.isFile()) {
			file.delete();
		}
	}

	/**
	 * Get a route and save their steps into a html file, without html format
	 * creates the new directory if it is not existing
	 * 
	 * @return
	 * @throws IOException
	 */
	private void saveHtmlFile(String text) throws IOException {
		String storagePath = new String(cm.configuration.getProperty("storage_path"));
		String storageFilename = new String(cm.configuration.getProperty("storage_file_name"));
		File file = null;
		if (storagePath.isEmpty()) {
			file = new File(storageFilename);
		} else {
			file = new File(storagePath + java.io.File.separator + storageFilename);
		}
		File path = new File(storagePath);
		if(!path.exists() || !path.isDirectory()){
			path.mkdirs();
		}
		BufferedWriter bw = new BufferedWriter(new FileWriter(file));
		bw.write(text);
		bw.close();
	}

	/**
	 * Generates a minimal html file of a string, which is inserted in the body
	 * 
	 * @param text
	 * @return
	 */
	private String buildHTMLFile(String text) {
		StringBuilder htmlFileBuilder = new StringBuilder();
		htmlFileBuilder.append("<html><head><title></title></head><body><p>");
		htmlFileBuilder.append(text);
		htmlFileBuilder.append("</p></body></html>");
		return htmlFileBuilder.toString();
	}

	/**
	 * Removes the html tags from the body.
	 * 
	 * @param string
	 * @return
	 */
	public static String removeTags(String string) {
		if (string == null || string.length() == 0) {
			return string;
		}
		if (string.contains("<div style=")) {
			string = string.replace("<div style=", ". <div style=");
		}
		// return string;
		Matcher m = REMOVE_TAGS.matcher(string);
		return m.replaceAll("");
	}

}
