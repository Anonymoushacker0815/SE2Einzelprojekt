package at.aau.serg.controllers

import at.aau.serg.models.GameResult
import at.aau.serg.services.GameResultService
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import kotlin.test.Test
import kotlin.test.assertEquals
import org.mockito.Mockito.`when` as whenever // when is a reserved keyword in Kotlin

class LeaderboardControllerTests {

    private lateinit var mockedService: GameResultService
    private lateinit var controller: LeaderboardController

    @BeforeEach
    fun setup() {
        mockedService = mock<GameResultService>()
        controller = LeaderboardController(mockedService)
    }

    @Test
    fun test_getLeaderboard_correctScoreSorting() {
        val first = GameResult(1, "first", 20, 20.0)
        val second = GameResult(2, "second", 15, 10.0)
        val third = GameResult(3, "third", 10, 15.0)

        whenever(mockedService.getGameResults()).thenReturn(listOf(second, first, third))

        val res: List<GameResult> = controller.getLeaderboard(null) // null needed after rank param

        verify(mockedService).getGameResults()
        assertEquals(3, res.size)
        assertEquals(first, res[0])
        assertEquals(second, res[1])
        assertEquals(third, res[2])
    }

    @Test
    fun test_getLeaderboard_sameScore_CorrectTimeSorting() { // changed name ID -> Time
        val first = GameResult(1, "first", 20, 20.0)
        val second = GameResult(2, "second", 20, 10.0)
        val third = GameResult(3, "third", 20, 15.0)

        whenever(mockedService.getGameResults()).thenReturn(listOf(second, first, third))

        val res: List<GameResult> = controller.getLeaderboard(null) // null needed after rank param

        verify(mockedService).getGameResults()
        assertEquals(3, res.size)
        assertEquals(second, res[0]) //Changed order
        assertEquals(third, res[1])
        assertEquals(first, res[2])
    }

    @Test
    fun test_getLeaderboard_withRank_returnsSurroundingRanks() {
        val p1 = GameResult(1, "p1", 100, 10.0)
        val p2 = GameResult(2, "p2", 90, 10.0)
        val p3 = GameResult(3, "p3", 80, 10.0)
        val p4 = GameResult(4, "p4", 70, 10.0)
        val p5 = GameResult(5, "p5", 60, 10.0)
        val p6 = GameResult(6, "p6", 50, 10.0)
        val p7 = GameResult(7, "p7", 40, 10.0)
        val p8 = GameResult(8, "p8", 30, 10.0)

        whenever(mockedService.getGameResults()).thenReturn(listOf(p8, p4, p2, p6, p1, p7, p3, p5))

        val res: List<GameResult> = controller.getLeaderboard(4)

        verify(mockedService).getGameResults()
        assertEquals(7, res.size)
        assertEquals(p1, res[0])
        assertEquals(p2, res[1])
        assertEquals(p3, res[2])
        assertEquals(p4, res[3])
        assertEquals(p5, res[4])
        assertEquals(p6, res[5])
        assertEquals(p7, res[6])
    }

    @Test
    fun test_getLeaderboard_withRankAtStart_returnsOnlyFollowingRanks() {
        val p1 = GameResult(1, "p1", 100, 10.0)
        val p2 = GameResult(2, "p2", 90, 10.0)
        val p3 = GameResult(3, "p3", 80, 10.0)
        val p4 = GameResult(4, "p4", 70, 10.0)
        val p5 = GameResult(5, "p5", 60, 10.0)

        whenever(mockedService.getGameResults()).thenReturn(listOf(p5, p3, p1, p4, p2))

        val res: List<GameResult> = controller.getLeaderboard(1)

        verify(mockedService).getGameResults()
        assertEquals(4, res.size)
        assertEquals(p1, res[0])
        assertEquals(p2, res[1])
        assertEquals(p3, res[2])
        assertEquals(p4, res[3])
    }

    @Test
    fun test_getLeaderboard_withRankTooSmall_throwsException() {
        whenever(mockedService.getGameResults()).thenReturn(emptyList())

        kotlin.test.assertFailsWith<LeaderboardController.InvalidRankException> {
            controller.getLeaderboard(0)
        }

        verify(mockedService).getGameResults()
    }

    @Test
    fun test_getLeaderboard_withRankTooLarge_throwsException() {
        val p1 = GameResult(1, "p1", 100, 10.0)

        whenever(mockedService.getGameResults()).thenReturn(listOf(p1))

        kotlin.test.assertFailsWith<LeaderboardController.InvalidRankException> {
            controller.getLeaderboard(2)
        }

        verify(mockedService).getGameResults()
    }
}