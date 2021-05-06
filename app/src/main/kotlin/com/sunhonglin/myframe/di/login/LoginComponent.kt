package com.sunhonglin.myframe.di.login

import com.sunhonglin.core.di.BaseActivityComponent
import com.sunhonglin.core.di.CoreComponent
import com.sunhonglin.core.di.scope.FeatureScope
import com.sunhonglin.myframe.ui.login.LoginActivity
import dagger.BindsInstance
import dagger.Component

@Component(
    modules = [LoginModule::class],
    dependencies = [CoreComponent::class]
)
@FeatureScope
interface LoginComponent: BaseActivityComponent<LoginActivity> {

    @Component.Builder
    interface Builder {
        fun build(): LoginComponent
        @BindsInstance
        fun loginActivity(activity: LoginActivity): Builder
        fun coreComponent(component: CoreComponent): Builder
    }
}