databaseChangeLog = {

	changeSet(author: 'burt', id: 't1') {
		createTable(tableName: 't1') {
			column(autoIncrement: true, name: 'id', type: 'BIGINT') {
				constraints(nullable: false, primaryKey: true)
			}
			column(name: 'name', type: 'VARCHAR(255)') {
				constraints(nullable: false)
			}
		}
	}

	include file: 'changelog.xml'

	include file: 'grails.plugin.databasemigration.jaxb.CreateDepartmentTable.class'

	include file: 'grails.plugin.databasemigration.jaxb.CreateDepartment2Table.class'

	changeSet(author: 'burt', id: 'change_t2') {
		addColumn(tableName: 't2') {
			column(name: 'new_col', type: 'varchar(123)') {
				constraints(nullable: false)
			}
		}
	}

	include file: 'changelog.sql'
}
