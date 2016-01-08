import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * The configuration manager handles the configuration, reads it at program
 * start and write it if it is not existing
 * 
 * @author Konry
 *
 */
public class ConfigurationManager {

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
			generateDefaultConfigurationFile();
		} 
		configuration = readDefaultConfiguarionFile();
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
	private void generateDefaultConfigurationFile() throws IOException {
		Properties defaultProperties = new Properties();
		FileOutputStream out = new FileOutputStream("config.cfg");
		
		defaultProperties.setProperty("version", "0.0.0.1");
		defaultProperties.setProperty("default_location", "53.872000, 10.677874");
		defaultProperties.setProperty("language", "de");
		defaultProperties.setProperty("storage_path", "");
		defaultProperties.setProperty("storage_file_name", "routelist.html");
		defaultProperties.setProperty("google_api_code", "TODO insert");
		
		defaultProperties.store(out, "This is the configuration file for the Application FFRouteList.\n"
				+ "- The shall not be modified, because it changes the stucture of the config file\n"
				+ "- The default location can be set to every street or google acceptable address, decimal geo informations\n"
				+ "- The language is also for the return language of the google api\n"
				+ "- At the storage path the route list is saved.\n\n");
		out.close();
	}
}
