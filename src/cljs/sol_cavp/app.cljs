(ns sol-cavp.app
  (:require [reagent.core :as reagent :refer [atom]]
            [reagent-forms.core :refer [bind-fields init-field value-of]]))

(enable-console-print!) ;; enable print at web inspector console

(def master-form (atom nil))

(def google-spreadsheet-url ;; READ-ONLY
  "https://docs.google.com/spreadsheets/d/1SIN6GlkVozeZCe-MxjUXDV2fpnlWvKmKWaOdZmNbWGk/pubhtml")

(defn init-table-top []
  (let [enabled?  (fn [x] (when (= (:enabled x) "TRUE") x))
        callback  (fn [data]
                    (reset! master-form (filter enabled? (:elements (:2015 (js->clj data :keywordize-keys true))))))]
    (.init js/Tabletop (js-obj "key" google-spreadsheet-url "callback" callback "simpleSheet" false))))

(defn row [label & body]
  [:div.row
   [:div {:class "col-md-2 col-lg-2"} [:span label]]
   [:div {:class "col-md-4 col-lg-4"} body]])

(defn text-input [label placeholder]
  [row label 
   [:input {:type "text" :class "form-control" :placeholder placeholder :size 20}]])

(defn date-picker [setdate]
  (let [today (.format (js/moment (new js/Date)) "MM/DD/YYYY")]
    (fn [] [:input#set-date {:type "text" :placeholder  "mm/dd/yyyy" :size 11 :value today}])))

(defn date-picker-did-mount []
  (.ready (js/$ js/document) (fn [] (.datepicker (js/$ "#set-date") (clj->js {:format "mm/dd/yyyy"})))))

(defn date-picker-component []
  (reagent/create-class {:render date-picker
                         :component-did-mount date-picker-did-mount}))

(defn input [label type]
  (row label [:input.form-control {:field (keyword type)}]))

(defn part-1 []
  (let []
    (fn []
      (println (count @master-form))
      [:div 
       [:div.header
        [:h2.text-right "part 1"]]
       [:br]
       ;;[row "set date : " [date-picker-component]]
       ;;[text-input "Callers first name initial only (do not enter complete name)" "for Richard Salibe, enter R only"]
       (for [c @master-form]
         ^{:key c} (input (:field-name c) (:type c)))
       ])))

(defn init []
  (init-table-top)
  (reagent/render-component [part-1] (.getElementById js/document "mount")))
