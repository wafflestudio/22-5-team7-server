package com.toyProject7.karrot

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserIntegrationTest
    @Autowired
    constructor(
        private val mvc: MockMvc,
        private val mapper: ObjectMapper,
    ) {
        @Test
        fun `회원가입시에 유저 아이디, 비밀번호, 닉네임, 이메일 중 한 가지라도 정해진 규칙에 맞지 않는 경우 Bad Request 응답을 내려준다`() {
            // 잘못된 userId (너무 짧은 경우)
            mvc.perform(
                post("/auth/sign/up")
                    .content(
                        mapper.writeValueAsString(
                            mapOf(
                                "userId" to "Kar",
                                "password" to "KarrotMarket",
                                "nickname" to "친절한 사람",
                                "email" to "Karrot@gmail.com",
                            ),
                        ),
                    )
                    .contentType(MediaType.APPLICATION_JSON),
            )
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.error").value("Bad userId, User ID must be 5-20 characters"))

            // 잘못된 password (너무 짧은 경우)
            mvc.perform(
                post("/auth/sign/up")
                    .content(
                        mapper.writeValueAsString(
                            mapOf(
                                "userId" to "Karrot",
                                "password" to "Karrot",
                                "nickname" to "당근마켓클론코딩",
                                "email" to "Karrot@gmail.com",
                            ),
                        ),
                    )
                    .contentType(MediaType.APPLICATION_JSON),
            )
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.error").value("Bad password, password must be 8-16 characters"))

            // 잘못된 nickname (너무 긴 경우)
            mvc.perform(
                post("/auth/sign/up")
                    .content(
                        mapper.writeValueAsString(
                            mapOf(
                                "userId" to "Karrot",
                                "password" to "KarrotMarket",
                                "nickname" to "당근마켓클론코딩닉네임이너무길어",
                                "email" to "Karrot@gmail.com",
                            ),
                        ),
                    )
                    .contentType(MediaType.APPLICATION_JSON),
            )
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.error").value("Bad nickname, User ID must be 2-10 characters"))

            // 잘못된 email 형식 (이메일 형식에 맞지 않는 경우)
            mvc.perform(
                post("/auth/sign/up")
                    .content(
                        mapper.writeValueAsString(
                            mapOf(
                                "userId" to "Karrot",
                                "password" to "KarrotMarket",
                                "nickname" to "당근마켓클론코딩",
                                "email" to "Karrotgmail.com",
                            ),
                        ),
                    )
                    .contentType(MediaType.APPLICATION_JSON),
            )
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.error").value("Invalid email, email must be in a valid format"))

            // 회원가입 성공 계정 1
            mvc.perform(
                post("/auth/sign/up")
                    .content(
                        mapper.writeValueAsString(
                            mapOf(
                                "userId" to "Karrot1",
                                "password" to "KarrotMarket1",
                                "nickname" to "당근마켓클론코딩",
                                "email" to "Karrot1@gmail.com",
                            ),
                        ),
                    )
                    .contentType(MediaType.APPLICATION_JSON),
            )
                .andExpect(status().isOk)
        }

        @Test
        fun `회원가입시에 이미 해당 유저 아이디나 닉네임이 존재하면 Conflict 응답을 내려준다`() {
            // 회원가입 성공 계정 2
            mvc.perform(
                post("/auth/sign/up")
                    .content(
                        mapper.writeValueAsString(
                            mapOf(
                                "userId" to "Karrot2",
                                "password" to "KarrotMarket2",
                                "nickname" to "사람들이친절해요",
                                "email" to "Karrot2@gmail.com",
                            ),
                        ),
                    )
                    .contentType(MediaType.APPLICATION_JSON),
            )
                .andExpect(status().isOk)

            // 유저 아이디가 존재하는 경우
            mvc.perform(
                post("/auth/sign/up")
                    .content(
                        mapper.writeValueAsString(
                            mapOf(
                                "userId" to "Karrot2",
                                "password" to "KarrotMarket",
                                "nickname" to "당근마켓클론코딩",
                                "email" to "Karrot1@gmail.com",
                            ),
                        ),
                    )
                    .contentType(MediaType.APPLICATION_JSON),
            )
                .andExpect(status().isConflict)
                .andExpect(jsonPath("$.error").value("UserId conflict"))

            // 유저 닉네임이 존재하는 경우
            mvc.perform(
                post("/auth/sign/up")
                    .content(
                        mapper.writeValueAsString(
                            mapOf(
                                "userId" to "PeopleAreKind",
                                "password" to "KarrotMarket",
                                "nickname" to "사람들이친절해요",
                                "email" to "People@gmail.com",
                            ),
                        ),
                    )
                    .contentType(MediaType.APPLICATION_JSON),
            )
                .andExpect(status().isConflict)
                .andExpect(jsonPath("$.error").value("Nickname conflict"))
        }

        @Test
        fun `로그인 정보가 정확하지 않으면 Unauthorized 응답을 내려준다`() {
            // 회원가입 성공 계정 3
            mvc.perform(
                post("/auth/sign/up")
                    .content(
                        mapper.writeValueAsString(
                            mapOf(
                                "userId" to "Karrot3",
                                "password" to "KarrotMarket3",
                                "nickname" to "내일까지반값입니다",
                                "email" to "Karrot3@gmail.com",
                            ),
                        ),
                    )
                    .contentType(MediaType.APPLICATION_JSON),
            )
                .andExpect(status().isOk)

            // 유저 아이디가 존재하지 않는 경우
            mvc.perform(
                post("/auth/sign/in")
                    .content(
                        mapper.writeValueAsString(
                            mapOf(
                                "userId" to "Karrot",
                                "password" to "KarrotMarket",
                            ),
                        ),
                    )
                    .contentType(MediaType.APPLICATION_JSON),
            )
                .andExpect(status().isUnauthorized)
                .andExpect(jsonPath("$.error").value("User not found"))

            // 유저 아이디가 존재하지만 비밀번호가 틀렸을 경우
            mvc.perform(
                post("/auth/sign/in")
                    .content(
                        mapper.writeValueAsString(
                            mapOf(
                                "userId" to "Karrot3",
                                "password" to "KarrotMarket",
                            ),
                        ),
                    )
                    .contentType(MediaType.APPLICATION_JSON),
            )
                .andExpect(status().isUnauthorized)
                .andExpect(jsonPath("$.error").value("Invalid password"))

            // 계정 3에 로그인 성공
            mvc.perform(
                post("/auth/sign/in")
                    .content(
                        mapper.writeValueAsString(
                            mapOf(
                                "userId" to "Karrot3",
                                "password" to "KarrotMarket3",
                            ),
                        ),
                    )
                    .contentType(MediaType.APPLICATION_JSON),
            )
                .andExpect(status().isOk)
        }

//        @Test
//        fun `잘못된 인증 토큰으로 인증시 Unauthorized 응답을 내려준다`() {
//            // 회원가입 성공 계정 4
//            val (userId, password, nickname) = Triple("Karrot4", "KarrotMarket4", "모두!!얼음!!!")
//            mvc.perform(
//                post("/auth/sign/up")
//                    .content(
//                        mapper.writeValueAsString(
//                            mapOf(
//                                "userId" to userId,
//                                "password" to password,
//                                "nickname" to nickname,
//                                "email" to "Karrot4@gmail.com",
//                            ),
//                        ),
//                    )
//                    .contentType(MediaType.APPLICATION_JSON),
//            )
//                .andExpect(status().isOk)
//
//            val accessToken =
//                mvc.perform(
//                    post("/auth/sign/in")
//                        .content(
//                            mapper.writeValueAsString(
//                                mapOf(
//                                    "userId" to userId,
//                                    "password" to password,
//                                ),
//                            ),
//                        )
//                        .contentType(MediaType.APPLICATION_JSON),
//                )
//                    .andExpect(status().isOk)
//                    .andReturn()
//                    .response.getContentAsString(Charsets.UTF_8)
//                    .let { mapper.readTree(it).get("accessToken").asText() }
//
//            mvc.perform(
//                get("/auth/me")
//                    .header("Authorization", "Bearer Bad"),
//            )
//                .andExpect(status().isUnauthorized)
//                .andExpect(jsonPath("$.error").value("Authenticate failed"))
//
//            mvc.perform(
//                get("/auth/me")
//                    .header("Authorization", "Bearer $accessToken"),
//            )
//                .andExpect(status().isOk)
//                .andExpect(jsonPath("$.id").value(userId))
//                .andExpect(jsonPath("$.nickname").value(nickname))
//        }
    }
