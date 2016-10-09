(ns crawlr.database
  (:require [clojure.data.json :as json])
  (:require [clojure.java.jdbc :as jdbc])
  (:require [postgre-types.json :refer [add-jsonb-type]]))

(add-jsonb-type json/write-str json/read-str)

(def db (or (System/getenv "DATABASE_URL") "postgres://crawlr:crawlr@localhost:5432/crawlr"))

(def create-tables-sql "
  create table products (
      id serial not null primary key,
      title text not null,
      url text not null unique,
      price integer,
      extras jsonb
  );

  create table prices (
    id serial primary key not null,
    product_id int not null references products,
    price integer,
    published_at timestamp not null
  );")

(def drop-tables-sql "
  drop table prices;
  drop table products;")

(def create-functions-sql "
  create function insert_product_price()
  returns trigger as $body$

  begin
    insert into prices (product_id, price, published_at)
    values (new.id, new.price, current_timestamp);

    return new;
  end;

  $body$ language plpgsql;

  create function update_product_price()
  returns trigger as $body$

  begin
    if (new.price is null and old.price is not null) or
       (new.price is not null and old.price is null) or
       (new.price <> old.price) then

      insert into prices (product_id, price, published_at)
      values (new.id, new.price, current_timestamp);
    end if;

    return new;
  end;

  $body$ language plpgsql;")

(def drop-functions-sql "
  drop function insert_product_price();
  drop function update_product_price();")

(def create-triggers-sql "
  create trigger product_inserted after insert on products for each row execute procedure insert_product_price();
  create trigger product_updated after update on products for each row execute procedure update_product_price();")

(def drop-triggers-sql "
  drop trigger product_inserted on products;
  drop trigger product_updated on products;")

(def insert-product-sql "
  insert into products (title, url, price, extras) values (?, ?, ?, ?)
  on conflict (url) do update set title = ?, price = ?, extras = ?")

(defn migrate-up []
  (jdbc/execute! db create-tables-sql)
  (jdbc/execute! db create-functions-sql)
  (jdbc/execute! db create-triggers-sql))

(defn migrate-down []
  (jdbc/execute! db drop-triggers-sql)
  (jdbc/execute! db drop-functions-sql)
  (jdbc/execute! db drop-tables-sql))

(defn create [product]
  (let [{:keys [title url price extras]} product]
    (jdbc/execute! db [insert-product-sql title url price extras title price extras])))
