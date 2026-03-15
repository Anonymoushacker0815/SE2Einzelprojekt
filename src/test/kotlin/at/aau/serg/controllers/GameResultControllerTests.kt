package at.aau.serg.controllers

import at.aau.serg.models.GameResult
import at.aau.serg.services.GameResultService
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import org.mockito.Mockito.`when` as whenever

class GameResultControllerTests {

    private lateinit var mockedService: GameResultService
    private lateinit var controller: GameResultController

    @BeforeEach
    fun setup() {
        mockedService = mock<GameResultService>()
        controller = GameResultController(mockedService)
    }

    @Test
    fun test_getGameResult_existingId_returnsGameResult() {
        val gameResult = GameResult(1, "first", 20, 10.0)

        whenever(mockedService.getGameResult(1)).thenReturn(gameResult)

        val res: GameResult? = controller.getGameResult(1)

        verify(mockedService).getGameResult(1)
        assertEquals(gameResult, res)
    }

    @Test
    fun test_getGameResult_unknownId_returnsNull() {
        whenever(mockedService.getGameResult(2)).thenReturn(null)

        val res: GameResult? = controller.getGameResult(2)

        verify(mockedService).getGameResult(2)
        assertNull(res)
    }

    @Test
    fun test_getAllGameResults_returnsAllGameResults() {
        val first = GameResult(1, "first", 1, 1.0)
        val second = GameResult(2, "second", 1, 2.0)
        val third = GameResult(3, "third", 1, 3.0)

        whenever(mockedService.getGameResults()).thenReturn(listOf(first, second, third))

        val res: List<GameResult> = controller.getAllGameResults()

        verify(mockedService).getGameResults()
        assertEquals(3, res.size)
        assertEquals(first, res[0])
        assertEquals(second, res[1])
        assertEquals(third, res[2])
    }

    @Test
    fun test_addGameResult_callsService() {
        val gameResult = GameResult(0, "first", 1, 1.0)

        controller.addGameResult(gameResult)

        verify(mockedService).addGameResult(gameResult)
    }

    @Test
    fun test_deleteGameResult_callsService() {
        controller.deleteGameResult(1)

        verify(mockedService).deleteGameResult(1)
    }
}