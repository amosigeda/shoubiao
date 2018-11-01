package com.bracelet.util;

import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import com.taobao.api.ApiException;

@SuppressWarnings({ "unused", "restriction" })
public class Utils {
	
	public static BASE64Encoder encoder = new sun.misc.BASE64Encoder();
	public static BASE64Decoder decoder = new sun.misc.BASE64Decoder();

	public final static String accessKeyId = "LTAI8CmVtQhm7KSG";  
	public final static String accessKeySecret = "c9wj4Lw22QmDLuQkx2l46xmfCD4H1e";
	public final static String accessKeyIdOfBeidou = "LTAI7YNEkaz5J7Vy";
	public final static String accessKeySecretOfBeidou = "EIHW3lpcPMXnxsdW7CW9jnriovXTch";
	public final static String MD5_MAGIC = "water";

	public final static String URL = "https://eco.taobao.com/router/rest";
	// public final static String APPKEY = "23632518";
	// public final static String SECRET = "41ad5356a0f7909afe2620b98bd151a7";
	public final static String SMSFREESIGNNAME = "沃特沃德";// 短信签名

	/*
	 * 发送短信
	 */
	public static final String SENDMSG_URL = "https://eco.taobao.com/router/rest";
	public static final String APPKEY = "24607772";
	public static final String SECRET = "9798140e8ff8375e519f5b9d296f94de";
	public static final String SMS_TYPE = "normal";
	public static final String useFingerprintOpenDoor_SMSTEMPLATE_CODE = "SMS_91985064";// 使用指纹打开短信模板
	public static final String pickALockSendMsg_SMSTEMPLATE_CODE = "SMS_91990103";// 使用指纹打开短信模板
	public static final String fingerSosSendMsg_SMSTEMPLATE_CODE = "SMS_125735073";// 使用报警指纹打开短信模板
	public static final String lowElectricSosMsgSendMsg_SMSTEMPLATE_CODE = "SMS_134515053";// 低电量报警

	public final static String USER_SAVE = "/upload/user/";
	
	public final static String dou = ",";
	public final static String you = "]";

