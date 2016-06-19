(ns crawlr.cmdline.worker
  (:require [crawlr.queue :as queue])
  (:require [taoensso.carmine.message-queue :as car-mq])
  (:gen-class))

(defn -main [& args]
  (car-mq/start queue/pages-worker)
  (car-mq/start queue/products-worker))
