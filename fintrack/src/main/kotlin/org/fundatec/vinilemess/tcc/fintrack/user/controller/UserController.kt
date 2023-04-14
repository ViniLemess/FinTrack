package org.fundatec.vinilemess.tcc.fintrack.user.controller

import org.fundatec.vinilemess.tcc.fintrack.user.domain.User
import org.fundatec.vinilemess.tcc.fintrack.user.domain.request.UserRequest
import org.fundatec.vinilemess.tcc.fintrack.user.domain.response.UserResponse
import org.fundatec.vinilemess.tcc.fintrack.user.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserController(private val userService: UserService) {

    @PostMapping
    fun registerUser(@RequestBody userRequest: UserRequest): ResponseEntity<UserResponse> {
        userRequest.validateRequest()
        return ResponseEntity.ok(userService.registerUser(userRequest).buildResponse())
    }

    private fun User.buildResponse() = UserResponse(this.name, this.email, this.userSignature)
}