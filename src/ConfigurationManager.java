import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

/**
 * The configuration manager handles the configuration, reads it at program
 * start and write it if it is not existing
 * 
 * @author Konry
 *
 */
public class ConfigurationManager {

	private final String configComment = "This is the configuration file for the Application FFRouteList.\n"
			+ "- The version number shall not be modified, because it changes the stucture of the config file\n"
			+ "- The default location can be set to every street or google acceptable address, decimal geo informations\n"
			+ "- The language is also for the return language of the google api\n"
			+ "- At the storage path the route list is saved.\n\n";

	private final String versionString = "0.1.2";

	public Properties configuration = null;

	public ConfigurationManager() throws IOException {
		initiate();
	}

	/**
	 * The initialization process, checks if a configuration exists,
	 * if not create a new configuration file
	 * and read it out
	 * 
	 * @throws IOException
	 */
	private void initiate() throws IOException {
		if (!checkPropertiesFileExisting()) {
			writeConfigurationFile(generateDefaultPropertiesObject());
		}
		configuration = readDefaultConfiguarionFile();
		if (configuration.getProperty("version") != versionString) {
			replaceConfigurationFile(configuration);
		}
	}

	/**
	 * Check if the config file is already existing
	 * 
	 * @return
	 */
	private boolean checkPropertiesFileExisting() {
		File file = new File("config.cfg");
		return file.exists();
	}

	/**
	 * Reads the configuration file and write it into the properties file.
	 * 
	 * @return
	 * @throws IOException
	 */
	private Properties readDefaultConfiguarionFile() throws IOException {
		Properties prop = new Properties();
		FileInputStream in = new FileInputStream("config.cfg");
		prop.load(in);
		in.close();

		return prop;
	}

	/**
	 * Writes the configuration file with default values.
	 * 
	 * @throws IOException
	 */
	private void writeConfigurationFile(Properties config) throws IOException {
		FileOutputStream out = new FileOutputStream("config.cfg");
		config.store(out, configComment);
		out.close();
	}

	private Properties generateDefaultPropertiesObject() {
		Properties defaultProperties = new Properties();

		defaultProperties.setProperty("version", versionString);
		defaultProperties.setProperty("default_location", "53.872000, 10.677874");
		defaultProperties.setProperty("language", "de");
		defaultProperties.setProperty("storage_path", "");
		defaultProperties.setProperty("storage_file_name", "routelist.html");
		defaultProperties.setProperty("google_api_code", "TODO insert");
		defaultProperties.setProperty("debug_mode", "true");
		return defaultProperties;
	}

	private void replaceConfigurationFile(Properties config) throws IOException {
		Properties newPropertiesFile = generateDefaultPropertiesObject();
		Iterator<Entry<Object, Object>> it = config.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Object, Object> pair = it.next();
			//System.out.println(pair.getKey() + " = " + pair.getValue());

			/* special rename handling... */
			if (!pair.getKey().toString().equals("version")) {
				newPropertiesFile.setProperty(pair.getKey().toString(), pair.getValue().toString());
			}
			it.remove(); // avoids a ConcurrentModificationException
		}
		configuration = newPropertiesFile;
		writeConfigurationFile(newPropertiesFile);
	}
}
