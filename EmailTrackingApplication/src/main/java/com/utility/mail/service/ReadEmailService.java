package com.utility.mail.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.mail.Flags;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import javax.mail.search.FlagTerm;
import javax.xml.bind.DatatypeConverter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.slf4j.Logger;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
//import com.utility.helper.EmailWriter;

import com.utility.mail.helper.*;
import com.utility.mail.sql.*;


public class ReadEmailService {

	// private static final SimpleDateFormat dateFormat = new
	// SimpleDateFormat("HH:mm:ss");
	Folder inbox;
	// static Logger log =
	// LogManager.getLogger(ReadEmailService.class.getName());
	
	
	static Logger log = LoggerFactory.getLogger(ReadEmailService.class
			.getName());
	StringWriter errors = new StringWriter();

	// Constructor of the class.
	public ReadEmailService(
			String mailHost, 
			String mailUserID,
			String mailIDPassword, 
			String mailProtocol, 
			String otmURL,
			String otmUserName, 
			String otmPassword, 
			String otmQualID,
			String mailLogo, 
			String mailPort, 
			String eventCodes, 
			String trackingObject) throws GeneralSecurityException,
			MessagingException {

		String ecc = null;
		String finalecc = "";
		String mailCC = null;
		String result = "";
		String dbXmlUrl = otmURL + Constants.OTM_DBXML;
		String usernameAndPassword = otmUserName + ":" + otmPassword;
		String authorizationHeaderValue = "Basic "
				+ DatatypeConverter.printBase64Binary(usernameAndPassword
						.getBytes());
		String id = "";
		String source = "";
		String dest = "";
		String replyTo = "";
		String sql = "";
		String content = "";
		// String emailBody="";
		String xsl = "";

		// log.info("Email read service started...", dateFormat.format(new
		// Date()));
		
		
		log.info("New email scanning started...");

		// create properties field
		Properties props = System.getProperties();
		Session session = Session.getDefaultInstance(props, null);

		// MailSSLSocketFactory sf = new MailSSLSocketFactory();
		// sf.setTrustAllHosts(true);
		props.setProperty("mail.store.protocol", mailProtocol);

		props.put("mail.imap.starttls.enable", "true");
		// props.put("mail.imap.ssl.socketFactory", sf);
		// props.put("mail.imaps.auth.plain.disable", "true");
		// props.put("mail.imaps.auth.ntlm.disable", "true");
		// props.put("mail.imaps.auth.gssapi.disable", "true");
		props.put("mail.smtp.port", mailPort);

		/*
		 * props.put("mail.pop3.port", "995");
		 * props.put("mail.pop3.starttls.enable", "true");
		 */

		try {
			Store store = session.getStore(mailProtocol);
			log.info("Getting connection to mail server");
			store.connect(mailHost, mailUserID, mailIDPassword);
			log.info("Got connection to mail server");
			/* Mention the folder name which you want to read. */
			inbox = store.getFolder("Inbox");

			/* Open the inbox using store. */
			inbox.open(Folder.READ_WRITE);

			if (inbox.getUnreadMessageCount() > 0) {

				log.info("No of Unread Messages :"
						+ inbox.getUnreadMessageCount());

			}

			else {

				log.info("No Unread Messages in Inbox");
			}

			Message[] msg = inbox.search(new FlagTerm(new Flags(Flag.SEEN),
					false));

			if (msg.length > 0) {

				/*
				 * Looping through the unread mails
				 */

				for (int i = 0; i < msg.length; i++) {

					// DateFormat dateFormat = new
					// SimpleDateFormat("yyyyMMddHHmmss");
					// Date date = msg[i].getSentDate();

					// String FinalDt = dateFormat.format(date);

					// System.out.println(FinalDt);

					String stringmsg = msg[i].getFrom()[0].toString();

					int beginIndex = msg[i].getFrom()[0].toString()
							.indexOf("<");

					int endIndex = msg[i].getFrom()[0].toString().indexOf(">");

					// System.out.println(stringmsg);
					String from = stringmsg.substring(beginIndex + 1, endIndex)
							.toLowerCase();

					log.info("Sender email addresses " + from);

					// Getting reply to for sending email reply
					replyTo = InternetAddress.toString(msg[i].getReplyTo());
					if (replyTo != null) {
						//System.out.println("Reply-to: " + replyTo);
						log.info("Reply-to: " + replyTo);
					}

					String cc = InternetAddress.toString(msg[i]
							.getRecipients(Message.RecipientType.CC));
					if (cc != null) {

						String[] tokens = cc.split(",");

						for (String t : tokens) {

							// System.out.println(t);

							if (t.toString().contains("<")) {

								int beginIndex1 = t.toString().indexOf("<");

								int endIndex1 = t.toString().indexOf(">");

								ecc = t.substring(beginIndex1 + 1, endIndex1);
							} else {
								ecc = t.toString();

							}

							finalecc = ecc + "," + finalecc;

						}

						int finalstr = finalecc.length() - 1;

						mailCC = finalecc.substring(0, finalstr);

						// System.out.println("Multiple CC :" + mailCC);

					}

					else {

						mailCC = "NA";

						// System.out.println("Multiple CC :" + mailCC);

					}
					log.info("Email CC addresses " + mailCC);

					// System.out.println("Subject:" + msg[i].getSubject());
					String str = msg[i].getSubject();
					String[] tokens1 = str.split(",");
					for (String subject : tokens1) {

						subject = subject.replaceAll("[^a-zA-Z 0-9]", "")
								.replaceAll(" ", "").trim().toUpperCase();

						// System.out.println(t);

						log.info("Subject in email " + str);

						// preparing Sql
						/*
						 * sql = "select " +
						 * "glog_util.remove_domain(a.order_release_gid) ORDER_ID,"
						 * +
						 * "glog_util.remove_domain(b.source_location_gid) SOURCE,"
						 * +
						 * "glog_util.remove_domain(b.dest_location_gid) DEST "
						 * + "from order_release_refnum a, order_release b " +
						 * "where a.order_release_refnum_value='"+subject+"' "
						 * //
						 * +"where a.order_release_refnum_value='20122016-001' "
						 * +
						 * "and glog_util.remove_domain(a.order_release_refnum_qual_gid)='"
						 * + otmQualID+ "' " +
						 * "and a.order_release_gid=b.order_release_gid and rownum=1"
						 * ;
						 */

						sql = SQL.preparedSQL(subject, otmQualID, null,trackingObject);

						if(trackingObject.trim().equalsIgnoreCase("ORDER")){
							
							content = "rootName=ORDER_DETAIL&sqlQuery=" + sql;
							
						}else{
							
							content = "rootName=SHIPMENT_DETAIL&sqlQuery=" + sql;
						}
						
						
						result = Utils.dbXmlPush(dbXmlUrl,
								authorizationHeaderValue, content);
						// System.out.println("OTM Result "+result);
						xsl = Constants.XSL_REMOVE_NS;
						result = Utils.stylizer(xsl, result);

						SendEmailService ses = new SendEmailService();
						// EmailWriter ew = new EmailWriter();

						if ((!result.contains(Constants.INVALID_USER))
								&& (!result.contains(Constants.NETWORK_ERROR))) {

							// List<String> eventData = new ArrayList<String>();

							// System.out.println("OTM XML " + result);
							log.info("XML after removing namespace : " + result);
							//orderID = parseXMLData("ORDER_ID", result);
							
							if(trackingObject.trim().equalsIgnoreCase("ORDER"))
							
							{
								
							id = parseXMLData("ORDER_ID", result,"/xml2sql/TRANSACTION_SET/ORDER_DETAIL/@ORDER_ID");
							log.info("Order ID from OTM content " + id);
							//orderSource = parseXMLData("ORDER_SOURCE", result);
							
							source = parseXMLData("ORDER_SOURCE", result,"/xml2sql/TRANSACTION_SET/ORDER_DETAIL/@SOURCE");

							log.info("Order Source from OTM content "
									+ source);
							//orderDestination = parseXMLData("ORDER_DEST",result);
							
							dest = parseXMLData("ORDER_DEST", result,"/xml2sql/TRANSACTION_SET/ORDER_DETAIL/@DEST");

							log.info("Order Destination from OTM content "
									+ dest);

							if (id == "NA") {

								// sending email for not valid ref#

								// emailBody=ew.createEmailContent(subject,
								// "NA",null,orderSource,orderDestination,subject);

								xsl = Constants.XSL_CREATE_HTML;
								// creating dummy XML because no data found in
								// OTM
								result = "<?xml version='1.0' encoding='utf-8'?><xml2sql>"
										+ "<TRANSACTION_SET>"
										+ "<ORDER_REF_DETAILS ORD_REF_NUM='"
										+ subject
										+ "'/>"
										+ "</TRANSACTION_SET>" + "</xml2sql>";

								result = Utils.stylizer(xsl, result);
								ses.sendEmail(replyTo, str, result, from,
										mailCC, mailUserID, mailIDPassword,
										mailHost, msg[i], mailLogo, mailPort);

							}

							else {

								/*
								 * sql ="select " +
								 * "ie.status_code_gid as status_code," +
								 * "max(ie.i_transaction_no) as itrn, " +
								 * "bs.description as status_desc, " +
								 * "l.location_name as loc_name, " +
								 * "ssh.event_location_gid as event_loc, " +
								 * "ssh.shipment_stop_num as stopnum," + //
								 * "to_char(ie.eventdate,'dd-mm-yy hh24:mi:ss') as eventdate,"
								 * +
								 * "to_char(utc.get_local_date(ie.eventdate,ssh.event_location_gid),'dd-mm-yy hh24:mi:ss') as eventdate,"
								 * + "ie.time_zone_gid as time_zone_gid " +
								 * "from view_shipment_order_release v,shipment s,ie_shipmentstatus ie,ss_status_history ssh,bs_status_code bs,location l "
								 * +
								 * "where v.order_release_gid='INSCOE.PETCHEM-CHARTER-01' "
								 * + //
								 * "where glog_util.remove_domain(v.order_release_gid)='"
								 * +orderID+"' "+
								 * "and s.shipment_gid=v.shipment_gid " +
								 * "and bs.bs_status_code_gid=ie.status_code_gid "
								 * +
								 * "and ie.i_transaction_no = ssh.i_transaction_no "
								 * + "and ssh.shipment_gid=s.shipment_gid " +
								 * "and l.location_gid = ssh.event_location_gid "
								 * + "and v.perspective='B' " +
								 * "and s.shipment_type_gid <> 'SECONDARY CHARGE' "
								 * +
								 * "group by v.shipment_gid,ie.status_code_gid,bs.description, l.location_name, ssh.event_location_gid, ssh.shipment_stop_num,ie.eventdate,ie.time_zone_gid "
								 * +
								 * "order by v.shipment_gid asc, ssh.shipment_stop_num asc,itrn desc"
								 * ;
								 */

								/*
								 * sql = "SELECT "+subject+
								 * " AS ORD_REF_NUM,O.ORDER_RELEASE_XID AS ORDER_ID,GLOG_UTIL.REMOVE_DOMAIN(O.SOURCE_LOCATION_GID) AS ORDER_SOURCE,"
								 * +
								 * "GLOG_UTIL.REMOVE_DOMAIN(O.DEST_LOCATION_GID) AS ORDER_DEST,"
								 * +
								 * "CURSOR (SELECT IE.STATUS_CODE_GID AS STATUS_CODE,"
								 * + "MAX(IE.I_TRANSACTION_NO) AS ITRN," +
								 * "BS.DESCRIPTION AS STATUS_DESC,L.LOCATION_NAME AS LOC_NAME,SSH.EVENT_LOCATION_GID AS EVENT_LOC,SSH.SHIPMENT_STOP_NUM AS STOPNUM, "
								 * +
								 * "TO_CHAR(UTC.GET_LOCAL_DATE(IE.EVENTDATE,SSH.EVENT_LOCATION_GID),'DD-MM-YY HH24:MI:SS') AS EVENTDATE,"
								 * + "IE.TIME_ZONE_GID AS TIME_ZONE_GID " +
								 * "FROM VIEW_SHIPMENT_ORDER_RELEASE V,SHIPMENT S,IE_SHIPMENTSTATUS IE,SS_STATUS_HISTORY SSH,BS_STATUS_CODE BS,LOCATION L "
								 * + //
								 * "WHERE V.ORDER_RELEASE_GID='INSCOE.PETCHEM-CHARTER-011' "
								 * +
								 * "WHERE V.ORDER_RELEASE_GID=O.ORDER_RELEASE_GID "
								 * + "AND S.SHIPMENT_GID=V.SHIPMENT_GID  " +
								 * "AND BS.BS_STATUS_CODE_GID=IE.STATUS_CODE_GID AND IE.I_TRANSACTION_NO = SSH.I_TRANSACTION_NO "
								 * + "AND SSH.SHIPMENT_GID=S.SHIPMENT_GID  " +
								 * "AND L.LOCATION_GID = SSH.EVENT_LOCATION_GID AND V.PERSPECTIVE='B' AND S.SHIPMENT_TYPE_GID <> 'SECONDARY CHARGE' "
								 * +
								 * "GROUP BY V.SHIPMENT_GID,IE.STATUS_CODE_GID,BS.DESCRIPTION, L.LOCATION_NAME, SSH.EVENT_LOCATION_GID, SSH.SHIPMENT_STOP_NUM, "
								 * +
								 * "TO_CHAR(UTC.GET_LOCAL_DATE(IE.EVENTDATE,SSH.EVENT_LOCATION_GID),'DD-MM-YY HH24:MI:SS'),IE.TIME_ZONE_GID "
								 * +
								 * "ORDER BY V.SHIPMENT_GID ASC, SSH.SHIPMENT_STOP_NUM ASC,ITRN DESC) AS ORD_EVENT FROM ORDER_RELEASE O "
								 * + //
								 * "WHERE O.ORDER_RELEASE_GID='INSCOE.PETCHEM-CHARTER-01'"
								 * "WHERE GLOG_UTIL.REMOVE_DOMAIN(O.ORDER_RELEASE_GID)='"
								 * +orderID+"' ";
								 */

								sql = SQL
										.preparedSQL(subject, id, eventCodes,"ORD_EVENT");
								content = "rootName=ORDER_EVENT_DETAILS&sqlQuery="
										+ sql;
								result = Utils.dbXmlPush(dbXmlUrl,
										authorizationHeaderValue, content);
								xsl = Constants.XSL_REMOVE_NS;
								result = Utils.stylizer(xsl, result);
								xsl = Constants.XSL_CREATE_HTML;
								result = Utils.stylizer(xsl, result);
								ses.sendEmail(replyTo, str, result, from,
										mailCC, mailUserID, mailIDPassword,
										mailHost, msg[i], mailLogo, mailPort);

								// System.out.println("OTM Result "+result);
								/*
								 * eventData = parseEventData(result);
								 * if(eventData.isEmpty()){
								 * 
								 * //sending email for valid ref# but no event
								 * information System.out.println(
								 * "No event information from OTM");
								 * 
								 * }
								 * 
								 * else {
								 * 
								 * //System.out.println(
								 * "Event information from OTM "+eventData);
								 * //sending email for valid ref# with event
								 * information
								 * emailBody=ew.createEmailContent(subject,
								 * "DATA"
								 * ,eventData,orderSource,orderDestination,
								 * subject)
								 * //System.out.println("Email Body "+emailBody
								 * ); ses.sendEmail(replyTo, subject, emailBody,
								 * from, mailCC, mailUserID, mailIDPassword,
								 * mailHost, msg[i],mailLogo,mailPort);
								 * 
								 * }
								 */

							}
							
							} else {
								
								
								id = parseXMLData("SHIPMENT_ID", result,"/xml2sql/TRANSACTION_SET/SHIPMENT_DETAIL/@SHIPMENT_ID");
								log.info("SHIPMENT ID from OTM content " + id);
								//orderSource = parseXMLData("ORDER_SOURCE", result);
								
								source = parseXMLData("ORDER_SOURCE", result,"/xml2sql/TRANSACTION_SET/SHIPMENT_DETAIL/@SOURCE");

								log.info("Shipment Source from OTM content "
										+ source);
								//orderDestination = parseXMLData("ORDER_DEST",result);
								
								dest = parseXMLData("SHIPMENT_DEST", result,"/xml2sql/TRANSACTION_SET/SHIPMENT_DETAIL/@DEST");

								log.info("Shipment Destination from OTM content "
										+ dest);
								
								if (id == "NA") {

									
									xsl = Constants.XSL_SHIP_CREATE_HTML;
									// creating dummy XML because no data found in
									// OTM
									result = "<?xml version='1.0' encoding='utf-8'?><xml2sql>"
											+ "<TRANSACTION_SET>"
											+ "<SHIPMENT_REF_DETAILS SHIP_REF_NUM='"
											+ subject
											+ "'/>"
											+ "</TRANSACTION_SET>" + "</xml2sql>";

									result = Utils.stylizer(xsl, result);
									ses.sendEmail(replyTo, str, result, from,
											mailCC, mailUserID, mailIDPassword,
											mailHost, msg[i], mailLogo, mailPort);

								}
								
								else {
	
									sql = SQL
											.preparedSQL(subject, id, eventCodes,"SHIP_EVENT");
									content = "rootName=SHIPMENT_EVENT_DETAILS&sqlQuery="
											+ sql;
									result = Utils.dbXmlPush(dbXmlUrl,
											authorizationHeaderValue, content);
									xsl = Constants.XSL_REMOVE_NS;
									result = Utils.stylizer(xsl, result);
									xsl = Constants.XSL_SHIP_CREATE_HTML;
									result = Utils.stylizer(xsl, result);
									ses.sendEmail(replyTo, str, result, from,
											mailCC, mailUserID, mailIDPassword,
											mailHost, msg[i], mailLogo, mailPort);
		
								}
								
							}
							
						} else

						{

							// emailBody=ew.createEmailContent(subject,
							// "SNA",null,orderSource,orderDestination,subject);
							// ses.sendEmail(replyTo, subject, emailBody, from,
							// mailCC, mailUserID, mailIDPassword, mailHost,
							// msg[i],mailLogo,mailPort);
							// creating dummy XML because no data found in OTM
							result = "<?xml version='1.0' encoding='utf-8'?><xml2sql>"
									+ "<TRANSACTION_SET>OTM_DOWN</TRANSACTION_SET>"
									+ "</xml2sql>";
							xsl = Constants.XSL_CREATE_HTML;
							result = Utils.stylizer(xsl, result);
							// log.error("Error occurred while posting to OTM "+
							// result);
							ses.sendEmail(replyTo, str, result, from, mailCC,
									mailUserID, mailIDPassword, mailHost,
									msg[i], mailLogo, mailPort);

						}

					}

					msg[i].setFlag(Flags.Flag.SEEN, true);
				}

			}

			if (inbox != null) {
				inbox.close(true);
				log.info("Inbox close after reading");
			}
			if (store != null) {
				store.close();
				log.info("Closed connection to mail server");

			}
			log.info("New email scanning end...");
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			log.error("Exception occured " + errors.toString());
			log.info("New email scanning end...");
		}

	}

