# flatmap
This is my self-educational project. Not to be confused with [flatMap](https://martinfowler.com/articles/collection-pipeline/flat-map.html). I took the idea from the [BerlinHousing](https://github.com/plamenpasliev/BerlinHousing) repository. My project is going to be somewhat similar once stabilized, currently it is still in progress. The ultimate goal is to utilize an interactive map to show properties, mainly apartments, some useful information about them (address, area, price etc) and the nearby facilities.

## Why Java?
First, I'm learning it and trying out different things from the language ecosystem. Second, I generally like it :)

## What's used
Currently, the following great stuff helps me to advance with my project:
1. [Jsoup](https://github.com/jhy/jsoup) as the base for the parser implementations
2. PostgreSQL and its [JDBC driver](https://github.com/pgjdbc/pgjdbc) to save results to the database
3. [HikariCP](https://github.com/brettwooldridge/HikariCP) as the database connection pool implementation
4. [Dotenv](https://github.com/cdimascio/java-dotenv) to be able to change application parameters between launches
5. [JCommander](https://github.com/cbeust/jcommander) to parse command line arguments
6. [Greenrobot EventBus](https://github.com/greenrobot/EventBus) to dispatch and handle events
7. [Cucumber](https://github.com/cucumber/cucumber-jvm) + [JUnit](https://github.com/junit-team/junit5) to write and execute tests
8. [WireMock](https://github.com/tomakehurst/wiremock) to be able to run integration tests without connecting to the real websites
9. [SLF4J](https://github.com/qos-ch/slf4j) backed by [Logback](https://github.com/qos-ch/logback) for logging
