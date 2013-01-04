/* Copyright 2013 SpringSource.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package grails.plugin.databasemigration.jaxb

import grails.plugin.databasemigration.GrailsChangeLogParser
import liquibase.changelog.ChangeLogParameters
import liquibase.changelog.DatabaseChangeLog
import liquibase.exception.ChangeLogParseException
import liquibase.parser.ChangeLogParser
import liquibase.resource.ResourceAccessor
import org.liquibase.xml.ns.dbchangelog.DatabaseChangeLog as JaxbDatabaseChangeLog

/**
 * @author <a href='mailto:burt@burtbeckwith.com'>Burt Beckwith</a>
 */
class JaxbChangeLogParser implements ChangeLogParser {

	protected GrailsChangeLogParser grailsChangeLogParser

	protected static final String NEWLINE = System.getProperty('line.separator')

	JaxbChangeLogParser(GrailsChangeLogParser grailsChangeLogParser) {
		this.grailsChangeLogParser = grailsChangeLogParser
	}

	DatabaseChangeLog parse(String physicalChangeLogLocation, ChangeLogParameters changeLogParameters,
			ResourceAccessor resourceAccessor) throws ChangeLogParseException {

		Class clazz = Class.forName(physicalChangeLogLocation - '.class', false, Thread.currentThread().contextClassLoader)
		def instance = clazz.newInstance()
		List<JaxbDatabaseChangeLog> changeLogs = instance.generate()

		def containerContent = new StringBuilder('databaseChangeLog = {')
		containerContent.append NEWLINE
		for (JaxbDatabaseChangeLog changeLog in changeLogs) {
			File file = File.createTempFile('jaxb-changelog-inner', '.groovy')
			file.deleteOnExit()
			file.withWriter('UTF-8') { Writer writer ->
				writer.write JaxbChangeLogMarshaller.changelogToGroovy(changeLog)
			}

			containerContent.append """include file: "$file.absolutePath", relativeToChangelogFile: false"""
			containerContent.append NEWLINE
		}
		containerContent.append '}'
		containerContent.append NEWLINE

		File file = File.createTempFile('jaxb-changelog-', '.groovy')
		file.deleteOnExit()
		file.withWriter('UTF-8') { Writer writer ->
			writer.write containerContent.toString()
		}

		grailsChangeLogParser.parse file.absolutePath, changeLogParameters, resourceAccessor
	}

	boolean supports(String changeLogFile, ResourceAccessor resourceAccessor) {
		changeLogFile.toLowerCase().endsWith '.class'
	}

	int getPriority() { PRIORITY_DEFAULT }
}
