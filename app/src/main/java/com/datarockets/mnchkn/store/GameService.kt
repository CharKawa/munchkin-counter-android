package com.datarockets.mnchkn.store

import com.datarockets.mnchkn.models.GameStep
import com.datarockets.mnchkn.models.Player

interface GameService {
    fun insertStep(player: Player)
    fun clearSteps()
    val isGameStarted: Boolean
    fun createPlayerIdGameStepsMap()
    fun setGameStatus(gameStatus: Boolean)
    val scoresChartData: Map<Player, List<GameStep>>
}
