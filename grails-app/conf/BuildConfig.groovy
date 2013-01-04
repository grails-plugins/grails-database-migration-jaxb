grails.project.work.dir = 'target'
grails.project.target.level = 1.7
grails.project.docs.output.dir = 'docs/manual' // for backwards-compatibility, the docs are checked into gh-pages branch

grails.project.dependency.resolution = {

	inherits 'global'
	log 'warn'

	repositories {
		grailsCentral()
	}

	plugins {
		build ':release:2.2.0', ':rest-client-builder:1.0.3', {
			export = false
		}

		compile ":hibernate:$grailsVersion", {
			export = false
		}

		compile ':database-migration:1.3.2'
	}
}
