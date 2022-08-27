(ns rads.portal-cli
  (:require [babashka.deps :as deps]
            [cheshire.core :as json]
            [clj-yaml.core :as yaml]
            [clojure.data.xml :as xml]
            [clojure.edn :as edn]
            [clojure.string :as str]))

(deps/add-deps '{:deps {org.babashka/spec.alpha {:git/url "https://github.com/babashka/spec.alpha"
                                                 :git/sha "1a841c4cc1d4f6dab7505a98ed2d532dd9d56b78"}
                        djblue/portal {:mvn/version "0.29.1"}}})

(require '[portal.api :as p])

(.addShutdownHook (Runtime/getRuntime)
                  (Thread. (fn [] (p/close))))


(def file (first *command-line-args*))
(when-not file
  (binding [*out* *err*]
    (println "Usage: portal.clj <file.(edn|json|xml|yaml)>"))
  (System/exit 1))

(defn xml->hiccup [xml]
  (if-let [t (:tag xml)]
    (let [elt [t]
          elt (if-let [attrs (:attr xml)]
                (conj elt attrs)
                elt)]
      (into elt (map xml->hiccup (:content xml))))
    xml))

(def extension (last (str/split file #"\.")))
(def contents (slurp file))
(def data
  (if-not (str/includes? file ".")
    (edn/read-string contents)
    (case extension
      ("edn")
      (edn/read-string contents)
      ("json")
      (json/parse-string contents)
      ("yml" "yaml")
      (yaml/parse-string contents)
      ("xml")
      (-> (xml/parse-str contents
                         :skip-whitespace true
                         :namespace-aware false)
          (xml->hiccup)))))

(defn -main [& _]
  (p/open)
  (p/tap)
  (tap> data)
  @(promise))
