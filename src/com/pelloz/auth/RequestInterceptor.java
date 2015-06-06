package com.pelloz.auth;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * 用于拦截过于频繁的请求
 * 
 * @author zp
 *
 */
public class RequestInterceptor extends HandlerInterceptorAdapter {

	HashMap<String, RequestCount> requestmap = new HashMap<String, RequestCount>();

	public RequestInterceptor() {
		super();

	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		String ipAddress = getIpAddr(request);
		int count = 0;

		// 每100000次请求清理一次map
		// TODO 可以另外开线程处理
		if (count < 100000) {
			count++;
		} else {
			Set<String> keys = requestmap.keySet();
			long systemtime = System.currentTimeMillis() - 1800000;

			// 半个小时没有连接的ip从map中清除
			for (String key : keys) {
				if (requestmap.get(key).getTimemillis() < systemtime) {
					requestmap.remove(key);
				}
			}
			// 为了提高反应速度
			return true;
		}

		if (ipAddress != null) {
			RequestCount rc = requestmap.get(ipAddress);
			// 新IP建立索引
			if (rc == null) {
				// 5秒最多10次请求
				requestmap.put(ipAddress, new RequestCount(10, 5000));
				return true;
			}
			if (rc.addAble()) {
				return true;
			} else {
				return false;
			}
		}
		return true;
	}

	/**
	 * 获取当前网络ip，可能返回null
	 * 
	 * @param request
	 * @return
	 */
	public String getIpAddr(HttpServletRequest request) {
		String ipAddress = request.getHeader("x-forwarded-for");
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getRemoteAddr();
			if (ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")) {
				// 根据网卡取本机配置的IP
				InetAddress inet = null;
				try {
					inet = InetAddress.getLocalHost();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				if (inet != null) {
					ipAddress = inet.getHostAddress();
				}
			}
		}
		// 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
		if (ipAddress != null && ipAddress.length() > 15) {
			// "***.***.***.***".length() = 15
			if (ipAddress.indexOf(",") > 0) {
				ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
			}
		}
		return ipAddress;
	}

	private class RequestCount {

		private long timemillis;
		private long maxtime;
		private ArrayList<Long> times;

		public RequestCount(int maxcount, long maxtimemillis) {
			super();
			this.timemillis = System.currentTimeMillis() - maxtimemillis - 1;
			this.maxtime = maxtimemillis;
			times = new ArrayList<>(maxcount);
			for (int i = 0; i < maxcount; i++) {
				times.add(timemillis);
			}
		}

		public boolean addAble() {
			timemillis = System.currentTimeMillis();
			for (int i = 0; i < times.size(); i++) {
				if (timemillis - times.get(i) > maxtime) {
					times.set(i, timemillis);
					return true;
				}
			}
			return false;
		}

		public long getTimemillis() {
			return timemillis;
		}
	}
}