rm -rf target/release
mkdir target/release
cd target/release
git clone git@github.com:grails-plugins/grails-database-migration-jaxb.git
cd grails-database-migration-jaxb
grails clean
grails compile
#grails publish-plugin --noScm --snapshot --stacktrace
grails publish-plugin --noScm --stacktrace
