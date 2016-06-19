(ns crawlr.cmdline.feed
  (:require [crawlr.queue :as queue])
  (:require [taoensso.carmine :as car :refer (wcar)])
  (:require [taoensso.carmine.message-queue :as car-mq])
  (:gen-class))

(defn -main [& args]
  (car/wcar queue/server
    (car-mq/enqueue "pages" "http://www.submarino.com.br/linha/351259/games/jogos-xbox-one")
    (car-mq/enqueue "pages" "http://www.americanas.com.br/linha/351258/games/jogos-xbox-one")
    (car-mq/enqueue "pages" "http://www.extra.com.br/Games/XboxOne/JogosXboxOne/?Filtro=C336_C2676_C2678")
    (car-mq/enqueue "pages" "http://www.pontofrio.com.br/Games/XboxOne/JogosXboxOne/?Filtro=C336_C2676_C2678")
    (car-mq/enqueue "pages" "http://www.casasbahia.com.br/Games/XboxOne/JogosXboxOne/?Filtro=C336_C2676_C2678")
    (car-mq/enqueue "pages" "http://www.magazineluiza.com.br/jogos-de-xbox-one/games/s/ga/gjxo")))
