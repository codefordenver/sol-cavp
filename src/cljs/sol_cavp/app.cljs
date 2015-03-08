(ns sol-cavp.app
  (:require [reagent.core :as reagent :refer [atom]]))

(def google-spreadsheet-url ;; READ-ONLY
  "https://docs.google.com/spreadsheets/d/1SIN6GlkVozeZCe-MxjUXDV2fpnlWvKmKWaOdZmNbWGk/pubhtml")

(defn logger [x]
  (.log js/console x))

(defn init-table-top []
  (let [callback 
        (fn [data] 
          (.log js/console (take 3 (vec (aget data 2015 "elements"))) ))]
    (.init js/Tabletop (js-obj "key" google-spreadsheet-url "callback" callback "simpleSheet" false))))

(defn row [label & body]
  [:div.row
   [:div {:class "col-md-2 col-lg-2"} [:span label]]
   [:div {:class "col-md-2 col-lg-2"} body]])


(defn text-input [label placeholder]
  [row label 
   [:input {:key placeholder :type "text" :class "form-control" :placeholder placeholder :size 20}]])

(defn date-picker [setdate]
  (let [today (.format (js/moment (new js/Date)) "MM/DD/YYYY")]
    (fn []
      [:input#set-date {:type "text" :placeholder  "mm/dd/yyyy" :size 11 :value today }])))

(defn date-picker-did-mount []
  (.ready (js/$ js/document)
          (fn [] (.datepicker (js/$ "#set-date") (clj->js {:format "mm/dd/yyyy"})))))

(defn date-picker-component []
  (reagent/create-class {:render date-picker
                         :component-did-mount date-picker-did-mount}))

(defn part-1 []
  [:div 
   [:div.header
    [:h2.text-right "part 1"]]
   [:br]
   ;; note [text-input arg] is not a function 
   ;; call since reagent will call as part 
   ;; of component hierarchy when it needs
   ;; to rendered
   [row "set date : " [date-picker-component]]
   [text-input "Callers first name initial only (do not enter complete name)" "for Richard Salibe, enter R only"]])

(defn init []
 (init-table-top)
 (reagent/render-component [part-1] (.getElementById js/document "mount")))
