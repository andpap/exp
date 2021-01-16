(ns exp.core
  (:require [org.httpkit.server :as server]
            [org.httpkit.client :as client]
            [clojure.string :as s]
            [compojure.core :refer :all ]
            [compojure.route :as rouite]
            [cheshire.core :as json]
            [clojure.core.async :refer [go-loop timeout <!]]))

(def work-path "/home/work/exp/client/src")

(defonce *api-server (atom nil))

(def interval 10000)

(defn get-paths [dir]
  (->> (file-seq (clojure.java.io/file dir))
       (map #(.getAbsolutePath %))
       (map #(subs % (count work-path)))
       (filter #(s/ends-with? % "cljs"))))

(defn create-action
  [type payload]
  {:type type :payload payload})

(defn send-files-update
  [channel]
  (let [data (get-paths work-path)
        data (mapv (fn [v] {:name v :type :module}) data)
        data (create-action :files-update data)
        data (json/generate-string data)]
    (server/send! channel data)))

(defn auto-send-updates
  [channel *is-need-stop]
  (let [*is-need-stop (atom false)]
    (go-loop []
      (when-not @*is-need-stop
        (<! (timeout interval))
        (send-files-update channel)
        (recur)))))

(defn ws-handler [req]
  (server/with-channel req channel
    (let [*is-need-stop (atom false)]
      (auto-send-updates channel *is-need-stop))
      (server/on-close   channel (fn [status]
                                   (println "channel closed")))
      (server/on-receive channel (fn [data]
                                   (println ">>>" data)
                                   (server/send! channel data)))))

  

(defn show-index-page [req]
  "hello112!!!")


(defroutes app-routes
  (GET "/" [] show-index-page)
  (GET  "/ws" [] ws-handler))

(defn start [opts]
  (reset! *api-server
          (server/run-server #'app-routes opts)))

(defn stop[opts]
  (@*api-server :timeout 100)
  (println "stopped server!!"))

(defn say-hello [ops]
  (println "hello"))

(comment
  (start {:port 8080})
  (stop {})
  

  (json/generate-string {:foo 1})
  




  )
  
