(ns crawlr.selectors
  (:require [clojure.string :as str]))

(defn convert-to-lazyseq [coll]
  (if (empty? coll)
    nil
    (lazy-seq (cons (first coll) (convert-to-lazyseq (rest coll))))))

(defn parse-price [value]
  (if (str/blank? value) nil (Integer/parseInt (str/replace value #"\D" ""))))

(def product-selectors [
  (fn [parent] (convert-to-lazyseq (.select parent ".cs-product-container")))
  (fn [parent] (convert-to-lazyseq (.select parent ".product-grid-item")))
  (fn [parent] (convert-to-lazyseq (.select parent ".hproduct")))])

(def product-title-selectors [
  (fn [parent] (not-empty (.text (.select parent ".cs-product-title a"))))
  (fn [parent] (not-empty (.attr (.select parent ".card-product-url") "title")))
  (fn [parent] (not-empty (.attr (.select parent ".link.url") "title")))])

(def product-url-selectors [
  (fn [parent] (not-empty (.attr (.select parent ".cs-product-title a") "abs:href")))
  (fn [parent] (not-empty (.attr (.select parent ".card-product-url") "abs:href")))
  (fn [parent] (not-empty (.attr (.select parent ".link.url") "abs:href")))])

(def product-price-selectors [
  (fn [parent] (parse-price (.text (.select parent ".cs-price-value"))))
  (fn [parent] (parse-price (.text (.select parent ".card-product-price"))))
  (fn [parent] (parse-price (.text (.select parent ".for.price.sale"))))])

(def product-barcode-selectors [
  (fn [parent] (seq (map #(.text %) (.select parent "th:contains(Cód. Barras) + td"))))
  (fn [parent] (seq (map #(.text %) (.select parent "td:contains(Código de barras) + td"))))])

(def product-isbn-selectors [
  (fn [parent] (seq (map #(.text %) (.select parent "th:contains(I.S.B.N.) + td"))))
  (fn [parent] (seq (map #(.text %) (.select parent "td:contains(ISBN) + td"))))
  (fn [parent] (seq (map #(.text %) (.select parent "dt:contains(ISBN) + dd"))))])

(def next-page-selectors [
  (fn [parent] (not-empty (.attr (.select parent "link[rel=next]") "abs:href")))])
