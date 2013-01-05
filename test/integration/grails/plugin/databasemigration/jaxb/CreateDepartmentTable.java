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
package grails.plugin.databasemigration.jaxb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.liquibase.xml.ns.dbchangelog.Column;
import org.liquibase.xml.ns.dbchangelog.Constraints;
import org.liquibase.xml.ns.dbchangelog.CreateTable;
import org.liquibase.xml.ns.dbchangelog.DatabaseChangeLog;
import org.liquibase.xml.ns.dbchangelog.DatabaseChangeLog.ChangeSet;

/**
 * Creates the 'department' table.
 *
 * @author <a href='mailto:burt@burtbeckwith.com'>Burt Beckwith</a>
 */
public class CreateDepartmentTable implements JaxbChangelogGenerator {

	public List<DatabaseChangeLog> generate() {

		ChangeSet changeSet = new DatabaseChangeLog.ChangeSet();
		changeSet.setId("department");
		changeSet.setAuthor("Bob");

		CreateTable createTable = new CreateTable();
		createTable.setTableName("department");

		Column column = new Column();
		column.setName("id");
		column.setType("int");
		List<Object> columnContent = new ArrayList<Object>();
		Constraints constraints = new Constraints();
		constraints.setPrimaryKey("true");
		constraints.setNullable("false");
		columnContent.add(constraints);
		column.setContent(columnContent);
		createTable.getColumn().add(column);

		column = new Column();
		column.setName("name");
		column.setType("varchar(50)");
		createTable.getColumn().add(column);

		column = new Column();
		column.setName("active");
		column.setType("boolean");
		column.setDefaultValueBoolean("true");
		columnContent = new ArrayList<Object>();
		constraints = new Constraints();
		constraints.setNullable("false");
		columnContent.add(constraints);
		column.setContent(columnContent);
		createTable.getColumn().add(column);

		changeSet.getChangeSetChildren().add(createTable);

		DatabaseChangeLog databaseChangeLog = new DatabaseChangeLog();
		databaseChangeLog.getChangeSetOrIncludeOrIncludeAll().add(changeSet);

		return Collections.singletonList(databaseChangeLog);
	}
}
