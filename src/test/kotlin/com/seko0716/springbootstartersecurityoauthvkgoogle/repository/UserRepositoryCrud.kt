package com.seko0716.springbootstartersecurityoauthvkgoogle.repository

import com.seko0716.springbootstartersecurityoauthvkgoogle.configurations.conditionals.MissingOtherImplementationCondition
import com.seko0716.springbootstartersecurityoauthvkgoogle.domains.User
import org.bson.types.ObjectId
import org.springframework.context.annotation.Conditional
import org.springframework.context.annotation.Primary
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
@Conditional(MissingOtherImplementationCondition::class)
interface UserRepositoryCrud : CrudRepository<User, ObjectId>, UserRepository