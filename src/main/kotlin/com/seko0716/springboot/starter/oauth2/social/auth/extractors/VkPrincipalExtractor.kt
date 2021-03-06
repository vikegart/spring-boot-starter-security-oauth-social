package com.seko0716.springboot.starter.oauth2.social.auth.extractors

import com.seko0716.springboot.starter.oauth2.social.domains.Role
import com.seko0716.springboot.starter.oauth2.social.domains.User
import com.seko0716.springboot.starter.oauth2.social.infrastructure.extension.createIfNull
import com.seko0716.springboot.starter.oauth2.social.infrastructure.properties.VkProperties
import com.seko0716.springboot.starter.oauth2.social.repository.IUserStorage
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor

class VkPrincipalExtractor(var userStorage: IUserStorage, var vk: VkProperties, var OAuth2UserService: OAuth2UserService) : PrincipalExtractor {
    private val log: Logger = LoggerFactory.getLogger(this::class.java)
    private val authServiceType = "VK"

    override fun extractPrincipal(map: MutableMap<String, Any>): Any {
        log.trace("credential map: {}", map)
        map["_authServiceType"] = authServiceType
        val result = OAuth2UserService.getDetails(map)
        val socialAccountId = result[vk.idField]
        val user = userStorage
                .findOneBySocialAccountId(socialAccountId!!)
                .createIfNull {
                    log.debug("user with social account id {} not found", socialAccountId)
                    var userT = User(login = result[vk.loginField]!!,
                            socialAccountId = socialAccountId,
                            email = result[vk.emailField],
                            firstName = result[vk.firstNameField],
                            lastName = result[vk.lastNameField],
                            roles = vk.defaultRoles.map { Role(name = it) },
                            authServiceType = authServiceType)
                    userT = userStorage.save(userT)
                    log.debug("user be created {}", userT)
                    return userT
                }
        log.trace("user with social account id {} exist {}", socialAccountId, user)
        return user
    }
}