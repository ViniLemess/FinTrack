package org.fundatec.vinilemess.fintrack.security.controller

import org.fundatec.vinilemess.fintrack.security.contract.request.LoginRequest
import org.fundatec.vinilemess.fintrack.security.contract.response.LoginResponse
import org.fundatec.vinilemess.fintrack.security.service.LoginService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/login")
class LoginController(private val loginService: LoginService) {

    @PostMapping
    fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<LoginResponse> {
        return ResponseEntity.ok(loginService.authenticateUser(loginRequest))
    }
}