	public String parseXMLData(String s, String xml, String xPath) {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
			Document document = builder.parse(new InputSource(new StringReader(
					xml)));
			
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			XPathExpression expr = xpath.compile(xPath);
			s = expr.evaluate(document, XPathConstants.STRING).toString();
			

			/*if (s == "ORDER_ID") {

				XPathFactory xPathfactory = XPathFactory.newInstance();
				XPath xpath = xPathfactory.newXPath();
				//XPathExpression expr = xpath
				//		.compile("/xml2sql/TRANSACTION_SET/ORDER_DETAIL/@ORDER_ID");
				XPathExpression expr = xpath
						.compile(xPath);
				s = expr.evaluate(document, XPathConstants.STRING).toString();

			} else if (s == "ORDER_SOURCE") {

				XPathFactory xPathfactory = XPathFactory.newInstance();
				XPath xpath = xPathfactory.newXPath();
				XPathExpression expr = xpath
						.compile("/xml2sql/TRANSACTION_SET/ORDER_DETAIL/@SOURCE");
				s = expr.evaluate(document, XPathConstants.STRING).toString();

			} else if (s == "ORDER_DEST") {

				XPathFactory xPathfactory = XPathFactory.newInstance();
				XPath xpath = xPathfactory.newXPath();
				XPathExpression expr = xpath
						.compile("/xml2sql/TRANSACTION_SET/ORDER_DETAIL/@DEST");
				s = expr.evaluate(document, XPathConstants.STRING).toString();

			}*/
			
			

		} catch (ParserConfigurationException e) {
			e.printStackTrace(new PrintWriter(errors));
			log.error("ParserConfigurationException occured in "
					+ new Object() {
					}.getClass().getEnclosingMethod().getName() + "\n"
					+ errors.toString());
		} catch (SAXException e) {
			e.printStackTrace(new PrintWriter(errors));
			log.error("SAXException occured in " + new Object() {
			}.getClass().getEnclosingMethod().getName() + "\n"
					+ errors.toString());
		} catch (IOException e) {
			e.printStackTrace(new PrintWriter(errors));
			log.error("IOException occured in " + new Object() {
			}.getClass().getEnclosingMethod().getName() + "\n"
					+ errors.toString());
		} catch (XPathExpressionException e) {
			e.printStackTrace(new PrintWriter(errors));
			log.error("XPathExpressionException" + new Object() {
			}.getClass().getEnclosingMethod().getName() + "\n"
					+ errors.toString());
		}

