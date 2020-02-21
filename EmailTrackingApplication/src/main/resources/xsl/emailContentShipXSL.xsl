<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	version="1.0">
	<xsl:output method="html" />

	<xsl:template match="/">

		<xsl:choose>

			<!-- Condition for invalid Ref# -->

			<xsl:when test="/xml2sql/TRANSACTION_SET/SHIPMENT_REF_DETAILS">

				<HTML>
					<a href="http://www.inspirage.com">

						<img style="float:right; padding-right:10px;padding-top:10px;"
							src="cid:inspiragelogo" />

					</a>
					<BR />
					<BR />
					<FONT FACE="VERDANA, ARIAL, HELVETICA" SIZE="2">
						<B>Dear Sender,</B>
					</FONT>
					<BR />
					<BR />
					<FONT FACE="VERDANA, ARIAL, HELVETICA" SIZE="2">
						<B>
							<xsl:value-of
								select="/xml2sql/TRANSACTION_SET/SHIPMENT_REF_DETAILS/@SHIP_REF_NUM" />
						</B>
						is invalid reference number, please send the valid reference
						number.
					</FONT>
					<BR />
					<BR />

				</HTML>

			</xsl:when>


			<!-- Condition for valid Ref# -->

			<xsl:when
				test="/xml2sql/TRANSACTION_SET/SHIPMENT_EVENT_DETAILS/SHIP_EVENT">

				<HTML>
					<a href="http://www.inspirage.com">

						<img style="float:right; padding-right:10px;padding-top:10px;"
							src="cid:inspiragelogo" />

					</a>
					<BR />
					<BR />

					<FONT FACE="VERDANA, ARIAL, HELVETICA" SIZE="2">
						<B>Dear Sender,</B>
					</FONT>
					<BR />
					<BR />
					<FONT FACE="VERDANA, ARIAL, HELVETICA" SIZE="2">We are please to
						inform you of the following details of your Shipment.</FONT>
					<BR />
					<BR />



					<TABLE CELLPADDING="4" CELLSPACING="1">
						<TR>
							<TD COLSPAN="2">
								<FONT FACE="VERDANA, ARIAL, HELVETICA" SIZE="2">
									<B>Shipment Information</B>
								</FONT>
							</TD>
						</TR>
						<TR>
							<TD WIDTH="200" HEIGHT="25">
								<FONT FACE="VERDANA, ARIAL, HELVETICA" SIZE="2">Shipment
									Reference Number</FONT>
							</TD>
							<TD>
								<FONT FACE="VERDANA, ARIAL, HELVETICA" SIZE="2">
									<B>
										<xsl:value-of
											select="/xml2sql/TRANSACTION_SET/SHIPMENT_EVENT_DETAILS/@SHIP_REF_NUM" />
									</B>
								</FONT>
							</TD>
						</TR>
						<TR>
							<TD WIDTH="109" HEIGHT="25">
								<FONT FACE="VERDANA, ARIAL, HELVETICA" SIZE="2">Start Location</FONT>
							</TD>
							<TD>
								<FONT FACE="VERDANA, ARIAL, HELVETICA" SIZE="2">
									<B>
										<xsl:value-of
											select="/xml2sql/TRANSACTION_SET/SHIPMENT_EVENT_DETAILS/@SHIPMENT_SOURCE" />
									</B>
								</FONT>
							</TD>
						</TR>
						<TR>
							<TD WIDTH="109" HEIGHT="25">
								<FONT FACE="VERDANA, ARIAL, HELVETICA" SIZE="2">End Location</FONT>
							</TD>
							<TD>
								<FONT FACE="VERDANA, ARIAL, HELVETICA" SIZE="2">
									<B>
										<xsl:value-of
											select="/xml2sql/TRANSACTION_SET/SHIPMENT_EVENT_DETAILS/@SHIPMENT_DEST" />
									</B>
								</FONT>
							</TD>
						</TR>
					</TABLE>
					<BR />
					<BR />


					<FONT FACE="VERDANA, ARIAL, HELVETICA" SIZE="2">
						<B>Sequence of events occured on Shipment,</B>
					</FONT>
					<BR />
					<BR />

					<TABLE BORDER="1" CELLPADDING="4" CELLSPACING="0">

						<xsl:for-each
							select="/xml2sql/TRANSACTION_SET/SHIPMENT_EVENT_DETAILS/SHIP_EVENT">



							<xsl:choose>
								<xsl:when test="(position() mod 2) = 1">
									<TR BGCOLOR="#E0E0E0">
										<TD>
											<FONT FACE="VERDANA, ARIAL, HELVETICA" SIZE="2">
												<!-- xsl:value-of select="@STOPNUM" -->
												<xsl:value-of select="position()" />
											</FONT>
										</TD>
										<TD>
											<FONT FACE="VERDANA, ARIAL, HELVETICA" SIZE="2">
												<xsl:value-of select="@LOC_NAME" />
											</FONT>
										</TD>
										<TD>
											<FONT FACE="VERDANA, ARIAL, HELVETICA" SIZE="2">
												<xsl:value-of select="@STATUS_DESC" />
											</FONT>
										</TD>
										<TD>
											<FONT FACE="VERDANA, ARIAL, HELVETICA" SIZE="2">
												<xsl:value-of
													select="concat(concat(@EVENTDATE,' '),@TIME_ZONE_GID)" />
											</FONT>
										</TD>

									</TR>
								</xsl:when>
								<xsl:otherwise>
									<TR BGCOLOR="#FFFFFF">
										<TD>
											<FONT FACE="VERDANA, ARIAL, HELVETICA" SIZE="2">
												<!-- xsl:value-of select="@STOPNUM" -->
												<xsl:value-of select="position()" />
											</FONT>
										</TD>
										<TD>
											<FONT FACE="VERDANA, ARIAL, HELVETICA" SIZE="2">
												<xsl:value-of select="@LOC_NAME" />
											</FONT>
										</TD>
										<TD>
											<FONT FACE="VERDANA, ARIAL, HELVETICA" SIZE="2">
												<xsl:value-of select="@STATUS_DESC" />
											</FONT>
										</TD>
										<TD>
											<FONT FACE="VERDANA, ARIAL, HELVETICA" SIZE="2">
												<xsl:value-of
													select="concat(concat(@EVENTDATE,' '),@TIME_ZONE_GID)" />
											</FONT>
										</TD>

									</TR>
								</xsl:otherwise>
							</xsl:choose>


						</xsl:for-each>
					</TABLE>



				</HTML>

			</xsl:when>

			<!-- Condition for valid Ref# but no event -->

			<xsl:when test="/xml2sql/TRANSACTION_SET/SHIPMENT_EVENT_DETAILS">
				<HTML>
					<a href="http://www.inspirage.com">

						<img style="float:right; padding-right:10px;padding-top:10px;"
							src="cid:inspiragelogo" />

					</a>
					<BR />
					<BR />

					<FONT FACE="VERDANA, ARIAL, HELVETICA" SIZE="2">
						<B>Dear Sender,</B>
					</FONT>
					<BR />
					<BR />
					<FONT FACE="VERDANA, ARIAL, HELVETICA" SIZE="2">Event data cannot
						be display because no activity has been done on your Shipment yet.</FONT>
					<BR />
					<BR />
					<TABLE CELLPADDING="4" CELLSPACING="1">
						<TR>
							<TD COLSPAN="2">
								<FONT FACE="VERDANA, ARIAL, HELVETICA" SIZE="2">
									<B>Order Information</B>
								</FONT>
							</TD>
						</TR>
						<TR>
							<TD WIDTH="200" HEIGHT="25">
								<FONT FACE="VERDANA, ARIAL, HELVETICA" SIZE="2">Shipment
									Reference Number</FONT>
							</TD>
							<TD>
								<FONT FACE="VERDANA, ARIAL, HELVETICA" SIZE="2">
									<B>
										<xsl:value-of
											select="/xml2sql/TRANSACTION_SET/SHIPMENT_EVENT_DETAILS/@SHIP_REF_NUM" />
									</B>
								</FONT>
							</TD>
						</TR>
						<TR>
							<TD WIDTH="109" HEIGHT="25">
								<FONT FACE="VERDANA, ARIAL, HELVETICA" SIZE="2">Start Location</FONT>
							</TD>
							<TD>
								<FONT FACE="VERDANA, ARIAL, HELVETICA" SIZE="2">
									<B>
										<xsl:value-of
											select="/xml2sql/TRANSACTION_SET/SHIPMENT_EVENT_DETAILS/@SHIPMENT_SOURCE" />
									</B>
								</FONT>
							</TD>
						</TR>
						<TR>
							<TD WIDTH="109" HEIGHT="25">
								<FONT FACE="VERDANA, ARIAL, HELVETICA" SIZE="2">End Location</FONT>
							</TD>
							<TD>
								<FONT FACE="VERDANA, ARIAL, HELVETICA" SIZE="2">
									<B>
										<xsl:value-of
											select="/xml2sql/TRANSACTION_SET/SHIPMENT_EVENT_DETAILS/@SHIPMENT_DEST" />
									</B>
								</FONT>
							</TD>
						</TR>
					</TABLE>
				</HTML>

			</xsl:when>

			<xsl:when test="/xml2sql/TRANSACTION_SET">
				<HTML>
					<a href="http://www.inspirage.com">

						<img style="float:right; padding-right:10px;padding-top:10px;"
							src="cid:inspiragelogo" />

					</a>
					<BR />
					<BR />
					<FONT FACE="VERDANA, ARIAL, HELVETICA" SIZE="2">
						<B>Dear Sender,</B>
					</FONT>
					<BR />
					<BR />
					<FONT FACE="VERDANA, ARIAL, HELVETICA" SIZE="2">OTM service is not
						available to provide the status. Please try after sometime.</FONT>
					<BR />
					<BR />
				</HTML>
			</xsl:when>
		</xsl:choose>




	</xsl:template>


</xsl:stylesheet>