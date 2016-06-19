(ns crawlr.database
  (:require [clojure.java.jdbc :as jdbc])
  (:require [clojure.tools.logging :as logging]))

(def db (or (System/getenv "DATABASE_URL") "postgres://crawlr:crawlr@localhost:5432/crawlr"))

(def create-tables-sql "
  create table if not exists products (
      id serial not null primary key,
      title text not null,
      url text not null unique,
      price integer
  );

  create table if not exists prices (
    id serial primary key not null,
    product_id int not null references products,
    price integer,
    published_at timestamp not null
  );")

(def create-functions-sql "
  create or replace function insert_product_price()
  returns trigger as $body$

  begin
    insert into prices (product_id, price, published_at)
    values (new.id, new.price, current_timestamp);

    return new;
  end;

  $body$ language plpgsql;

  create or replace function update_product_price()
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

(def create-triggers-sql "
  drop trigger if exists product_inserted on products;
  drop trigger if exists product_updated on products;

  create trigger product_inserted after insert on products for each row execute procedure insert_product_price();
  create trigger product_updated after update on products for each row execute procedure update_product_price();")

(def insert-product-sql "
  insert into products (title, url, price) values (?, ?, ?)
  on conflict (url) do update set title = ?, price = ?")

(defn migrate []
  (logging/info "Running migrations")

  (jdbc/execute! db create-tables-sql)
  (jdbc/execute! db create-functions-sql)
  (jdbc/execute! db create-triggers-sql)

  (logging/info "Database migrated to latest version."))

(defn create [product]
  (logging/info "Creating product " product)

  (let [{:keys [title url price]} product]
    (jdbc/execute! db [insert-product-sql title url price title price])))
