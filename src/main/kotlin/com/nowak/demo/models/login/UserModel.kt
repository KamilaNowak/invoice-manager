package com.nowak.demo.models.login

import com.nowak.demo.models.login.User
import tornadofx.*

class UserModel : ItemViewModel<User>(){
    val id = bind{
        item?.idProperty
    }
    val username = bind{
        item?.userNameProperty
    }
    val password = bind{
        item?.passwordProperty
    }
    val birthDate = bind{
        item?.birthDateProperty
    }
    val email = bind{
        item?.emailProperty
    }
}