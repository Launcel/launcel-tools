package xyz.launcel.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import xyz.launcel.exception.SystemException;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Stream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StringUtils
{

    public static int indexOf(final CharSequence cs, final CharSequence searchChar, final int start)
    {
        return cs.toString().indexOf(searchChar.toString(), start);
    }


    public static boolean contains(final CharSequence seq, final CharSequence searchSeq)
    {
        return seq != null && searchSeq != null && indexOf(seq, searchSeq, 0) >= 0;
    }

    public static boolean isNotEmpty(final CharSequence cs) { return !StringUtils.isEmpty(cs); }

    public static boolean isEmpty(final CharSequence cs)    { return cs == null || cs.length() == 0; }

    public static boolean isNotBlank(final CharSequence cs) { return !isBlank(cs); }

    public static boolean isBlank(final CharSequence cs)
    {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0)
        {
            return true;
        }
        for (int i = 0; i < strLen; i++)
        {
            if (!Character.isWhitespace(cs.charAt(i)))
            {
                return false;
            }
        }
        return true;
    }

    public static String capitalize(final String str)
    {
        if (isBlank(str))
        {
            return null;
        }

        char firstChar = str.charAt(0);
        if (Character.isTitleCase(firstChar))
        {
            return str;
        }

        return Character.toTitleCase(firstChar) + str.substring(1);
    }

    public static boolean isTrue(String s)
    {
        return "true".equalsIgnoreCase(s);
    }


    public static boolean isFalse(String s)
    {
        return "false".equalsIgnoreCase(s);
    }

    public static String getUUID()
    {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static Stream spiltStream(String strings, String split)
    {
        return Arrays.stream(strings.split(split)).filter(StringUtils::isNotBlank);
    }

    private static final Random RANDOM = new Random();

    public static String random(final int count)
    {
        return random(count, false, true);
    }

    private static String random(final int count, final boolean letters, final boolean numbers)
    {
        return random(count, 0, 0, letters, numbers);
    }

    private static String random(final int count, final int start, final int end, final boolean letters, final boolean numbers)
    {
        return random(count, start, end, letters, numbers, null, RANDOM);
    }

    private static String random(int count, int start, int end, final boolean letters, final boolean numbers, final char[] chars,
                                 final Random random)
    {
        if (count == 0)
        {
            return "";
        }
        else if (count < 0)
        {
            throw new SystemException("_DEFINE_ERROR_CODE_011", "Requested random string length " + count + " is less than 0.");
        }
        if (chars != null && chars.length == 0)
        {
            throw new SystemException("_DEFINE_ERROR_CODE_011", "The chars array must not be empty");
        }

        if (start == 0 && end == 0)
        {
            if (chars != null)
            {
                end = chars.length;
            }
            else
            {
                if (!letters && !numbers)
                {
                    end = Integer.MAX_VALUE;
                }
                else
                {
                    end = 'z' + 1;
                    start = ' ';
                }
            }
        }
        else
        {
            if (end <= start)
            {
                throw new SystemException("_DEFINE_ERROR_CODE_011",
                        "Parameter end (" + end + ") must be greater than start (" + start + ")");
            }
        }

        char[] buffer = new char[count];
        int    gap    = end - start;

        while (count-- != 0)
        {
            char ch;
            if (chars == null)
            {
                ch = (char) (random.nextInt(gap) + start);
            }
            else
            {
                ch = chars[random.nextInt(gap) + start];
            }
            if (!(letters && Character.isLetter(ch) || numbers && Character.isDigit(ch) || !letters && !numbers))
            {
                count++;
                continue;

            }
            if (ch >= 56320 && ch <= 57343)
            {
                if (count == 0)
                {
                    count++;
                    continue;
                }
                buffer[count] = ch;
                count--;
                buffer[count] = (char) (55296 + random.nextInt(128));
                continue;
            }
            else if (ch >= 55296 && ch <= 56191)
            {
                if (count == 0)
                {
                    count++;
                    continue;
                }

                buffer[count] = (char) (56320 + random.nextInt(128));
                count--;
                buffer[count] = ch;
                continue;
            }
            else if (ch >= 56192 && ch <= 56319)
            {
                count++;
                continue;
            }
            buffer[count] = ch;
        }
        return new String(buffer);
    }


    public static byte[] serializer(String key)
    {
        return key.getBytes(StandardCharsets.UTF_8);
    }

    public static String deserialize(byte[] bytes)
    {
        return new String(bytes, StandardCharsets.UTF_8);
    }

}

