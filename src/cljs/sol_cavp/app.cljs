(ns sol-cavp.app
  (:require [reagent.core :as reagent :refer [atom]]
            [reagent-forms.core :refer [bind-fields]]))

(enable-console-print!) ;; enable print at web inspector console

(def google-spreadsheet-url "https://docs.google.com/spreadsheets/d/1SIN6GlkVozeZCe-MxjUXDV2fpnlWvKmKWaOdZmNbWGk/pubhtml")

(def master-form (atom {}))

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

(defn date-picker [setdate]
  (let [today (.format (js/moment (new js/Date)) "MM/DD/YYYY")]
    (fn [] [:input#set-date {:type "text" :placeholder  "mm/dd/yyyy" :size 11 :value today}])))

(defn date-picker-did-mount []
  (.ready (js/$ js/document) 
          (fn [] (.datepicker (js/$ "#set-date") (clj->js {:format "mm/dd/yyyy"})))))

(defn date-picker-component []
  (reagent/create-class {:render date-picker
                         :component-did-mount date-picker-did-mount}))
(defn return-component [type]
  (let [input-component (fn [x] [:input.form-control {                                                                       
                                         :type x 
                                         :on-change #(print (-> % .-target .-value))}])]
    (case type
      "datepicker" [date-picker-component]
      [input-component type])))

(defn part-1 []
  (let []
    (fn []
      (println (count @master-form))     
      [:div 
         [:div.header
       [:h2.text-right "part 1"]]
      [:br]
      (for [c @master-form]
        ^{:key c} [row (:field-name c) [:div {:key (str "part-1-" (:rowNumber c))}                                       
                                        [return-component (:type c)]]])]))) 
                                                                 
(defn init []
  (init-table-top)
  (reagent/render-component [part-1] (.getElementById js/document "mount")))
