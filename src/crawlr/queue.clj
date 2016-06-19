(ns crawlr.queue
  (:require [clojure.tools.logging :as logging])
  (:require [crawlr.crawler :as crawler])
  (:require [crawlr.database :as database])
  (:require [taoensso.carmine :as car :refer (wcar)])
  (:require [taoensso.carmine.message-queue :as car-mq]))

(def server {:pool {} :spec {:uri (or (System/getenv "REDIS_URL") "redis://localhost:6379")}})

(defn pages-handler [{:keys [message attempt]}]
  (logging/info "Processing page" message)

  (let [{products :products next-page :next-page} (crawler/process message)]
    (doseq [product products]
      (car/wcar server
        (car-mq/enqueue "products" product)))

    (if-not (nil? next-page)
      (car/wcar server
        (car-mq/enqueue "pages" next-page))))

  {:status :success})

(defn products-handler [{:keys [message attempt]}]
  (logging/info "Processing product" (:title message))

  (database/create message)

  {:status :success})

(def pages-worker
  (car-mq/worker server "pages" {:handler pages-handler :auto-start false :nthreads 2}))

(def products-worker
  (car-mq/worker server "products" {:handler products-handler :auto-start false :nthreads 5}))
