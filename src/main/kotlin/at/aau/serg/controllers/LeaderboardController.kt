package at.aau.serg.controllers

import at.aau.serg.models.GameResult
import at.aau.serg.services.GameResultService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestParam // New import for parameter
import org.springframework.web.bind.annotation.ResponseStatus // New import for bad request

@RestController
@RequestMapping("/leaderboard")
class LeaderboardController(
    private val gameResultService: GameResultService
) {
    @ResponseStatus(org.springframework.http.HttpStatus.BAD_REQUEST)
    class InvalidRankException : RuntimeException()

    @GetMapping
    fun getLeaderboard(@RequestParam(required = false) rank: Int?): List<GameResult> { //changed .id -> .tIS
        val sortedLeaderboard = gameResultService.getGameResults().sortedWith(compareBy({ -it.score }, { it.timeInSeconds }))

        if (rank == null) return sortedLeaderboard
        if (rank < 1 || rank > sortedLeaderboard.size) throw InvalidRankException()

        val targetIndex = rank - 1
        val from = maxOf(0, targetIndex - 3)
        val to = minOf(sortedLeaderboard.size, targetIndex + 4)

        return sortedLeaderboard.subList(from, to)
    }

}