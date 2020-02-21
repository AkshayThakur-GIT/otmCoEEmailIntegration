<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema">
   <xsl:template match="comment()|processing-instruction()|/">
      <xsl:copy>
         <xsl:apply-templates/>
      </xsl:copy>
   </xsl:template>
   <xsl:template match="*">
      <xsl:element name="{local-name()}">
         <xsl:apply-templates select="@*|node()"/>
      </xsl:element>
   </xsl:template>
   <xsl:template match="@*">
      <xsl:choose>
         <xsl:when test="name() != 'xmlns'">
            <xsl:attribute name="{local-name()}">
               <xsl:value-of select="."/>
            </xsl:attribute>
         </xsl:when>
      </xsl:choose>
   </xsl:template>
</xsl:stylesheet>
