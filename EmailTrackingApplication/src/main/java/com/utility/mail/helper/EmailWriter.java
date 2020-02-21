package com.utility.mail.helper;

import java.util.Iterator;
import java.util.List;

public class EmailWriter {

	public String createEmailContent(String subject, String emailType,
			List<String> eventData, String ordSource, String ordDest,
			String ordRefnum) {

		String l_body = null;
		int loop = 1;

		if (emailType == "NA") {

			l_body = "<HTML><a href=http://www.inspirage.com/><p><img src='cid:inspiragelogo'></p></a>";

			l_body = l_body
					+ "<FONT FACE=VERDANA, ARIAL, HELVETICA SIZE=2><B>Dear Sender,</B></FONT><BR/><BR/>";
			l_body = l_body + "<FONT FACE=VERDANA, ARIAL, HELVETICA SIZE=2><B>"
					+ subject + "</B></FONT>";
			l_body = l_body
					+ "<FONT FACE=VERDANA, ARIAL, HELVETICA SIZE=2> is invalid reference number, please send the valid reference number.</FONT><BR/><BR/>";
			l_body = l_body + "<FONT FACE=VERDANA, ARIAL, HELVETICA SIZE=2>";
			/*
			 * // l_body = l_body+"<B><U>DISCLAIMER</U></B><BR/><BR/>"; l_body =
			 * l_body + "<BR/><BR/>"; l_body = l_body +
			 * "==============================================================================================================================="
			 * ; l_body = l_body + "<BR/><BR/>"; l_body = l_body +
			 * "This is a auto-generated email. Do not reply to this email. In case of any queries, please call customer service at 888 888 8888."
			 * ;
			 * 
			 * l_body = l_body + "<BR/><BR/>"; l_body = l_body +
			 * "==============================================================================================================================="
			 * ; l_body = l_body + "<BR/><BR/><BR/><BR/></FONT>";
			 */
			l_body = l_body + "</HTML>";

		} else if (emailType == "DATA") {

			l_body = "<HTML><a href=http://www.inspirage.com/><p><img src='cid:inspiragelogo'></p></a>";
			l_body = l_body
					+ "<FONT FACE=VERDANA, ARIAL, HELVETICA SIZE=2><B>Dear Sender,</B></FONT><BR/><BR/>";
			l_body = l_body
					+ "<FONT FACE=VERDANA, ARIAL, HELVETICA SIZE=2>We are please to inform you of the following details of your Order.</FONT><BR/><BR/>";
			l_body = l_body + "<FONT FACE=VERDANA, ARIAL, HELVETICA SIZE=2>";

			l_body = l_body + "<BR/><BR/>";
			l_body = l_body + "<TABLE CELLPADDING=4 CELLSPACING=1>";
			l_body = l_body + "<TR>";
			l_body = l_body
					+ "<TD COLSPAN=2><FONT FACE=VERDANA, ARIAL, HELVETICA SIZE=2><B>Order Information</B></FONT></TD>";
			l_body = l_body + "</TR>";

			l_body = l_body + "<TR>";
			l_body = l_body
					+ "<TD WIDTH=200 HEIGHT=25><FONT FACE=VERDANA, ARIAL, HELVETICA SIZE=2>Order Reference Number</FONT></TD>";
			l_body = l_body
					+ "<TD><FONT FACE=VERDANA, ARIAL, HELVETICA SIZE=2><B>"
					+ ordRefnum + "</B></FONT></TD></TR>";

			l_body = l_body + "<TR>";
			l_body = l_body
					+ "<TD WIDTH=109 HEIGHT=25><FONT FACE=VERDANA, ARIAL, HELVETICA SIZE=2>Start Location</FONT></TD>";
			l_body = l_body
					+ "<TD><FONT FACE=VERDANA, ARIAL, HELVETICA SIZE=2><B>"
					+ ordSource + "</B></FONT></TD></TR>";

			l_body = l_body + "<TR>";
			l_body = l_body
					+ "<TD WIDTH=109 HEIGHT=25><FONT FACE=VERDANA, ARIAL, HELVETICA SIZE=2>End Location</FONT></TD>";
			l_body = l_body
					+ "<TD><FONT FACE=VERDANA, ARIAL, HELVETICA SIZE=2><B>"
					+ ordDest + "</B></FONT></TD></TR></TABLE>";

			l_body = l_body
					+ "<FONT FACE=VERDANA, ARIAL, HELVETICA SIZE=2 ><B>Sequence of events occured on order";
			l_body = l_body + "</B></FONT><BR/><BR/>";

			l_body = l_body + "<TABLE BORDER=1 CELLPADDING=4 CELLSPACING=0>";

			for (Iterator<String> itr = eventData.iterator(); itr.hasNext();) {

				@SuppressWarnings("unused")
				String iTranscationNo = (String) itr.next();
				String statusDesc = (String) itr.next();
				String locName = (String) itr.next();
				String stopNum = (String) itr.next();
				String eventDate = (String) itr.next();
				String timeZone = (String) itr.next();

				if (loop % 2 == 1) {

					l_body = l_body + "<TR BGCOLOR=#E0E0E0>";
					l_body = l_body
							+ "<TD FONT FACE=VERDANA, ARIAL, HELVETICA SIZE=2>"
							+ stopNum + ".</TD>";
					l_body = l_body
							+ "<TD FONT FACE=VERDANA, ARIAL, HELVETICA SIZE=2>"
							+ locName + "</TD>";
					l_body = l_body
							+ "<TD FONT FACE=VERDANA, ARIAL, HELVETICA SIZE=2>"
							+ statusDesc + "</TD>";
					l_body = l_body
							+ "<TD FONT FACE=VERDANA, ARIAL, HELVETICA SIZE=2>"
							+ eventDate + " " + timeZone + "</TD></TR>";

				} else {

					l_body = l_body + "<TR BGCOLOR=#FFFFFF>";
					l_body = l_body
							+ "<TD FONT FACE=VERDANA, ARIAL, HELVETICA SIZE=2>"
							+ stopNum + ".</TD>";
					l_body = l_body
							+ "<TD FONT FACE=VERDANA, ARIAL, HELVETICA SIZE=2>"
							+ locName + "</TD>";
					l_body = l_body
							+ "<TD FONT FACE=VERDANA, ARIAL, HELVETICA SIZE=2>"
							+ statusDesc + "</TD>";
					l_body = l_body
							+ "<TD FONT FACE=VERDANA, ARIAL, HELVETICA SIZE=2>"
							+ eventDate + " " + timeZone + "</TD></TR>";

				}

				loop = loop + 1;

			}

			l_body = l_body + "</TABLE><BR/><BR/>";
			l_body = l_body + "<FONT FACE=VERDANA, ARIAL, HELVETICA SIZE=2>";
			/*
			 * // l_body = l_body+"<B><U>DISCLAIMER</U></B><BR/><BR/>"; l_body =
			 * l_body + "<BR/><BR/>"; l_body = l_body +
			 * "================================================================================="
			 * ; l_body = l_body + "<BR/><BR/>"; l_body = l_body +
			 * "This is a auto-generated email. Do not reply to this email. In case of any queries, please call customer service at 888 888 8888."
			 * ;
			 * 
			 * l_body = l_body + "<BR/><BR/>"; l_body = l_body +
			 * "================================================================================="
			 * ; l_body = l_body + "<BR/><BR/><BR/><BR/></FONT>";
			 */
			l_body = l_body + "</HTML>";

		} else if (emailType == "DATA_NO_EVENT") {

			l_body = "<HTML><a href=http://www.inspirage.com/><p><img src='cid:inspiragelogo'></p></a>";

			l_body = l_body
					+ "<FONT FACE=VERDANA, ARIAL, HELVETICA SIZE=2><B>Dear Sender,</B></FONT><BR/><BR/>";

			l_body = l_body
					+ "<FONT FACE=VERDANA, ARIAL, HELVETICA SIZE=2>Event data cannot be display because no activity has been done on your Order yet.</FONT><BR/><BR/>";
			l_body = l_body + "<FONT FACE=VERDANA, ARIAL, HELVETICA SIZE=2>";
			/*
			 * // l_body = l_body+"<B><U>DISCLAIMER</U></B><BR/><BR/>"; l_body =
			 * l_body + "<BR/><BR/>"; l_body = l_body +
			 * "==============================================================================================================================="
			 * ; l_body = l_body + "<BR/><BR/>"; l_body = l_body +
			 * "This is a auto-generated email. Do not reply to this email. In case of any queries, please call customer service at 888 888 8888."
			 * ;
			 * 
			 * l_body = l_body + "<BR/><BR/>"; l_body = l_body +
			 * "==============================================================================================================================="
			 * ; l_body = l_body + "<BR/><BR/><BR/><BR/></FONT>";
			 */
			l_body = l_body + "</HTML>";
		} else if (emailType == "SNA") {

			l_body = "<HTML><a href=http://www.inspirage.com/><p><img src='cid:inspiragelogo'></p></a>";

			l_body = l_body
					+ "<FONT FACE=VERDANA, ARIAL, HELVETICA SIZE=2><B>Dear Sender,</B></FONT><BR/><BR/>";
			l_body = l_body
					+ "<FONT FACE=VERDANA, ARIAL, HELVETICA SIZE=2></FONT>";
			l_body = l_body
					+ "<FONT FACE=VERDANA, ARIAL, HELVETICA SIZE=2>OTM service is not available to provide the status. Please try after sometime.</FONT><BR/><BR/>";
			l_body = l_body + "<FONT FACE=VERDANA, ARIAL, HELVETICA SIZE=2>";
			/*
			 * // l_body = l_body+"<B><U>DISCLAIMER</U></B><BR/><BR/>"; l_body =
			 * l_body + "<BR/><BR/>"; l_body = l_body +
			 * "==============================================================================================================================="
			 * ; l_body = l_body + "<BR/><BR/>"; l_body = l_body +
			 * "This is a auto-generated email. Do not reply to this email. In case of any queries, please call customer service at 888 888 8888."
			 * ;
			 * 
			 * l_body = l_body + "<BR/><BR/>"; l_body = l_body +
			 * "==============================================================================================================================="
			 * ; l_body = l_body + "<BR/><BR/><BR/><BR/></FONT>";
			 */
			l_body = l_body + "</HTML>";
		}

		return l_body;
	}

}
