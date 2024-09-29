package org.fundatec.vinilemess.fintrack.user

import org.fundatec.vinilemess.fintrack.user.contract.request.UserRequest
import org.fundatec.vinilemess.fintrack.user.contract.response.UserResponse
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserController(private val userService: UserService) {

    @PostMapping
    @PreAuthorize("hasAnyRole('USER')")
    fun registerUser(@RequestBody userRequest: UserRequest): ResponseEntity<UserResponse> {
        return ResponseEntity.ok(userService.registerUser(userRequest))
    }
}