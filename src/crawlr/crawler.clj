(ns crawlr.crawler
  (:require [clojure.string :as str])
  (:require [crawlr.selectors :as selectors])
  (:import [org.jsoup Jsoup]))

(defn connect [page]
  (-> page
    (Jsoup/connect)
    (.userAgent "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1")
    (.timeout 60000)
    (.get)))

(defn title [parent]
  (some #(% parent) selectors/product-title-selectors))

(defn url [parent]
  (some #(% parent) selectors/product-url-selectors))

(defn price [parent]
  (some #(% parent) selectors/product-price-selectors))

(defn barcode [parent]
  (some #(% parent) selectors/product-barcode-selectors))

(defn isbn [parent]
  (some #(% parent) selectors/product-isbn-selectors))

(defn extras [page]
  (if (str/blank? page)
    nil
    (let [doc (connect page)]
      {:barcode (barcode doc) :isbn (isbn doc)})))

(defn product [parent]
  (let [url (url parent)]
    {:title (title parent) :url url :price (price parent) :extras (extras url)}))

(defn products [parent]
  (map product (some #(% parent) selectors/product-selectors)))

(defn next-page [parent]
  (some #(% parent) selectors/next-page-selectors))

(defn process [page]
  (let [doc (connect page)]
    {:products (products doc) :next-page (next-page doc)}))
