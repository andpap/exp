(ns exp.db)

(def content-core-clj "(ns project1.core)

")
(def content-readme-txt "readme file
* [TODO] task 1
* [TODO] task 2
")
(def content-foo-clj "(ns project1.utils.foo)
  (defn func-from-foo []
    (println \"hello from foo\"))
")
(def content-buzz-clj "(ns project2.utils.buzz)
  (defn func-from-buzz []
    (println \"hello from buzz\") )
")

(def default-db
  {:name "re-frame"
   :entities {"/project1/core.clj" {:id "123123"
                                    :type :module
                                    :content content-core-clj}
              "/project1/code.org" {:id "43234234"
                                    :type :org
                                    :entities [{:id "4234555"
                                                :type :module
                                                :content "(+ 1 2)"}
                                               {:id "6456456"
                                                :type :text
                                                :content "some text"}]}
              "/project1/readme.txt" {:id "689384"
                                      :type :text
                                      :content content-readme-txt}
              "/project1/utils/foo.clj" {:id "03287834"
                                         :type :module
                                         :content content-foo-clj}
              "/project1/utils/buzz.clj" {:id "832838"
                                          :type :module
                                          :content content-buzz-clj}
              "/project2/readme.txt" {:id "947893"
                                      :type :text
                                      :content content-readme-txt}}
   }
  )
