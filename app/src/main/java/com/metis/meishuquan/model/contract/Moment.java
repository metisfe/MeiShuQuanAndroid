package com.metis.meishuquan.model.contract;

import android.text.TextUtils;

import com.metis.meishuquan.util.ContractUtility;
import com.metis.meishuquan.util.Utils;

import org.json.JSONObject;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wudi on 3/15/2015.
 */
public final class Moment implements Serializable
{
    private static final long serialVersionUID = 1L;

    private static String AvaterUrlKey = "AvatarLink";
    private static String CreateTimeKey = "CreateTime";
    private static String DetectTimeKey = "DetectTime";
    private static String TopMarkKey = "TopMark";
    private static String TypeKey = "ShowType";
    private static String TitleKey = "Title";
    private static String IdKey = "TimelineItemId";
    private static String DescriptionKey = "Description";
    private static String RepostDescriptionKey = "RepostDescription";
    private static String PublishLinkKey = "PublishLink";
    private static String SourceKey = "Source";
    private static String DisplaySourceKey = "DisplaySource";
    private static String UserDisplayNameKey = "UserDisplayName";
    private static String UserIdKey = "UserId";
    private static String ThumbnailPictureKey = "ThumbnailPicture";
    private static String MiddlePictureKey = "MiddlePicture";
    private static String OriginalPictureKey = "OriginalPicture";
    private static String ShareCountKey = "ShareCount";
    private static String LikeCountKey = "LikeCount";
    private static String CommentCountKey = "CommentCount";
    private static String ScoreAtThisMomentKey = "ScoreAtThisMoment";
    private static String AdditionalThumbnailPicturesKey = "AdditionalThumbnailPictures";
    private static String AdditionalMiddlePicturesKey = "AdditionalMiddlePictures";
    private static String AdditionalOriginalPicturesKey = "AdditionalOriginalPictures";

    public static Moment parse(JSONObject jsonObject)
    {
        if (jsonObject == null)
        {
            return null;
        }

        Moment moment = new Moment();

        moment.Type = parseType(ContractUtility.getString(jsonObject, TypeKey, null));
        moment.Id = ContractUtility.getString(jsonObject, IdKey, null);

        moment.IsMarkAsTop = ContractUtility.getBoolean(jsonObject, TopMarkKey, false);

        moment.Title = ContractUtility.getString(jsonObject, TitleKey, null, true);
        moment.Description = ContractUtility.getString(jsonObject, DescriptionKey, null, true);
        moment.RepostDescription = ContractUtility.getString(jsonObject, RepostDescriptionKey, null, true);

        moment.PublishLink = ContractUtility.getUrl(jsonObject, PublishLinkKey, null);
        moment.CreateTime = ContractUtility.getDate(jsonObject, CreateTimeKey, null);
        moment.DetectTime = ContractUtility.getDate(jsonObject, DetectTimeKey, null);
        //moment.SourceUrl = parseSource(ContractUtility.getString(jsonObject, SourceKey, null));
        moment.Source = ContractUtility.getString(jsonObject, SourceKey, null);
        moment.DisplaySource = ContractUtility.getString(jsonObject, DisplaySourceKey, null);
        moment.UserName = ContractUtility.getString(jsonObject, UserDisplayNameKey, null);
        moment.UserId = ContractUtility.getString(jsonObject, UserIdKey, null);
        moment.AvatarLink = ContractUtility.getUrl(jsonObject, AvaterUrlKey, null);
        moment.ThumbnailPictures = parseImageLinks(jsonObject, ThumbnailPictureKey, AdditionalThumbnailPicturesKey);
        moment.MiddlePictures = parseImageLinks(jsonObject, MiddlePictureKey, AdditionalMiddlePicturesKey);
        moment.OriginalPictures = parseImageLinks(jsonObject, OriginalPictureKey, AdditionalOriginalPicturesKey);
        moment.HasLiked = false;
        moment.Score = ContractUtility.getFloat(jsonObject, ScoreAtThisMomentKey, Float.NaN);

        moment.ShareCount = ContractUtility.getInt(jsonObject, ShareCountKey, 0, 0);
        moment.LikeCount = ContractUtility.getInt(jsonObject, LikeCountKey, 0, 0);
        moment.CommentCount = ContractUtility.getInt(jsonObject, CommentCountKey, 0, 0);

        if (moment.Type == MomentType.Tweet && moment.RepostDescription != null && moment.RepostDescription.length() > 0)
        {
            moment.Type = MomentType.RepostTweet;
        }

        return moment.isValid() ? moment : null;
    }

    /*
    private static URL parseSource(String value)
    {
        if (value == null || value.length() == 0)
        {
            return null;
        }

        return Environments.getSNSSourceIcon(value);
    }
    */

    private static MomentType parseType(String value)
    {
        if (value == null || value.length() == 0)
        {
            return MomentType.Unknown;
        }

        if ("news".equalsIgnoreCase(value))
        {
            return MomentType.News;
        }
        else if ("video".equalsIgnoreCase(value))
        {
            return MomentType.Video;
        }
        else if ("tweet".equalsIgnoreCase(value))
        {
            return MomentType.Tweet;
        }

        return MomentType.Unknown;
    }

    private static List<URL> parseImageLinks(JSONObject jsonObject, String imageKey, String additionalImagesKey)
    {
        URL imageUrl = ContractUtility.getUrl(jsonObject, imageKey, null);
        List<URL> imagesUrls = ContractUtility.getUrls(jsonObject, additionalImagesKey, null);

        List<URL> result = new ArrayList<URL>();

        if (imageUrl != null)
        {
            result.add(imageUrl);
        }

        if (imagesUrls != null && imagesUrls.size() > 0)
        {
            result.addAll(imagesUrls);
        }

        return result;
    }

    public MomentType Type;
    public String Title;
    public String Id;
    public String Description;
    public String RepostDescription;
    public boolean IsMarkAsTop;
    public URL PublishLink;
    public Date DetectTime;
    public Date CreateTime;
    public String Source;
    public URL SourceUrl;
    public String DisplaySource;
    public String UserName;
    public String UserId;
    public URL AvatarLink;
    public List<URL> ThumbnailPictures;
    public List<URL> MiddlePictures;
    public List<URL> OriginalPictures;
    public float Score;
    public boolean HasLiked;
    public int LikeCount;
    public int ShareCount;
    public int CommentCount;

    public Moment()
    {
    }

    @Override
    public int hashCode()
    {
        return this.Id != null ? this.Id.hashCode() : super.hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj != null && obj instanceof Moment)
        {
            return ((Moment) obj).Id.compareTo(this.Id) == 0;
        }

        return false;
    }

    public String getDetectTimeStampText()
    {
        return Utils.getDateFromNow(this.DetectTime);
    }

    public String getBingScoreText()
    {
        return "0";
    }

    public int getImageCount()
    {
        return this.ThumbnailPictures.size();
    }

    private boolean isValid()
    {
        return true;
    }

}