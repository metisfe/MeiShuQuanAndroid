package com.metis.meishuquan.view.circle.moment.comment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;

import com.metis.meishuquan.MainApplication;
import com.metis.meishuquan.R;

public class CommentUtility
{
    private final static String EmotionDrawablePrefix = "shared_emotion";   
    public static String EmotionStringStart = "[";
    public static String EmotionStringEnd = "]";
    public static Pattern EmotionStringPattern;
    public static int EmotionIconSize = 44;
    public static List<String> EmotionNames = new ArrayList<String>();
    public static Map<String, Integer> EmotionIds = new HashMap<String, Integer>();
    
    public static void buildPattern()
    {
        StringBuilder patternString = new StringBuilder();
        patternString.append('(');
        for (int i = 0; i < EmotionNames.size(); i++)
        {
            String s = EmotionNames.get(i);
            patternString.append(Pattern.quote(s));
            patternString.append('|');
        }

        patternString.replace(patternString.length() - 1, patternString.length(), ")");
        EmotionStringPattern = Pattern.compile(patternString.toString());
    }

    public static void initEmotionIcons()
    {
        Resources resources = MainApplication.Resources;
        String[] emotion_names = resources.getStringArray(R.array.emotion_names);

        for (int i = 1; i <= emotion_names.length; i++)
        {
            String emoticonsName = CommentUtility.EmotionStringStart + emotion_names[i - 1] + CommentUtility.EmotionStringEnd;
            int emoticonsId = resources.getIdentifier(EmotionDrawablePrefix + i, "drawable", MainApplication.PackageName);
            EmotionNames.add(emoticonsName);
            EmotionIds.put(emoticonsName, emoticonsId);
        }

        CommentUtility.buildPattern();
    }
    
    public static CharSequence replace(CharSequence text, Resources resources, Context context)
    {
        try
        {
            SpannableStringBuilder builder = new SpannableStringBuilder(text);
            Matcher matcher = CommentUtility.EmotionStringPattern.matcher(text);
            while (matcher.find())
            {
                if (EmotionIds.containsKey(matcher.group()))
                {
                    int id = EmotionIds.get(matcher.group());
                    Bitmap bitmap = BitmapFactory.decodeResource(resources, id);
                    if (bitmap != null)
                    {
                        ImageSpan span = new ImageSpan(context, bitmap);
                        builder.setSpan(span, matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            }
            
            return builder;
        }
        catch (Exception e)
        {
            return text;
        }
    }
}