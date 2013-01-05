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

import grails.plugin.databasemigration.GrailsChangeLogParser
import grails.plugin.databasemigration.jaxb.JaxbChangeLogParser
import liquibase.parser.ChangeLogParserFactory

class DatabaseMigrationJaxbGrailsPlugin {
	String version = '0.1'
	String grailsVersion = '2.0 > *'
	String author = 'Burt Beckwith'
	String authorEmail = 'beckwithb@vmware.com'
	String title = 'Grails JAXB Database Migration Plugin'
	String description = 'Adds support for migrations using JAXB classes'
	String documentation = 'http://grails-plugins.github.com/grails-database-migration-jaxb/'

	List pluginExcludes = [
		'docs/**',
		'src/docs/**',
		'grails-app/migrations/**'
	]

	String license = 'APACHE'
	def organization = [name: 'SpringSource', url: 'http://www.springsource.org/']
	def issueManagement = [system: 'Github', url: 'https://github.com/grails-plugins/grails-database-migration-jaxb/issues']
	def scm = [url: 'https://github.com/grails-plugins/grails-database-migration-jaxb']

	def doWithApplicationContext = { ctx ->
		// adds support for compiled JAXB .class files
		ChangeLogParserFactory.instance.register new JaxbChangeLogParser(new GrailsChangeLogParser(ctx))
	}
}
