(ns crawlr.crawler
  (:require [clojure.string :as str])
  (:import [org.jsoup Jsoup]))

(defn attr [element name]
  (let [value (.attr element name)]
    (if (str/blank? value) nil value)))

(defn text [element]
  (let [value (.text element)]
    (if (str/blank? value) nil value)))

(defn sanitize-price [value]
  (if (str/blank? value) nil (Integer/parseInt (str/replace value #"\D" ""))))

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

(def next-page-selectors [
  (fn [parent] (attr (.select parent "link[rel=next]") "href"))])

(defn next-page [parent]
  (some #(% parent) next-page-selectors))

(defn price [parent]
  (some #(% parent) product-price-selectors))

(defn url [parent]
  (some #(% parent) product-url-selectors))

(defn title [parent]
  (some #(% parent) product-title-selectors))

(defn product [parent]
  {:title (title parent) :url (url parent) :price (price parent)})

(defn products [parent]
  (map product (some #(% parent) product-selectors)))

(defn connect [page]
  (.get (Jsoup/connect page)))

(defn process [page]
  (let [doc (connect page)]
    {:products (products doc) :next-page (next-page doc)}))
