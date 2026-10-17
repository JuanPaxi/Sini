package com.juanpaxi.sini.core.network.demo

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Test

class NetworkMovieDetailTest : SiniNetworkTestBase() {
    @Test
    fun getMovieDetail_returnsMovieDetail() =
        runTest {
            enqueueResponse("detail/969681.json")
            val response = service.getMovieDetail(969681, "en-US")
            assertThat(response.id).isEqualTo(969681)
            assertThat(response.title).isEqualTo("Spider-Man: Brand New Day")
        }

    @Test
    fun getMovieDetail_sendsExpectedRequest() =
        runTest {
            enqueueResponse("detail/969681.json")
            service.getMovieDetail(969681, "en-US")
            val request = mockWebServer.takeRequest()
            assertThat(request.method).isEqualTo("GET")
            assertThat(request.url.encodedPath).isEqualTo("/movie/969681")
            assertThat(request.url.queryParameter("language")).isEqualTo("en-US")
        }
}
