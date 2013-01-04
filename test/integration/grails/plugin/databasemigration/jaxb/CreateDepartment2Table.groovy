/* Copyright 2013 SpringSource.
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
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

import org.liquibase.xml.ns.dbchangelog.Column
import org.liquibase.xml.ns.dbchangelog.Constraints
import org.liquibase.xml.ns.dbchangelog.CreateTable
import org.liquibase.xml.ns.dbchangelog.DatabaseChangeLog
import org.liquibase.xml.ns.dbchangelog.DatabaseChangeLog.ChangeSet

/**
 * Identical to CreateDepartmentTable except for department name, change set id, and massive Java cruft.
 * Also doesn't implement JaxbChangelogGenerator since it's optional (but a good idea).
 *
 * @author <a href='mailto:burt@burtbeckwith.com'>Burt Beckwith</a>
 */
class CreateDepartment2Table {

	def generate() {

		def changeSet = new DatabaseChangeLog.ChangeSet(id: 'department2', author: 'Bob')

		def createTable = new CreateTable(tableName: 'department2')
		createTable.column << new Column(name: 'id', type: 'int', content: [new Constraints(primaryKey: true, nullable: false)])
		createTable.column << new Column(name: 'name', type: 'varchar(50)')
		createTable.column << new Column(name: 'active', type: 'boolean', defaultValueBoolean: true, content: [new Constraints(nullable: false)])
		changeSet.changeSetChildren << createTable

		[new DatabaseChangeLog(changeSetOrIncludeOrIncludeAll: [changeSet])]
	}
}
