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

import java.util.List;

import org.liquibase.xml.ns.dbchangelog.DatabaseChangeLog;

/**
 * @author <a href='mailto:burt@burtbeckwith.com'>Burt Beckwith</a>
 */
public interface JaxbChangelogGenerator {

	/**
	 * Programmatically build one or more DatabaseChangeLog instances to be run in a Liquibase changelog set.
	 * @return the change logs
	 */
	List<DatabaseChangeLog> generate();
}
