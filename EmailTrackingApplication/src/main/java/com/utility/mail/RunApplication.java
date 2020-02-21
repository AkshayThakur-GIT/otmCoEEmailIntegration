package com.utility.mail;

import java.security.GeneralSecurityException;
import javax.mail.MessagingException;
import org.slf4j.Logger;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
//import org.springframework.context.annotation.PropertySources;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.utility.mail.properties.MailProperties;
import com.utility.mail.service.ReadEmailService;

@SpringBootApplication
@EnableScheduling
@Configuration
@PropertySource("classpath:Mail.properties")
/*
 * @PropertySources({
 * 
 * @PropertySource("classpath:application.properties"),
 * 
 * @PropertySource("classpath:Mail.properties") })
 */
public class RunApplication {
	// static Logger log = LogManager.getLogger(Application.class.getName());
	static Logger log = LoggerFactory.getLogger(RunApplication.class.getName());

	// private static final SimpleDateFormat dateFormat = new
	// SimpleDateFormat("HH:mm:ss");
	public static void main(String[] args) throws Exception {

		log.info("Email Trakcing Application Started...");

		SpringApplication.run(RunApplication.class);

	}

	/*
	 * @Value("${MAIL.MAILHOST}") public String mailHost;
	 * 
	 * @Value("${MAIL.MAILUESRID}") public String mailUserID;
	 * 
	 * @Value("${MAIL.MAILPASSWORD}") public String mailPassword;
	 * 
	 * @Value("${MAIL.MAIL_LOGO_PATH}") public String mailLogo;
	 * 
	 * @Value("${MAIL.PROTOCOL}") public String mailProtocol;
	 * 
	 * @Value("${MAIL.OTM_URL}") public String otmURL;
	 * 
	 * @Value("${MAIL.OTM_USERNAME}") public String otmUserName;
	 * 
	 * @Value("${MAIL.OTM_PASSWORD}") public String otmPassword;
	 * 
	 * @Value("${MAIL.QUALIFIER_ID}") public String otmQualID;
	 * 
	 * @Value("${MAIL.PORT}") public String mailPort;
	 */

	@Autowired
	private MailProperties mailProperties;
	
	
	@Scheduled(fixedRateString = "${MAIL.POLLING_FREQUENCY}")
	public void runEmailService() throws GeneralSecurityException,
			MessagingException {

		/*
		 * new ReadEmailService(mailHost, mailUserID, mailPassword,
		 * mailProtocol, otmURL, otmUserName, otmPassword, otmQualID, mailLogo,
		 * mailPort);
		 */
		
		//System.out.println("Event Codes "+mailProperties.getEVENT_CODES());
		
		
		
		
		String otmEventCodes = mailProperties.getEVENT_CODES();
		if (!otmEventCodes.equalsIgnoreCase("")) {
			// System.out.println("Value");
			mailProperties.setEVENT_CODES(otmEventCodes);
		}
		else
		{
			mailProperties.setEVENT_CODES("ALL");
			// System.out.println("Blank");
		}
		// System.out.println("After setting "+mailProperties.getEVENT_CODES());
		new ReadEmailService(
				mailProperties.getHOST(),
				mailProperties.getEMAIL_UESR_ID(),
				mailProperties.getEMAIL_PASSWORD(),
				mailProperties.getPROTOCOL(),
				mailProperties.getOTM_URL(),
				mailProperties.getOTM_USERNAME(),
				mailProperties.getOTM_PASSWORD(),
				mailProperties.getQUALIFIER_ID(),
				mailProperties.getLOGO_PATH(),
				mailProperties.getPORT(),
				mailProperties.getEVENT_CODES(),
				mailProperties.getTRACKING_OBJECT());

	}

}
