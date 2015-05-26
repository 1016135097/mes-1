package com.pelloz.properties;

import java.io.IOException;
import java.util.Properties;

/**
 * 读取属性文件的单例静态类，用于读取 sql/sql.properties 文件中列出的文件名与相对路径，
 * 然后可以使用 getProperty(String filename) 方法转化为绝对路径返回
 * @author zp
 *
 */
public class PropertyMgr {
	static Properties props = new Properties();

	static {
		try {
			props.load(PropertyMgr.class.getClassLoader().getResourceAsStream(
					"sql/sql.properties"));
		} catch (IOException e) {
			System.out.println("配置文件读取错误");
			e.printStackTrace();
		}
	}

	/**
	 * 由文件名取得文件的绝对路径。
	 * 从属性表中取得文件的相对路径，然后转化为绝对路径返回
	 * @param 文件名
	 * @return 绝对路径
	 */
	public static String getProperty(String filename) {
		String relativepath = (String) props.get(filename);
		return PropertyMgr.class.getClassLoader().getResource(relativepath).getPath();
	}
}
