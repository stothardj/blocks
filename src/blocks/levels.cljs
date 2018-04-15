(ns blocks.levels
  (:require [blocks.grid :as grid]
            [blocks.goal :as goal]
            [blocks.character :as character]
            [blocks.position :as pos]
            [blocks.stationary :as stationary]
            [blocks.sliding :as sliding]
            [blocks.light :as light]))

(defn pos-obj [grid constr pos]
  (constr (apply pos/->Position pos) grid))

(defn parse-level [grid spec]
  (let [{:keys [player stationary sliding light goal]} spec
        po (partial pos-obj grid)]
    {:grid grid
     :player (po character/->Character player)
     :blocks (concat (map (partial po stationary/->Stationary) stationary)
                     (map (partial po sliding/->Sliding) sliding)
                     (map (partial po light/->Light) light))
     :goal (po goal/->Goal goal)}))

(defn tut0 [game-area]
  (parse-level
   (grid/Grid. 4 5 game-area)
   {:player [1 1]
    :goal [3 4]}))

(defn tut1 [game-area]
  (parse-level
   (grid/Grid. 11 16 game-area)
   {:player [1 1]
    :goal [8 13]
    :stationary [[3 0] [3 2] [0 3] [1 3] [2 3] [3 3] [4 3] [5 3] [8 3] [9 3] [10 3]
                 [4 4] [4 5] [4 8] [0 9] [2 9] [3 9] [4 9] [5 9] [6 9] [7 9] [8 9]
                 [9 9] [10 9] [5 10] [5 11] [5 14] [5 15]]
    :sliding [[6 3] [7 3] [6 4] [5 12] [6 13]]
    :light [[3 6] [4 6] [2 7] [3 7] [4 7] [6 12] [5 13]]}))

(defn level0 [game-area]
  (parse-level
   (grid/Grid. 11 16 game-area)
   {:player [0 0]
    :stationary [[0 2] [1 2] [3 1] [5 0] [5 1] [6 2] [7 2] [8 2] [6 3] [8 3] [2 4]
                 [2 5] [3 5] [4 5] [5 5] [6 5] [3 6] [0 8] [1 8] [2 8] [4 8] [5 8]
                 [8 8] [7 9] [2 10] [3 10] [4 10] [5 10] [6 10] [9 10]
                 [4 11] [4 12] [4 13] [3 14] [4 14] [5 14] [6 14] [7 14] [8 14]
                 [9 14]]
    :sliding [[2 2] [2 3] [3 3] [6 4] [7 4] [0 5] [1 5] [1 6] [6 6]
              [7 6] [5 7] [6 7] [9 7] [2 11] [7 11] [8 11]
              [9 11] [1 12] [2 12] [8 12] [0 13] [1 13] [7 13] [0 14] [2 14]]
    :light [[9 4] [9 5] [9 6] [10 7] [10 10]]
    :goal [3 13]}))

(defn level1 [game-area]
  (parse-level
   (grid/Grid. 11 16 game-area)
   {:player [5 3]
    :stationary [[4 2] [6 2] [4 3] [6 3] [4 6] [6 6] [4 7] [6 7] [0 8] [1 8] [2 8]
                 [3 8] [4 8] [6 8] [7 8] [8 8] [9 8] [10 8] [1 10] [4 10] [6 10]
                 [7 10] [4 11] [1 12] [4 12] [8 12] [10 12] [1 13] [8 13] [10 13]
                 [1 14] [8 14] [9 14] [10 14]]
    :sliding [[5 4] [4 5] [4 9] [6 9] [3 10] [5 10] [8 10] [8 11] [9 11] [3 14]]
    :light [[3 1] [4 1] [5 1] [5 5] [7 3] [4 4] [7 4] [7 5] [2 10] [2 11] [2 12]
            [5 12] [4 13] [5 13] [4 14] [5 14] [4 15] [5 15]]
    :goal [0 15]}))


(defn level2 [game-area]
  (let [grid (grid/Grid. 12 10 game-area)]
    {:grid grid
     :player (character/Character. (pos/Position. 5 9) grid)
     :blocks [(stationary/Stationary. (pos/Position. 5 8) grid)
              (sliding/Sliding. (pos/Position. 3 1) grid)
              (sliding/Sliding. (pos/Position. 4 7) grid)]
     :goal (goal/Goal. (pos/Position. 1 2) grid)}))

(def levels [tut0 tut1 level0 level1 level2])

(defn get-level [num game-area]
  ((nth levels num) game-area))

(defn get-at [level pos]
  (let [same-pos? (comp (partial = pos) :pos)
        {:keys [blocks goal]} level
        blocks-at (filter same-pos? blocks)
        goal-at (if (same-pos? goal) goal nil)
        m {:blocks blocks-at :goal goal-at}]
    (into {} (remove (comp empty? val) m))))
