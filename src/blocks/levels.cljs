(ns blocks.levels
  (:require [blocks.grid :as grid]
            [blocks.goal :as goal]
            [blocks.character :as character]
            [blocks.position :as pos]
            [blocks.stationary :as stationary]
            [blocks.sliding :as sliding]))

(defn pos-obj [grid constr pos]
  (constr (apply pos/->Position pos) grid))

(defn parse-level [grid spec]
  (let [{:keys [player stationary sliding goal]} spec
        po (partial pos-obj grid)]
    {:grid grid
     :player (po character/->Character player)
     :blocks (concat (map (partial po stationary/->Stationary) stationary)
                     (map (partial po sliding/->Sliding) sliding))
     :goal (po goal/->Goal goal)}))

(defn level1 [game-area]
  (parse-level
   (grid/Grid. 11 16 game-area)
   {:player [0 0]
    :stationary [[0 2] [1 2] [3 1] [5 0] [5 1] [6 2] [7 2] [8 2] [6 3] [8 3] [2 4]
                 [2 5] [3 5] [4 5] [5 5] [6 5] [3 6] [0 8] [1 8] [2 8] [4 8] [5 8]
                 [8 8] [7 9] [2 10] [3 10] [4 10] [5 10] [6 10] [9 10] [10 10]
                 [4 11] [4 12] [4 13] [3 14] [4 14] [5 14] [6 14] [7 14] [8 14]
                 [9 14]]
    :sliding [[2 2] [2 3] [3 3] [6 4] [7 4] [9 4] [0 5] [1 5] [9 5] [1 6] [6 6]
              [7 6] [9 6] [5 7] [6 7] [9 7] [10 7] [2 11] [7 11] [8 11]
              [9 11] [1 12] [2 12] [8 12] [0 13] [1 13] [7 13] [0 14] [2 14]]
    :goal [3 13]}))

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
