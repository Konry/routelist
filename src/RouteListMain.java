import java.io.IOException;

public class RouteListMain {

	public static void main(String[] args) {
		try {
			ConfigurationManager configmanager = new ConfigurationManager();
			if(args.length > 0){
				StringBuilder sb = new StringBuilder();
				
				for(String s : args){
					sb.append(s+ " ");
				}
				new DirectionParser(sb.toString(), configmanager);
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//

	}

}
