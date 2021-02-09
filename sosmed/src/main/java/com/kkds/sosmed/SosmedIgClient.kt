package com.kkds.sosmedlibs

import android.content.Context
import android.util.Log
import com.sanardev.instagramapijava.IGConstants
import com.sanardev.instagramapijava.InstaClient
import com.sanardev.instagramapijava.app.Cookie
import com.sanardev.instagramapijava.model.timeline.FeedItems
import com.sanardev.instagramapijava.processor.*
import com.sanardev.instagramapijava.request.IGLoginRequest
import com.sanardev.instagramapijava.response.IGLoginResponse
import com.sanardev.instagramapijava.response.IGTimeLinePostsResponse
import com.sanardev.instagramapijava.response.IGUserInfoResponse
import com.sanardev.instagramapijava.utils.InstaHashUtils
import com.sanardev.instagramapijava.utils.StorageUtils
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import java.lang.reflect.Type
import java.net.URLDecoder


public class SosmedIgClient {



    lateinit var instaClient: InstaClient

    fun SosmedIgClient(context: Context, username: String, password: String){
        instaClient = InstaClient(context, username, password)
    }

    fun login(): Observable<IGLoginResponse> {
        return instaClient.accountProcessor.login()
    }

    fun getUserInfoByUsername(username: String) :Observable<IGUserInfoResponse>{
        return instaClient.userProcessor.getUserInfoByUsername(username)
    }

    fun getTimelinePosts(): Observable<IGTimeLinePostsResponse>{
        return  instaClient.mediaProcessor.timelinePosts
    }

    fun getMoreTimeline(maxId: String): Observable<IGTimeLinePostsResponse>{
        return instaClient.mediaProcessor.getTimelinePosts(maxId)
    }

    fun convertTimelineToInstagramFeed(listTimelineFromAPI: List<FeedItems>, nextMaxId: String) : ArrayList<InstagramFeed>{

        var listFeed = ArrayList<InstagramFeed>()
        listFeed.clear()
        listTimelineFromAPI
            .forEach {

                if(it.mediaOrAd == null) return@forEach

                Log.v("CRX", "Fetch Position : " + listTimelineFromAPI.indexOf(it))
                var newFeed =
                    InstagramFeed(
                        id = it.mediaOrAd.id,
                        pk = it.mediaOrAd.pk,
                        username = it.mediaOrAd.user.username,
                        profilePic = URLDecoder.decode(it.mediaOrAd.user.profilePicUrl, "UTF-8"),
                        caption = it.mediaOrAd.caption?.text,
                        url = if (it.mediaOrAd.carouselMediaCount > 0) it.mediaOrAd.carouselMedias.first().imageVersions2.candidates.first().url else URLDecoder.decode(
                            it.mediaOrAd.imageVersions2.candidates.last().url,
                            "UTF-8" ),
                        devTime = it.mediaOrAd.deviceTimestamp,
                        height = it.mediaOrAd.originalHeight,
                        width = it.mediaOrAd.originalHeight,
                        likeCount = it.mediaOrAd.likeCount
                    )

                listFeed.add(newFeed)
                Log.v("CRX", "Size Feed " + listFeed.size)

            }

        return listFeed
    }


}