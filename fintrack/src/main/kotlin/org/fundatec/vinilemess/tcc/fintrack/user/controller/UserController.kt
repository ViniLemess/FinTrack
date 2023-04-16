package org.fundatec.vinilemess.tcc.fintrack.user.controller

import org.fundatec.vinilemess.tcc.fintrack.user.domain.request.LoginRequest
import org.fundatec.vinilemess.tcc.fintrack.user.domain.request.UserRequest
import org.fundatec.vinilemess.tcc.fintrack.user.domain.response.UserResponse
import org.fundatec.vinilemess.tcc.fintrack.user.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserController(private val userService: UserService) {

    @PostMapping
    fun registerUser(@RequestBody userRequest: UserRequest): ResponseEntity<UserResponse> {
        userRequest.validateRequest()
        return ResponseEntity.ok(userService.registerUser(userRequest))
    }

    @GetMapping("login")
    fun login(@RequestParam("email") email: String, @RequestParam("password") password: String): ResponseEntity<UserResponse> {
        val loginRequest = LoginRequest(email, password)
        loginRequest.validateRequest()
        return ResponseEntity.ok(userService.login(loginRequest))
    }
}