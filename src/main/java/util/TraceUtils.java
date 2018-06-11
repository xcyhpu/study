/**
 * 
 */
package util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

import java.security.SecureRandom;

/**
 * 
 * @author Fermi(fermi@youleyu.com)
 * @date Dec 8, 2013
 * @desc
 */
public class TraceUtils {
	private static SecureRandom random = new SecureRandom();
	public static final String TRACE_ID_KEY = "traceId";
	private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

	/**
	 * 开始Trace, 默认生成本次Trace的ID(8字符长)并放入MDC.
	 */
	public static void beginTrace(String serverName, int commond) {
		String traceId = encodeBase62(random.nextLong());
		MDC.put(TRACE_ID_KEY, serverName + "-" + commond + "-" + traceId);
	}

	public static void beginTrace(String serverName, int commond, String traceId) {
		if (StringUtils.isNotBlank(traceId)) {
			MDC.put(TRACE_ID_KEY, serverName + "-" + commond + "-" + traceId);
		} else {
			beginTrace(serverName, commond);
		}
	}

	public static void beginTrace(Object bussinessInfo){
		String traceId = encodeBase62(random.nextLong());
		MDC.put(TRACE_ID_KEY, bussinessInfo.toString() + "-" + traceId);
	}

	public static void beginTrace() {
		long epoch = System.currentTimeMillis() / 1000;
		String traceId = encodeBase62(random.nextLong());
		MDC.put(TRACE_ID_KEY, epoch + traceId);
	}

	public static void beginTrace(String traceId){
		MDC.put(TRACE_ID_KEY,  traceId);
	}

	public static String getTrace() {
		Object obj = MDC.get(TRACE_ID_KEY);
		if (obj != null) {
			return obj.toString();
		}
		return "";
	}

	/**
	 * 结束一次Trace, 清除traceId.
	 */
	public static void endTrace() {
		MDC.remove(TRACE_ID_KEY);
	}

	/**
	 * Base62(0_9A_Za_z)编码数字, long->String.
	 */
	private static String encodeBase62(long num) {
		return alphabetEncode(num, 62);
	}

	private static String alphabetEncode(long num, int base) {
		num = Math.abs(num);
		StringBuilder sb = new StringBuilder();
		for (; num > 0; num /= base) {
			sb.append(ALPHABET.charAt((int) (num % base)));
		}

		return sb.toString();
	}
	
	public static void setDid(String did) {
		MDC.put("did", did);
	}

	public static void setVid(String vid) {
		MDC.put("vid", vid);
	}

	public static void setPid(String pid) {
		MDC.put("pid", pid);
	}
	
	public static String getDid() {
		Object obj = MDC.get("did");
		if (obj != null) {
			return obj.toString();
		}
		return "";
	}

	public static String getVid() {
		Object obj = MDC.get("vid");
		if (obj != null) {
			return obj.toString();
		}
		return "";
	}
	
	public static String getPid() {
		Object obj = MDC.get("pid");
		if (obj != null) {
			return obj.toString();
		}
		return "";
	}
}
