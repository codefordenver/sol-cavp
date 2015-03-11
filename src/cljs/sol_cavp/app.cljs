(ns sol-cavp.app
  (:require [reagent.core :as reagent :refer [atom]]
            [reagent-forms.core :refer [bind-fields]]))

(enable-console-print!) ;; enable print at web inspector console

(def google-spreadsheet-url ;; READ-ONLY
  "https://docs.google.com/spreadsheets/d/1SIN6GlkVozeZCe-MxjUXDV2fpnlWvKmKWaOdZmNbWGk/pubhtml")

(def master-form (atom nil))

(defn init-table-top []
  (let [enabled?  (fn [x] (when (= (:enabled x) "TRUE") x))
        with-data (fn [data] (filter enabled? (:elements (:2015 (js->clj data :keywordize-keys true)))))
        callback  (fn [data]            
                    (reset! master-form (with-data data)))]
    (.init js/Tabletop (js-obj "key" google-spreadsheet-url "callback" callback "simpleSheet" false))))

(defn row [label & body]
  [:div.row
   [:div {:class "col-md-2 col-lg-2"} [:label label]]
   [:div {:class "col-md-4 col-lg-4"} body]])

(defn text-input [label placeholder]
  [row label 
   [:input {:type "text" :class "form-control" :placeholder placeholder :size 20}]])

(defn date-picker [setdate]
  (let [today (.format (js/moment (new js/Date)) "MM/DD/YYYY")]
    (fn [] [:input#set-date {:type "text" :placeholder  "MM/DD/YYYY" :size 11 :value today}])))

(defn date-picker-did-mount []
  (.ready (js/$ js/document) 
          (fn [] (.datepicker (js/$ "#set-date") (clj->js {:format "MM/DD/YYYY"})))))

(defn date-picker-component []
  (reagent/create-class {:render date-picker
                         :component-did-mount date-picker-did-mount}))
(defn part-1 []
  (let []
    (fn []
      (println (count @master-form))     
      [:div 
         [:div.header
       [:h2.text-right "part 1"]]
      [:br]
      (for [c @master-form]
        ^{:key c} [row (:field-name c) [:input.form-control {:key (:rowNumber c) 
                                                             :type (:type c) 
                                                             ;; (-> % .-target .-value)
                                                             :on-change #(print (-> % .-target .-value))}]])])))

(defn init []
  (init-table-top)
  (reagent/render-component [part-1] (.getElementById js/document "mount")))
