package com.juanpaxi.sini.core.navigation

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test

private object TestFirstTopLevelKey : NavKey

private object TestSecondTopLevelKey : NavKey

private object TestThirdTopLevelKey : NavKey

private object TestKeyFirst : NavKey

private object TestKeySecond : NavKey

class NavigatorTest {
    private lateinit var navigationState: NavigationState
    private lateinit var navigator: Navigator

    @Before
    fun setup() {
        val startKey = TestFirstTopLevelKey
        val topLevelStack = NavBackStack<NavKey>(startKey)
        val topLevelKeys =
            listOf(
                startKey,
                TestSecondTopLevelKey,
                TestThirdTopLevelKey,
            )
        val subStacks = topLevelKeys.associateWith { key -> NavBackStack(key) }

        navigationState =
            NavigationState(
                startKey = startKey,
                topLevelStack = topLevelStack,
                subStacks = subStacks,
            )
        navigator = Navigator(navigationState)
    }

    @Test
    fun testStartKey() {
        assertEquals(TestFirstTopLevelKey, navigationState.startKey)
        assertEquals(TestFirstTopLevelKey, navigationState.currentTopLevelKey)
    }

    @Test
    fun testNavigate() {
        navigator.navigate(TestKeyFirst)
        assertEquals(TestFirstTopLevelKey, navigationState.currentTopLevelKey)
        assertEquals(TestKeyFirst, navigationState.subStacks[TestFirstTopLevelKey]?.last())
    }

    @Test
    fun testNavigateTopLevel() {
        navigator.navigate(TestSecondTopLevelKey)
        assertEquals(TestSecondTopLevelKey, navigationState.currentTopLevelKey)
    }

    @Test
    fun testNavigateSingleTop() {
        navigator.navigate(TestKeyFirst)
        assertEquals(2, navigationState.currentSubStack.size)
        assertEquals(TestFirstTopLevelKey, navigationState.currentSubStack[0])
        assertEquals(TestKeyFirst, navigationState.currentSubStack[1])

        navigator.navigate(TestKeyFirst)
        assertEquals(2, navigationState.currentSubStack.size)
        assertEquals(TestFirstTopLevelKey, navigationState.currentSubStack[0])
        assertEquals(TestKeyFirst, navigationState.currentSubStack[1])
    }

    @Test
    fun testNavigateTopLevelSingleTop() {
        navigator.navigate(TestSecondTopLevelKey)
        navigator.navigate(TestKeyFirst)

        assertEquals(2, navigationState.currentSubStack.size)
        assertEquals(TestSecondTopLevelKey, navigationState.currentSubStack[0])
        assertEquals(TestKeyFirst, navigationState.currentSubStack[1])

        navigator.navigate(TestSecondTopLevelKey)
        assertEquals(2, navigationState.currentSubStack.size)
        assertEquals(TestSecondTopLevelKey, navigationState.currentSubStack[0])
    }

    @Test
    fun testSubStack() {
        navigator.navigate(TestKeyFirst)
        assertEquals(TestKeyFirst, navigationState.currentKey)
        assertEquals(TestFirstTopLevelKey, navigationState.currentTopLevelKey)

        navigator.navigate(TestKeySecond)
        assertEquals(TestKeySecond, navigationState.currentKey)
        assertEquals(TestFirstTopLevelKey, navigationState.currentTopLevelKey)
    }

    @Test
    fun testMultiStack() {
        navigator.navigate(TestKeyFirst)
        assertEquals(TestKeyFirst, navigationState.currentKey)
        assertEquals(TestFirstTopLevelKey, navigationState.currentTopLevelKey)

        navigator.navigate(TestSecondTopLevelKey)
        assertEquals(TestSecondTopLevelKey, navigationState.currentKey)
        assertEquals(TestSecondTopLevelKey, navigationState.currentTopLevelKey)

        navigator.navigate(TestKeySecond)
        assertEquals(TestKeySecond, navigationState.currentKey)
        assertEquals(TestSecondTopLevelKey, navigationState.currentTopLevelKey)

        navigator.navigate(TestFirstTopLevelKey)
        assertEquals(TestKeyFirst, navigationState.currentKey)
        assertEquals(TestFirstTopLevelKey, navigationState.currentTopLevelKey)
    }

