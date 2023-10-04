package dev.axgr

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.treeToValue
import com.github.fge.jsonpatch.JsonPatch
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(private val mapper: ObjectMapper) {

  private val users = mutableMapOf("axgr" to User("axgr", Address("123 Main St", "Anytown")))

  @GetMapping("/{username}")
  fun user(@PathVariable username: String): User {
    return users[username] ?: throw IllegalStateException(username)
  }

  @PatchMapping("/{username}")
  fun patch(@PathVariable username: String, @RequestBody patch: JsonPatch): User {
    val user = users[username] ?: throw IllegalStateException("axgr")
    val tree = mapper.valueToTree<JsonNode>(user)
    val update = patch.apply(tree)
    users[username] = mapper.treeToValue<User>(update)

    return users[username]!!
  }
}

data class Address(val street: String, val city: String)
data class User(val username: String, val address: Address)
