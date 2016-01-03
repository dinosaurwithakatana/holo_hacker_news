package io.dwak.holohackernews.app.base.mvp

import io.dwak.holohackernews.app.base.mvp.dagger.DaggerPresenter
import io.dwak.holohackernews.app.dagger.component.InteractorComponent
import rx.subscriptions.CompositeSubscription

public abstract class AbstractPresenter<T : PresenterView>(protected val view : T,
                                                           protected val interactorComponent: InteractorComponent)
: DaggerPresenter {
    val viewSubscription = CompositeSubscription()

    abstract override fun inject()

    init {
        inject()
    }
}