package com.juanpaxi.sini.core.network.demo

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Test
import retrofit2.HttpException

class NetworkMovieTest : SiniNetworkTestBase() {
    @Test
    fun getPopularMovies_returnsPopularMovies() =
        runTest {
            enqueueResponse("popular/page_1.json")
            val response = service.getPopularMovies(1, "en-US")
            assertThat(response.page).isEqualTo(1)
            assertThat(response.results).hasSize(20)
            assertThat(response.results[0].id).isEqualTo(969681)
            assertThat(response.results[0].title)
                .isEqualTo("Spider-Man: Brand New Day")
        }

    @Test
    fun getPopularMovies_throwsHttpExceptionOnHttpError() =
        runTest {
            enqueueHttpError(401)
            val result =
                runCatching {
                    service.getPopularMovies(1, "en-US")
                }
            assertThat(result.exceptionOrNull())
                .isInstanceOf(HttpException::class.java)
            val exception = result.exceptionOrNull() as HttpException
            assertThat(exception.code()).isEqualTo(401)
        }

    @Test
    fun getPopularMovies_sendsExpectedRequest() =
        runTest {
            enqueueResponse("popular/page_1.json")
            service.getPopularMovies(1, "en-US")
            val request = mockWebServer.takeRequest()
            assertThat(request.method).isEqualTo("GET")
            assertThat(request.url.encodedPath).isEqualTo("/movie/popular")
            assertThat(request.url.queryParameter("page")).isEqualTo("1")
            assertThat(request.url.queryParameter("language")).isEqualTo("en-US")
        }
}
