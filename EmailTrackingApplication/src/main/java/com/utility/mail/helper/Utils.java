package com.utility.mail.helper;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.slf4j.Logger;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class Utils {

	// static Logger logger = LogManager.getLogger(Utils.class.getName());
	static Logger logger = LoggerFactory.getLogger(Utils.class.getName());

	public static String dbXmlPush(String dbXmlUrl, String authStringEnc,
			String content) {
		String result = "";
		try {
			logger.info("Db XML Url:" + dbXmlUrl);
			logger.info("Db XML Post content: " + content);
			if (dbXmlUrl.toUpperCase().startsWith(Constants.HTTP)) {
				URL url = new URL(dbXmlUrl);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.setRequestProperty("Authorization", authStringEnc);
				conn.setDoOutput(true);
				conn.setDoInput(true);
				conn.setInstanceFollowRedirects(false);
				conn.setRequestMethod("POST");
				conn.setRequestProperty("charset", "utf-8");
				conn.setRequestProperty("Content-Type",
						"application/x-www-form-urlencoded");
				conn.setRequestProperty("Content-Length",
						Integer.toString(content.getBytes().length));
				conn.setUseCaches(false);
				DataOutputStream wr = new DataOutputStream(
						conn.getOutputStream());
				wr.writeBytes(content);

				wr.flush();
				wr.close();

				InputStream is = conn.getInputStream();
				InputStreamReader isr = new InputStreamReader(is);
				int numCharsRead;
				char[] charArray = new char[1024];
				StringBuffer sb = new StringBuffer();
				while ((numCharsRead = isr.read(charArray)) > 0) {
					sb.append(charArray, 0, numCharsRead);
				}
				result = sb.toString();
				conn.disconnect();

			} else {
				URL url = new URL(dbXmlUrl);
				HttpsURLConnection conn = (HttpsURLConnection) url
						.openConnection();
				conn.setRequestProperty("Authorization", authStringEnc);
				conn.setDoOutput(true);
				conn.setDoInput(true);
				conn.setInstanceFollowRedirects(false);
				conn.setRequestMethod("POST");
				conn.setRequestProperty("charset", "utf-8");
				conn.setRequestProperty("Content-Type",
						"application/x-www-form-urlencoded");
				conn.setRequestProperty("Content-Length",
						Integer.toString(content.getBytes().length));
				conn.setUseCaches(false);
				DataOutputStream wr = new DataOutputStream(
						conn.getOutputStream());
				wr.writeBytes(content);

				wr.flush();
				wr.close();

				InputStream is = conn.getInputStream();
				InputStreamReader isr = new InputStreamReader(is);
				int numCharsRead;
				char[] charArray = new char[1024];
				StringBuffer sb = new StringBuffer();
				while ((numCharsRead = isr.read(charArray)) > 0) {
					sb.append(charArray, 0, numCharsRead);
				}
				result = sb.toString();
				conn.disconnect();

			}
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error("Exception occured in " + new Object() {
			}.getClass().getEnclosingMethod().getName() + "\n"
					+ errors.toString());
			if (errors.toString().contains(Constants.ERROR_CODE_401)) {
				result = Constants.INVALID_USER;
				// logger.debug("Other Stack Trace : " + result);
			} else if (errors.toString().contains(
					Constants.ERROR_CODE_CONNECT_EXCEPTION)) {
				result = Constants.NETWORK_ERROR;
			}
		}

		if (result.contains("StackTrace")) {
			// logger.debug("Stack Trace : " + result);
			result = Constants.INVALID_USER;
		}

		logger.info("Db XML Servlet Response: " + result);
		return result;
	}

	public static class miTM implements javax.net.ssl.TrustManager,
			javax.net.ssl.X509TrustManager {
		@Override
		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		public boolean isServerTrusted(
				java.security.cert.X509Certificate[] certs) {
			return true;
		}

		public boolean isClientTrusted(
				java.security.cert.X509Certificate[] certs) {
			return true;
		}

		@Override
		public void checkServerTrusted(
				java.security.cert.X509Certificate[] certs, String authType)
				throws java.security.cert.CertificateException {
			return;
		}

		@Override
		public void checkClientTrusted(
				java.security.cert.X509Certificate[] certs, String authType)
				throws java.security.cert.CertificateException {
			return;
		}
	}

	@SuppressWarnings("unused")
	private static void trustAllHttpsCertificates() throws Exception {

		// Create a trust manager that does not validate certificate chains:

		javax.net.ssl.TrustManager[] trustAllCerts =

		new javax.net.ssl.TrustManager[1];

		javax.net.ssl.TrustManager tm = new miTM();

		trustAllCerts[0] = tm;

		javax.net.ssl.SSLContext sc =

		javax.net.ssl.SSLContext.getInstance("SSL");

		sc.init(null, trustAllCerts, null);

		javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(

		sc.getSocketFactory());

	}

	/**
	 * Stylizer method is used to apply transformation on the provided xml using
	 * provided xsl file.
	 * 
	 * @param xsltFile
	 *            This is the xsl file which will be used for xslt
	 *            transformation.
	 * @param xml
	 *            This is the xml file which needs to be transformed
	 * @return This will be the transformed message.
	 */
	public static String stylizer(String xsltFile, String xml) {
		String xmlString = "";
		// logger.debug("Got xml for transformation. Transforming it using
		// "+xsltFile + " xsl file.");
		try {
			ClassLoader classLoader = Thread.currentThread()
					.getContextClassLoader();
			InputStream in = classLoader.getResourceAsStream(xsltFile);
			String xsl = Utils.inputStreamToString(in);
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			// logger.debug("Xml for transformation="+xml);
			Document document = builder.parse(new InputSource(new StringReader(
					xml)));

			// Use a Transformer for output
			TransformerFactory tFactory = TransformerFactory.newInstance();
			StreamSource stylesource = new StreamSource(new StringReader(xsl));
			Transformer transformer = tFactory.newTransformer(stylesource);
			DOMSource source = new DOMSource(document);
			// StreamResult result = new StreamResult(new File(outPutFile));
			StreamResult sw = new StreamResult(new StringWriter());
			// StreamResult result = new StreamResult(new File(System.out));
			transformer.transform(source, sw);
			xmlString = sw.getWriter().toString();

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error("Exception occured in " + new Object() {
			}.getClass().getEnclosingMethod().getName() + "\n"
					+ errors.toString());
		}
		return xmlString;
	}

	public static String inputStreamToString(InputStream in)
			throws UnsupportedEncodingException {
		ByteArrayOutputStream result = null;
		try {
			result = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int length;
			while ((length = in.read(buffer)) != -1) {
				result.write(buffer, 0, length);
			}
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error("Exception occured in " + new Object() {
			}.getClass().getEnclosingMethod().getName() + "\n"
					+ errors.toString());
		}
		return result.toString("UTF-8");
	}
	
	public static String parseString(String s) {
		List<String> testList = Arrays.asList(s.split(","));

		String finalStr = "";
		for (String t : testList) {

			finalStr = finalStr + "'" + t.toString().trim() + "',";

		}

		// System.out.println(finalStr.substring(0, finalStr.length()-1));

		return finalStr.substring(0, finalStr.length() - 1);
	}

}