    @Test
    fun testPopOneNonTopLevel() {
        navigator.navigate(TestKeyFirst)
        navigator.navigate(TestKeySecond)

        assertEquals(3, navigationState.currentSubStack.size)
        assertEquals(TestFirstTopLevelKey, navigationState.currentSubStack[0])
        assertEquals(TestKeyFirst, navigationState.currentSubStack[1])
        assertEquals(TestKeySecond, navigationState.currentSubStack[2])

        navigator.goBack()

        assertEquals(2, navigationState.currentSubStack.size)
        assertEquals(TestFirstTopLevelKey, navigationState.currentSubStack[0])
        assertEquals(TestKeyFirst, navigationState.currentSubStack[1])

        assertEquals(TestKeyFirst, navigationState.currentKey)
        assertEquals(TestFirstTopLevelKey, navigationState.currentTopLevelKey)
    }

    @Test
    fun testPopOneTopLevel() {
        navigator.navigate(TestKeyFirst)
        navigator.navigate(TestSecondTopLevelKey)

        assertEquals(1, navigationState.currentSubStack.size)
        assertEquals(TestSecondTopLevelKey, navigationState.currentSubStack[0])
        assertEquals(TestSecondTopLevelKey, navigationState.currentKey)
        assertEquals(TestSecondTopLevelKey, navigationState.currentTopLevelKey)

        navigator.goBack()
        assertEquals(2, navigationState.currentSubStack.size)
        assertEquals(TestFirstTopLevelKey, navigationState.currentSubStack[0])
        assertEquals(TestKeyFirst, navigationState.currentSubStack[1])
        assertEquals(TestKeyFirst, navigationState.currentKey)
        assertEquals(TestFirstTopLevelKey, navigationState.currentTopLevelKey)
    }

    @Test
    fun popMultipleNonTopLevel() {
        navigator.navigate(TestKeyFirst)
        navigator.navigate(TestKeySecond)
        assertEquals(3, navigationState.currentSubStack.size)
        assertEquals(TestFirstTopLevelKey, navigationState.currentSubStack[0])
        assertEquals(TestKeyFirst, navigationState.currentSubStack[1])
        assertEquals(TestKeySecond, navigationState.currentSubStack[2])

        navigator.goBack()
        navigator.goBack()
        assertEquals(1, navigationState.currentSubStack.size)
        assertEquals(TestFirstTopLevelKey, navigationState.currentSubStack[0])
        assertEquals(TestFirstTopLevelKey, navigationState.currentKey)
        assertEquals(TestFirstTopLevelKey, navigationState.currentTopLevelKey)
    }

    @Test
    fun popMultipleTopLevel() {
        navigator.navigate(TestSecondTopLevelKey)
        navigator.navigate(TestKeyFirst)
        assertEquals(2, navigationState.currentSubStack.size)
        assertEquals(TestSecondTopLevelKey, navigationState.currentSubStack[0])
        assertEquals(TestKeyFirst, navigationState.currentSubStack[1])

        navigator.navigate(TestThirdTopLevelKey)
        navigator.navigate(TestKeySecond)
        assertEquals(2, navigationState.currentSubStack.size)
        assertEquals(TestThirdTopLevelKey, navigationState.currentSubStack[0])
        assertEquals(TestKeySecond, navigationState.currentSubStack[1])

        repeat(4) { navigator.goBack() }
        assertEquals(1, navigationState.currentSubStack.size)
        assertEquals(TestFirstTopLevelKey, navigationState.currentSubStack[0])

        assertEquals(TestFirstTopLevelKey, navigationState.currentKey)
        assertEquals(TestFirstTopLevelKey, navigationState.currentTopLevelKey)
    }

    @Test
    fun throwOnEmptyBackStack() {
        assertThrows(IllegalStateException::class.java) { navigator.goBack() }
    }
}
