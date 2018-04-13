(ns blocks.levels
  (:require [blocks.grid :as grid]
            [blocks.goal :as goal]
            [blocks.character :as character]
            [blocks.position :as pos]
            [blocks.stationary :as stationary]
            [blocks.sliding :as sliding]))

(defn level1 [game-area]
  (let [grid (grid/Grid. 10 8 game-area)]
    {:grid grid
     :player (character/Character. (pos/Position. 2 1) grid)
     :blocks [(stationary/Stationary. (pos/Position. 3 4) grid)
              (stationary/Stationary. (pos/Position. 8 2) grid)
              (sliding/Sliding. (pos/Position. 5 5) grid)]
     :goal (goal/Goal. (pos/Position. 9 6) grid)}))

(defn level2 [game-area]
  (let [grid (grid/Grid. 12 10 game-area)]
    {:grid grid
     :player (character/Character. (pos/Position. 5 9) grid)
     :blocks [(stationary/Stationary. (pos/Position. 5 8) grid)
              (sliding/Sliding. (pos/Position. 3 1) grid)
              (sliding/Sliding. (pos/Position. 4 7) grid)]
     :goal (goal/Goal. (pos/Position. 1 2) grid)}))

(def levels [level1 level2])

(defn get-level [num game-area]
  ((nth levels num) game-area))

(defn get-at [level pos]
  (let [same-pos? (comp (partial = pos) :pos)
        {:keys [blocks goal]} level
        blocks-at (filter same-pos? blocks)
        goal-at (if (same-pos? goal) goal nil)
        m {:blocks blocks-at :goal goal-at}]
    (into {} (remove (comp empty? val) m))))
