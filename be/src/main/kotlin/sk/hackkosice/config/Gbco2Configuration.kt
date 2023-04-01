package sk.esten.uss.gbco2.config

import io.netty.channel.ChannelOption
import io.netty.handler.timeout.ReadTimeoutHandler
import io.netty.handler.timeout.WriteTimeoutHandler
import java.util.concurrent.TimeUnit
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import reactor.netty.Connection
import reactor.netty.http.client.HttpClient
import reactor.netty.transport.ProxyProvider
import sk.esten.uss.gbco2.config.tracing.TraceIdCleanerInterceptor
import sk.esten.uss.gbco2.utils.logger

@Configuration
class Gbco2Configuration : WebMvcConfigurer {

    @Bean
    fun httpClient(): HttpClient {
        val httpClient =
            HttpClient.create()
                .wiretap(true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, TimeUnit.SECONDS.toMillis(15).toInt())
                .doOnConnected { conn: Connection ->
                    conn.addHandlerLast(ReadTimeoutHandler(15, TimeUnit.SECONDS))
                        .addHandlerLast(WriteTimeoutHandler(15, TimeUnit.SECONDS))
                }

        return when (!System.getProperty("https.proxyHost").isNullOrEmpty() &&
                !System.getProperty("https.proxyPort").isNullOrEmpty()
        ) {
            true ->
                httpClient.proxy {
                    logger()
                        .debug(
                            "'https.proxyHost' and 'https.proxyPort' system properties detected. Using proxy for connection with Azure"
                        )
                    it.type(ProxyProvider.Proxy.HTTP)
                        .host(
                            System.getProperty("https.proxyHost")
                                ?: throw IllegalArgumentException("Proxy host is null")
                        )
                        .port(
                            System.getProperty("https.proxyPort")?.toInt()
                                ?: throw IllegalArgumentException("Proxy port is null")
                        )
                        .username(System.getProperty("https.proxyUser"))
                        .password { System.getProperty("https.proxyPassword") }
                }
            else -> httpClient
        }
    }

    @Bean
    fun reactorClientHttpConnector(httpClient: HttpClient): ReactorClientHttpConnector {
        return ReactorClientHttpConnector(httpClient)
    }

    @Bean
    fun webClient(
        builder: WebClient.Builder,
        reactorClientHttpConnector: ReactorClientHttpConnector
    ): WebClient {
        return builder.clientConnector(reactorClientHttpConnector).build()
    }

    /**
     * [TraceIdCleanerInterceptor] instance
     *
     * @return TraceIdCleanerInterceptor
     */
    @Bean
    fun traceIdCleanerInterceptor(): TraceIdCleanerInterceptor {
        return TraceIdCleanerInterceptor()
    }

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(traceIdCleanerInterceptor())
    }
}
