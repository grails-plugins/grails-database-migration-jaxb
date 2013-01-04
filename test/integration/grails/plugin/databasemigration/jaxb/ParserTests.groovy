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

import grails.plugin.databasemigration.MigrationUtils

import java.sql.DatabaseMetaData
import java.sql.ResultSet

import liquibase.Liquibase

/**
 * @author <a href='mailto:burt@burtbeckwith.com'>Burt Beckwith</a>
 */
class ParserTests extends GroovyTestCase {

	def dataSource

	void testParse() {

		def tables = getTableData()
		assert !tables

		MigrationUtils.executeInSession {
			Liquibase liquibase = MigrationUtils.getLiquibase(MigrationUtils.database, 'changelog.groovy')
			def unrun = liquibase.listUnrunChangeSets()
			assert 6 == unrun.size()
			assert ['t1', 't2', 'department', 'department2', 'change_t2', 'raw'] == unrun*.id

			liquibase.update null
		}

		tables = getTableData()

		assert 7 == tables.size()

		assert tables.DATABASECHANGELOG
		assert tables.DATABASECHANGELOGLOCK

		def department = tables.DEPARTMENT
		assert department
		assert 3 == department.size()
		assert([name: 'ID', typeCode: 4, typeName: 'INTEGER', length: 10, digits: 0, nullable: false, defaultValue: null, ordinal: 1, autoincrement: false] == department[0])
		assert([name: 'NAME', typeCode: 12, typeName: 'VARCHAR', length: 50, digits: 0, nullable: true, defaultValue: null, ordinal: 2, autoincrement: false] == department[1])
		assert([name: 'ACTIVE', typeCode: 16, typeName: 'BOOLEAN', length: 1, digits: 0, nullable: false, defaultValue: 'TRUE', ordinal: 3, autoincrement: false] == department[2])

		assert tables.DEPARTMENT2 == tables.DEPARTMENT

		def t1 = tables.T1
		assert t1
		assert 2 == t1.size()
		def idColumn = t1[0]
		assert idColumn.remove('defaultValue').startsWith('(NEXT VALUE FOR PUBLIC.SYSTEM_SEQUENCE')
		assert([name: 'ID', typeCode: -5, typeName: 'BIGINT', length: 19, digits: 0, nullable: false, ordinal: 1, autoincrement: true] == idColumn)
		assert([name: 'NAME', typeCode: 12, typeName: 'VARCHAR', length: 255, digits: 0, nullable: false, defaultValue: null, ordinal: 2, autoincrement:false] == t1[1])

		def t2 = tables.T2
		assert t2
		assert 3 == t2.size()
		idColumn = t2[0]
		assert idColumn.remove('defaultValue').startsWith('(NEXT VALUE FOR PUBLIC.SYSTEM_SEQUENCE')
		assert([name: 'ID', typeCode: -5, typeName: 'BIGINT', length: 19, digits: 0, nullable: false, ordinal: 1, autoincrement: true] == idColumn)
		assert([name: 'NAME', typeCode: 12, typeName: 'VARCHAR', length: 255, digits: 0, nullable: false, defaultValue: null, ordinal: 2, autoincrement:false] == t2[1])
		assert([name: 'NEW_COL', typeCode: 12, typeName: 'VARCHAR', length: 123, digits: 0, nullable: false, defaultValue: null, ordinal: 3, autoincrement:false] == t2[2])

		def ssqqll = tables.SSQQLL
		assert ssqqll
		assert 2 == ssqqll.size()
		assert([name: 'ID', typeCode: 12, typeName: 'VARCHAR', length: 255, digits: 0, nullable: false, defaultValue: null, ordinal: 1, autoincrement: false] == ssqqll[0])
		assert([name: 'BYTES', typeCode: -3, typeName: 'VARBINARY', length: 100000, digits: 0, nullable: false, defaultValue: null, ordinal: 2, autoincrement:false] == ssqqll[1])
	}

	private Map getTableData() {

		def connection
		try {
			connection = dataSource.connection

			DatabaseMetaData metaData = connection.metaData
			ResultSet rs = metaData.getTables(null, null, null, ['TABLE'] as String[])

			def names = []
			while (rs.next()) {
				names << rs.getString("TABLE_NAME")
			}
			rs.close()

			def tables = [:]

			for (String name in names) {
				tables[name] = []
				rs = metaData.getColumns(null, null, name, null)
				while (rs.next()) {
					tables[name] << [
						name: rs.getString('COLUMN_NAME'),
						typeCode: rs.getInt('DATA_TYPE'),
						typeName: rs.getString('TYPE_NAME'),
						length: rs.getInt('COLUMN_SIZE'),
						digits: rs.getInt('DECIMAL_DIGITS'),
						nullable: 'YES' == rs.getString('IS_NULLABLE'),
						defaultValue: rs.getObject('COLUMN_DEF'),
						ordinal: rs.getInt('ORDINAL_POSITION'),
						autoincrement: 'YES' == rs.getString('IS_AUTOINCREMENT')]
				}
				rs.close()
			}

			tables
		}
		finally {
			 connection?.close()
		}
	}
}
