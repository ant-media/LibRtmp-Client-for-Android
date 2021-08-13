package io.antmedia.rtmp_client.test;

import android.app.Activity;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import io.antmedia.rtmp_client.RTMPMuxer;
import io.antmedia.rtmp_client.RtmpClient;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;


/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ApplicationTest {

    @Rule
    public ActivityTestRule mActivityRule = new ActivityTestRule<MainActivity>(
            MainActivity.class, true);

    RTMPMuxer rtmpMuxer;

    @After
    public void afterTest() {
        if (rtmpMuxer != null) {
            rtmpMuxer.close();
            rtmpMuxer = null;
        }
    }

    @Test
    public void testRTMPClientThrowException() {
        RtmpClient rtmpClient = new RtmpClient();
        boolean exceptionCheck = false;
        try {
            rtmpClient.open("rtmp://dummy/vod", false);
        } catch (RtmpClient.RtmpIOException e) {
            e.printStackTrace();
            exceptionCheck = true;
        }
        assertTrue(exceptionCheck);

    }

    @Test
    public void testRTMPIsConnectedIfServerDowns() {


        rtmpMuxer = new RTMPMuxer();
        assertFalse(rtmpMuxer.isConnected());

        int result = rtmpMuxer.open("rtmp://192.168.1.23/live/test",0, 0);

        assertTrue("open a local rtmp server", result > 0);

        assertTrue(rtmpMuxer.isConnected());

        System.out.println("Waiting for 25 seconds doing nothing");


        new Thread() {
            @Override
            public void run() {
                byte[] data = new byte[1024];
                int length;
                while ((length = rtmpMuxer.read(data, 0, 1024)) > 0) {
                    System.out.println("Read data: " + new String(data, 0, length));
                }
                System.out.println("Finishing read thread");
            }
        }.start();


        try {
            Thread.sleep(35000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /*
        while (rtmpStreamer.isConnected() == 1) {
            System.out.println("Turn off rtmp server");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        */

        assertFalse(rtmpMuxer.isConnected());


    }




    @Test
    public void testRTMPIsConnected() {

        Activity activity = mActivityRule.getActivity();
        Thread thread = new Thread() {
            @Override
            public void run() {

                rtmpMuxer = new RTMPMuxer();
                assertFalse(rtmpMuxer.isConnected());

                int result = rtmpMuxer.open("rtmp://192.168.1.23/live/new_stream", 0, 0);

                assertTrue("open a local rtmp server", result > 0);

                assertTrue(rtmpMuxer.isConnected());

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                rtmpMuxer.close();
                assertFalse(rtmpMuxer.isConnected());



                assertFalse(rtmpMuxer.isConnected());

            }
        };
        thread.start();


        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertFalse(rtmpMuxer.isConnected());

    }


}

