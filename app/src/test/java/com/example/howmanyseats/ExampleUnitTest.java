package com.example.howmanyseats;

import android.util.Log;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        String str = "이상윤";
        String s = "ㅌ";
        Assert.assertEquals(str.contains(s),false);
    }
}