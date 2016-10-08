(ns crawlr.crawler
  (:require [clojure.string :as str])
  (:import [org.jsoup Jsoup]))

(defn connect [page]
  (.get (Jsoup/connect page)))

(defn attr [element name]
  (let [value (.attr element name)]
    (if (str/blank? value) nil value)))

(defn text [element]
  (let [value (.text element)]
    (if (str/blank? value) nil value)))

(defn sanitize-price [value]
  (if (str/blank? value) nil (Integer/parseInt (str/replace value #"\D" ""))))

(defn sanitize-barcode [value]
  (if (str/blank? value) nil (str/replace (str/replace value "Cód EAN" "") #"\W" "")))

(defn sanitize-model [value]
  (if (str/blank? value) nil (str/replace value #"\W" "")))

(def product-selectors [
  (fn [parent] (seq (.select parent ".single-product")))
  (fn [parent] (seq (.select parent ".hproduct")))
  (fn [parent] (seq (.select parent ".product")))])

(def product-title-selectors [
  (fn [parent] (attr (.select parent ".url") "title"))
  (fn [parent] (attr (.select parent ".link.url") "title"))
  (fn [parent] (attr (.select parent ".product-li") "title"))])

(def product-url-selectors [
  (fn [parent] (attr (.select parent ".url") "abs:href"))
  (fn [parent] (attr (.select parent ".link.url") "abs:href"))
  (fn [parent] (attr (.select parent ".product-li") "abs:href"))])

(def product-price-selectors [
  (fn [parent] (sanitize-price (text (.select parent ".price.sale"))))
  (fn [parent] (sanitize-price (text (.select parent ".price"))))])

(def product-barcodes-selectors [
  (fn [parent] (seq (map sanitize-barcode (map text (.select parent "th:contains(Código de Barras) + td")))))
  (fn [parent] (seq (map sanitize-barcode (map text (.select parent "span:contains(Cód EAN)")))))])

(def product-models-selectors [
  (fn [parent] (seq (map sanitize-model (map text (.select parent "th:contains(Referência do Modelo) + td")))))
  (fn [parent] (seq (map sanitize-model (map text (.select parent "span:contains(Referência) + p")))))])

(def next-page-selectors [
  (fn [parent] (attr (.select parent "link[rel=next]") "href"))])

(defn title [parent]
  (some #(% parent) product-title-selectors))

(defn url [parent]
  (some #(% parent) product-url-selectors))

(defn price [parent]
  (some #(% parent) product-price-selectors))

(defn barcodes [parent]
  (some #(% parent) product-barcodes-selectors))

(defn models [parent]
  (some #(% parent) product-models-selectors))

(defn extras [page]
  (let [doc (connect page)]
    {:barcodes (barcodes doc) :models (models doc)}))

(defn next-page [parent]
  (some #(% parent) next-page-selectors))

(defn product [parent]
  (let [title (title parent) url (url parent) price (price parent) extras (if (str/blank? url) nil (extras url))]
    {:title title :url url :price price :extras extras}))

(defn products [parent]
  (map product (some #(% parent) product-selectors)))

(defn process [page]
  (let [doc (connect page)]
    {:products (products doc) :next-page (next-page doc)}))
