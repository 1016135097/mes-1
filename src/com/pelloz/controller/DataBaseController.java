package com.pelloz.controller;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pelloz.auth.AuthPassport;
import com.pelloz.properties.PropertyMgr;

/**
 * 用于重置数据库，通过database.do访问
 * @author zp
 *
 */
@Controller
@RequestMapping("/database.do")
public class DataBaseController {

	@Resource
	private HibernateTemplate hibernateTemplate;

	/**
	 * 重置数据库表内容，提交method=reset执行，并返回主页。
	 * 需要 admin 权限
	 * @return
	 */
	@AuthPassport(department = { "admin" })
	@RequestMapping(params = "method=reset")
	public String reset() {

		Session session = hibernateTemplate.getSessionFactory().openSession();
		session.beginTransaction();

		System.out.println("dropTables begin");
		this.dataBaseControl("droptables", session);
		
		System.out.println("createtables begin");
		this.dataBaseControl("createtables", session);
		
		System.out.println("insertvalues begin");
		this.dataBaseControl("insertvalues", session);
		
		session.getTransaction().commit();
		session.close();

		return "redirect:index.jsp";

	}
	
	/**
	 * 由.properties文件中定义的key找到 .sql 文件，然后执行它
	 * @param actionname 必须是.properties文件中定义的key
	 * @param session
	 */
	private void dataBaseControl(String actionname,Session session){
		
		//由配置文件取得绝对路径
		String realpath = PropertyMgr.getProperty(actionname);
		System.out.println(realpath);
		StringBuffer sqlbuffer = new StringBuffer("");
		BufferedReader br = null;

		try {
			br = new BufferedReader(new FileReader(realpath));
			String data = br.readLine();
			
			while (data != null) {
				sqlbuffer.append(data);
				data = br.readLine(); // 接着读下一行
			}
			
			//以分号为界，将文件中命令断行运行
			String[] sqls = sqlbuffer.toString().split(";");
			for (int i = 0; i < sqls.length; i++) {
				session.createSQLQuery(sqls[i]).executeUpdate();
			}

		} catch (FileNotFoundException e) {
			System.out.println("读取SQL文件出错");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("读取SQL文件出错");
			e.printStackTrace();
		} finally {
			//关闭IO流
			try {
				if (br != null) {
					br.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

}


















