package com.kkds.sosmedlibs



data class InstagramFeed(
        val id: String,
        val pk : Long,
        val username : String,
        val profilePic : String,
        val caption: String?,
        val media: List<MediaFeed>,
        val devTime : Long,
        val height: Int,
        val width : Int,
        val likeCount: Int

)

data class MediaFeed(
        val id: String,
        val mediaType: Int,
        val url: String

)

data class BigUserInstagram(
        val pk: Long,
        val urlProfile : String,
        val username: String

)

data class FacebookUser(
        val id: String,
        val name: String,
        val picture: PictureProfile
)

data class PictureProfile(
        val data: DataPicture
)

data class DataPicture(
        val height: Int,
        val is_silhouette: Boolean,
        val url: String,
        val width: Int
)

data class FacebookFeed(
        val data : List<DataFeedFacebook>,
        val paging: DataPagingFeedFacebook
)

data class DataFeedFacebook(
        val id: String,
        val created_time : String,
        val attachments: AttachmentsFeedFB,
        val message: String,
        val from : FromFacebookPostObj

)

data class FromFacebookPostObj(
        val name: String,
        val id: String
)

data class AttachmentsFeedFB(
        val data: List<DataAttachment>
)

data class DataAttachment(
        val description: String,
        val description_tags: List<DescriptionTags>,
        val media: Media,
        val target: Target,
        val title: String,
        val type: String,
        val url: String
)

data class Target(
        val id: String,
        val url: String
)

data class Media(
        val image: Image
)
data class Image(
        val height: Int,
        val src: String,
        val width: Int
)

data class DescriptionTags(
        val id: String,
        val length: Int,
        val name: String,
        val offset: Int,
        val type: String
)

data class DataPagingFeedFacebook(
        val previous: String,
        val next: String
)

data class InstagramTaggedMediaObj(
        val data: List<InstagramTaggedMedia>,
        val paging: DataPagingTaggedMedia
)

data class DataPagingTaggedMedia(
        val cursor: DataCursor,
        val next: String
)

data class DataCursor(
        val after: String
)

data class InstagramTaggedMedia(
        val id: String,
        val username: String?,
        val media_url: String,
        val media_type: String,
        val like_count: Int,
        val caption: String?,
        val timestamp: String,
        val urlPhoto: String,
        val children: ChildrenMedia?
)

data class ChildrenMedia(
        val id: String
)

data class HashtagId(
        val data: List<DataHashtag>
)

data class DataHashtag(
        val id: String
)

data class DataPageAccount(
        val data: List<PageAccount>
)

data class PageAccount(
        val access_token: String,
        val category: String,
        val category_list: List<Category>,
        val name: String,
        val id: String,
        val task: List<String>
)

data class Category(
        val id: String,
        val name: String
)

data class DataInstagramAccount(
        val instagram_business_account: InstagramBusinessAccount,
        val id: String
)

data class InstagramBusinessAccount(
        val id: String
)