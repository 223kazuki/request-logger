(ns request-logger.handler.ok
  (:require [ataraxy.core :as ataraxy]
            [ataraxy.response :as response]
            [integrant.core :as ig]
            [taoensso.timbre :refer [info debug]]
            [clojure.data.json :as json]))

(defmethod ig/init-key :request-logger.handler/ok [_ options]
  (fn [{[] :ataraxy/result :as req}]
    (try
      (debug "[Request]: " (pr-str req))
      (let [{:keys [scheme request-method uri
                    headers params
                    body-params body]} req
            body-str (or (some-> body-params json/write-str pr-str)
                         (some-> body
                                 slurp
                                 str))]
        (info "[Scheme]        :" (json/write-str scheme))
        (info "[RequestMethod] :" (json/write-str request-method))
        (info "[Uri]           :" (json/write-str uri))
        (info "[Headers]       :" (json/write-str headers))
        (info "[Params]        :" (json/write-str params))
        (info "[Body]          :" body-str))
      (catch Exception e
        (.stackTrace e))
      (finally
        [::response/ok "OK"]))))
