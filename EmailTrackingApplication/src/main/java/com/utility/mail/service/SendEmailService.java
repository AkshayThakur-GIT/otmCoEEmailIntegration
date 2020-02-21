package com.utility.mail.service;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Properties;
import java.util.StringTokenizer;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.slf4j.Logger;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;

public class SendEmailService {

	// static Logger log =
	// LogManager.getLogger(SendEmailService.class.getName());
	static Logger log = LoggerFactory.getLogger(SendEmailService.class
			.getName());

	public void sendEmail(String to, String subject, String html_body,
			String from, String cc, final String mailUserID,
			final String mailIDPassword, String mailHost, Message message,
			String mailLogo, String mailPort) {

		log.info("Preparing Email");
		Properties props = new Properties();
		/*
		 * props.put("mail.smtp.auth", "true");
		 * props.put("mail.smtp.starttls.enable", "true");
		 * props.put("mail.smtp.host", "smtp.gmail.com");
		 * props.put("mail.smtp.port", "587");
		 */

		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", mailHost);
		props.put("mail.smtp.port", mailPort);

		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(mailUserID,
								mailIDPassword);
					}
				});

		// Session session = Session.getDefaultInstance(props,null);

		try {

			/*
			 * Message message = new MimeMessage(session); message.setFrom(new
			 * InternetAddress(from));
			 * message.setRecipients(Message.RecipientType.TO, InternetAddress
			 * .parse(to));
			 * 
			 * Handle CC part in email
			 * 
			 * if (cc != "NA") { StringTokenizer st = new StringTokenizer(cc,
			 * ","); int tokenCount = st.countTokens(); InternetAddress[]
			 * recipientList = new InternetAddress[tokenCount]; for (int i = 0;
			 * st.hasMoreTokens(); i++) { String msgCC = st.nextToken();
			 * 
			 * if (msgCC != null && msgCC.trim().length() > 0) {
			 * 
			 * recipientList[i] = new InternetAddress(msgCC); }
			 * 
			 * }
			 * 
			 * message .setRecipients(Message.RecipientType.CC,
			 * (recipientList)); } message.setSubject(subject);
			 * 
			 * Setting LOGO in email
			 * 
			 * Multipart multipart = new MimeMultipart("related"); MimeBodyPart
			 * messageBodyPart = new MimeBodyPart();
			 * messageBodyPart.setContent(html_body, "text/html");
			 * multipart.addBodyPart(messageBodyPart); MimeBodyPart iconBodyPart
			 * = new MimeBodyPart(); DataSource iconDataSource = new
			 * FileDataSource(new File("D://inspirage.jpg"));
			 * iconBodyPart.setDataHandler(new DataHandler(iconDataSource));
			 * iconBodyPart.setDisposition(Part.INLINE);
			 * iconBodyPart.setContentID("<inspiragelogo>");
			 * iconBodyPart.addHeader("Content-Type", "image/jpeg");
			 * multipart.addBodyPart(iconBodyPart);
			 * 
			 * message.setContent(multipart);
			 * 
			 * Transport.send(message);
			 * 
			 * // Transport t = session.getTransport("smtp"); //
			 * t.connect(props.getProperty(MailConstants.MAILHOST), username, //
			 * password) ; // send the message // t.send(message); // t.close();
			 */

			Message replyMessage = new MimeMessage(session);

			// replyMessage = (MimeMessage) message.reply(false);
			replyMessage.setFrom(new InternetAddress(from));
			replyMessage.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(to));
			// replyMessage.setReplyTo(new InternetAddress[] { new
			// InternetAddress(to) });

			Address[] replyTo = { new InternetAddress(to) };
			replyMessage.setReplyTo(replyTo);

			if (cc != "NA") {
				StringTokenizer st = new StringTokenizer(cc, ",");
				int tokenCount = st.countTokens();
				InternetAddress[] recipientList = new InternetAddress[tokenCount];
				for (int i = 0; st.hasMoreTokens(); i++) {
					String msgCC = st.nextToken();

					if (msgCC != null && msgCC.trim().length() > 0) {

						recipientList[i] = new InternetAddress(msgCC);
					}

				}

				replyMessage.setRecipients(Message.RecipientType.CC,
						(recipientList));
			}
			replyMessage.setSubject("RE: " + subject);

			Multipart multipart = new MimeMultipart("related");
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent(html_body, "text/html");
			multipart.addBodyPart(messageBodyPart);
			MimeBodyPart iconBodyPart = new MimeBodyPart();
			// DataSource iconDataSource = new FileDataSource(new
			// File("D://inspirage.jpg"));
			// File file =
			// ResourceUtils.getFile("classpath:image/inspirage.jpg");

			// File file = ResourceUtils.getFile("classpath:"+mailLogo);

			DataSource iconDataSource = new FileDataSource(new File(mailLogo));
			iconBodyPart.setDataHandler(new DataHandler(iconDataSource));
			iconBodyPart.setDisposition(Part.INLINE);
			iconBodyPart.setContentID("<inspiragelogo>");
			iconBodyPart.addHeader("Content-Type", "image/jpeg");
			multipart.addBodyPart(iconBodyPart);

			replyMessage.setContent(multipart);
			log.info("Sending Email...");
			Transport.send(replyMessage);
			log.info("Email Sent...");

			// System.out.println("Email Sent Successfully");

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			log.error("Exception occured in " + new Object() {
			}.getClass().getEnclosingMethod().getName() + "\n"
					+ errors.toString());
		}

	}
}
