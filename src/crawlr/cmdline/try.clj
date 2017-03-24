(ns crawlr.cmdline.try
  (:require [clojure.tools.logging :as logging])
  (:require [crawlr.crawler :as crawler])
  (:gen-class))

(defn run [page]
  (logging/info "Processing page" page)

  (let [{products :products next-page :next-page} (crawler/process page)]
    (doseq [product products]
      (logging/info "Processing product" product))

    (if-not (nil? next-page)
      (if-not (= page next-page)
        (recur next-page)))))

(defn -main [& args]
  (run (first args)))
