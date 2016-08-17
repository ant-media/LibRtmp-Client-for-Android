package net.butterflytv.rtmp_client.test;

import android.app.Activity;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import net.butterflytv.rtmp_client.RTMPMuxer;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;


/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ApplicationTest {

    @Rule
    public ActivityTestRule mActivityRule = new ActivityTestRule<>(
            MainActivity.class, true);

    RTMPMuxer rtmpMuxer;


    @Test
    public void testRTMPIsConnectedIfServerDowns() {


        rtmpMuxer = new RTMPMuxer();
        assertEquals(rtmpMuxer.isConnected(), 0);

        int result = rtmpMuxer.open("rtmp://192.168.1.23/live/test",0, 0);

        assertTrue("open a local rtmp server", result > 0);

        assertEquals(rtmpMuxer.isConnected(), 1);

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

        assertEquals(rtmpMuxer.isConnected(), 0);


    }




    @Test
    public void testRTMPIsConnected() {

        Activity activity = mActivityRule.getActivity();
        Thread thread = new Thread() {
            @Override
            public void run() {

                rtmpMuxer = new RTMPMuxer();
                assertEquals(rtmpMuxer.isConnected(), 0);

                int result = rtmpMuxer.open("rtmp://192.168.1.23/live/new_stream", 0, 0);

                assertTrue("open a local rtmp server", result > 0);

                assertEquals(rtmpMuxer.isConnected(), 1);

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                rtmpMuxer.close();
                assertEquals(rtmpMuxer.isConnected(), 0);



                assertEquals(rtmpMuxer.isConnected(), 0);

            }
        };
        thread.start();


        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals(rtmpMuxer.isConnected(), 0);

    }


}

