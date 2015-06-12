# Experimental OrgRef API

This is a very small app I put together to play around with creating REST APIs
with clojure. It provides a fully functional API for retrieving organizations
in the [OrgRef](http://www.orgref.org/) dataset. Responses follow the
[json:api](http://jsonapi.org/) specification, see the [API docs](docs/API.md)
for details and examples.

**NOTE**: I am not affiliated with the OrgRef initiative in any way.

* [ring][]/[compojure][] - simple web request handling and routing components
* [Korma][] - SQL abstraction

## Prerequisites

You will need [Leiningen][] 2.0.0 or above installed.

## Installation

    git clone https://github.com/pdlug/orgref-api.git

Download the [OrgRef Dataset](http://www.orgref.org/web/download.htm) in CSV
format and then run the import tool to create the database structure and
populate it:

    lein run -m orgref-api.tools.load <csvfile>

## Running

To start a web server for the application:

    lein ring server-headless

This starts up on http://localhost:3000/

Let's look at some organizations:

    curl http://localhost:3000/organizations

Maybe just those with "college" in the name in the US?

    curl http://localhost:3000/organizations\?country=US\&name=college

Have [jq](http://stedolan.github.io/jq/) installed? Print out just the names:

    curl http://localhost:3000/organizations\?country\=US\&name\=college | jq .data\[\].attributes.name

See the [API docs](docs/API.md) for details.

## Deployment

    lein ring uberwar

## License

Copyright © 2015 FIXME

[leiningen]: https://github.com/technomancy/leiningen
[compojure]: https://github.com/weavejester/compojure
[ring]: https://github.com/ring-clojure/ring
[Korma]: http://sqlkorma.com/
