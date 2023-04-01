//package sk.esten.uss.gbco2.config
//
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.security.config.Customizer
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
//import org.springframework.security.config.annotation.web.builders.HttpSecurity
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
//import org.springframework.security.config.http.SessionCreationPolicy
//import org.springframework.security.web.SecurityFilterChain
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
//import org.springframework.web.cors.CorsConfiguration
//import org.springframework.web.cors.CorsConfigurationSource
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource
//import sk.esten.uss.gbco2.config.jwt.Gbco2PrincipalJwtConvertor
//import sk.esten.uss.gbco2.config.jwt.JwtAuthenticationEntryPoint
//import sk.esten.uss.gbco2.config.tracing.TraceIdFilter
//import sk.esten.uss.gbco2.properties.ApplicationProperties
//import sk.esten.uss.gbco2.service.UserService
//import sk.esten.uss.gbco2.utils.logger
//
//@Configuration
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
//class SecurityConfiguration(
//    val jwtAuthenticationEntryPoint: JwtAuthenticationEntryPoint,
//    val userService: UserService
//) {
//
//    @Bean
//    @Throws(Exception::class)
//    fun filterChain(http: HttpSecurity): SecurityFilterChain {
//        return http.cors(Customizer.withDefaults())
//            .csrf()
//            .disable()
//            .addFilterBefore(TraceIdFilter(), UsernamePasswordAuthenticationFilter::class.java)
//            .authorizeRequests()
//            .antMatchers(
//                "/",
//                "/public/**",
//                "/version",
//                "/oauth2/**",
//                "/actuator/**",
//                "/swagger-ui/**",
//                "/api-docs/**",
//                "/api/api-docs/**"
//            )
//            .permitAll()
//            .anyRequest()
//            .authenticated()
//            .and()
//            .sessionManagement()
//            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//            .and()
//            .exceptionHandling()
//            .authenticationEntryPoint(jwtAuthenticationEntryPoint)
//            .and()
//            .oauth2ResourceServer { rc ->
//                rc.jwt { jwtc ->
//                    jwtc.jwtAuthenticationConverter(Gbco2PrincipalJwtConvertor(userService))
//                }
//            }
//            .build()
//    }
//
//    @Bean
//    fun corsConfigurationSource(
//        applicationProperties: ApplicationProperties
//    ): CorsConfigurationSource {
//        val configuration = CorsConfiguration()
//        configuration.allowedOrigins = applicationProperties.allowedCorsOrigins
//        logger()
//            .info(
//                "Set ${applicationProperties.allowedCorsOrigins} as allowed CORS origins for the incoming requests..."
//            )
//        configuration.allowedMethods = listOf("GET", "POST", "DELETE", "PUT", "PATCH")
//        configuration.allowedHeaders = listOf("*")
//        configuration.allowCredentials = true
//        val source = UrlBasedCorsConfigurationSource()
//        source.registerCorsConfiguration("/**", configuration)
//        return source
//    }
//}