		if (s == "") {

			s = "NA";
		}

		return s;
	}

	public List<String> parseEventData(String xml) {
		List<String> eventData = new ArrayList<String>();

		DocumentBuilder db;
		try {
			db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xml));

			Document doc = db.parse(is);
			NodeList nodes = doc.getElementsByTagName("ORDER_EVENT_DETAILS");
			// System.out.println(nodes.getLength());

			for (int i = 0; i < nodes.getLength(); i++) {

				Element e = (Element) nodes.item(i);
				String ITRN = e.getAttribute("ITRN");
				// String STATUS_CODE = e.getAttribute("STATUS_CODE");
				String STATUS_DESC = e.getAttribute("STATUS_DESC");
				String LOC_NAME = e.getAttribute("LOC_NAME");
				// String EVENT_LOC = e.getAttribute("EVENT_LOC");
				String STOPNUM = e.getAttribute("STOPNUM");
				String EVENTDATE = e.getAttribute("EVENTDATE");
				String TIME_ZONE_GID = e.getAttribute("TIME_ZONE_GID");

				eventData.add(ITRN);
				eventData.add(STATUS_DESC);
				eventData.add(LOC_NAME);
				eventData.add(STOPNUM);
				eventData.add(EVENTDATE);
				eventData.add(TIME_ZONE_GID);
			}

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return eventData;
	}

}
