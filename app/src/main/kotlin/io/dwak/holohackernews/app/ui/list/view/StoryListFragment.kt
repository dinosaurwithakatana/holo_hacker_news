package io.dwak.holohackernews.app.ui.list.view

import android.content.Context
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.dwak.holohackernews.app.R
import io.dwak.holohackernews.app.base.mvp.MvpFragment
import io.dwak.holohackernews.app.butterknife.bindView
import io.dwak.holohackernews.app.dagger.component.DaggerInteractorComponent
import io.dwak.holohackernews.app.dagger.component.DaggerPresenterComponent
import io.dwak.holohackernews.app.dagger.module.InteractorModule
import io.dwak.holohackernews.app.dagger.module.PresenterModule
import io.dwak.holohackernews.app.model.Feed
import io.dwak.holohackernews.app.model.json.StoryJson
import io.dwak.holohackernews.app.ui.list.presenter.StoryListPresenter
import rx.android.schedulers.AndroidSchedulers

class StoryListFragment : MvpFragment<StoryListPresenter>(), StoryListView {
    val storyList : RecyclerView by bindView(R.id.story_list)
    var adapter : StoryListAdapter? = null
    var interactionListener : StoryListInteractionListener? = null

    companion object {
        val FEED_ARG = "FEED"
        fun newInstance(feed : Feed) : StoryListFragment {
            val args = Bundle()
            args.putSerializable(FEED_ARG, feed)
            val frag = StoryListFragment()
            frag.arguments = args
            return frag
        }
    }

    override fun inject() {
        DaggerPresenterComponent.builder()
                .presenterModule(PresenterModule(this))
                .interactorComponent(DaggerInteractorComponent.builder()
                        .interactorModule(InteractorModule(activity))
                        .build())
                .build()
                .inject(this);
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if(activity is StoryListInteractionListener){
            interactionListener = activity as StoryListInteractionListener
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.feed = arguments.getSerializable(FEED_ARG) as Feed
    }

    override fun onCreateView(inflater : LayoutInflater?, container : ViewGroup?, savedInstanceState : Bundle?) : View? {
        return inflater!!.inflate(R.layout.fragment_storylist_list, container, false)
    }

    override fun onViewCreated(view : View?, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = StoryListAdapter(activity)
        adapter?.itemClicks?.observeOn(AndroidSchedulers.mainThread())?.subscribe { presenter.storyClicked(it) }
        storyList.adapter = adapter
        storyList.layoutManager = LinearLayoutManager(activity)
    }

    override fun displayStories(@StringRes titleRes: Int,
                                storyList: List<StoryJson>) {
        activity.setTitle(titleRes)
        storyList.forEach { adapter?.addStory(it) }
    }

    override fun navigateToStoryDetail(itemId : Long?) {
        interactionListener?.navigateToStoryDetail(itemId)
    }

    interface StoryListInteractionListener {
        fun navigateToStoryDetail(itemId: Long?)
    }
}