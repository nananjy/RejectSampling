package 包名;

import java.io.File;

/**
 * 通过文件名生成文件路径
 * @author 作者名
 */
public class FilePathUtils {

	private static String filePath;//文件路径
	
	/**
	 * 通过文件名获取文件路径
	 * @param fileName 文件名
	 * @return 文件路径
	 */
	public static String generateFilePath(String fileName) {
		filePath="";//初始化文件路径
		try {
			File directory = new File("");
			String dirpath = directory.getCanonicalPath();
			filePath += (dirpath + File.separator + "Data" + File.separator + fileName);
		} catch (Exception e) {
			throw new RuntimeException();
		}
		return filePath;
	}
}