	public static String randomString(int len) {
		if (len <= 0) {
			len = 32;
		}
		String chars = "ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz2345678";
		int maxPos = chars.length();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < len; i++) {
			sb.append(chars.charAt((int) Math.floor(Math.random() * maxPos)));
		}
		return sb.toString();
	}

	public static int randomInt(int low, int high) {
		return (int) Math.floor(Math.random() * (high - low) + low);
	}

	/**
	 * 生成md5
	 * 
	 * @param message
	 */
	public static String getMD5(String message) {
		String md5str = "";
		try {
			// 1 创建一个提供信息摘要算法的对象，初始化为md5算法对象
			MessageDigest md = MessageDigest.getInstance("MD5");

			// 2 将消息变成byte数组
			byte[] input = message.getBytes();

			// 3 计算后获得字节数组,这就是那128位了
			byte[] buff = md.digest(input);

			// 4 把数组每一字节（一个字节占八位）换成16进制连成md5字符串
			md5str = bytesToHex(buff);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return md5str;
	}

	/**
	 * 二进制转十六进制
	 * 
	 * @param bytes
	 * @return
	 */
	public static String bytesToHex(byte[] bytes) {
		StringBuffer md5str = new StringBuffer();
		// 把数组每一字节换成16进制连成md5字符串
		int digital;
		for (int i = 0; i < bytes.length; i++) {
			digital = bytes[i];

			if (digital < 0) {
				digital += 256;
			}
			if (digital < 16) {
				md5str.append("0");
			}
			md5str.append(Integer.toHexString(digital));
		}
		return md5str.toString().toUpperCase();
	}

	/**
	 * 计算两个坐标点距离
	 * 
	 * @param lngA
	 * @param latA
	 * @param lngB
	 * @param latB
	 * @return 单位：米
	 */
	public static double calcDistance(double lngA, double latA, double lngB,
			double latB) {
		double earthR = 6371000;
		double x = Math.cos(latA * Math.PI / 180.)
				* Math.cos(latB * Math.PI / 180.)
				* Math.cos((lngA - lngB) * Math.PI / 180);
		double y = Math.sin(latA * Math.PI / 180.)
				* Math.sin(latB * Math.PI / 180.);
		double s = x + y;
		if (s > 1)
			s = 1;
		if (s < -1)
			s = -1;
		double alpha = Math.acos(s);
		return alpha * earthR;
	}

	public static String format14DateString(Date date) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = dateFormat.format(date);
		return dateString;
	}

	public static String format14DateString(long time) {
		return format14DateString(new Date(time));
	}

	public static Timestamp getCurrentTimestamp() {
		return new Timestamp(new Date().getTime());
	}

	/**
	 * 90mmHg<收缩压<140mmHg 60mmHg<舒张压<90mmHg
	 */
	public static List<Map<String, Object>> checkHeartPressure(
			Integer maxHeartPressure, Integer minHeartPressure) {
		List<Map<String, Object>> resultList = new LinkedList<Map<String, Object>>();
		Map<String, Object> maxResultMap = new HashMap<>();
		if (maxHeartPressure < 90) {
			maxResultMap.put("type", 1);
			maxResultMap.put("code", 2);
			maxResultMap.put("msg", "偏低");
		} else if (maxHeartPressure > 140) {
			maxResultMap.put("type", 1);
			maxResultMap.put("code", 1);
			maxResultMap.put("msg", "偏高");
		} else {
			maxResultMap.put("type", 1);
			maxResultMap.put("code", 0);
			maxResultMap.put("msg", "正常");
		}
		Map<String, Object> minResultMap = new HashMap<>();
		if (minHeartPressure < 60) {
			minResultMap.put("type", 2);
			minResultMap.put("code", 2);
			minResultMap.put("msg", "偏低");
		} else if (minHeartPressure > 90) {
			minResultMap.put("type", 2);
			minResultMap.put("code", 1);
			minResultMap.put("msg", "偏高");
		} else {
			minResultMap.put("type", 2);
			minResultMap.put("code", 0);
			minResultMap.put("msg", "正常");
		}
		resultList.add(maxResultMap);
		resultList.add(minResultMap);
		return resultList;
	}

	/**
	 * 60～100次/分
	 */
	public static Map<String, Object> checkHeartRate(Integer heartRate) {
		Map<String, Object> resultMap = new HashMap<>();
		if (heartRate < 60) {
			resultMap.put("code", 2);
			resultMap.put("msg", "偏慢");
		} else if (heartRate > 100) {
			resultMap.put("code", 1);
			resultMap.put("msg", "偏快");
		} else {
			resultMap.put("code", 0);
			resultMap.put("msg", "正常");
		}
		return resultMap;
	}

	// 根据UnicodeBlock方法判断中文标点符号
	public static boolean isChinesePunctuation(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_FORMS
				|| ub == Character.UnicodeBlock.VERTICAL_FORMS) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 围栏 判断点是否在多边形内
	 * 
	 * @param point
	 *            检测点
	 * @param pts
	 *            多边形的顶点
	 * @return 点在多边形内返回true,否则返回false
	 * @author wei
	 */
	public static boolean IsPtInPoly(Point2D.Double point,
			List<Point2D.Double> pts) {

		int N = pts.size();
		boolean boundOrVertex = true; // 如果点位于多边形的顶点或边上，也算做点在多边形内，直接返回true
		int intersectCount = 0;// cross points count of x
		double precision = 2e-10; // 浮点类型计算时候与0比较时候的容差
		Point2D.Double p1, p2;// neighbour bound vertices
		Point2D.Double p = point; // 当前点

		p1 = pts.get(0);// left vertex
		for (int i = 1; i <= N; ++i) {// check all rays
			if (p.equals(p1)) {
				return boundOrVertex;// p is an vertex
			}

			p2 = pts.get(i % N);// right vertex
			if (p.x < Math.min(p1.x, p2.x) || p.x > Math.max(p1.x, p2.x)) {// ray
																			// is
																			// outside
																			// of
																			// our
																			// interests
				p1 = p2;
				continue;// next ray left point
			}

			if (p.x > Math.min(p1.x, p2.x) && p.x < Math.max(p1.x, p2.x)) {// ray
																			// is
																			// crossing
																			// over
																			// by
																			// the
																			// algorithm
																			// (common
																			// part
																			// of)
				if (p.y <= Math.max(p1.y, p2.y)) {// x is before of ray
					if (p1.x == p2.x && p.y >= Math.min(p1.y, p2.y)) {// overlies
																		// on a
																		// horizontal
																		// ray
						return boundOrVertex;
					}

					if (p1.y == p2.y) {// ray is vertical
						if (p1.y == p.y) {// overlies on a vertical ray
							return boundOrVertex;
						} else {// before ray
							++intersectCount;
						}
					} else {// cross point on the left side
						double xinters = (p.x - p1.x) * (p2.y - p1.y)
								/ (p2.x - p1.x) + p1.y;// cross point of y
						if (Math.abs(p.y - xinters) < precision) {// overlies on
																	// a ray
							return boundOrVertex;
						}

						if (p.y < xinters) {// before ray
							++intersectCount;
						}
					}
				}
			} else {// special case when ray is crossing through the vertex
				if (p.x == p2.x && p.y <= p2.y) {// p crossing over p2
					Point2D.Double p3 = pts.get((i + 1) % N); // next vertex
					if (p.x >= Math.min(p1.x, p3.x)
							&& p.x <= Math.max(p1.x, p3.x)) {// p.x lies between
																// p1.x & p3.x
						++intersectCount;
					} else {
						intersectCount += 2;
					}
				}
			}
			p1 = p2;// next ray left point
		}

		if (intersectCount % 2 == 0) {// 偶数在多边形外
			return false;
		} else { // 奇数在多边形内
			return true;
		}

	}

	/**
	 * 将经纬度点集合转换为GeneralPath对象
	 * 
	 * @param points
	 *            点集合(有序)
	 * 
	 * @return GeneralPath对象
	 * @author Administrator 存在点重合的问题
	 */
	public static GeneralPath genGeneralPath(ArrayList<Point2D.Double> points) {
		GeneralPath path = new GeneralPath();

		if (points.size() < 3) {
			return null;
		}

		path.moveTo((float) points.get(0).getX(), (float) points.get(0).getY());

		for (Iterator<Point2D.Double> it = points.iterator(); it.hasNext();) {
			Point2D.Double point = (Point2D.Double) it.next();

			path.lineTo((float) point.getX(), (float) point.getY());
		}

		path.closePath();

		return path;
	}

	public static String rendom6() {
		StringBuilder sb = new StringBuilder();
		String[] chars = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };
		for (int i = 0; i < 6; i++) {
			int id = (int) Math.floor(Math.random() * 10);
			sb.append(chars[id]);
		}
		return sb.toString();
	}

	public static void deleteFile(String deletePath) {
		File file = new File(deletePath);
		if (file.exists() && file.isDirectory()) {
			String[] arr = file.list();
			for (String tmp : arr) {
				new File(deletePath + "/" + tmp).delete();
			}
		}
	}

	public static String getUniqueCode(String unique) {
		Calendar c = Calendar.getInstance();
		long now = c.getTime().getTime(); // 用时间的ms数确定唯一码
		return unique + "_" + String.valueOf(now);
	}

	/**
	 * 文件内容
	 * 
	 * @param path
	 * @param content
	 * @throws IOException
	 */
	public static void createFileContent(String path, String fileName,
			byte[] content) {
		try {
			createFile(path);

			FileOutputStream fos = new FileOutputStream(path + "/" + fileName);
			fos.write(content);
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 文件路径不存在,则创建
	 * 
	 * @param path
	 */
	public static void createFile(String path) {
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	public static String getDownloadPath(String url, String port, String path) {
		String tmp = url + port + path;
		return tmp;
	}
	
	public static String getTime(){
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd,HH:mm:ss");
		return ft.format(new Date());
	}

	
	/**
	 * 将图片转换成二进制
	 * 
	 * @return
	 */
	@SuppressWarnings("restriction")
	public static String getImageBinary() {
		File f = new File("e:/1.jpg");
		BufferedImage bi;
		try {
			bi = ImageIO.read(f);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(bi, "jpg", baos);  //经测试转换的图片是格式这里就什么格式，否则会失真
			byte[] bytes = baos.toByteArray();
 
			return encoder.encodeBuffer(bytes).trim();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
 
	/**
	 * 将二进制转换为图片
	 * 
	 * @param base64String
	 */
	@SuppressWarnings("restriction")
	public static void base64StringToImage(String base64String) {
		try {
			byte[] bytes1 = decoder.decodeBuffer(base64String);
 
			ByteArrayInputStream bais = new ByteArrayInputStream(bytes1);
			BufferedImage bi1 = ImageIO.read(bais);
			File w2 = new File("e://QQ.jpg");// 可以是jpg,png,gif格式
			ImageIO.write(bi1, "jpg", w2);// 不管输出什么格式图片，此处不需改动
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) throws UnsupportedEncodingException {
		 int valueTen = 65535;
	        //将其转换为十六进制并输出
	        String strHex = Integer.toHexString(valueTen);
	        System.out.println(valueTen + " [十进制]---->[十六进制] " + strHex);
	        //将十六进制格式化输出
	        String strHex2 = String.format("%08x",valueTen);
	        System.out.println(valueTen + " [十进制]---->[十六进制] " + strHex2);

	        System.out.println("==========================================================");
	        //定义一个十六进制值
	        String strHex3 = "0002";
	        //将十六进制转化成十进制
	        int valueTen2 = Integer.parseInt(strHex3,16);
	        System.out.println(strHex3 + " [十六进制]---->[十进制] " + valueTen2);

	        System.out.println("==========================================================");
	        //可以在声明十进制时，自动完成十六进制到十进制的转换
	        int valueHex = 0x00001322;
	        System.out.println("int valueHex = 0x00001322 --> " + valueHex);
	        
	    /*    65535 [十进制]---->[十六进制] ffff
	        65535 [十进制]---->[十六进制] 0000ffff
	        ==========================================================
	        ffff [十六进制]---->[十进制] 65535
	        ==========================================================
	        int valueHex = 0x00001322 --> 4898*/
	        
	        System.out.println("测试".getBytes("ISO8859-1").length);
	     // 运行结果：4
	     System.out.println("测试".getBytes("GB2312").length);
	     // 运行结果：4
	     System.out.println("测试".getBytes("GBK").length);
	     // 运行结果：6
	     System.out.println("测试".getBytes("UTF-8").length);
	     
	     
	     System.out.println("ab".getBytes("ISO8859-1").length);
	     // 运行结果：4
	     System.out.println("ab".getBytes("GB2312").length);
	     // 运行结果：4
	     System.out.println("ab".getBytes("GBK").length);
	     // 运行结果：6
	     System.out.println("ab".getBytes("UTF-8").length);
	     
			//语音转换
			/*byte[] voiceData = Base64.decodeBase64("sdfasdfasfdfads");
			
			String fileName = "1.amr";
			
			createFileContent("E:/resin/resin-pro-4.0.53/webapps/GXCareDevice/voice", fileName,voiceData);*/
	     
	     byte[] voiceData = Base64.decodeBase64("sdfasdfasfdfads");
			
	     String xmlinfo="<ads><advertising><customerName>hahhaahahahah</customerName><advertisingUrl>https://www.baidu.com/</advertisingUrl><iconUrl>201809280516.png</iconUrl>"+
"<lanugage>en</lanugage>"+
"</advertising>"+
"</ads>";
			String fileName = "te.xml";
			
			createFileContent("E:/resin/resin-pro-4.0.53/webapps/GXCareDevice/voice", fileName,xmlinfo.getBytes("UTF-8"));
	}
	
	
  
}