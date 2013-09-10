<?xml version="1.0" encoding="UTF-8"?>
<!--
  Copyright 2011 selunit.org
  
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  
    http://www.apache.org/licenses/LICENSE-2.0
  
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
-->
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:html="http://www.w3.org/1999/xhtml">
	<xsl:output method="xml" version="1.0" encoding="UTF-8" />

	<xsl:template match="/testsuite">
		<xsl:element name="testsuite">
			<xsl:attribute name="time">
      <xsl:value-of select="@time" />
    </xsl:attribute>
			<xsl:attribute name="tests">
      <xsl:value-of select="@total" />
    </xsl:attribute>
			<xsl:attribute name="errors">
      <xsl:value-of select="@failures" />
    </xsl:attribute>
			<xsl:attribute name="skipped">0</xsl:attribute>
			<xsl:attribute name="failures">0</xsl:attribute>
	<xsl:attribute name="name">
      <xsl:value-of select="job-info/prop[@name='hierarchicalTestName']/@value" />
    </xsl:attribute>
			<xsl:apply-templates select="testcase" />
		</xsl:element>
	</xsl:template>

	<xsl:template match="testcase">
		<xsl:element name="testcase">
			<xsl:attribute name="time">
      <xsl:value-of select="@time" />
    </xsl:attribute>
			<xsl:attribute name="classname">
      <xsl:value-of select="../job-info/prop[@name='hierarchicalTestName']/@value" />
    </xsl:attribute>
			<xsl:attribute name="name">
      <xsl:value-of select="@name" />
    </xsl:attribute>
			<xsl:if test="@result='failed'">
				<xsl:apply-templates select="log" />
			</xsl:if>
		</xsl:element>
	</xsl:template>


	<xsl:template match="log">
		<xsl:element name="error">
			<xsl:attribute name="type">error</xsl:attribute>
			<xsl:attribute name="message">
      <xsl:value-of select="html-summary/text()" />
    </xsl:attribute>
			<xsl:value-of select="system-log/text()" />
		</xsl:element>
	</xsl:template>

</xsl:stylesheet>
