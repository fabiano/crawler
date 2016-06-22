## crawlr

[![Build Status](https://travis-ci.org/fabiano/crawlr.svg?branch=master)](https://travis-ci.org/fabiano/crawlr)
[![Dependencies Status](https://jarkeeper.com/fabiano/crawlr/status.svg)](https://jarkeeper.com/fabiano/crawlr)

Simple crawler to extract product data from Americanas, Casas Bahia, Extra, Magazine Luiza, Ponto Frio, and Submarino websites.

### Playing with

Use the command `lein try <url>` to play with the crawler. You should pass a category page as argument: `lein try http://www.americanas.com.br/linha/351258/games/jogos-xbox-one`.

### Running the application

#### Dependencies

Postgres 9.5 and Redis.

#### Running the migrations

In order to create all necessary tables run the command: `lein migrate`.

#### Starting the worker

Just type `lein worker`.

#### Feeding the worker

Type `lein feed` to feed the worker with some pages.
