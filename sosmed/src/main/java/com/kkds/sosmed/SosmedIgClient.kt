package com.kkds.sosmedlibs

import android.content.Context
import android.util.Log
import com.sanardev.instagramapijava.InstaClient
import com.sanardev.instagramapijava.model.timeline.FeedItems
import com.sanardev.instagramapijava.model.timeline.MediaOrAd
import com.sanardev.instagramapijava.model.user.BigUser
import com.sanardev.instagramapijava.processor.*
import com.sanardev.instagramapijava.response.IGLoginResponse
import com.sanardev.instagramapijava.response.IGPostsResponse
import com.sanardev.instagramapijava.response.IGTimeLinePostsResponse
import com.sanardev.instagramapijava.response.IGUserInfoResponse
import io.reactivex.Observable
import java.net.URLDecoder


public class SosmedIgClient {



    lateinit var instaClient: InstaClient

    fun sosmedIgInstance(context: Context, username: String, password: String) : SosmedIgClient{
        this.instaClient = InstaClient(context, username, password)
        return SosmedIgClient()
    }

    fun login(): Observable<IGLoginResponse> {
        return instaClient.accountProcessor.login()
    }

    fun getUserInfoByUsername(username: String) :Observable<IGUserInfoResponse>{
        return instaClient.userProcessor.getUserInfoByUsername(username)
    }

    fun getUserFeed(pk: Long): Observable<IGPostsResponse>{
        return instaClient.userProcessor.getPosts(pk)
    }

    fun reloadMoreDataFeed(pk: Long,id: String): Observable<IGPostsResponse> {
        return instaClient.userProcessor.getMorePosts(pk, id)
    }

    fun saveDataToInstagramFeeds(listPostFromAPI: List<MediaOrAd>, user: BigUser) : ArrayList<InstagramFeed> {
        var listFeed = ArrayList<InstagramFeed>()
        var isCanReaload = false
        var lastId = ""
        var url = URLDecoder.decode(user.hdProfilePicUrlInfo.url, "UTF-8")
        listPostFromAPI
            .forEach {
                Log.v("CRX","Data InstagramFeed "+it)
                if(it.clientCacheKey.isNullOrBlank())it.clientCacheKey = "-"
                var newMedia = ArrayList<MediaFeed>()
                    if(it.carouselMediaCount > 0) {
                         it.carouselMedias.forEach {
                             newMedia.add(MediaFeed(
                                     id = it.id,
                                     mediaType = it.mediaType,
                                     url = it.imageVersions2.candidates.first().url
                             ))
                         }
                        }else{
                            newMedia.add(MediaFeed(
                                    id = it.id,
                                    mediaType = it.mediaType,
                                    url =  URLDecoder.decode(it.imageVersions2.candidates.first().url,"UTF-8")
                            ))
                        }
                var newFeed =
                    InstagramFeed(
                        id = it.id,
                        pk = user.pk,
                        username = user.username,
                        profilePic = url,
                        caption = it.caption?.text,
                        media = newMedia,
                        devTime = it.deviceTimestamp,
                        height = it.originalHeight,
                        width = it.originalHeight,
                        likeCount = it.likeCount
                    )

                listFeed.add(newFeed)
                Log.v("CRX", "Size Feed " + listFeed.size)

                if(it == listPostFromAPI.last()){
                    //if(listFeed.size > 20) {
                    //isCanReaload = true
                    lastId = it.id
                    Log.v("CRX", "Feed Last")
                }
            }

        return listFeed

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

                var newMedia = ArrayList<MediaFeed>()
                if(it.mediaOrAd.carouselMediaCount > 0) {
                    it.mediaOrAd.carouselMedias.forEach {
                        newMedia.add(MediaFeed(
                                id = it.id,
                                mediaType = it.mediaType,
                                url = it.imageVersions2.candidates.first().url
                        ))
                    }
                }else{
                    newMedia.add(MediaFeed(
                            id = it.mediaOrAd.id,
                            mediaType = it.mediaOrAd.mediaType,
                            url =  URLDecoder.decode(it.mediaOrAd.imageVersions2.candidates.first().url,"UTF-8")
                    ))
                }

                Log.v("CRX", "Fetch Position : " + listTimelineFromAPI.indexOf(it))
                var newFeed =
                    InstagramFeed(
                        id = it.mediaOrAd.id,
                        pk = it.mediaOrAd.pk,
                        username = it.mediaOrAd.user.username,
                        profilePic = URLDecoder.decode(it.mediaOrAd.user.profilePicUrl, "UTF-8"),
                        caption = it.mediaOrAd.caption?.text,
                        media = newMedia,
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