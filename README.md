[![license](https://img.shields.io/github/license/patriziobruno/crawlertest.svg)](https://raw.githubusercontent.com/patriziobruno/crawlertest/master/LICENSE)
[![Build Status](https://travis-ci.org/patriziobruno/crawlertest.svg?branch=master)](https://travis-ci.org/patriziobruno/crawlertest)

The project has been tested with JDK 1.8.0 update 66 on Windows 2008 and Ubuntu.
The projects uses:
- Maven 3 as build tool
- JUnit, Spring Test, JMockit and Hamcrest as testing frameworks
- Spring MVC for REST
- Spring Data JPA, Hibernate, H2 as data-layer frameworks
- JSoup to download and parse HTML
- ModelMapper to convert DB entities to DTOs
- Eclipse's jetty-maven-plugin to run it in an embedded Jetty server

To run automatic tests just launch from a command line: mvn test

To run jetty-maven-plugin's server: mvn jetty:run
The webapp is accessible at the following URL: http://localhost:8080/crawlertest

The project includes two HTML pages:
- index.html contains a very basic HTML5 client app to pass URLs to the service to be crawled
- test.html is a simple test page used to test the crawler, simply pass http://localhost:8080/crawlertest as part of the input
