package com.kkds.sosmedlibs

import android.util.Log
import com.google.gson.Gson
import facebook4j.Facebook
import facebook4j.FacebookFactory
import facebook4j.auth.AccessToken

class SosmedFbClient {

    lateinit var facebookClient: Facebook
    var gson = Gson()
    fun sosmedFbInstance(appId: String, appSecret: String,commaSeparetedPermissions: String, accessToken: String): SosmedFbClient{
        this.facebookClient = FacebookFactory().instance
        this.facebookClient.setOAuthAppId(appId, appSecret)
        this.facebookClient.setOAuthPermissions(commaSeparetedPermissions)
        this.facebookClient.oAuthAccessToken = AccessToken(accessToken, null)
        return SosmedFbClient()
    }

    fun getUserFbAccount(): FacebookUser{
        val urlAccount = "me?fields=id,name,picture"
        val accounts = facebookClient.callGetAPI(urlAccount).asJSONObject().toString()
        val dataAccount = gson.fromJson(accounts, FacebookUser::class.java)
        return dataAccount
    }

    fun getUserFbTimeline(): FacebookFeed{
        var url = "me/feed?fields=id,message,attachments,from,created_time,description"
        var response = facebookClient.callGetAPI(url)
        var data = response.asJSONObject().toString()
        var mdata = gson.fromJson(data, FacebookFeed::class.java)

        return mdata
    }

    fun getInstagramHastag(key: String, idIgAccount: String):InstagramTaggedMediaObj? {
        var urlReqIdHastag ="ig_hastag_search?user_id=$idIgAccount&q=$key"
        var reqIdHastag = facebookClient.callGetAPI(urlReqIdHastag).asJSONObject().toString()
        var dataHashtag = gson.fromJson(reqIdHastag, HashtagId::class.java)
        var idHashtag = dataHashtag.data.first().id
        var urlHastag = "$idHashtag/recent_media?user_id=17841431449073183&fields=id,caption,like_count,media_url,media_type,children"
        var tagedMedia = facebookClient.callGetAPI(urlHastag)
        Log.v("CRX", "TagMedia is : " + tagedMedia)
        Log.v("CRX", "TagMedia is Json Object =  " + tagedMedia.isJSONObject)
        var data = tagedMedia.asJSONObject().toString()
        var mdata = gson.fromJson(data, InstagramTaggedMediaObj::class.java)
        return mdata
    }

    fun getInstagramMention(idIgAccount: String): InstagramTaggedMediaObj?{
        var urlTag = "$idIgAccount/tags?fields=id,username,media_url,media_type,like_count,caption,timestamp"

        var tagedMedia = facebookClient.callGetAPI(urlTag)
        Log.v("CRX", "TagMedia is Json Array =  " + tagedMedia)
        Log.v("CRX", "TagMedia is Json Object =  " + tagedMedia.isJSONObject)
        var data = tagedMedia.asJSONObject().toString()
        var mdata = gson.fromJson(data, InstagramTaggedMediaObj::class.java)
        return mdata
    }

    fun getIdMyAccountInstagram(username: String): String?{
        val urlPageAccount ="me/account"
        var dataPageAccount = facebookClient.callGetAPI(urlPageAccount)
        var dataConvert = dataPageAccount.asJSONObject().toString()
        val mData = gson.fromJson(dataConvert, DataPageAccount::class.java)
        val idPageAccount = mData.data.find { it.name == username }
        if(idPageAccount != null){
            val urlGetIdInsta = "${idPageAccount.id}?fields=instagram_business_account"
            val dataInstagramAccount = facebookClient.callGetAPI(urlGetIdInsta).asJSONObject().toString()
            val jsonDataInstaAccount = gson.fromJson(dataInstagramAccount, DataInstagramAccount::class.java)
            return jsonDataInstaAccount.instagram_business_account.id
        }else return null
    }


}