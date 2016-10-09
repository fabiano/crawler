(ns crawlr.cmdline.worker
  (:require [clojure.java.io :as io])
  (:require [clojure.tools.logging :as logging])
  (:require [crawlr.crawler :as crawler])
  (:require [crawlr.database :as database])
  (:gen-class))

(defn process-product [product]
  (logging/info "Processing product" (:title product))

  (try
    (database/create product)
  (catch Exception e
    (logging/error e "Error processing product" (:title product)))))

(defn process-page [page]
  (logging/info "Processing page" page)

  (try
    (let [{products :products next-page :next-page} (crawler/process page)]
      (doseq [product products]
        (process-product product))

      (if-not (nil? next-page)
        (process-page next-page)))
  (catch Exception e
    (logging/error e "Error processing page" page))))

(defn -main [& args]
  (with-open [reader (io/reader (first args))]
    (doseq [page (line-seq reader)]
      (process-page page))))
