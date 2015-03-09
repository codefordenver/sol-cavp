(ns sol-cavp.styles
  (:require
   [garden.def :refer [defrule defstyles]]
   [garden.stylesheet :refer [rule]]))

(defstyles screen
  (let [body (rule :body)
        rows (rule :.row)]
   (body {:font-family "Helvetica Neue"
      :font-size "16px" 
      :line-height 1.5})
   (rows {:padding-bottom "20px"})))



