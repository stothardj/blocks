(ns blocks.core
  (:require [play-cljs.core :as p]
            [goog.events :as events]
            [blocks.grid :as grid]
            [blocks.rect :as rect]
            [blocks.block :as block]
            [blocks.goal :as goal]
            [blocks.character :as character]
            [blocks.position :as pos]
            [blocks.drawable :as drawable]
            [blocks.levels :as levels]
            [blocks.direction :as direction]))

(defonce border-size 20)

(defonce game (p/create-game (.-innerWidth js/window) (.-innerHeight js/window)))
(defonce state (atom {}))

(defn log [x]
  (.log js/console (clj->js x)))

(defn get-level [num]
  (let [width (.-innerWidth js/window)
        height (.-innerHeight js/window)
        game-width (- width (* 2 border-size))
        game-height (- height (* 2 border-size))
        game-area (rect/Rect. border-size border-size game-width game-height)]
    (levels/get-level num game-area)))

(defn next-level [state]
  (let [lvlnum (:lvlnum state)
        num (+ lvlnum 1)]
    {:level (get-level num)
     :lvlnum num}))

(def main-screen
  (reify p/Screen
    (on-show [this]
      (reset! state {:level (get-level 0)
                     :lvlnum 0}))
    (on-hide [this])
    (on-render [this]
      (let [width (.-innerWidth js/window)
            height (.-innerHeight js/window)
            level (:level @state)
            {:keys [grid goal player blocks]} level]
        (p/render game
                  [[:fill {:color "#f90"}
                    [:rect {:x 0 :y 0 :width width :height height}]]
                   [:fill {:color "#fff"}
                    [:rect (:bounds grid)]]
                   (grid/draw grid)
                   (goal/draw goal)
                   (character/draw player)
                   (for [b blocks]
                     (drawable/draw b))
                   ])))))

(events/listen js/window "resize"
  (fn [event]
    (p/set-size game js/window.innerWidth js/window.innerHeight)))

(defn push-blocks [blocks pos dir]
  (let [same-pos? (partial = pos)
        split-blocks (group-by (comp same-pos? :pos) blocks)
        block (get split-blocks true)
        other-blocks (get split-blocks false)]
    (when block
      (block/push (first block) dir other-blocks))))

(defn try-move [dir state]
  (let [{:keys [level lvlnum]} state
        {:keys [player blocks]} level
        new-player (character/move player dir)
        new-pos (:pos new-player)
        at-pos (levels/get-at level new-pos)]
    (cond
      (:blocks at-pos)
      (let [new-blocks (push-blocks blocks new-pos dir)]
        (cond
          (and new-blocks (:goal at-pos))
          (next-level state)

          new-blocks
          (-> state
              (assoc-in [:level :player] new-player)
              (assoc-in [:level :blocks] new-blocks))

          :else
          state))

      (:goal at-pos)
      (next-level state)
      
      :else
      (assoc-in state [:level :player] new-player))))

(events/listen js/window "keydown"
               (fn [event]
                 (case (.-key event)
                   "ArrowUp" (swap! state (partial try-move direction/up))
                   "ArrowDown" (swap! state (partial try-move direction/down))
                   "ArrowLeft" (swap! state (partial try-move direction/left))
                   "ArrowRight" (swap! state (partial try-move direction/right))
                   nil
                   )
                 ))

(doto game
  (p/start)
  (p/set-screen main-screen))
