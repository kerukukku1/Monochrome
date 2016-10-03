package Main.UI.Util;

import com.asprise.imaging.core.Imaging;
import com.asprise.imaging.core.Request;
import com.asprise.imaging.core.Result;

public class ScannerUtil {

	public static void scan(int dpi,String meta) throws Exception{
		String dpis = ""+dpi;
		Imaging imaging = new Imaging("myApp", 0);
		String path = FolderManager.getScannerPath(dpi, meta);
		System.out.println(path);
		Result res = imaging.scan(Request.fromJson(
		         "{"
				 + "\"twain_cap_setting\" : {"
		         + "  \"ICAP_PIXELTYPE\" : \"TWPT_RGB\","
				 + "  \"ICAP_XRESOLUTION\" : \"" + dpis + "\","
		         + "  \"ICAP_YRESOLUTION\" : \""+dpis+"\","
				 + "  \"ICAP_SUPPORTEDSIZES\" : \"TWSS_A3\""
				 + "},"
		         + "\"output_settings\" : [ {"
		         + "  \"type\" : \"save\","
		         + "  \"format\" : \"png\","
		         + "  \"save_path\" : \""+path +"\""
		         + "} ]"
		       + "}"), "select", false, false);
	}
}
